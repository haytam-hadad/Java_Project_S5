import java.sql.*;

/**
 * Data Access Object pour la gestion des ventes
 */
public class VenteDAO {
    private Connection connection;
    private VehiculeDAO vehiculeDAO;
    private ClientDAO clientDAO;

    public VenteDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.vehiculeDAO = new VehiculeDAO();
        this.clientDAO = new ClientDAO();
    }

    /**
     * Afficher les statistiques de ventes
     */
    public void afficherStatistiques() {
        try {
            // Nombre total de véhicules vendus
            String sqlVendus = "SELECT COUNT(*) as total FROM vehicules WHERE statut = 'VENDU'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlVendus);
            
            int totalVendus = 0;
            if (rs.next()) {
                totalVendus = rs.getInt("total");
            }
            
            // Chiffre d'affaires total
            String sqlCA = "SELECT SUM(prix_vente) as ca FROM vehicules WHERE statut = 'VENDU'";
            rs = stmt.executeQuery(sqlCA);
            
            double chiffreAffaires = 0;
            if (rs.next()) {
                chiffreAffaires = rs.getDouble("ca");
            }
            
            // Nombre de véhicules disponibles
            String sqlDisponibles = "SELECT COUNT(*) as total FROM vehicules WHERE statut = 'DISPONIBLE'";
            rs = stmt.executeQuery(sqlDisponibles);
            
            int totalDisponibles = 0;
            if (rs.next()) {
                totalDisponibles = rs.getInt("total");
            }
            
            System.out.println("\n" + ColorUtil.title("=== STATISTIQUES DE VENTES ==="));
            System.out.println(ColorUtil.colorize("Véhicules vendus: ", ColorUtil.CYAN) + ColorUtil.highlight(String.valueOf(totalVendus)));
            System.out.println(ColorUtil.colorize("Véhicules disponibles: ", ColorUtil.CYAN) + ColorUtil.highlight(String.valueOf(totalDisponibles)));
            System.out.println(ColorUtil.colorize("Chiffre d'affaires total: ", ColorUtil.CYAN) + 
                             ColorUtil.colorize(String.format("%.2f", chiffreAffaires), ColorUtil.GREEN_BOLD) + " DH");
            
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des statistiques: " + e.getMessage());
        }
    }

}
