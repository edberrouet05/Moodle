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

    public void chargerTravailByCourseId(int courseId) {
        try {
            TravailDao.getTravailByCourseId(courseId, new EcouteurDeDonnees() {
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

    public void soumettreTravail(int travailId) {
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
