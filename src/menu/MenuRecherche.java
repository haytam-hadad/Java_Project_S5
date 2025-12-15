package menu;

import dao.VehiculeDAO;
import util.ColorUtil;
import util.ConsoleUtil;

public class MenuRecherche {

    public static void afficherMenu(VehiculeDAO vehiculeDAO) {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n" + ColorUtil.header("--- RECHERCHE ET FILTRAGE ---"));
            System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "Recherche par prix");
            System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "Recherche par catégorie");
            System.out.println(ColorUtil.colorize("0. ", ColorUtil.RED) + "Retour au menu principal");
            System.out.print(ColorUtil.info("Votre choix: "));

            int choix = ConsoleUtil.lireChoix();

            switch (choix) {
                case 1:
                    rechercherParPrix(vehiculeDAO);
                    break;
                case 2:
                    rechercherParCategorie(vehiculeDAO);
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println(ColorUtil.error("Choix invalide!"));
            }
        }
    }

    private static void rechercherParPrix(VehiculeDAO vehiculeDAO) {
        System.out.print("\n" + ColorUtil.info("Prix maximum (DH): "));
        double prixMax = ConsoleUtil.lireDouble();

        if (prixMax > 0) {
            vehiculeDAO.rechercherEtAfficherVehicules(null, null, prixMax, null);
        } else {
            System.out.println(ColorUtil.error("Prix invalide!"));
        }
    }

    private static void rechercherParCategorie(VehiculeDAO vehiculeDAO) {
        String type = ConsoleUtil.choisirTypeVehiculeDepuisMenu();
        if (type == null) {
            return;
        }

        vehiculeDAO.rechercherEtAfficherVehicules(null, type, null, null);
    }
}


