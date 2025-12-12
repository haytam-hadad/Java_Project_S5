public class VoitureElectrique extends Vehicule {
    private int autonomieKm;
    private int tempsChargeHeures;

    public VoitureElectrique(String id, String marque, String modele,
                             double prixAchat, double prixVente,
                             int annee, int kilometrage,
                             String statut, String dateAjout, String dateVente,
                             int autonomieKm, int tempsChargeHeures) {

        super(id, marque, modele, prixAchat, prixVente, annee, kilometrage,
                "ELECTRIQUE", statut, dateAjout, dateVente);

        this.autonomieKm = autonomieKm;
        this.tempsChargeHeures = tempsChargeHeures;
    }

    @Override
    public double calculerPrixFinal() {
        return prixVente * 1.10; // taxe sur Voiture Electrique 11%
    }

    @Override
    public void Afficher() {
        super.Afficher();
        System.out.println( " ||  Autonomie=" + autonomieKm + "km || Charge=" + tempsChargeHeures + "h");
    }  
}
