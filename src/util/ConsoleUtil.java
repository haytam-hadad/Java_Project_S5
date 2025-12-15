package util;

import java.util.Scanner;

/**
 * Utilitaires pour les entrées clavier et quelques menus communs.
 */
public class ConsoleUtil {
    public static final Scanner SCANNER = new Scanner(System.in);

    public static int lireChoix() {
        try {
            return Integer.parseInt(SCANNER.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static double lireDouble() {
        try {
            return Double.parseDouble(SCANNER.nextLine());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Affiche le menu de choix de type de véhicule et retourne la valeur de type correspondante.
     * Retourne null si le choix est invalide (avec message d'erreur affiché).
     */
    public static String choisirTypeVehiculeDepuisMenu() {
        System.out.println("\n" + ColorUtil.info("Catégories disponibles:"));
        System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "BERLINE");
        System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "SUV");
        System.out.println(ColorUtil.colorize("3. ", ColorUtil.YELLOW) + "CAMION");
        System.out.println(ColorUtil.colorize("4. ", ColorUtil.YELLOW) + "ELECTRIQUE");
        System.out.print(ColorUtil.info("Votre choix: "));

        int choix = lireChoix();
        switch (choix) {
            case 1: return "BERLINE";
            case 2: return "SUV";
            case 3: return "CAMION";
            case 4: return "ELECTRIQUE";
            default:
                System.out.println(ColorUtil.error("Choix invalide!"));
                return null;
        }
    }

    /**
     * Affiche le menu des modes de paiement et retourne la valeur choisie.
     * Retourne null si le choix est invalide (avec message d'erreur cohérent).
     */
    public static String choisirModePaiementDepuisMenu() {
        System.out.println("\n" + ColorUtil.info("Mode de paiement :"));
        System.out.println(ColorUtil.colorize("1. ", ColorUtil.YELLOW) + "COMPTANT");
        System.out.println(ColorUtil.colorize("2. ", ColorUtil.YELLOW) + "CREDIT");
        System.out.println(ColorUtil.colorize("3. ", ColorUtil.YELLOW) + "CHEQUE");
        System.out.print(ColorUtil.info("Votre choix: "));

        int choixPaiement = lireChoix();
        switch (choixPaiement) {
            case 1: return "COMPTANT";
            case 2: return "CREDIT";
            case 3: return "CHEQUE";
            default:
                System.out.println(ColorUtil.error("Choix de mode de paiement invalide, annulation de la vente."));
                return null;
        }
    }
}


