import java.util.Calendar;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private static ClientDAO clientDAO = new ClientDAO();
    private static VenteDAO venteDAO = new VenteDAO();

    public static void main(String[] args) {
        System.out.println(ColorUtil.colorize("═══════════════════════════════════════════════════", ColorUtil.CYAN));
        System.out.println(ColorUtil.title("   SYSTÈME DE GESTION - CONCESSION VOITURES OCCASION"));
        System.out.println(ColorUtil.colorize("═══════════════════════════════════════════════════", ColorUtil.CYAN) + "\n");
        
        // Vérification du mot de passe pour accéder à l'application
        if (!verifierMotDePasse()) {
            System.out.println(ColorUtil.error("Accès refusé. L'application se ferme."));
            return;
        }
        
        // Test de connexion
        DatabaseConnection db = DatabaseConnection.getInstance();
        
        if (!db.isConnected()) {
            System.out.println(ColorUtil.error("Échec de la connexion à la base de données!"));
            System.out.println(ColorUtil.warning("Veuillez vérifier votre configuration MySQL."));
            return;
        }
        
        System.out.println(ColorUtil.success("Connexion à la base de données établie avec succès!") + "\n");
        
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
                    System.out.println("\n" + ColorUtil.info("Merci d'avoir utilisé le système de gestion!"));
                    db.closeConnection();
                    break;
                default:
                    System.out.println(ColorUtil.error("Choix invalide! Veuillez réessayer."));
            }
        }
        
        scanner.close();
    }

    /**
     * Vérifier le mot de passe pour accéder à l'application
     */
    private static boolean verifierMotDePasse() {
        System.out.print(ColorUtil.info("Mot de passe: "));
        String motDePasse = scanner.nextLine();
        
        if ("111111".equals(motDePasse)) {
            System.out.println(ColorUtil.success("Accès autorisé!") + "\n");
            return true;
        } else {
            System.out.println(ColorUtil.error("Mot de passe incorrect!"));
            return false;
        }
    }

    private static void afficherMenuPrincipal() {
        System.out.println("\n" + ColorUtil.colorize("═══════════════════════════════════════════════════", ColorUtil.CYAN));
        System.out.println(ColorUtil.header("              MENU PRINCIPAL"));
        System.out.println(ColorUtil.colorize("═══════════════════════════════════════════════════", ColorUtil.CYAN));
        System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "Gestion des Véhicules");
        System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "Gestion des Clients");
        System.out.println(ColorUtil.colorize("3. ", ColorUtil.YELLOW) + "Recherche et Filtrage");
        System.out.println(ColorUtil.colorize("4. ", ColorUtil.YELLOW) + "Rapports");
        System.out.println(ColorUtil.colorize("0. ", ColorUtil.RED) + "Quitter");
        System.out.println(ColorUtil.colorize("═══════════════════════════════════════════════════", ColorUtil.CYAN));
        System.out.print(ColorUtil.info("Votre choix: "));
    }

    // ==================== MENU VÉHICULES ====================
    private static void menuVehicules() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n" + ColorUtil.header("--- GESTION DES VÉHICULES ---"));
            System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "Ajouter un véhicule");
            System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "Modifier un véhicule");
            System.out.println(ColorUtil.colorize("3. ", ColorUtil.YELLOW) + "Supprimer un véhicule");
            System.out.println(ColorUtil.colorize("4. ", ColorUtil.YELLOW) + "Afficher tous les véhicules");
            System.out.println(ColorUtil.colorize("0. ", ColorUtil.RED) + "Retour au menu principal");
            System.out.print(ColorUtil.info("Votre choix: "));
            
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
                    System.out.println(ColorUtil.error("Choix invalide!"));
            }
        }
    }

    private static void ajouterVehicule() {
        System.out.println("\n" + ColorUtil.header("--- Ajout d'un véhicule ---"));
        
        System.out.print(ColorUtil.info("Marque: "));
        String marque = scanner.nextLine();
        
        System.out.print(ColorUtil.info("Modèle: "));
        String modele = scanner.nextLine();
        
        System.out.print(ColorUtil.info("Prix d'achat (DH): "));
        double prixAchat = lireDouble();
        
        System.out.print(ColorUtil.info("Prix de vente (DH): "));
        double prixVente = lireDouble();
        
        // Validation: prix de vente > prix d'achat
        if (prixVente <= prixAchat) {
            System.out.println(ColorUtil.error("Erreur: Le prix de vente doit être supérieur au prix d'achat!"));
            return;
        }
        
        System.out.print(ColorUtil.info("Année: "));
        int annee = scanner.nextInt();
        scanner.nextLine();
        
        // Validation: année entre 1900 et année actuelle
        int anneeActuelle = Calendar.getInstance().get(Calendar.YEAR);
        if (annee < 1900 || annee > anneeActuelle) {
            System.out.println(ColorUtil.error("Erreur: Année invalide! Doit être entre 1900 et " + anneeActuelle));
            return;
        }
        
        System.out.print(ColorUtil.info("Kilométrage: "));
        int kilometrage = scanner.nextInt();
        scanner.nextLine();
        
        // Validation: kilométrage positif
        if (kilometrage < 0) {
            System.out.println(ColorUtil.error("Erreur: Le kilométrage doit être positif!"));
            return;
        }
        
        System.out.println("\n" + ColorUtil.info("Type de véhicule:"));
        System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "BERLINE");
        System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "SUV");
        System.out.println(ColorUtil.colorize("3. ", ColorUtil.YELLOW) + "CAMION");
        System.out.println(ColorUtil.colorize("4. ", ColorUtil.YELLOW) + "ELECTRIQUE");
        System.out.print(ColorUtil.info("Votre choix: "));
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
                System.out.println(ColorUtil.error("Type invalide!"));
                return;
        }
        
        if (vehiculeDAO.ajouterVehicule(vehicule)) {
            System.out.println(ColorUtil.success("Véhicule ajouté avec succès! ID: " + ColorUtil.highlight(vehicule.getId())));
            System.out.println(ColorUtil.info("Prix final (avec taxes): ") + 
                             ColorUtil.highlight(String.format("%.2f", vehicule.calculerPrixFinal()) + " DH"));
        } else {
            System.out.println(ColorUtil.error("Erreur lors de l'ajout du véhicule!"));
        }
    }

    private static void modifierVehicule() {
        System.out.println("\n" + ColorUtil.header("--- Modification d'un véhicule ---"));
        System.out.print(ColorUtil.info("ID du véhicule: "));
        String id = scanner.nextLine();
        
        Vehicule vehicule = vehiculeDAO.getVehiculeById(id);
        if (vehicule == null) {
            System.out.println(ColorUtil.error("Véhicule introuvable!"));
            return;
        }
        
        System.out.println(ColorUtil.info("Véhicule actuel:"));
        vehicule.Afficher();
        System.out.println();
        
        System.out.println("\n" + ColorUtil.colorize("(Laissez vide pour conserver la valeur actuelle)", ColorUtil.YELLOW));
        
        System.out.print(ColorUtil.info("Nouveau statut (DISPONIBLE/EN_NEGOCIATION/VENDU): "));
        String statut = scanner.nextLine();
        if (!statut.isEmpty()) {
            vehicule.setStatut(statut.toUpperCase());
        }
        
        if (vehiculeDAO.modifierVehicule(vehicule)) {
            System.out.println(ColorUtil.success("Véhicule modifié avec succès!"));
        } else {
            System.out.println(ColorUtil.error("Erreur lors de la modification!"));
        }
    }

    private static void supprimerVehicule() {
        System.out.println("\n" + ColorUtil.header("--- Suppression d'un véhicule ---"));
        System.out.print(ColorUtil.info("ID du véhicule: "));
        String id = scanner.nextLine();
        
        System.out.print(ColorUtil.warning("Êtes-vous sûr? (o/n): "));
        String confirmation = scanner.nextLine();
        
        if (confirmation.equalsIgnoreCase("o")) {
            if (vehiculeDAO.supprimerVehicule(id)) {
                System.out.println(ColorUtil.success("Véhicule supprimé avec succès!"));
            } else {
                System.out.println(ColorUtil.error("Erreur lors de la suppression!"));
            }
        }
    }

    // ==================== MENU CLIENTS ====================
    private static void menuClients() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n" + ColorUtil.header("--- GESTION DES CLIENTS ---"));
            System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "Ajouter un client");
            System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "Modifier un client");
            System.out.println(ColorUtil.colorize("3. ", ColorUtil.YELLOW) + "Rechercher un client");
            System.out.println(ColorUtil.colorize("4. ", ColorUtil.YELLOW) + "Afficher tous les clients");
            System.out.println(ColorUtil.colorize("0. ", ColorUtil.RED) + "Retour au menu principal");
            System.out.print(ColorUtil.info("Votre choix: "));
            
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
                    System.out.println(ColorUtil.error("Choix invalide!"));
            }
        }
    }

    private static void ajouterClient() {
        System.out.println("\n" + ColorUtil.header("--- Ajout d'un client ---"));
        
        System.out.print(ColorUtil.info("Nom: "));
        String nom = scanner.nextLine();
        
        System.out.print(ColorUtil.info("Téléphone: "));
        String telephone = scanner.nextLine();
        
        // Vérifier si le client existe déjà
        Client clientExistant = clientDAO.getClientByNomEtTelephone(nom, telephone);
        if (clientExistant != null) {
            System.out.println("\n" + ColorUtil.warning("Ce client existe déjà dans la base de données:"));
            clientExistant.afficher();
            System.out.print("\n" + ColorUtil.warning("Voulez-vous continuer quand même? (o/n): "));
            String reponse = scanner.nextLine();
            if (!reponse.equalsIgnoreCase("o")) {
                System.out.println(ColorUtil.info("Ajout annulé."));
                return;
            }
        } else {
            // Vérifier aussi par téléphone seul
            clientExistant = clientDAO.getClientByTelephone(telephone);
            if (clientExistant != null) {
                System.out.println("\n" + ColorUtil.warning("Un client avec ce numéro de téléphone existe déjà:"));
                clientExistant.afficher();
                System.out.print("\n" + ColorUtil.warning("Voulez-vous continuer quand même? (o/n): "));
                String reponse = scanner.nextLine();
                if (!reponse.equalsIgnoreCase("o")) {
                    System.out.println(ColorUtil.info("Ajout annulé."));
                    return;
                }
            }
        }
        
        System.out.print(ColorUtil.info("Email (optionnel): "));
        String email = scanner.nextLine();
        if (email.isEmpty()) email = null;
        
        System.out.print(ColorUtil.info("Adresse (optionnel): "));
        String adresse = scanner.nextLine();
        if (adresse.isEmpty()) adresse = null;
        
        Client client = new Client(null, nom, telephone, email, adresse);
        
        if (clientDAO.ajouterClient(client)) {
            System.out.println("\n" + ColorUtil.success("Client ajouté avec succès! ID: " + ColorUtil.highlight(client.getId())));
            System.out.println("\n" + ColorUtil.info("Informations du client:"));
            client.afficher();
        } else {
            System.out.println(ColorUtil.error("Erreur lors de l'ajout du client!"));
        }
    }

    private static void modifierClient() {
        System.out.println("\n" + ColorUtil.header("--- Modification d'un client ---"));
        System.out.print(ColorUtil.info("ID du client: "));
        String id = scanner.nextLine();
        
        Client client = clientDAO.getClientById(id);
        if (client == null) {
            System.out.println(ColorUtil.error("Client introuvable!"));
            return;
        }
        
        System.out.println(ColorUtil.info("Client actuel:"));
        client.afficher();
        
        System.out.println("\n" + ColorUtil.colorize("(Laissez vide pour conserver la valeur actuelle)", ColorUtil.YELLOW));
        
        System.out.print(ColorUtil.info("Nouveau nom: "));
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) client.setNom(nom);
        
        System.out.print(ColorUtil.info("Nouveau téléphone: "));
        String telephone = scanner.nextLine();
        if (!telephone.isEmpty()) client.setTelephone(telephone);
        
        System.out.print(ColorUtil.info("Nouvel email: "));
        String email = scanner.nextLine();
        if (!email.isEmpty()) client.setEmail(email);
        
        System.out.print(ColorUtil.info("Nouvelle adresse: "));
        String adresse = scanner.nextLine();
        if (!adresse.isEmpty()) client.setAdresse(adresse);
        
        if (clientDAO.modifierClient(client)) {
            System.out.println(ColorUtil.success("Client modifié avec succès!"));
        } else {
            System.out.println(ColorUtil.error("Erreur lors de la modification!"));
        }
    }

    private static void rechercherClient() {
        System.out.println("\n" + ColorUtil.header("--- Recherche d'un client ---"));
        System.out.print(ColorUtil.info("Nom ou téléphone: "));
        String critere = scanner.nextLine();
        
        clientDAO.rechercherEtAfficherClients(critere);
    }

    // ==================== MENU RECHERCHE ====================
    private static void menuRecherche() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n" + ColorUtil.header("--- RECHERCHE ET FILTRAGE ---"));
            System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "Recherche par marque");
            System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "Recherche par prix");
            System.out.println(ColorUtil.colorize("3. ", ColorUtil.YELLOW) + "Recherche par catégorie");
            System.out.println(ColorUtil.colorize("4. ", ColorUtil.YELLOW) + "Recherche avancée");
            System.out.println(ColorUtil.colorize("0. ", ColorUtil.RED) + "Retour au menu principal");
            System.out.print(ColorUtil.info("Votre choix: "));
            
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
                    System.out.println(ColorUtil.error("Choix invalide!"));
            }
        }
    }

    private static void rechercherParMarque() {
        System.out.print("\n" + ColorUtil.info("Marque à rechercher: "));
        String marque = scanner.nextLine();
        vehiculeDAO.rechercherEtAfficherVehicules(marque, null, null, null);
    }

    private static void rechercherParPrix() {
        System.out.print("\n" + ColorUtil.info("Prix maximum (DH): "));
        double prixMax = lireDouble();
        
        if (prixMax > 0) {
            vehiculeDAO.rechercherEtAfficherVehicules(null, null, prixMax, null);
        } else {
            System.out.println(ColorUtil.error("Prix invalide!"));
        }
    }

    private static void rechercherParCategorie() {
        System.out.println("\n" + ColorUtil.info("Catégories disponibles:"));
        System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "BERLINE");
        System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "SUV");
        System.out.println(ColorUtil.colorize("3. ", ColorUtil.YELLOW) + "CAMION");
        System.out.println(ColorUtil.colorize("4. ", ColorUtil.YELLOW) + "ELECTRIQUE");
        System.out.print(ColorUtil.info("Votre choix: "));
        
        int choix = lireChoix();
        String type = "";
        
        switch (choix) {
            case 1: type = "BERLINE"; break;
            case 2: type = "SUV"; break;
            case 3: type = "CAMION"; break;
            case 4: type = "ELECTRIQUE"; break;
            default:
                System.out.println(ColorUtil.error("Choix invalide!"));
                return;
        }
        
        vehiculeDAO.rechercherEtAfficherVehicules(null, type, null, null);
    }

    private static void rechercherAvancee() {
        System.out.println("\n" + ColorUtil.header("--- Recherche avancée ---"));
        System.out.print(ColorUtil.info("Marque (laisser vide pour ignorer): "));
        String marque = scanner.nextLine();
        
        System.out.print(ColorUtil.info("Type (BERLINE/SUV/CAMION/ELECTRIQUE, laisser vide pour ignorer): "));
        String type = scanner.nextLine();
        if (!type.isEmpty()) type = type.toUpperCase();
        
        System.out.print(ColorUtil.info("Prix maximum (0 pour ignorer): "));
        double prixMax = lireDouble();
        
        System.out.print(ColorUtil.info("Année minimum (0 pour ignorer): "));
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
            System.out.println("\n" + ColorUtil.header("--- RAPPORTS ---"));
            System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "Véhicules disponibles");
            System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "Véhicules vendus");
            System.out.println(ColorUtil.colorize("3. ", ColorUtil.YELLOW) + "Statistiques de ventes");
            System.out.println(ColorUtil.colorize("0. ", ColorUtil.RED) + "Retour au menu principal");
            System.out.print(ColorUtil.info("Votre choix: "));
            
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
                    System.out.println(ColorUtil.error("Choix invalide!"));
            }
        }
    }

    private static void afficherVehiculesVendus() {
        System.out.println("\n" + ColorUtil.title("=== VÉHICULES VENDUS ==="));
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
