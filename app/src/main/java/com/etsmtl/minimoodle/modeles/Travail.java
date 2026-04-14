package com.etsmtl.minimoodle.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Travail {

    public enum Statut { A_FAIRE, REMIS, EN_RETARD, CORRIGE }

    private String id;

    @JsonProperty("title")
    private String titre;

    private String description;

    @JsonProperty("dueDate")
    private String dateRemise;

    private String instructions;

    @JsonProperty("grade")
    private Integer note;

    @JsonProperty("totalPoints")
    private int noteMax;

    @JsonProperty("status")
    private String etat;

    @JsonProperty("comment")
    private String commentaire;

    private String type;

    @JsonProperty("courseId")
    private String coursId;

    public Travail() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDateRemise() { return dateRemise; }
    public void setDateRemise(String dateRemise) { this.dateRemise = dateRemise; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }

    public int getNoteMax() { return noteMax; }
    public void setNoteMax(int noteMax) { this.noteMax = noteMax; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCoursId() { return coursId; }
    public void setCoursId(String coursId) { this.coursId = coursId; }

    public boolean isRemis() {
        return "Remis".equals(etat) || note != null;
    }

    public Statut getStatut() {
        if (note != null) return Statut.CORRIGE;
        if ("Remis".equals(etat)) return Statut.REMIS;
        if (estEnRetard()) return Statut.EN_RETARD;
        return Statut.A_FAIRE;
    }

    public boolean estEnRetard() {
        if (isRemis() || dateRemise == null) return false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA_FRENCH);
            Date limite = sdf.parse(dateRemise);
            return limite != null && new Date().after(limite);
        } catch (ParseException e) {
            return false;
        }
    }
}
