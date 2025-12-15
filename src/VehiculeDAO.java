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
        String sql = "INSERT INTO vehicules (marque, modele, prix_achat, prix_vente, annee, " +
                     "kilometrage, type_vehicule, statut, date_ajout) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, vehicule.getMarque());
            stmt.setString(2, vehicule.getModele());
            stmt.setDouble(3, vehicule.getPrixAchat());
            stmt.setDouble(4, vehicule.getPrixVente());
            stmt.setInt(5, vehicule.getAnnee());
            stmt.setInt(6, vehicule.getKilometrage());
            stmt.setString(7, vehicule.getTypeVehicule());
            stmt.setString(8, vehicule.getStatut());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    vehicule.setId(String.valueOf(generatedId));
                }
                return true;
            }
            return false;
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
                System.out.println(ColorUtil.warning("Aucun véhicule trouvé."));
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
                System.out.println(ColorUtil.warning("Aucun véhicule disponible."));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules disponibles: " + e.getMessage());
        }
    }

    /**
     * Afficher les véhicules vendus
     */
    public void afficherVehiculesVendus() {
        String sql = "SELECT * FROM vehicules WHERE statut = 'VENDU' ORDER BY date_vende DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println(ColorUtil.colorize("═══════════════════════════════════════════════════", ColorUtil.CYAN));
                System.out.println(ColorUtil.colorize("ID: ", ColorUtil.CYAN) + ColorUtil.highlight(String.valueOf(rs.getInt("id"))));
                System.out.println(ColorUtil.colorize("Véhicule: ", ColorUtil.CYAN) + 
                                 ColorUtil.colorize(rs.getString("marque") + " " + rs.getString("modele"), ColorUtil.YELLOW) + 
                                 ColorUtil.colorize(" (", ColorUtil.WHITE) + ColorUtil.highlight(String.valueOf(rs.getInt("annee"))) + 
                                 ColorUtil.colorize(")", ColorUtil.WHITE));
                System.out.println(ColorUtil.colorize("Type: ", ColorUtil.CYAN) + ColorUtil.colorize(rs.getString("type_vehicule"), ColorUtil.MAGENTA));
                System.out.println(ColorUtil.colorize("Kilométrage: ", ColorUtil.CYAN) + ColorUtil.highlight(rs.getInt("kilometrage") + " km"));
                System.out.println(ColorUtil.colorize("Prix de vente: ", ColorUtil.CYAN) + ColorUtil.colorize(String.format("%.2f", rs.getDouble("prix_vente")), ColorUtil.GREEN) + " DH");
                System.out.println(ColorUtil.colorize("Date de vente: ", ColorUtil.CYAN) + 
                                 (rs.getTimestamp("date_vende") != null ? 
                                 ColorUtil.highlight(rs.getTimestamp("date_vende").toString()) : ColorUtil.colorize("N/A", ColorUtil.WHITE)));
                System.out.println(ColorUtil.colorize("═══════════════════════════════════════════════════", ColorUtil.CYAN));
            }
            
            if (!hasData) {
                System.out.println(ColorUtil.warning("Aucun véhicule vendu pour le moment."));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des véhicules vendus: " + e.getMessage());
        }
    }

    /**
     * Récupérer un véhicule par son ID
     */
    public Vehicule getVehiculeById(int id) {
        String sql = "SELECT * FROM vehicules WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
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
     * Supprimer un véhicule
     */
    public boolean supprimerVehicule(int id) {
        String sql = "DELETE FROM vehicules WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
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
        
        if (marque != null && !marque.isEmpty()) {
            sql.append(" AND marque LIKE ?");
        }
        if (typeVehicule != null && !typeVehicule.isEmpty()) {
            sql.append(" AND type_vehicule = ?");
        }
        if (prixMax != null) {
            sql.append(" AND prix_vente <= ?");
        }
        if (anneeMin != null) {
            sql.append(" AND annee >= ?");
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
                System.out.println(ColorUtil.warning("Aucun véhicule trouvé avec ces critères."));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
        }
    }

    /**
     * Afficher un véhicule depuis un ResultSet
     */
    private void afficherVehicule(ResultSet rs) throws SQLException {
        String statut = rs.getString("statut");
        String statutColor = getStatutColor(statut);
        System.out.print(ColorUtil.colorize("ID=", ColorUtil.CYAN) + ColorUtil.highlight(String.valueOf(rs.getInt("id"))) + 
                        ColorUtil.colorize(" ||  ", ColorUtil.WHITE) + 
                        ColorUtil.colorize(rs.getString("marque") + " " + rs.getString("modele"), ColorUtil.YELLOW) + 
                        ColorUtil.colorize(" (", ColorUtil.WHITE) + ColorUtil.highlight(String.valueOf(rs.getInt("annee"))) + 
                        ColorUtil.colorize(") ||  Km=", ColorUtil.WHITE) + ColorUtil.highlight(String.valueOf(rs.getInt("kilometrage"))) +
                        ColorUtil.colorize(" ||  Achat=", ColorUtil.WHITE) + ColorUtil.colorize(String.format("%.2f", rs.getDouble("prix_achat")), ColorUtil.GREEN) + 
                        ColorUtil.colorize(" ||  Vente=", ColorUtil.WHITE) + ColorUtil.colorize(String.format("%.2f", rs.getDouble("prix_vente")), ColorUtil.GREEN) +
                        ColorUtil.colorize(" ||  Type=", ColorUtil.WHITE) + ColorUtil.colorize(rs.getString("type_vehicule"), ColorUtil.MAGENTA) + 
                        ColorUtil.colorize(" ||  Statut=", ColorUtil.WHITE) + statutColor);
        System.out.println();
    }
    
    private String getStatutColor(String statut) {
        switch (statut.toUpperCase()) {
            case "DISPONIBLE":
                return ColorUtil.colorize(statut, ColorUtil.GREEN_BOLD);
            case "VENDU":
                return ColorUtil.colorize(statut, ColorUtil.RED_BOLD);
            default:
                return ColorUtil.colorize(statut, ColorUtil.WHITE);
        }
    }

    /**
     * Mapper un ResultSet vers un objet Vehicule
     */
    private Vehicule mapResultSetToVehicule(ResultSet rs) throws SQLException {
        String id = String.valueOf(rs.getInt("id"));
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

    /**
     * Modifier un véhicule
     */
    public boolean modifierVehicule(Vehicule vehicule) {
        String sql = "UPDATE vehicules SET statut = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, vehicule.getStatut());
            stmt.setInt(2, Integer.parseInt(vehicule.getId()));
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du véhicule: " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            System.err.println("ID invalide: " + vehicule.getId());
            return false;
        }
    }
}
