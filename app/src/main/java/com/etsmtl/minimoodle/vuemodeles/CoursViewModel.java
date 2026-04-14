package com.etsmtl.minimoodle.vuemodeles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.etsmtl.minimoodle.dao.CoursDao;
import com.etsmtl.minimoodle.modeles.Cours;
import com.etsmtl.minimoodle.services.EcouteurDeDonnees;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CoursViewModel extends ViewModel {

    private final MutableLiveData<List<Cours>> listeCoursLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> erreur = new MutableLiveData<>();

    public LiveData<List<Cours>> getListeCours() { return listeCoursLiveData; }
    public LiveData<String> getErreur() { return erreur; }

    public void chargerCours() {
        try {
            CoursDao.getCours(new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    listeCoursLiveData.postValue((List<Cours>) data);
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

    public void chargerCoursParIds(String idsInscritsStr) {
        final List<String> ids;
        if (idsInscritsStr != null && !idsInscritsStr.isEmpty()) {
            ids = Arrays.asList(idsInscritsStr.split(","));
        } else {
            ids = null;
        }
        try {
            CoursDao.getCours(new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    List<Cours> tousLesCours = (List<Cours>) data;
                    if (ids == null) {
                        listeCoursLiveData.postValue(tousLesCours);
                    } else {
                        List<Cours> filtres = new java.util.ArrayList<>();
                        for (Cours c : tousLesCours) {
                            if (ids.contains(c.getId())) {
                                filtres.add(c);
                            }
                        }
                        listeCoursLiveData.postValue(filtres);
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
}
