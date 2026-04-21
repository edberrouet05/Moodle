package com.edwidge_jason.minimoodle.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Utilisateur {

    private String id;

    @JsonProperty("username")
    private String nomUtilisateur;

    private String nom;
    private String prenom;

    @JsonProperty("email")
    private String courriel;

    @JsonProperty("password")
    private String motDePasse;

    private String telephone;

    @JsonProperty("photoUrl")
    private String photoProfil;

    @JsonProperty("enrolledCourseIds")
    private List<String> coursInscrits;

    public Utilisateur() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getCourriel() { return courriel; }
    public void setCourriel(String courriel) { this.courriel = courriel; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getPhotoProfil() { return photoProfil; }
    public void setPhotoProfil(String photoProfil) { this.photoProfil = photoProfil; }

    public List<String> getCoursInscrits() { return coursInscrits; }
    public void setCoursInscrits(List<String> coursInscrits) { this.coursInscrits = coursInscrits; }

    public String getNomComplet() {
        return prenom + " " + nom;
    }
}
