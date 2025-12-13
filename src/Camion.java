public class Camion extends Vehicule {
    
    public Camion(String id, String marque, String modele, double prixAchat, double prixVente,
                  int annee, int kilometrage, String statut, String dateAjout, String dateVente) {
        super(id, marque, modele, prixAchat, prixVente, annee, kilometrage, "CAMION", statut, dateAjout, dateVente);
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
    }
}


