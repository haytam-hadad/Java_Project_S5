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
        String sql = "INSERT INTO clients (id, nom, telephone, email, adresse, date_creation) VALUES (?, ?, ?, ?, ?, NOW())";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getId());
            stmt.setString(2, client.getNom());
            stmt.setString(3, client.getTelephone());
            stmt.setString(4, client.getEmail());
            stmt.setString(5, client.getAdresse());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
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
                System.out.println("Aucun client trouvé.");
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
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du client: " + e.getMessage());
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
            stmt.setString(5, client.getId());
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du client: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprimer un client
     */
    public boolean supprimerClient(String id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du client: " + e.getMessage());
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
                System.out.println("Aucun client trouvé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
        }
    }

    /**
     * Afficher un client depuis un ResultSet
     */
    private void afficherClient(ResultSet rs) throws SQLException {
        System.out.println("ID: " + rs.getString("id") + " | Nom: " + rs.getString("nom") + 
                          " | Téléphone: " + rs.getString("telephone") + 
                          " | Email: " + (rs.getString("email") != null ? rs.getString("email") : "N/A") + 
                          " | Adresse: " + (rs.getString("adresse") != null ? rs.getString("adresse") : "N/A"));
    }

    /**
     * Mapper un ResultSet vers un objet Client
     */
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nom = rs.getString("nom");
        String telephone = rs.getString("telephone");
        String email = rs.getString("email");
        String adresse = rs.getString("adresse");
        Timestamp dateCreation = rs.getTimestamp("date_creation");
        
        return new Client(id, nom, telephone, email, adresse, dateCreation);
    }

}
