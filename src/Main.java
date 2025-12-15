public class Main {
    private static VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private static ClientDAO clientDAO = new ClientDAO();
    private static VenteDAO venteDAO = new VenteDAO();

    public static void main(String[] args) {
        System.out.println(ColorUtil.colorize("═══════════════════════════════════════════════════", ColorUtil.CYAN));
        System.out.println(ColorUtil.title("   SYSTEME DE GESTION - CONCESSION VOITURES OCCASION"));
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
            int choix = ConsoleUtil.lireChoix();
            
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
        
        ConsoleUtil.SCANNER.close();
    }

    /**
     * Vérifier le mot de passe pour accéder à l'application
     */
    private static boolean verifierMotDePasse() {
        System.out.print(ColorUtil.info("Mot de passe: "));
        String motDePasse = ConsoleUtil.SCANNER.nextLine();
        
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
        MenuVehicules.afficherMenu(vehiculeDAO, clientDAO, venteDAO);
    }

    // ==================== MENU CLIENTS ====================
    private static void menuClients() {
        MenuClients.afficherMenu(clientDAO);
    }

    // ==================== MENU RECHERCHE ====================
    private static void menuRecherche() {
        MenuRecherche.afficherMenu(vehiculeDAO);
    }

    // ==================== MENU RAPPORTS ====================
    private static void menuRapports() {
        MenuRapports.afficherMenu(vehiculeDAO, venteDAO);
    }
}
