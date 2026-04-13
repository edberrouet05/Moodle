package com.etsmtl.minimoodle.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cours {

    private int id;
    private String sigle;
    private String titre;
    private int credits;
    private String description;
    private String enseignant;
    private String userId;

    public Cours() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSigle() { return sigle; }
    public void setSigle(String sigle) { this.sigle = sigle; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEnseignant() { return enseignant; }
    public void setEnseignant(String enseignant) { this.enseignant = enseignant; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}
