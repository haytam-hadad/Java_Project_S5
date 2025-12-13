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
     * Enregistrer une vente (client achète un véhicule)
     */
    public boolean enregistrerVente(String vehiculeId, String clientId) {
        try {
            connection.setAutoCommit(false);
            
            // Vérifier que le véhicule existe et est disponible
            Vehicule vehicule = vehiculeDAO.getVehiculeById(vehiculeId);
            if (vehicule == null) {
                System.out.println(ColorUtil.error("Véhicule introuvable!"));
                return false;
            }
            
            if (!"DISPONIBLE".equals(vehicule.getStatut()) && !"EN_NEGOCIATION".equals(vehicule.getStatut())) {
                System.out.println(ColorUtil.error("Ce véhicule n'est pas disponible à la vente!"));
                return false;
            }
            
            // Vérifier que le client existe
            Client client = clientDAO.getClientById(clientId);
            if (client == null) {
                System.out.println(ColorUtil.error("Client introuvable!"));
                return false;
            }
            
            // Marquer le véhicule comme vendu
            boolean success = vehiculeDAO.marquerCommeVendu(vehiculeId);
            
            if (success) {
                connection.commit();
                System.out.println("\n" + ColorUtil.success("Vente enregistrée avec succès!"));
                System.out.println(ColorUtil.colorize("Véhicule: ", ColorUtil.CYAN) + 
                                 ColorUtil.colorize(vehicule.getMarque() + " " + vehicule.getModele(), ColorUtil.YELLOW));
                System.out.println(ColorUtil.colorize("Client: ", ColorUtil.CYAN) + ColorUtil.colorize(client.getNom(), ColorUtil.YELLOW));
                System.out.println(ColorUtil.colorize("Prix de vente: ", ColorUtil.CYAN) + 
                                 ColorUtil.colorize(String.format("%.2f", vehicule.getPrixVente()), ColorUtil.GREEN) + " DH");
                System.out.println(ColorUtil.colorize("Prix final (avec taxes): ", ColorUtil.CYAN) + 
                                 ColorUtil.highlight(String.format("%.2f", vehicule.calculerPrixFinal()) + " DH"));
                return true;
            } else {
                connection.rollback();
                return false;
            }
            
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Erreur lors du rollback: " + ex.getMessage());
            }
            System.err.println("Erreur lors de l'enregistrement de la vente: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la réinitialisation de l'auto-commit: " + e.getMessage());
            }
        }
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
            
            // Nombre de véhicules en négociation
            String sqlNegociation = "SELECT COUNT(*) as total FROM vehicules WHERE statut = 'EN_NEGOCIATION'";
            rs = stmt.executeQuery(sqlNegociation);
            
            int totalNegociation = 0;
            if (rs.next()) {
                totalNegociation = rs.getInt("total");
            }
            
            System.out.println("\n" + ColorUtil.title("=== STATISTIQUES DE VENTES ==="));
            System.out.println(ColorUtil.colorize("Véhicules vendus: ", ColorUtil.CYAN) + ColorUtil.highlight(String.valueOf(totalVendus)));
            System.out.println(ColorUtil.colorize("Véhicules disponibles: ", ColorUtil.CYAN) + ColorUtil.highlight(String.valueOf(totalDisponibles)));
            System.out.println(ColorUtil.colorize("Véhicules en négociation: ", ColorUtil.CYAN) + ColorUtil.highlight(String.valueOf(totalNegociation)));
            System.out.println(ColorUtil.colorize("Chiffre d'affaires total: ", ColorUtil.CYAN) + 
                             ColorUtil.colorize(String.format("%.2f", chiffreAffaires), ColorUtil.GREEN_BOLD) + " DH");
            
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des statistiques: " + e.getMessage());
        }
    }

}
