package com.etsmtl.minimoodle.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cours {

    private String id;

    @JsonProperty("code")
    private String sigle;

    @JsonProperty("title")
    private String titre;

    private String description;

    @JsonProperty("teacher")
    private String enseignant;

    private String session;

    @JsonProperty("imageUrl")
    private String urlImage;

    private List<String> annonces;

    public Cours() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSigle() { return sigle; }
    public void setSigle(String sigle) { this.sigle = sigle; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEnseignant() { return enseignant; }
    public void setEnseignant(String enseignant) { this.enseignant = enseignant; }

    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }

    public String getUrlImage() { return urlImage; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }

    public List<String> getAnnonces() { return annonces; }
    public void setAnnonces(List<String> annonces) { this.annonces = annonces; }
}
