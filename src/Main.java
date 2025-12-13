import java.util.Calendar;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private static ClientDAO clientDAO = new ClientDAO();
    private static VenteDAO venteDAO = new VenteDAO();

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("   SYSTÈME DE GESTION - CONCESSION VOITURES OCCASION");
        System.out.println("═══════════════════════════════════════════════════\n");
        
        // Vérification du mot de passe pour accéder à l'application
        if (!verifierMotDePasse()) {
            System.out.println("✗ Accès refusé. L'application se ferme.");
            return;
        }
        
        // Test de connexion
        DatabaseConnection db = DatabaseConnection.getInstance();
        
        if (!db.isConnected()) {
            System.out.println("✗ Échec de la connexion à la base de données!");
            System.out.println("Veuillez vérifier votre configuration MySQL.");
            return;
        }
        
        System.out.println("✓ Connexion à la base de données établie avec succès!\n");
        
        // Menu principal
        boolean continuer = true;
        while (continuer) {
            afficherMenuPrincipal();
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    menuVehicules();
                    break;
                case 2:
                    menuClients();
                    break;
                case 3:
                    menuRecherche();
                    break;
                case 4:
                    menuRapports();
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

    /**
     * Vérifier le mot de passe pour accéder à l'application
     */
    private static boolean verifierMotDePasse() {
        System.out.print("Mot de passe: ");
        String motDePasse = scanner.nextLine();
        
        if ("111111".equals(motDePasse)) {
            System.out.println("✓ Accès autorisé!\n");
            return true;
        } else {
            System.out.println("✗ Mot de passe incorrect!");
            return false;
        }
    }

    private static void afficherMenuPrincipal() {
        System.out.println("\n═══════════════════════════════════════════════════");
        System.out.println("              MENU PRINCIPAL");
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("1. Gestion des Véhicules");
        System.out.println("2. Gestion des Clients");
        System.out.println("3. Recherche et Filtrage");
        System.out.println("4. Rapports");
        System.out.println("0. Quitter");
        System.out.println("═══════════════════════════════════════════════════");
        System.out.print("Votre choix: ");
    }

    // ==================== MENU VÉHICULES ====================
    private static void menuVehicules() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- GESTION DES VÉHICULES ---");
            System.out.println("1. Ajouter un véhicule");
            System.out.println("2. Modifier un véhicule");
            System.out.println("3. Supprimer un véhicule");
            System.out.println("4. Afficher tous les véhicules");
            System.out.println("0. Retour au menu principal");
            System.out.print("Votre choix: ");
            
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    ajouterVehicule();
                    break;
                case 2:
                    modifierVehicule();
                    break;
                case 3:
                    supprimerVehicule();
                    break;
                case 4:
                    vehiculeDAO.afficherTousVehicules();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        }
    }

    private static void ajouterVehicule() {
        System.out.println("\n--- Ajout d'un véhicule ---");
        
        System.out.print("Marque: ");
        String marque = scanner.nextLine();
        
        System.out.print("Modèle: ");
        String modele = scanner.nextLine();
        
        System.out.print("Prix d'achat (DH): ");
        double prixAchat = lireDouble();
        
        System.out.print("Prix de vente (DH): ");
        double prixVente = lireDouble();
        
        // Validation: prix de vente > prix d'achat
        if (prixVente <= prixAchat) {
            System.out.println("✗ Erreur: Le prix de vente doit être supérieur au prix d'achat!");
            return;
        }
        
        System.out.print("Année: ");
        int annee = scanner.nextInt();
        scanner.nextLine();
        
        // Validation: année entre 1900 et année actuelle
        int anneeActuelle = Calendar.getInstance().get(Calendar.YEAR);
        if (annee < 1900 || annee > anneeActuelle) {
            System.out.println("✗ Erreur: Année invalide! Doit être entre 1900 et " + anneeActuelle);
            return;
        }
        
        System.out.print("Kilométrage: ");
        int kilometrage = scanner.nextInt();
        scanner.nextLine();
        
        // Validation: kilométrage positif
        if (kilometrage < 0) {
            System.out.println("✗ Erreur: Le kilométrage doit être positif!");
            return;
        }
        
        System.out.println("\nType de véhicule:");
        System.out.println("1. BERLINE");
        System.out.println("2. SUV");
        System.out.println("3. CAMION");
        System.out.println("4. ELECTRIQUE");
        System.out.print("Votre choix: ");
        int typeChoix = lireChoix();
        
        Vehicule vehicule = null;
        
        switch (typeChoix) {
            case 1:
                vehicule = new Berline(null, marque, modele, prixAchat, prixVente, annee, 
                                      kilometrage, "DISPONIBLE", null, null);
                break;
            case 2:
                vehicule = new SUV(null, marque, modele, prixAchat, prixVente, annee, 
                                  kilometrage, "DISPONIBLE", null, null);
                break;
            case 3:
                vehicule = new Camion(null, marque, modele, prixAchat, prixVente, annee, 
                                     kilometrage, "DISPONIBLE", null, null);
                break;
            case 4:
                vehicule = new Electrique(null, marque, modele, prixAchat, prixVente, annee, 
                                         kilometrage, "DISPONIBLE", null, null);
                break;
            default:
                System.out.println("Type invalide!");
                return;
        }
        
        if (vehiculeDAO.ajouterVehicule(vehicule)) {
            System.out.println("✓ Véhicule ajouté avec succès! ID: " + vehicule.getId());
            System.out.println("Prix final (avec taxes): " + 
                             String.format("%.2f", vehicule.calculerPrixFinal()) + " DH");
        } else {
            System.out.println("✗ Erreur lors de l'ajout du véhicule!");
        }
    }

    private static void modifierVehicule() {
        System.out.println("\n--- Modification d'un véhicule ---");
        System.out.print("ID du véhicule: ");
        String id = scanner.nextLine();
        
        Vehicule vehicule = vehiculeDAO.getVehiculeById(id);
        if (vehicule == null) {
            System.out.println("Véhicule introuvable!");
            return;
        }
        
        System.out.println("Véhicule actuel:");
        vehicule.Afficher();
        System.out.println();
        
        System.out.println("\n(Laissez vide pour conserver la valeur actuelle)");
        
        System.out.print("Nouveau statut (DISPONIBLE/EN_NEGOCIATION/VENDU): ");
        String statut = scanner.nextLine();
        if (!statut.isEmpty()) {
            vehicule.setStatut(statut.toUpperCase());
        }
        
        if (vehiculeDAO.modifierVehicule(vehicule)) {
            System.out.println("✓ Véhicule modifié avec succès!");
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

    // ==================== MENU CLIENTS ====================
    private static void menuClients() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- GESTION DES CLIENTS ---");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Modifier un client");
            System.out.println("3. Rechercher un client");
            System.out.println("4. Afficher tous les clients");
            System.out.println("0. Retour au menu principal");
            System.out.print("Votre choix: ");
            
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    ajouterClient();
                    break;
                case 2:
                    modifierClient();
                    break;
                case 3:
                    rechercherClient();
                    break;
                case 4:
                    clientDAO.afficherTousClients();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        }
    }

    private static void ajouterClient() {
        System.out.println("\n--- Ajout d'un client ---");
        
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        
        System.out.print("Téléphone: ");
        String telephone = scanner.nextLine();
        
        // Vérifier si le client existe déjà
        Client clientExistant = clientDAO.getClientByNomEtTelephone(nom, telephone);
        if (clientExistant != null) {
            System.out.println("\n⚠ Ce client existe déjà dans la base de données:");
            clientExistant.afficher();
            System.out.print("\nVoulez-vous continuer quand même? (o/n): ");
            String reponse = scanner.nextLine();
            if (!reponse.equalsIgnoreCase("o")) {
                System.out.println("Ajout annulé.");
                return;
            }
        } else {
            // Vérifier aussi par téléphone seul
            clientExistant = clientDAO.getClientByTelephone(telephone);
            if (clientExistant != null) {
                System.out.println("\n⚠ Un client avec ce numéro de téléphone existe déjà:");
                clientExistant.afficher();
                System.out.print("\nVoulez-vous continuer quand même? (o/n): ");
                String reponse = scanner.nextLine();
                if (!reponse.equalsIgnoreCase("o")) {
                    System.out.println("Ajout annulé.");
                    return;
                }
            }
        }
        
        System.out.print("Email (optionnel): ");
        String email = scanner.nextLine();
        if (email.isEmpty()) email = null;
        
        System.out.print("Adresse (optionnel): ");
        String adresse = scanner.nextLine();
        if (adresse.isEmpty()) adresse = null;
        
        Client client = new Client(null, nom, telephone, email, adresse);
        
        if (clientDAO.ajouterClient(client)) {
            System.out.println("\n✓ Client ajouté avec succès! ID: " + client.getId());
            System.out.println("\nInformations du client:");
            client.afficher();
        } else {
            System.out.println("✗ Erreur lors de l'ajout du client!");
        }
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
        
        System.out.println("\n(Laissez vide pour conserver la valeur actuelle)");
        
        System.out.print("Nouveau nom: ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) client.setNom(nom);
        
        System.out.print("Nouveau téléphone: ");
        String telephone = scanner.nextLine();
        if (!telephone.isEmpty()) client.setTelephone(telephone);
        
        System.out.print("Nouvel email: ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) client.setEmail(email);
        
        System.out.print("Nouvelle adresse: ");
        String adresse = scanner.nextLine();
        if (!adresse.isEmpty()) client.setAdresse(adresse);
        
        if (clientDAO.modifierClient(client)) {
            System.out.println("✓ Client modifié avec succès!");
        } else {
            System.out.println("✗ Erreur lors de la modification!");
        }
    }

    private static void rechercherClient() {
        System.out.println("\n--- Recherche d'un client ---");
        System.out.print("Nom ou téléphone: ");
        String critere = scanner.nextLine();
        
        clientDAO.rechercherEtAfficherClients(critere);
    }

    // ==================== MENU RECHERCHE ====================
    private static void menuRecherche() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- RECHERCHE ET FILTRAGE ---");
            System.out.println("1. Recherche par marque");
            System.out.println("2. Recherche par prix");
            System.out.println("3. Recherche par catégorie");
            System.out.println("4. Recherche avancée");
            System.out.println("0. Retour au menu principal");
            System.out.print("Votre choix: ");
            
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    rechercherParMarque();
                    break;
                case 2:
                    rechercherParPrix();
                    break;
                case 3:
                    rechercherParCategorie();
                    break;
                case 4:
                    rechercherAvancee();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide!");
            }
        }
    }

    private static void rechercherParMarque() {
        System.out.print("\nMarque à rechercher: ");
        String marque = scanner.nextLine();
        vehiculeDAO.rechercherEtAfficherVehicules(marque, null, null, null);
    }

    private static void rechercherParPrix() {
        System.out.print("\nPrix maximum (DH): ");
        double prixMax = lireDouble();
        
        if (prixMax > 0) {
            vehiculeDAO.rechercherEtAfficherVehicules(null, null, prixMax, null);
        } else {
            System.out.println("Prix invalide!");
        }
    }

    private static void rechercherParCategorie() {
        System.out.println("\nCatégories disponibles:");
        System.out.println("1. BERLINE");
        System.out.println("2. SUV");
        System.out.println("3. CAMION");
        System.out.println("4. ELECTRIQUE");
        System.out.print("Votre choix: ");
        
        int choix = lireChoix();
        String type = "";
        
        switch (choix) {
            case 1: type = "BERLINE"; break;
            case 2: type = "SUV"; break;
            case 3: type = "CAMION"; break;
            case 4: type = "ELECTRIQUE"; break;
            default:
                System.out.println("Choix invalide!");
                return;
        }
        
        vehiculeDAO.rechercherEtAfficherVehicules(null, type, null, null);
    }

    private static void rechercherAvancee() {
        System.out.println("\n--- Recherche avancée ---");
        System.out.print("Marque (laisser vide pour ignorer): ");
        String marque = scanner.nextLine();
        
        System.out.print("Type (BERLINE/SUV/CAMION/ELECTRIQUE, laisser vide pour ignorer): ");
        String type = scanner.nextLine();
        if (!type.isEmpty()) type = type.toUpperCase();
        
        System.out.print("Prix maximum (0 pour ignorer): ");
        double prixMax = lireDouble();
        
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

    // ==================== MENU RAPPORTS ====================
    private static void menuRapports() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- RAPPORTS ---");
            System.out.println("1. Véhicules disponibles");
            System.out.println("2. Véhicules vendus");
            System.out.println("3. Statistiques de ventes");
            System.out.println("0. Retour au menu principal");
            System.out.print("Votre choix: ");
            
            int choix = lireChoix();
            
            switch (choix) {
                case 1:
                    vehiculeDAO.afficherVehiculesDisponibles();
                    break;
                case 2:
                    afficherVehiculesVendus();
                    break;
                case 3:
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

    private static void afficherVehiculesVendus() {
        System.out.println("\n=== VÉHICULES VENDUS ===");
        vehiculeDAO.afficherVehiculesVendus();
    }

    // ==================== UTILITAIRES ====================
    private static int lireChoix() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static double lireDouble() {
        try {
            double valeur = Double.parseDouble(scanner.nextLine());
            return valeur;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
