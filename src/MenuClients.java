public class MenuClients {

    public static void afficherMenu(ClientDAO clientDAO) {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n" + ColorUtil.header("--- GESTION DES CLIENTS ---"));
            System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "Ajouter un client");
            System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "Modifier un client");
            System.out.println(ColorUtil.colorize("3. ", ColorUtil.YELLOW) + "Rechercher un client");
            System.out.println(ColorUtil.colorize("4. ", ColorUtil.YELLOW) + "Afficher tous les clients");
            System.out.println(ColorUtil.colorize("0. ", ColorUtil.RED) + "Retour au menu principal");
            System.out.print(ColorUtil.info("Votre choix: "));

            int choix = ConsoleUtil.lireChoix();

            switch (choix) {
                case 1:
                    ajouterClient(clientDAO);
                    break;
                case 2:
                    modifierClient(clientDAO);
                    break;
                case 3:
                    rechercherClient(clientDAO);
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

    private static void ajouterClient(ClientDAO clientDAO) {
        System.out.println("\n" + ColorUtil.header("--- Ajout d'un client ---"));

        System.out.print(ColorUtil.info("Nom: "));
        String nom = ConsoleUtil.SCANNER.nextLine().trim();

        if (nom.isEmpty()) {
            System.out.println(ColorUtil.error("Erreur: Le nom ne peut pas être vide!"));
            return;
        }

        System.out.print(ColorUtil.info("Téléphone: "));
        String telephone = ConsoleUtil.SCANNER.nextLine().trim();

        if (telephone.isEmpty()) {
            System.out.println(ColorUtil.error("Erreur: Le téléphone ne peut pas être vide!"));
            return;
        }

        if (telephone.length() < 8) {
            System.out.println(ColorUtil.error("Erreur: Le téléphone doit contenir au moins 8 chiffres!"));
            return;
        }

        Client clientExistant = clientDAO.getClientByNomEtTelephone(nom, telephone);
        if (clientExistant != null) {
            System.out.println("\n" + ColorUtil.warning("Ce client existe déjà dans la base de données:"));
            clientExistant.afficher();
            System.out.print("\n" + ColorUtil.warning("Voulez-vous continuer quand même? (o/n): "));
            String reponse = ConsoleUtil.SCANNER.nextLine();
            if (!reponse.equalsIgnoreCase("o")) {
                System.out.println(ColorUtil.info("Ajout annulé."));
                return;
            }
        } else {
            clientExistant = clientDAO.getClientByTelephone(telephone);
            if (clientExistant != null) {
                System.out.println("\n" + ColorUtil.warning("Un client avec ce numéro de téléphone existe déjà:"));
                clientExistant.afficher();
                System.out.print("\n" + ColorUtil.warning("Voulez-vous continuer quand même? (o/n): "));
                String reponse = ConsoleUtil.SCANNER.nextLine();
                if (!reponse.equalsIgnoreCase("o")) {
                    System.out.println(ColorUtil.info("Ajout annulé."));
                    return;
                }
            }
        }

        System.out.print(ColorUtil.info("Email (optionnel): "));
        String email = ConsoleUtil.SCANNER.nextLine();
        if (email.isEmpty()) email = null;

        System.out.print(ColorUtil.info("Adresse (optionnel): "));
        String adresse = ConsoleUtil.SCANNER.nextLine();
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

    private static void modifierClient(ClientDAO clientDAO) {
        System.out.println("\n" + ColorUtil.header("--- Modification d'un client ---"));
        System.out.print(ColorUtil.info("ID du client: "));
        String idStr = ConsoleUtil.SCANNER.nextLine();

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            System.out.println(ColorUtil.error("ID invalide!"));
            return;
        }

        Client client = clientDAO.getClientById(id);
        if (client == null) {
            System.out.println(ColorUtil.error("Client introuvable!"));
            return;
        }

        System.out.println(ColorUtil.info("Client actuel:"));
        client.afficher();

        System.out.println("\n" + ColorUtil.colorize("(Laissez vide pour conserver la valeur actuelle)", ColorUtil.YELLOW));

        System.out.print(ColorUtil.info("Nouveau nom: "));
        String nom = ConsoleUtil.SCANNER.nextLine();
        if (!nom.isEmpty()) client.setNom(nom);

        System.out.print(ColorUtil.info("Nouveau téléphone: "));
        String telephone = ConsoleUtil.SCANNER.nextLine();
        if (!telephone.isEmpty()) client.setTelephone(telephone);

        System.out.print(ColorUtil.info("Nouvel email: "));
        String email = ConsoleUtil.SCANNER.nextLine();
        if (!email.isEmpty()) client.setEmail(email);

        System.out.print(ColorUtil.info("Nouvelle adresse: "));
        String adresse = ConsoleUtil.SCANNER.nextLine();
        if (!adresse.isEmpty()) client.setAdresse(adresse);

        if (clientDAO.modifierClient(client)) {
            System.out.println(ColorUtil.success("Client modifié avec succès!"));
        } else {
            System.out.println(ColorUtil.error("Erreur lors de la modification!"));
        }
    }

    private static void rechercherClient(ClientDAO clientDAO) {
        System.out.println("\n" + ColorUtil.header("--- Recherche d'un client ---"));
        System.out.print(ColorUtil.info("Nom ou téléphone: "));
        String critere = ConsoleUtil.SCANNER.nextLine();

        clientDAO.rechercherEtAfficherClients(critere);
    }
}


