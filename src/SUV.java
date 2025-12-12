public class SUV extends Vehicule {

    private String traction; // 4x4 / 4x2

    public SUV(String id, String marque, String modele,
               double prixAchat, double prixVente,
               int annee, int kilometrage,
               String statut, String dateAjout, String dateVente,
               String traction) {

        super(id, marque, modele, prixAchat, prixVente, annee, kilometrage,
                "SUV", statut, dateAjout, dateVente);

        this.traction = traction;
    }

    @Override
    public double calculerPrixFinal() {
        return prixVente * 1.20;  // tax sur SUV 12%
    }

    @Override
    public void Afficher() {
        super.Afficher();
        System.out.println(" ||  Traction=" + traction);
    }
}
