package com.etsmtl.minimoodle.vuemodeles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.etsmtl.minimoodle.dao.TravailDao;
import com.etsmtl.minimoodle.modeles.Travail;
import com.etsmtl.minimoodle.services.EcouteurDeDonnees;

import java.io.IOException;
import java.util.List;

public class TravailViewModel extends ViewModel {

    private final MutableLiveData<List<Travail>> listeTravailLiveData = new MutableLiveData<>();
    private final MutableLiveData<Travail> travailSoumisLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> erreur = new MutableLiveData<>();

    public LiveData<List<Travail>> getListeTravail() { return listeTravailLiveData; }
    public LiveData<Travail> getTravailSoumis() { return travailSoumisLiveData; }
    public LiveData<String> getErreur() { return erreur; }

    public void chargerTravailByCourseId(String coursId) {
        try {
            TravailDao.getTravaux(new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    List<Travail> tous = (List<Travail>) data;
                    if (coursId == null) {
                        listeTravailLiveData.postValue(tous);
                        return;
                    }
                    java.util.List<Travail> filtres = new java.util.ArrayList<>();
                    for (Travail t : tous) {
                        if (coursId.equals(t.getCoursId())) filtres.add(t);
                    }
                    listeTravailLiveData.postValue(filtres);
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

    public void chargerTousTravaux() {
        try {
            TravailDao.getTravaux(new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    listeTravailLiveData.postValue((List<Travail>) data);
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

    public void reinitialiserTravailSoumis() {
        travailSoumisLiveData.postValue(null);
    }

    public void soumettreTravail(String travailId) {
        try {
            TravailDao.soumettreTravail(travailId, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    travailSoumisLiveData.postValue((Travail) data);
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
