public class Electrique extends Vehicule {
    
    public Electrique(String id, String marque, String modele, double prixAchat, double prixVente,
                     int annee, int kilometrage, String statut, String dateAjout, String dateVente) {
        super(id, marque, modele, prixAchat, prixVente, annee, kilometrage, "ELECTRIQUE", statut, dateAjout, dateVente);
    }
    
    @Override
    public double calculerPrixFinal() {
        // Prix de vente avec taxe de 10% pour les véhicules électriques
        return prixVente * 1.10;
    }
    
    @Override
    public void Afficher() {
        System.out.print("[ELECTRIQUE] ");
        super.Afficher();
    }
}

