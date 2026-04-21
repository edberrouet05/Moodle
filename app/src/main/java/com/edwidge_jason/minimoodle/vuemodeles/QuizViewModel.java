package com.edwidge_jason.minimoodle.vuemodeles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.edwidge_jason.minimoodle.dao.QuizDao;
import com.edwidge_jason.minimoodle.modeles.Quiz;
import com.edwidge_jason.minimoodle.services.EcouteurDeDonnees;

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
            QuizDao.getTousQuiz(new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    List<Quiz> tous = (List<Quiz>) data;
                    if (courseId == null) {
                        listeQuizLiveData.postValue(tous);
                        return;
                    }
                    java.util.List<Quiz> filtres = new java.util.ArrayList<>();
                    for (Quiz q : tous) {
                        if (courseId.equals(q.getCoursId())) filtres.add(q);
                    }
                    listeQuizLiveData.postValue(filtres);
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
