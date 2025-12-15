import java.util.Calendar;

public class MenuVehicules {

    public static void afficherMenu(VehiculeDAO vehiculeDAO, ClientDAO clientDAO, VenteDAO venteDAO) {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n" + ColorUtil.header("--- GESTION DES VÉHICULES ---"));
            System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "Ajouter un véhicule");
            System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "Modifier le statut d'un véhicule");
            System.out.println(ColorUtil.colorize("3. ", ColorUtil.YELLOW) + "Supprimer un véhicule");
            System.out.println(ColorUtil.colorize("4. ", ColorUtil.YELLOW) + "Afficher tous les véhicules");
            System.out.println(ColorUtil.colorize("5. ", ColorUtil.YELLOW) + "Vendre un véhicule (avec client)");
            System.out.println(ColorUtil.colorize("0. ", ColorUtil.RED) + "Retour au menu principal");
            System.out.print(ColorUtil.info("Votre choix: "));

            int choix = ConsoleUtil.lireChoix();

            switch (choix) {
                case 1:
                    ajouterVehicule(vehiculeDAO);
                    break;
                case 2:
                    modifierVehicule(vehiculeDAO);
                    break;
                case 3:
                    supprimerVehicule(vehiculeDAO);
                    break;
                case 4:
                    vehiculeDAO.afficherTousVehicules();
                    break;
                case 5:
                    vendreVehicule(vehiculeDAO, clientDAO, venteDAO);
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println(ColorUtil.error("Choix invalide!"));
            }
        }
    }

    private static void ajouterVehicule(VehiculeDAO vehiculeDAO) {
        System.out.println("\n" + ColorUtil.header("--- Ajout d'un véhicule ---"));

        System.out.print(ColorUtil.info("Marque: "));
        String marque = ConsoleUtil.SCANNER.nextLine().trim();

        if (marque.isEmpty()) {
            System.out.println(ColorUtil.error("Erreur: La marque ne peut pas être vide!"));
            return;
        }

        System.out.print(ColorUtil.info("Modèle: "));
        String modele = ConsoleUtil.SCANNER.nextLine().trim();

        if (modele.isEmpty()) {
            System.out.println(ColorUtil.error("Erreur: Le modèle ne peut pas être vide!"));
            return;
        }

        System.out.print(ColorUtil.info("Prix d'achat (DH): "));
        double prixAchat = ConsoleUtil.lireDouble();
        if (prixAchat <= 0) {
            System.out.println(ColorUtil.error("Erreur: Le prix d'achat doit être positif!"));
            return;
        }

        System.out.print(ColorUtil.info("Prix de vente (DH): "));
        double prixVente = ConsoleUtil.lireDouble();
        if (prixVente <= 0) {
            System.out.println(ColorUtil.error("Erreur: Le prix de vente doit être positif!"));
            return;
        }
        if (prixVente <= prixAchat) {
            System.out.println(ColorUtil.error("Erreur: Le prix de vente doit être supérieur au prix d'achat!"));
            return;
        }

        System.out.print(ColorUtil.info("Année: "));
        int annee;
        try {
            annee = Integer.parseInt(ConsoleUtil.SCANNER.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(ColorUtil.error("Erreur: Année invalide!"));
            return;
        }

        int anneeActuelle = Calendar.getInstance().get(Calendar.YEAR);
        if (annee < 1900 || annee > anneeActuelle) {
            System.out.println(ColorUtil.error("Erreur: Année invalide! Doit être entre 1900 et " + anneeActuelle));
            return;
        }

        System.out.print(ColorUtil.info("Kilométrage: "));
        int kilometrage;
        try {
            kilometrage = Integer.parseInt(ConsoleUtil.SCANNER.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(ColorUtil.error("Erreur: Kilométrage invalide!"));
            return;
        }

        if (kilometrage < 0) {
            System.out.println(ColorUtil.error("Erreur: Le kilométrage doit être positif ou zéro!"));
            return;
        }

        String typeVehicule = ConsoleUtil.choisirTypeVehiculeDepuisMenu();
        if (typeVehicule == null) {
            return;
        }

        Vehicule vehicule = null;
        switch (typeVehicule) {
            case "BERLINE":
                vehicule = new Berline(null, marque, modele, prixAchat, prixVente, annee,
                        kilometrage, "DISPONIBLE", null, null);
                break;
            case "SUV":
                vehicule = new SUV(null, marque, modele, prixAchat, prixVente, annee,
                        kilometrage, "DISPONIBLE", null, null);
                break;
            case "CAMION":
                vehicule = new Camion(null, marque, modele, prixAchat, prixVente, annee,
                        kilometrage, "DISPONIBLE", null, null);
                break;
            case "ELECTRIQUE":
                vehicule = new Electrique(null, marque, modele, prixAchat, prixVente, annee,
                        kilometrage, "DISPONIBLE", null, null);
                break;
            default:
                System.out.println(ColorUtil.error("Type de véhicule inconnu."));
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

    private static void modifierVehicule(VehiculeDAO vehiculeDAO) {
        System.out.println("\n" + ColorUtil.header("--- Modification d'un véhicule ---"));
        System.out.print(ColorUtil.info("ID du véhicule: "));
        String idStr = ConsoleUtil.SCANNER.nextLine();

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            System.out.println(ColorUtil.error("ID invalide!"));
            return;
        }

        Vehicule vehicule = vehiculeDAO.getVehiculeById(id);
        if (vehicule == null) {
            System.out.println(ColorUtil.error("Véhicule introuvable!"));
            return;
        }

        System.out.println(ColorUtil.info("Véhicule actuel:"));
        vehicule.Afficher();
        System.out.println();

        System.out.println("\n" + ColorUtil.info("Nouveau statut:"));
        System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "DISPONIBLE");
        System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "VENDU");
        System.out.println(ColorUtil.colorize("0. ", ColorUtil.RED) + "Conserver le statut actuel");
        System.out.print(ColorUtil.info("Votre choix: "));

        int choix = ConsoleUtil.lireChoix();
        boolean modificationEffectuee = false;

        switch (choix) {
            case 1:
                vehicule.setStatut("DISPONIBLE");
                modificationEffectuee = true;
                break;
            case 2:
                vehicule.setStatut("VENDU");
                modificationEffectuee = true;
                break;
            case 0:
                System.out.println(ColorUtil.info("Statut conservé."));
                return;
            default:
                System.out.println(ColorUtil.error("Choix invalide!"));
                return;
        }

        if (modificationEffectuee && vehiculeDAO.modifierVehicule(vehicule)) {
            System.out.println(ColorUtil.success("Véhicule modifié avec succès! Nouveau statut: " + vehicule.getStatut()));
        } else {
            System.out.println(ColorUtil.error("Erreur lors de la modification!"));
        }
    }

    private static void vendreVehicule(VehiculeDAO vehiculeDAO, ClientDAO clientDAO, VenteDAO venteDAO) {
        System.out.println("\n" + ColorUtil.header("--- Vente d'un véhicule ---"));
        System.out.print(ColorUtil.info("ID du véhicule à vendre: "));
        String vehiculeIdStr = ConsoleUtil.SCANNER.nextLine();

        int vehiculeId;
        try {
            vehiculeId = Integer.parseInt(vehiculeIdStr);
        } catch (NumberFormatException e) {
            System.out.println(ColorUtil.error("ID de véhicule invalide!"));
            return;
        }

        Vehicule vehicule = vehiculeDAO.getVehiculeById(vehiculeId);
        if (vehicule == null) {
            System.out.println(ColorUtil.error("Véhicule introuvable!"));
            return;
        }

        if (!"DISPONIBLE".equalsIgnoreCase(vehicule.getStatut())) {
            System.out.println(ColorUtil.error("Ce véhicule n'est pas disponible (statut actuel: " + vehicule.getStatut() + ")."));
            return;
        }

        System.out.println(ColorUtil.info("Véhicule sélectionné :"));
        vehicule.Afficher();
        System.out.println();

        System.out.print(ColorUtil.info("ID du client acheteur (le client doit déjà exister): "));
        String clientIdStr = ConsoleUtil.SCANNER.nextLine();

        int clientId;
        try {
            clientId = Integer.parseInt(clientIdStr);
        } catch (NumberFormatException e) {
            System.out.println(ColorUtil.error("ID de client invalide!"));
            return;
        }

        Client client = clientDAO.getClientById(clientId);
        if (client == null) {
            System.out.println(ColorUtil.error("Client introuvable! Veuillez d'abord créer le client dans le menu Clients."));
            return;
        }

        System.out.println(ColorUtil.info("Client sélectionné :"));
        client.afficher();
        System.out.println();

        double prixParDefaut = vehicule.calculerPrixFinal();
        System.out.println(ColorUtil.info("Prix final par défaut (avec taxes) : ") +
                ColorUtil.highlight(String.format("%.2f", prixParDefaut) + " DH"));
        System.out.print(ColorUtil.info("Entrer le prix de vente final (laisser vide pour garder le prix par défaut): "));
        String saisiePrix = ConsoleUtil.SCANNER.nextLine().trim();
        double prixFinal;
        if (saisiePrix.isEmpty()) {
            prixFinal = prixParDefaut;
        } else {
            try {
                prixFinal = Double.parseDouble(saisiePrix);
            } catch (NumberFormatException e) {
                System.out.println(ColorUtil.error("Prix invalide, annulation de la vente."));
                return;
            }
        }

        String modePaiement = ConsoleUtil.choisirModePaiementDepuisMenu();
        if (modePaiement == null) {
            return;
        }

        System.out.print(ColorUtil.info("Notes (optionnel): "));
        String notes = ConsoleUtil.SCANNER.nextLine();
        if (notes.isEmpty()) {
            notes = null;
        }

        boolean ok = venteDAO.enregistrerVente(vehiculeId, clientId, prixFinal, modePaiement, notes);
        if (ok) {
            System.out.println(ColorUtil.success("Vente réalisée avec succès."));
        }
    }

    private static void supprimerVehicule(VehiculeDAO vehiculeDAO) {
        System.out.println("\n" + ColorUtil.header("--- Suppression d'un véhicule ---"));
        System.out.print(ColorUtil.info("ID du véhicule: "));
        String idStr = ConsoleUtil.SCANNER.nextLine();

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            System.out.println(ColorUtil.error("ID invalide!"));
            return;
        }

        System.out.print(ColorUtil.warning("Êtes-vous sûr? (o/n): "));
        String confirmation = ConsoleUtil.SCANNER.nextLine();

        if (confirmation.equalsIgnoreCase("o")) {
            if (vehiculeDAO.supprimerVehicule(id)) {
                System.out.println(ColorUtil.success("Véhicule supprimé avec succès!"));
            } else {
                System.out.println(ColorUtil.error("Erreur lors de la suppression!"));
            }
        }
    }
}


