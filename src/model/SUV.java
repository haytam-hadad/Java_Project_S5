package model;

import util.ColorUtil;

public class SUV extends Vehicule {
    private String traction; // 4x4 ou 4x2
    
    public SUV(String id, String marque, String modele, double prixAchat, double prixVente,
               int annee, int kilometrage, String statut, String dateAjout, String dateVente, String traction) {
        super(id, marque, modele, prixAchat, prixVente, annee, kilometrage, "SUV", statut, dateAjout, dateVente);
        this.traction = traction;
    }
    
    public String getTraction() {
        return traction;
    }
    
    public void setTraction(String traction) {
        this.traction = traction;
    }
    
    @Override
    public double calculerPrixFinal() {
        // Prix de vente avec taxe de 20% pour les SUV
        return prixVente * 1.20;
    }
    
    @Override
    public void Afficher() {
        System.out.print(ColorUtil.colorize("[SUV]", ColorUtil.MAGENTA_BOLD) + " ");
        super.Afficher();
        System.out.print(ColorUtil.colorize(" ||  Traction=", ColorUtil.WHITE) + 
                        ColorUtil.highlight(traction));
        System.out.println();
    }
}