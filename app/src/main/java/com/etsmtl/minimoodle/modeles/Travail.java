package com.etsmtl.minimoodle.modeles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Travail {

    public enum Statut { A_FAIRE, REMIS, EN_RETARD, CORRIGE }

    private int id;
    private String titre;
    private String description;
    private String dateRemise;
    private Integer note;
    private int noteMax;
    private boolean remis;
    private boolean corrige;
    private int courseId;

    public Travail() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDateRemise() { return dateRemise; }
    public void setDateRemise(String dateRemise) { this.dateRemise = dateRemise; }

    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }

    public int getNoteMax() { return noteMax; }
    public void setNoteMax(int noteMax) { this.noteMax = noteMax; }

    public boolean isRemis() { return remis; }
    public void setRemis(boolean remis) { this.remis = remis; }

    public boolean isCorrige() { return corrige; }
    public void setCorrige(boolean corrige) { this.corrige = corrige; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    /** Calcule le statut réel en tenant compte de la date limite */
    public Statut getStatut() {
        if (corrige || (remis && note != null)) return Statut.CORRIGE;
        if (remis) return Statut.REMIS;
        if (estEnRetard()) return Statut.EN_RETARD;
        return Statut.A_FAIRE;
    }

    public boolean estEnRetard() {
        if (remis || dateRemise == null) return false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA_FRENCH);
            Date limite = sdf.parse(dateRemise);
            return limite != null && new Date().after(limite);
        } catch (ParseException e) {
            return false;
        }
    }
}
