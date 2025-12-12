public class Camion extends Vehicule {

    private int capaciteChargement; 

    public Camion(String id, String marque, String modele,
                  double prixAchat, double prixVente,
                  int annee, int kilometrage,
                  String statut, String dateAjout, String dateVente,
                  int capaciteChargement) {

        super(id, marque, modele, prixAchat, prixVente, annee, kilometrage,
                "CAMION", statut, dateAjout, dateVente);

        this.capaciteChargement = capaciteChargement;
    }

    @Override
    public double calculerPrixFinal() {
        return prixVente * 1.18;
    }

    @Override
    public void Afficher() {
        super.Afficher();
        System.out.println(" ||  Charge=" + capaciteChargement + "kg");
    }  
}
