import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe simple de gestion de la connexion à la base de données MySQL
 */
public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    // Configuration de la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_voitures";
    private static final String USERNAME = "root";
    // Le mot de passe est lu depuis un fichier d'environnement simple (db.env)
    private static final String ENV_FILE = "db.env";
    private static final String PASSWORD_KEY = "DB_PASSWORD";
    
    /**
     * Constructeur privé pour le pattern Singleton
     */
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String password = loadPasswordFromEnvFile();
            this.connection = DriverManager.getConnection(URL, USERNAME, password);
            System.out.println("Connexion à la base de données réussie!");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL introuvable: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur de connexion: " + e.getMessage());
        }
    }
    
    /**
     * Retourne l'instance unique de la connexion (Singleton)
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Retourne la connexion active
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String password = loadPasswordFromEnvFile();
                connection = DriverManager.getConnection(URL, USERNAME, password);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la connexion: " + e.getMessage());
        }
        return connection;
    }
    
    /**
     * Ferme la connexion à la base de données
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée!");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture: " + e.getMessage());
        }
    }
    
    /**
     * Teste si la connexion est active
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Lit le mot de passe MySQL depuis le fichier db.env (clé DB_PASSWORD).
     * Si le fichier ou la clé n'existent pas, retourne une chaîne vide (comportement actuel).
     */
    private String loadPasswordFromEnvFile() {
        String password = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(ENV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(PASSWORD_KEY + "=")) {
                    password = line.substring((PASSWORD_KEY + "=").length());
                    break;
                }
            }
        } catch (IOException e) {
            // Fichier absent ou problème de lecture : on garde le mot de passe vide par défaut
        }
        return password;
    }
}