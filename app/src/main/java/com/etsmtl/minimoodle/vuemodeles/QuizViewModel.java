package com.etsmtl.minimoodle.vuemodeles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.etsmtl.minimoodle.dao.QuizDao;
import com.etsmtl.minimoodle.modeles.Quiz;
import com.etsmtl.minimoodle.services.EcouteurDeDonnees;

import java.io.IOException;
import java.util.List;

public class QuizViewModel extends ViewModel {

    private final MutableLiveData<List<Quiz>> listeQuizLiveData = new MutableLiveData<>();
    private final MutableLiveData<Quiz> quizActifLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> erreur = new MutableLiveData<>();

    public LiveData<List<Quiz>> getListeQuiz() { return listeQuizLiveData; }
    public LiveData<Quiz> getQuizActif() { return quizActifLiveData; }
    public LiveData<String> getErreur() { return erreur; }

    public void chargerQuizByCourseId(String courseId) {
        try {
            QuizDao.getQuizByCourseId(courseId, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    listeQuizLiveData.postValue((List<Quiz>) data);
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

    public void chargerQuizParId(String quizId) {
        try {
            QuizDao.getQuizParId(quizId, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    quizActifLiveData.postValue((Quiz) data);
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

    public void chargerTousQuiz() {
        try {
            QuizDao.getTousQuiz(new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    listeQuizLiveData.postValue((List<Quiz>) data);
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
