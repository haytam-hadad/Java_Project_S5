import java.sql.*;

/**
 * Data Access Object pour la gestion des véhicules
 */
public class VehiculeDAO {
    private Connection connection;

    public VehiculeDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Ajouter un véhicule à la base de données
     */
    public boolean ajouterVehicule(Vehicule vehicule) {
        String sql = "INSERT INTO vehicules (id, marque, modele, prix_achat, prix_vente, annee, " +
                     "kilometrage, type_vehicule, statut, date_ajout) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vehicule.getId());
            stmt.setString(2, vehicule.getMarque());
            stmt.setString(3, vehicule.getModele());
            stmt.setDouble(4, vehicule.getPrixAchat());
            stmt.setDouble(5, vehicule.getPrixVente());
            stmt.setInt(6, vehicule.getAnnee());
            stmt.setInt(7, vehicule.getKilometrage());
            stmt.setString(8, vehicule.getTypeVehicule());
            stmt.setString(9, vehicule.getStatut());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du véhicule: " + e.getMessage());
            return false;
        }
    }

    /**
     * Afficher tous les véhicules
     */
    public void afficherTousVehicules() {
        String sql = "SELECT * FROM vehicules ORDER BY date_ajout DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                afficherVehicule(rs);
            }
            
            if (!hasData) {
                System.out.println("Aucun véhicule trouvé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules: " + e.getMessage());
        }
    }

    /**
     * Afficher les véhicules disponibles
     */
    public void afficherVehiculesDisponibles() {
        String sql = "SELECT * FROM vehicules WHERE statut = 'DISPONIBLE' ORDER BY date_ajout DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                afficherVehicule(rs);
            }
            
            if (!hasData) {
                System.out.println("Aucun véhicule disponible.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules disponibles: " + e.getMessage());
        }
    }

    /**
     * Récupérer un véhicule par son ID
     */
    public Vehicule getVehiculeById(String id) {
        String sql = "SELECT * FROM vehicules WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVehicule(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du véhicule: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Mettre à jour le statut d'un véhicule
     */
    public boolean updateStatut(String id, String nouveauStatut) {
        String sql = "UPDATE vehicules SET statut = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nouveauStatut);
            stmt.setString(2, id);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du statut: " + e.getMessage());
            return false;
        }
    }

    /**
     * Marquer un véhicule comme vendu
     */
    public boolean marquerCommeVendu(String id) {
        String sql = "UPDATE vehicules SET statut = 'VENDU', date_vende = NOW() WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vente du véhicule: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprimer un véhicule
     */
    public boolean supprimerVehicule(String id) {
        String sql = "DELETE FROM vehicules WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du véhicule: " + e.getMessage());
            return false;
        }
    }

    /**
     * Rechercher et afficher des véhicules par critères
     */
    public void rechercherEtAfficherVehicules(String marque, String typeVehicule, Double prixMax, Integer anneeMin) {
        StringBuilder sql = new StringBuilder("SELECT * FROM vehicules WHERE 1=1");
        int paramCount = 0;
        
        if (marque != null && !marque.isEmpty()) {
            sql.append(" AND marque LIKE ?");
            paramCount++;
        }
        if (typeVehicule != null && !typeVehicule.isEmpty()) {
            sql.append(" AND type_vehicule = ?");
            paramCount++;
        }
        if (prixMax != null) {
            sql.append(" AND prix_vente <= ?");
            paramCount++;
        }
        if (anneeMin != null) {
            sql.append(" AND annee >= ?");
            paramCount++;
        }
        
        sql.append(" ORDER BY date_ajout DESC");
        
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (marque != null && !marque.isEmpty()) {
                stmt.setString(paramIndex++, "%" + marque + "%");
            }
            if (typeVehicule != null && !typeVehicule.isEmpty()) {
                stmt.setString(paramIndex++, typeVehicule);
            }
            if (prixMax != null) {
                stmt.setDouble(paramIndex++, prixMax);
            }
            if (anneeMin != null) {
                stmt.setInt(paramIndex++, anneeMin);
            }
            
            ResultSet rs = stmt.executeQuery();
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                afficherVehicule(rs);
            }
            
            if (!hasData) {
                System.out.println("Aucun véhicule trouvé avec ces critères.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
        }
    }

    /**
     * Afficher un véhicule depuis un ResultSet
     */
    private void afficherVehicule(ResultSet rs) throws SQLException {
        System.out.print("ID=" + rs.getString("id") + " ||  " + 
                        rs.getString("marque") + " " + rs.getString("modele") + 
                        " (" + rs.getInt("annee") + ") ||  Km=" + rs.getInt("kilometrage") +
                        " ||  Achat=" + rs.getDouble("prix_achat") + 
                        " ||  Vente=" + rs.getDouble("prix_vente") +
                        " ||  Type=" + rs.getString("type_vehicule") + 
                        " ||  Statut=" + rs.getString("statut"));
        System.out.println();
    }

    /**
     * Mapper un ResultSet vers un objet Vehicule
     */
    private Vehicule mapResultSetToVehicule(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String marque = rs.getString("marque");
        String modele = rs.getString("modele");
        double prixAchat = rs.getDouble("prix_achat");
        double prixVente = rs.getDouble("prix_vente");
        int annee = rs.getInt("annee");
        int kilometrage = rs.getInt("kilometrage");
        String typeVehicule = rs.getString("type_vehicule");
        String statut = rs.getString("statut");
        Timestamp dateAjout = rs.getTimestamp("date_ajout");
        Timestamp dateVente = rs.getTimestamp("date_vende");
        
        String dateAjoutStr = dateAjout != null ? dateAjout.toString() : null;
        String dateVenteStr = dateVente != null ? dateVente.toString() : null;
        
        return new Vehicule(id, marque, modele, prixAchat, prixVente, annee, kilometrage, 
                           typeVehicule, statut, dateAjoutStr, dateVenteStr);
    }
}
