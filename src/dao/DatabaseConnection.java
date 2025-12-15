package dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Classe simple de gestion de la connexion à la base de données MySQL
 * Sans lecture depuis un fichier .env
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    // Configuration de la base de données
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_voitures";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; // Mettre le mot de passe ici si nécessaire

    /**
     * Constructeur privé (Singleton)
     */
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connexion à la base de données réussie !");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL introuvable : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
    }

    /**
     * Retourne l'instance unique
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
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la connexion : " + e.getMessage());
        }
        return connection;
    }

    /**
     * Ferme la connexion
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion fermée !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture : " + e.getMessage());
        }
    }

    /**
     * Vérifie si la connexion est active
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
