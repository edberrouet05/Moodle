package com.etsmtl.minimoodle.vuemodeles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.etsmtl.minimoodle.dao.UtilisateurDao;
import com.etsmtl.minimoodle.modeles.Utilisateur;
import com.etsmtl.minimoodle.services.EcouteurDeDonnees;

import java.io.IOException;
import java.util.List;

public class AuthViewModel extends ViewModel {

    private final MutableLiveData<Utilisateur> utilisateurConnecte = new MutableLiveData<>();
    private final MutableLiveData<String> erreur = new MutableLiveData<>();
    private final MutableLiveData<Boolean> inscriptionReussie = new MutableLiveData<>();
    private final MutableLiveData<Boolean> miseAJourReussie = new MutableLiveData<>();

    public LiveData<Utilisateur> getUtilisateurConnecte() { return utilisateurConnecte; }
    public LiveData<String> getErreur() { return erreur; }
    public LiveData<Boolean> getInscriptionReussie() { return inscriptionReussie; }
    public LiveData<Boolean> getMiseAJourReussie() { return miseAJourReussie; }

    public void connecter(String courriel, String motDePasse) {
        try {
            UtilisateurDao.getUtilisateurs(new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    List<Utilisateur> users = (List<Utilisateur>) data;
                    Utilisateur trouve = null;
                    for (Utilisateur u : users) {
                        if (u.getCourriel().equals(courriel) && u.getMotDePasse().equals(motDePasse)) {
                            trouve = u;
                            break;
                        }
                    }
                    if (trouve != null) {
                        utilisateurConnecte.postValue(trouve);
                    } else {
                        erreur.postValue("Courriel ou mot de passe incorrect");
                    }
                }

                @Override
                public void onError(String messageErreur) {
                    erreur.postValue(messageErreur);
                }
            });
        } catch (IOException e) {
            erreur.postValue("Impossible de joindre le serveur");
        }
    }

    public void inscrire(String nom, String prenom, String courriel, String motDePasse,
                         String programme, String telephone, String photoProfil) {
        Utilisateur nouvelUtilisateur = new Utilisateur();
        nouvelUtilisateur.setNom(nom);
        nouvelUtilisateur.setPrenom(prenom);
        nouvelUtilisateur.setCourriel(courriel);
        nouvelUtilisateur.setMotDePasse(motDePasse);
        nouvelUtilisateur.setProgramme(programme);
        nouvelUtilisateur.setTelephone(telephone.isEmpty() ? null : telephone);
        nouvelUtilisateur.setPhotoProfil(photoProfil.isEmpty() ? null : photoProfil);

        try {
            UtilisateurDao.creerUtilisateur(nouvelUtilisateur, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    inscriptionReussie.postValue(true);
                }

                @Override
                public void onError(String messageErreur) {
                    erreur.postValue(messageErreur);
                }
            });
        } catch (IOException e) {
            erreur.postValue("Impossible de joindre le serveur");
        }
    }

    public void mettreAJourProfil(String userId, String prenom, String nom, String telephone,
                                   String photoProfil, String motDePasse) {
        Utilisateur miseAJour = new Utilisateur();
        miseAJour.setPrenom(prenom);
        miseAJour.setNom(nom);
        miseAJour.setTelephone(telephone.isEmpty() ? null : telephone);
        miseAJour.setPhotoProfil(photoProfil.isEmpty() ? null : photoProfil);
        if (!motDePasse.isEmpty()) {
            miseAJour.setMotDePasse(motDePasse);
        }

        try {
            UtilisateurDao.mettreAJourUtilisateur(userId, miseAJour, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    miseAJourReussie.postValue(true);
                }

                @Override
                public void onError(String messageErreur) {
                    erreur.postValue(messageErreur);
                }
            });
        } catch (IOException e) {
            erreur.postValue("Impossible de joindre le serveur");
        }
    }
}
