public class SUV extends Vehicule {
    
    public SUV(String id, String marque, String modele, double prixAchat, double prixVente,
               int annee, int kilometrage, String statut, String dateAjout, String dateVente) {
        super(id, marque, modele, prixAchat, prixVente, annee, kilometrage, "SUV", statut, dateAjout, dateVente);
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
    }
}


