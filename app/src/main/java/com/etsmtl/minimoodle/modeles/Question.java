package com.etsmtl.minimoodle.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {

    private String id;

    @JsonProperty("question")
    private String enonce;

    @JsonProperty("options")
    private List<String> choix;

    @JsonProperty("correctOption")
    private int bonneReponse;

    public Question() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEnonce() { return enonce; }
    public void setEnonce(String enonce) { this.enonce = enonce; }

    public List<String> getChoix() { return choix; }
    public void setChoix(List<String> choix) { this.choix = choix; }

    public int getBonneReponse() { return bonneReponse; }
    public void setBonneReponse(int bonneReponse) { this.bonneReponse = bonneReponse; }
}
