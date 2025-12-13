import java.sql.*;

/**
 * Data Access Object pour la gestion des clients
 */
public class ClientDAO {
    private Connection connection;

    public ClientDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Ajouter un client
     */
    public boolean ajouterClient(Client client) {
        String sql = "INSERT INTO clients (nom, telephone, email, adresse, date_creation) VALUES (?, ?, ?, ?, NOW())";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getTelephone());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getAdresse());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    client.setId(String.valueOf(generatedId));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du client: " + e.getMessage());
            return false;
        }
    }

    /**
     * Afficher tous les clients
     */
    public void afficherTousClients() {
        String sql = "SELECT * FROM clients ORDER BY date_creation DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                afficherClient(rs);
            }
            
            if (!hasData) {
                System.out.println(ColorUtil.warning("Aucun client trouvé."));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients: " + e.getMessage());
        }
    }

    /**
     * Récupérer un client par son ID
     */
    public Client getClientById(String id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du client: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("ID invalide: " + id);
        }
        
        return null;
    }

    /**
     * Mettre à jour un client
     */
    public boolean modifierClient(Client client) {
        String sql = "UPDATE clients SET nom = ?, telephone = ?, email = ?, adresse = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getTelephone());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getAdresse());
            stmt.setInt(5, Integer.parseInt(client.getId()));
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du client: " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            System.err.println("ID invalide: " + client.getId());
            return false;
        }
    }

    /**
     * Rechercher et afficher des clients par nom ou téléphone
     */
    public void rechercherEtAfficherClients(String critere) {
        String sql = "SELECT * FROM clients WHERE nom LIKE ? OR telephone LIKE ? ORDER BY date_creation DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String pattern = "%" + critere + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            
            ResultSet rs = stmt.executeQuery();
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                afficherClient(rs);
            }
            
            if (!hasData) {
                System.out.println(ColorUtil.warning("Aucun client trouvé."));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
        }
    }

    /**
     * Afficher un client depuis un ResultSet
     */
    private void afficherClient(ResultSet rs) throws SQLException {
        System.out.println(ColorUtil.colorize("═══════════════════════════════════════════════════", ColorUtil.CYAN));
        System.out.println(ColorUtil.colorize("ID: ", ColorUtil.CYAN) + ColorUtil.highlight(String.valueOf(rs.getInt("id"))));
        System.out.println(ColorUtil.colorize("Nom: ", ColorUtil.CYAN) + ColorUtil.colorize(rs.getString("nom"), ColorUtil.YELLOW));
        System.out.println(ColorUtil.colorize("Téléphone: ", ColorUtil.CYAN) + ColorUtil.highlight(rs.getString("telephone")));
        String email = rs.getString("email");
        System.out.println(ColorUtil.colorize("Email: ", ColorUtil.CYAN) + 
                          (email != null ? ColorUtil.colorize(email, ColorUtil.CYAN) : ColorUtil.colorize("N/A", ColorUtil.WHITE)));
        String adresse = rs.getString("adresse");
        System.out.println(ColorUtil.colorize("Adresse: ", ColorUtil.CYAN) + 
                          (adresse != null ? ColorUtil.colorize(adresse, ColorUtil.CYAN) : ColorUtil.colorize("N/A", ColorUtil.WHITE)));
        System.out.println(ColorUtil.colorize("═══════════════════════════════════════════════════", ColorUtil.CYAN));
    }

    /**
     * Vérifier si un client existe par téléphone
     */
    public Client getClientByTelephone(String telephone) {
        String sql = "SELECT * FROM clients WHERE telephone = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, telephone);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du client: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Vérifier si un client existe par nom et téléphone
     */
    public Client getClientByNomEtTelephone(String nom, String telephone) {
        String sql = "SELECT * FROM clients WHERE nom = ? AND telephone = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, telephone);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du client: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Mapper un ResultSet vers un objet Client
     */
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        String id = String.valueOf(rs.getInt("id"));
        String nom = rs.getString("nom");
        String telephone = rs.getString("telephone");
        String email = rs.getString("email");
        String adresse = rs.getString("adresse");
        Timestamp dateCreation = rs.getTimestamp("date_creation");
        
        return new Client(id, nom, telephone, email, adresse, dateCreation);
    }
}
