import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
       System.out.println("=== Test de connexion à la base de données ===\n");
        
        // Test 1: Obtenir l'instance et la connexion
        System.out.println("Test 1: Connexion à la base de données...");
        DatabaseConnection db = DatabaseConnection.getInstance();
        Connection conn = db.getConnection();
        
        if (db.isConnected()) {
            System.out.println("✓ Connexion établie avec succès!\n");
        } else {
            System.out.println("✗ Échec de la connexion!\n");
            return;
        }
    }
}