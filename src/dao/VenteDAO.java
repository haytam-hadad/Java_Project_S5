package dao;

import java.sql.*;
import model.Client;
import model.Vehicule;
import util.ColorUtil;

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
    public boolean enregistrerVente(int vehiculeId, int clientId,double prixVenteFinal, String modePaiement, String notes) {
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

    /**
     * Afficher l'historique complet des ventes
     */
    public void afficherHistoriqueVentes() {
        String sql = "SELECT v.id as vente_id, v.prix_vente_final, v.date_vente, v.mode_paiement, v.notes, " +
                     "ve.id as vehicule_id, ve.marque, ve.modele, ve.annee, ve.type_vehicule, " +
                     "c.id as client_id, c.nom, c.telephone " +
                     "FROM ventes v " +
                     "INNER JOIN vehicules ve ON v.vehicule_id = ve.id " +
                     "INNER JOIN clients c ON v.client_id = c.id " +
                     "ORDER BY v.date_vente DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n" + ColorUtil.title("=== HISTORIQUE DES VENTES ===\n"));
            
            boolean hasData = false;
            int compteur = 0;
            double totalVentes = 0;
            
            while (rs.next()) {
                hasData = true;
                compteur++;
                double prix = rs.getDouble("prix_vente_final");
                totalVentes += prix;
                
                System.out.println(ColorUtil.colorize("VENTE #" + compteur, ColorUtil.YELLOW_BOLD) + 
                                 ColorUtil.colorize(" - " + rs.getTimestamp("date_vente"), ColorUtil.WHITE));
                
                System.out.println(ColorUtil.colorize("  Véhicule: ", ColorUtil.CYAN) + 
                                 ColorUtil.colorize(rs.getString("marque") + " " + rs.getString("modele") + 
                                 " (" + rs.getInt("annee") + ")", ColorUtil.YELLOW) +
                                 ColorUtil.colorize(" [" + rs.getString("type_vehicule") + "]", ColorUtil.MAGENTA));
                
                System.out.println(ColorUtil.colorize("  Client:   ", ColorUtil.CYAN) + 
                                 ColorUtil.colorize(rs.getString("nom"), ColorUtil.YELLOW) +
                                 ColorUtil.colorize(" - " + rs.getString("telephone"), ColorUtil.WHITE));
                
                System.out.println(ColorUtil.colorize("  Prix:     ", ColorUtil.CYAN) + 
                                 ColorUtil.colorize(String.format("%.2f DH", prix), ColorUtil.GREEN_BOLD) +
                                 ColorUtil.colorize(" - " + rs.getString("mode_paiement"), ColorUtil.WHITE));
                
                String notes = rs.getString("notes");
                if (notes != null && !notes.isEmpty()) {
                    System.out.println(ColorUtil.colorize("  Notes:    ", ColorUtil.CYAN) + 
                                     ColorUtil.colorize(notes, ColorUtil.WHITE));
                }
                
                System.out.println();
            }
            
            if (!hasData) {
                System.out.println(ColorUtil.warning("Aucune vente enregistrée pour le moment."));
            } else {
                System.out.println(ColorUtil.colorize("─────────────────────────────────────────", ColorUtil.CYAN));
                System.out.println(ColorUtil.colorize("Total des ventes: ", ColorUtil.CYAN_BOLD) + 
                                 ColorUtil.highlight(String.valueOf(compteur)) +
                                 ColorUtil.colorize(" | Chiffre d'affaires: ", ColorUtil.CYAN_BOLD) +
                                 ColorUtil.colorize(String.format("%.2f DH", totalVentes), ColorUtil.GREEN_BOLD));
                System.out.println(ColorUtil.colorize("─────────────────────────────────────────", ColorUtil.CYAN));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique des ventes: " + e.getMessage());
        }
    }
}