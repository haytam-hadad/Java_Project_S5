public class Vehicule {
    protected String id;   
    protected String marque;
    protected String modele;
    protected double prixAchat;
    protected double prixVente;
    protected int annee;
    protected int kilometrage;
    protected String typeVehicule; 
    protected String statut;    
    protected String dateAjout;
    protected String dateVente;

    public Vehicule(String id, String marque, String modele,double prixAchat, double prixVente,int annee,
                    int kilometrage,String typeVehicule, String statut,String dateAjout, String dateVente) 
    {
        this.id = id;
        this.marque = marque;
        this.modele = modele;
        this.prixAchat = prixAchat;
        this.prixVente = prixVente;
        this.annee = annee;
        this.kilometrage = kilometrage;
        this.typeVehicule = typeVehicule;
        this.statut = statut;
        this.dateAjout = dateAjout;
        this.dateVente = dateVente;
    }

    public double calculerPrixFinal() {
        switch (typeVehicule) {
            case "BERLINE": return prixVente * 1.15;
            case "SUV": return prixVente * 1.20;
            case "CAMION": return prixVente * 1.18;
            case "ELECTRIQUE": return prixVente * 1.10;
            default: return prixVente;
        }
    }

    public String getId() { return id; }
    public String getMarque() { return marque; }
    public String getModele() { return modele; }
    public double getPrixAchat() { return prixAchat; }
    public double getPrixVente() { return prixVente; }
    public int getAnnee() { return annee; }
    public int getKilometrage() { return kilometrage; }
    public String getTypeVehicule() { return typeVehicule; }
    public String getStatut() { return statut; }
    public String getDateAjout() { return dateAjout; }
    public String getDateVente() { return dateVente; }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void Afficher() {
        System.out.print("ID=" + id + " ||  " + marque + " " + modele + " (" + annee + ") ||  Km=" + kilometrage +
                " ||  Achat=" + prixAchat + " ||  Vente=" + prixVente +
                " ||  Type=" + typeVehicule + " ||  Statut=" + statut);
    }
}
