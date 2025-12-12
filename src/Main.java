import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private static ClientDAO clientDAO = new ClientDAO();
    private static VenteDAO venteDAO = new VenteDAO();
    private static String clientIdActuel = null; // Pour suivre le client connecté

    public static void main(String[] args) {
        System.out.println("=== GESTION DE VENTE DE VÉHICULES D'OCCASION ===\n");
        
        // Test de connexion
        DatabaseConnection db = DatabaseConnection.getInstance();
        
        if (!db.isConnected()) {
            System.out.println("✗ Échec de la connexion à la base de données!");
            System.out.println("Veuillez vérifier votre configuration MySQL.");
            return;
        }
        
        System.out.println("✓ Connexion à la base de données établie avec succès!\n");
        
        // Menu de sélection du type d'utilisateur
        boolean continuer = true;
        while (continuer) {
            afficherMenuTypeUtilisateur();
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    menuClient();
                    break;
                case 2:
                    if (verifierMotDePasseAdmin()) {
                        menuAdmin();
                    }
                    break;
                case 0:
                    continuer = false;
                    System.out.println("\nMerci d'avoir utilisé le système de gestion!");
                    db.closeConnection();
                    break;
                default:
                    System.out.println("Choix invalide! Veuillez réessayer.");
            }
        }
        
        scanner.close();
    }

    private static void afficherMenuTypeUtilisateur() {
        System.out.println("\n========== TYPE D'UTILISATEUR ==========");
        System.out.println("1. Client (Acheter un véhicule)");
        System.out.println("2. Administrateur (Gestion complète)");
        System.out.println("0. Quitter");
        System.out.println("=========================================");
        System.out.print("Votre choix: ");
    }

    /**
     * Vérifier le mot de passe admin
     */
    private static boolean verifierMotDePasseAdmin() {
        System.out.print("\nMot de passe administrateur: ");
        String motDePasse = scanner.nextLine();
        
        if ("111111".equals(motDePasse)) {
            System.out.println("✓ Accès autorisé!");
            return true;
        } else {
            System.out.println("✗ Mot de passe incorrect! Accès refusé.");
            return false;
        }
    }

    // ==================== MENU CLIENT ====================
    private static void menuClient() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n========== MENU CLIENT ==========");
            System.out.println("1. Voir les véhicules disponibles");
            System.out.println("2. Rechercher un véhicule");
            System.out.println("3. Acheter un véhicule");
            System.out.println("4. Créer un compte client");
            System.out.println("0. Retour au menu principal");
            System.out.println("=================================");
            System.out.print("Votre choix: ");
            
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    vehiculeDAO.afficherVehiculesDisponibles();
                    break;
                case 2:
                    rechercherVehiculeClient();
                    break;
                case 3:
                    acheterVehicule();
                    break;
                case 4:
                    creerCompteClient();
                    break;
                case 0:
                    continuer = false;
                    clientIdActuel = null;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        }
    }

    private static void creerCompteClient() {
        System.out.println("\n--- Création de compte client ---");
        System.out.print("ID client: ");
        String id = scanner.nextLine();
        
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        
        System.out.print("Téléphone: ");
        String telephone = scanner.nextLine();
        
        System.out.print("Email (optionnel): ");
        String email = scanner.nextLine();
        
        System.out.print("Adresse (optionnel): ");
        String adresse = scanner.nextLine();
        
        Client client = new Client(id, nom, telephone, email, adresse);
        
        if (clientDAO.ajouterClient(client)) {
            System.out.println("✓ Compte client créé avec succès!");
            clientIdActuel = id;
        } else {
            System.out.println("✗ Erreur lors de la création du compte!");
        }
    }

    private static void rechercherVehiculeClient() {
        System.out.println("\n--- Recherche de véhicule ---");
        System.out.print("Marque (laisser vide pour ignorer): ");
        String marque = scanner.nextLine();
        
        System.out.print("Type (BERLINE/SUV/CAMION/ELECTRIQUE, laisser vide pour ignorer): ");
        String type = scanner.nextLine();
        if (!type.isEmpty()) type = type.toUpperCase();
        
        System.out.print("Prix maximum (0 pour ignorer): ");
        double prixMax = 0;
        try {
            prixMax = scanner.nextDouble();
        } catch (Exception e) {
            prixMax = 0;
        }
        scanner.nextLine();
        
        System.out.print("Année minimum (0 pour ignorer): ");
        int anneeMin = 0;
        try {
            anneeMin = scanner.nextInt();
        } catch (Exception e) {
            anneeMin = 0;
        }
        scanner.nextLine();
        
        vehiculeDAO.rechercherEtAfficherVehicules(
            marque.isEmpty() ? null : marque,
            type.isEmpty() ? null : type,
            prixMax == 0 ? null : prixMax,
            anneeMin == 0 ? null : anneeMin
        );
    }

    private static void acheterVehicule() {
        System.out.println("\n--- Achat d'un véhicule ---");
        
        // Demander l'ID du client s'il n'est pas connecté
        if (clientIdActuel == null) {
            System.out.print("Votre ID client: ");
            clientIdActuel = scanner.nextLine();
        }
        
        // Afficher les véhicules disponibles
        System.out.println("\nVéhicules disponibles:");
        vehiculeDAO.afficherVehiculesDisponibles();
        
        System.out.print("\nID du véhicule à acheter: ");
        String vehiculeId = scanner.nextLine();
        
        if (venteDAO.enregistrerVente(vehiculeId, clientIdActuel)) {
            System.out.println("\nFélicitations! Vous avez acheté ce véhicule.");
        }
    }

    // ==================== MENU ADMIN ====================
    private static void menuAdmin() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n========== MENU ADMINISTRATEUR ==========");
            System.out.println("1. Gestion des véhicules");
            System.out.println("2. Gestion des clients");
            System.out.println("3. Gestion des ventes");
            System.out.println("4. Statistiques");
            System.out.println("0. Retour au menu principal");
            System.out.println("==========================================");
            System.out.print("Votre choix: ");
            
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    menuVehiculesAdmin();
                    break;
                case 2:
                    menuClientsAdmin();
                    break;
                case 3:
                    menuVentesAdmin();
                    break;
                case 4:
                    venteDAO.afficherStatistiques();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        }
    }

    private static void menuVehiculesAdmin() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- GESTION DES VÉHICULES ---");
            System.out.println("1. Ajouter un véhicule");
            System.out.println("2. Lister tous les véhicules");
            System.out.println("3. Lister les véhicules disponibles");
            System.out.println("4. Rechercher un véhicule");
            System.out.println("5. Modifier le statut d'un véhicule");
            System.out.println("6. Supprimer un véhicule");
            System.out.println("0. Retour au menu admin");
            System.out.print("Votre choix: ");
            
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    ajouterVehicule();
                    break;
                case 2:
                    vehiculeDAO.afficherTousVehicules();
                    break;
                case 3:
                    vehiculeDAO.afficherVehiculesDisponibles();
                    break;
                case 4:
                    rechercherVehiculeAdmin();
                    break;
                case 5:
                    modifierStatutVehicule();
                    break;
                case 6:
                    supprimerVehicule();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        }
    }

    private static void menuClientsAdmin() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- GESTION DES CLIENTS ---");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Lister tous les clients");
            System.out.println("3. Rechercher un client");
            System.out.println("4. Modifier un client");
            System.out.println("5. Supprimer un client");
            System.out.println("0. Retour au menu admin");
            System.out.print("Votre choix: ");
            
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    ajouterClient();
                    break;
                case 2:
                    clientDAO.afficherTousClients();
                    break;
                case 3:
                    rechercherClient();
                    break;
                case 4:
                    modifierClient();
                    break;
                case 5:
                    supprimerClient();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        }
    }

    private static void menuVentesAdmin() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- GESTION DES VENTES ---");
            System.out.println("1. Enregistrer une vente");
            System.out.println("2. Voir les statistiques");
            System.out.println("0. Retour au menu admin");
            System.out.print("Votre choix: ");
            
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    enregistrerVenteAdmin();
                    break;
                case 2:
                    venteDAO.afficherStatistiques();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        }
    }

    // Méthodes pour les véhicules (Admin)
    private static void ajouterVehicule() {
        System.out.println("\n--- Ajout d'un véhicule ---");
        System.out.print("ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Marque: ");
        String marque = scanner.nextLine();
        
        System.out.print("Modèle: ");
        String modele = scanner.nextLine();
        
        System.out.print("Prix d'achat: ");
        double prixAchat = scanner.nextDouble();
        
        System.out.print("Prix de vente: ");
        double prixVente = scanner.nextDouble();
        
        System.out.print("Année: ");
        int annee = scanner.nextInt();
        
        System.out.print("Kilométrage: ");
        int kilometrage = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Type (BERLINE/SUV/CAMION/ELECTRIQUE): ");
        String type = scanner.nextLine().toUpperCase();
        
        System.out.print("Statut (DISPONIBLE/EN_NEGOCIATION/VENDU): ");
        String statut = scanner.nextLine().toUpperCase();
        
        Vehicule vehicule = new Vehicule(id, marque, modele, prixAchat, prixVente, 
                                        annee, kilometrage, type, statut, null, null);
        
        if (vehiculeDAO.ajouterVehicule(vehicule)) {
            System.out.println("✓ Véhicule ajouté avec succès!");
        } else {
            System.out.println("✗ Erreur lors de l'ajout du véhicule!");
        }
    }

    private static void rechercherVehiculeAdmin() {
        System.out.println("\n--- Recherche de véhicule ---");
        System.out.print("Marque (laisser vide pour ignorer): ");
        String marque = scanner.nextLine();
        
        System.out.print("Type (BERLINE/SUV/CAMION/ELECTRIQUE, laisser vide pour ignorer): ");
        String type = scanner.nextLine();
        if (!type.isEmpty()) type = type.toUpperCase();
        
        System.out.print("Prix maximum (0 pour ignorer): ");
        double prixMax = 0;
        try {
            prixMax = scanner.nextDouble();
        } catch (Exception e) {
            prixMax = 0;
        }
        scanner.nextLine();
        
        System.out.print("Année minimum (0 pour ignorer): ");
        int anneeMin = 0;
        try {
            anneeMin = scanner.nextInt();
        } catch (Exception e) {
            anneeMin = 0;
        }
        scanner.nextLine();
        
        vehiculeDAO.rechercherEtAfficherVehicules(
            marque.isEmpty() ? null : marque,
            type.isEmpty() ? null : type,
            prixMax == 0 ? null : prixMax,
            anneeMin == 0 ? null : anneeMin
        );
    }

    private static void modifierStatutVehicule() {
        System.out.println("\n--- Modification du statut d'un véhicule ---");
        System.out.print("ID du véhicule: ");
        String id = scanner.nextLine();
        
        System.out.print("Nouveau statut (DISPONIBLE/EN_NEGOCIATION/VENDU): ");
        String statut = scanner.nextLine().toUpperCase();
        
        if (vehiculeDAO.updateStatut(id, statut)) {
            System.out.println("✓ Statut modifié avec succès!");
        } else {
            System.out.println("✗ Erreur lors de la modification!");
        }
    }

    private static void supprimerVehicule() {
        System.out.println("\n--- Suppression d'un véhicule ---");
        System.out.print("ID du véhicule: ");
        String id = scanner.nextLine();
        
        System.out.print("Êtes-vous sûr? (o/n): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("o")) {
            if (vehiculeDAO.supprimerVehicule(id)) {
                System.out.println("✓ Véhicule supprimé avec succès!");
            } else {
                System.out.println("✗ Erreur lors de la suppression!");
            }
        }
    }

    // Méthodes pour les clients (Admin)
    private static void ajouterClient() {
        System.out.println("\n--- Ajout d'un client ---");
        System.out.print("ID: ");
        String id = scanner.nextLine();
        
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        
        System.out.print("Téléphone: ");
        String telephone = scanner.nextLine();
        
        System.out.print("Email (optionnel): ");
        String email = scanner.nextLine();
        
        System.out.print("Adresse (optionnel): ");
        String adresse = scanner.nextLine();
        
        Client client = new Client(id, nom, telephone, email, adresse);
        
        if (clientDAO.ajouterClient(client)) {
            System.out.println("✓ Client ajouté avec succès!");
        } else {
            System.out.println("✗ Erreur lors de l'ajout du client!");
        }
    }

    private static void rechercherClient() {
        System.out.println("\n--- Recherche d'un client ---");
        System.out.print("Nom ou téléphone: ");
        String critere = scanner.nextLine();
        
        clientDAO.rechercherEtAfficherClients(critere);
    }

    private static void modifierClient() {
        System.out.println("\n--- Modification d'un client ---");
        System.out.print("ID du client: ");
        String id = scanner.nextLine();
        
        Client client = clientDAO.getClientById(id);
        if (client == null) {
            System.out.println("Client introuvable!");
            return;
        }
        
        System.out.println("Client actuel:");
        client.afficher();
        
        System.out.print("\nNouveau nom (laisser vide pour garder): ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) client.setNom(nom);
        
        System.out.print("Nouveau téléphone (laisser vide pour garder): ");
        String telephone = scanner.nextLine();
        if (!telephone.isEmpty()) client.setTelephone(telephone);
        
        System.out.print("Nouvel email (laisser vide pour garder): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) client.setEmail(email);
        
        System.out.print("Nouvelle adresse (laisser vide pour garder): ");
        String adresse = scanner.nextLine();
        if (!adresse.isEmpty()) client.setAdresse(adresse);
        
        if (clientDAO.modifierClient(client)) {
            System.out.println("✓ Client modifié avec succès!");
        } else {
            System.out.println("✗ Erreur lors de la modification!");
        }
    }

    private static void supprimerClient() {
        System.out.println("\n--- Suppression d'un client ---");
        System.out.print("ID du client: ");
        String id = scanner.nextLine();
        
        System.out.print("Êtes-vous sûr? (o/n): ");
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("o")) {
            if (clientDAO.supprimerClient(id)) {
                System.out.println("✓ Client supprimé avec succès!");
            } else {
                System.out.println("✗ Erreur lors de la suppression!");
            }
        }
    }

    // Méthodes pour les ventes (Admin)
    private static void enregistrerVenteAdmin() {
        System.out.println("\n--- Enregistrement d'une vente ---");
        
        System.out.println("Véhicules disponibles:");
        vehiculeDAO.afficherVehiculesDisponibles();
        
        System.out.print("\nID du véhicule: ");
        String vehiculeId = scanner.nextLine();
        
        System.out.println("\nClients:");
        clientDAO.afficherTousClients();
        
        System.out.print("\nID du client: ");
        String clientId = scanner.nextLine();
        
        venteDAO.enregistrerVente(vehiculeId, clientId);
    }

    private static int lireChoix() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
