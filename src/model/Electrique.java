package model;

import util.ColorUtil;

public class Electrique extends Vehicule {
    private int autonomieKm; // Autonomie en kilomètres
    private double tempsChargeHeures; // Temps de charge en heures
    
    public Electrique(String id, String marque, String modele, double prixAchat, double prixVente,
                     int annee, int kilometrage, String statut, String dateAjout, String dateVente, 
                     int autonomieKm, double tempsChargeHeures) {
        super(id, marque, modele, prixAchat, prixVente, annee, kilometrage, "ELECTRIQUE", statut, dateAjout, dateVente);
        this.autonomieKm = autonomieKm;
        this.tempsChargeHeures = tempsChargeHeures;
    }
    
    public int getAutonomieKm() {
        return autonomieKm;
    }
    
    public void setAutonomieKm(int autonomieKm) {
        this.autonomieKm = autonomieKm;
    }
    
    public double getTempsChargeHeures() {
        return tempsChargeHeures;
    }
    
    public void setTempsChargeHeures(double tempsChargeHeures) {
        this.tempsChargeHeures = tempsChargeHeures;
    }
    
    @Override
    public double calculerPrixFinal() {
        // Prix de vente avec taxe de 10% pour les véhicules électriques
        return prixVente * 1.10;
    }
    
    @Override
    public void Afficher() {
        System.out.print(ColorUtil.colorize("[ELECTRIQUE]", ColorUtil.MAGENTA_BOLD) + " ");
        super.Afficher();
        System.out.print(ColorUtil.colorize(" ||  Autonomie=", ColorUtil.WHITE) + 
                        ColorUtil.highlight(autonomieKm + "km") +
                        ColorUtil.colorize(" ||  Charge=", ColorUtil.WHITE) + 
                        ColorUtil.highlight(tempsChargeHeures + "h"));
        System.out.println();
    }
}