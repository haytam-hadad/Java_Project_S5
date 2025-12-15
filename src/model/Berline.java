package model;

import util.ColorUtil;

public class Berline extends Vehicule {
    private int capaciteCoffre; // Capacité du coffre en litres
    
    public Berline(String id, String marque, String modele, double prixAchat, double prixVente,
                   int annee, int kilometrage, String statut, String dateAjout, String dateVente, int capaciteCoffre) {
        super(id, marque, modele, prixAchat, prixVente, annee, kilometrage, "BERLINE", statut, dateAjout, dateVente);
        this.capaciteCoffre = capaciteCoffre;
    }
    
    public int getCapaciteCoffre() {
        return capaciteCoffre;
    }
    
    public void setCapaciteCoffre(int capaciteCoffre) {
        this.capaciteCoffre = capaciteCoffre;
    }
    
    @Override
    public double calculerPrixFinal() {
        // Prix de vente avec taxe de 15% pour les berlines
        return prixVente * 1.15;
    }
    
    @Override
    public void Afficher() {
        System.out.print(ColorUtil.colorize("[BERLINE]", ColorUtil.MAGENTA_BOLD) + " ");
        super.Afficher();
        System.out.print(ColorUtil.colorize(" ||  Coffre=", ColorUtil.WHITE) + 
                        ColorUtil.highlight(capaciteCoffre + "L"));
        System.out.println();
    }
}