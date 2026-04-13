package com.etsmtl.minimoodle.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {

    private int id;
    private String enonce;
    private List<String> choix;
    private int bonneReponse;

    public Question() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEnonce() { return enonce; }
    public void setEnonce(String enonce) { this.enonce = enonce; }

    public List<String> getChoix() { return choix; }
    public void setChoix(List<String> choix) { this.choix = choix; }

    public int getBonneReponse() { return bonneReponse; }
    public void setBonneReponse(int bonneReponse) { this.bonneReponse = bonneReponse; }
}
