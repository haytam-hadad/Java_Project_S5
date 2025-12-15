package model;

import java.sql.Timestamp;

import util.ColorUtil;

/**
 * Classe représentant un client
 */
public class Client {
    private String id;
    private String nom;
    private String telephone;
    private String email;
    private String adresse;
    private Timestamp dateCreation;

    public Client(String id, String nom, String telephone, String email, String adresse) {
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
    }

    public Client(String id, String nom, String telephone, String email, String adresse, Timestamp dateCreation) {
        this.id = id;
        this.nom = nom;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.dateCreation = dateCreation;
    }

    // Getters
    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    public String getAdresse() { return adresse; }
    public Timestamp getDateCreation() { return dateCreation; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setEmail(String email) { this.email = email; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setDateCreation(Timestamp dateCreation) { this.dateCreation = dateCreation; }

    public void afficher() {
        System.out.println(ColorUtil.colorize("ID: ", ColorUtil.CYAN) + ColorUtil.highlight(id) + 
                          ColorUtil.colorize(" | Nom: ", ColorUtil.WHITE) + ColorUtil.colorize(nom, ColorUtil.YELLOW) + 
                          ColorUtil.colorize(" | Téléphone: ", ColorUtil.WHITE) + ColorUtil.highlight(telephone) + 
                          ColorUtil.colorize(" | Email: ", ColorUtil.WHITE) + 
                          ColorUtil.colorize((email != null ? email : "N/A"), email != null ? ColorUtil.CYAN : ColorUtil.WHITE) + 
                          ColorUtil.colorize(" | Adresse: ", ColorUtil.WHITE) + 
                          ColorUtil.colorize((adresse != null ? adresse : "N/A"), adresse != null ? ColorUtil.CYAN : ColorUtil.WHITE));
    }
}


