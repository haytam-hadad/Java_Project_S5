package menu;

import dao.VehiculeDAO;
import dao.VenteDAO;
import util.ColorUtil;
import util.ConsoleUtil;

public class MenuRapports {

    public static void afficherMenu(VehiculeDAO vehiculeDAO, VenteDAO venteDAO) {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n" + ColorUtil.header("--- RAPPORTS ---"));
            System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "Véhicules disponibles");
            System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "Véhicules vendus");
            System.out.println(ColorUtil.colorize("3. ", ColorUtil.YELLOW) + "Statistiques de ventes");
            System.out.println(ColorUtil.colorize("4. ", ColorUtil.YELLOW) + "Historique complet des ventes (Véhicules + Clients + Paiements)");
            System.out.println(ColorUtil.colorize("0. ", ColorUtil.RED) + "Retour au menu principal");
            System.out.print(ColorUtil.info("Votre choix: "));

            int choix = ConsoleUtil.lireChoix();

            switch (choix) {
                case 1:
                    vehiculeDAO.afficherVehiculesDisponibles();
                    break;
                case 2:
                    afficherVehiculesVendus(vehiculeDAO);
                    break;
                case 3:
                    venteDAO.afficherStatistiques();
                    break;
                case 4:
                    venteDAO.afficherHistoriqueVentes();
                    break;
                case 0:
                    continuer = false;
                    break;
                default:
                    System.out.println(ColorUtil.error("Choix invalide!"));
            }
        }
    }

    private static void afficherVehiculesVendus(VehiculeDAO vehiculeDAO) {
        System.out.println("\n" + ColorUtil.title("=== VÉHICULES VENDUS ==="));
        vehiculeDAO.afficherVehiculesVendus();
    }
}