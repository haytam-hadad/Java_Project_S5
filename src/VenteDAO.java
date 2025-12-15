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
     * Enregistrer une vente (création de la ligne dans ventes + mise à jour du véhicule)
     */
    public boolean enregistrerVente(int vehiculeId, int clientId,
                                    double prixVenteFinal, String modePaiement, String notes) {
        try {
            // Vérifier l'existence du véhicule et qu'il est disponible
            Vehicule vehicule = vehiculeDAO.getVehiculeById(vehiculeId);
            if (vehicule == null) {
                System.out.println(ColorUtil.error("Véhicule introuvable."));
                return false;
            }
            if (!"DISPONIBLE".equalsIgnoreCase(vehicule.getStatut())) {
                System.out.println(ColorUtil.error("Ce véhicule n'est pas disponible pour la vente."));
                return false;
            }

            // Vérifier l'existence du client
            Client client = clientDAO.getClientById(clientId);
            if (client == null) {
                System.out.println(ColorUtil.error("Client introuvable."));
                return false;
            }

            // Transaction : insertion dans ventes + mise à jour du véhicule
            connection.setAutoCommit(false);

            String sqlInsert = "INSERT INTO ventes (vehicule_id, client_id, prix_vente_final, mode_paiement, notes) " +
                               "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sqlInsert)) {
                stmt.setInt(1, vehiculeId);
                stmt.setInt(2, clientId);
                stmt.setDouble(3, prixVenteFinal);
                stmt.setString(4, modePaiement);
                stmt.setString(5, notes);
                stmt.executeUpdate();
            }

            // Mettre à jour le véhicule comme vendu
            String sqlUpdateVehicule = "UPDATE vehicules SET statut = 'VENDU', date_vende = NOW() WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sqlUpdateVehicule)) {
                stmt.setInt(1, vehiculeId);
                stmt.executeUpdate();
            }

            connection.commit();
            connection.setAutoCommit(true);

            System.out.println(ColorUtil.success("Vente enregistrée avec succès !"));
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement de la vente: " + e.getMessage());
            rollbackQuietly();
            return false;
        }
    }

    private void rollbackQuietly() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException ignored) {
        }
    }

    /**
     * Afficher les statistiques de ventes
     */
    public void afficherStatistiques() {
        try {
            // Nombre total de ventes
            String sqlVendus = "SELECT COUNT(*) as total FROM ventes";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlVendus);
            
            int totalVendus = 0;
            if (rs.next()) {
                totalVendus = rs.getInt("total");
            }
            
            // Chiffre d'affaires total
            String sqlCA = "SELECT SUM(prix_vente_final) as ca FROM ventes";
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
