public class Berline extends Vehicule {
    private int capaciteCoffre;

    public Berline(String id, String marque, String modele,
                   double prixAchat, double prixVente,
                   int annee, int kilometrage,
                   String statut, String dateAjout, String dateVente,
                   int capaciteCoffre) {

        super(id, marque, modele, prixAchat, prixVente, annee, kilometrage,
                "BERLINE", statut, dateAjout, dateVente);

        this.capaciteCoffre = capaciteCoffre;
    }

    @Override
    public double calculerPrixFinal() {
        return prixVente * 1.15; // taxe sur Berlines 15%
    }

    @Override
    public void Afficher() {
        super.Afficher();
        System.out.println(" ||  Coffre=" + capaciteCoffre + "L");
    }

}
