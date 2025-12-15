package model;

import util.ColorUtil;

public class Camion extends Vehicule {
    private double capaciteChargement; // Capacité de chargement en tonnes
    
    public Camion(String id, String marque, String modele, double prixAchat, double prixVente,
                  int annee, int kilometrage, String statut, String dateAjout, String dateVente, double capaciteChargement) {
        super(id, marque, modele, prixAchat, prixVente, annee, kilometrage, "CAMION", statut, dateAjout, dateVente);
        this.capaciteChargement = capaciteChargement;
    }
    
    public double getCapaciteChargement() {
        return capaciteChargement;
    }
    
    public void setCapaciteChargement(double capaciteChargement) {
        this.capaciteChargement = capaciteChargement;
    }
    
    @Override
    public double calculerPrixFinal() {
        // Prix de vente avec taxe de 18% pour les camions
        return prixVente * 1.18;
    }
    
    @Override
    public void Afficher() {
        System.out.print(ColorUtil.colorize("[CAMION]", ColorUtil.MAGENTA_BOLD) + " ");
        super.Afficher();
        System.out.print(ColorUtil.colorize(" ||  Chargement=", ColorUtil.WHITE) + 
                        ColorUtil.highlight(capaciteChargement + "T"));
        System.out.println();
    }
}