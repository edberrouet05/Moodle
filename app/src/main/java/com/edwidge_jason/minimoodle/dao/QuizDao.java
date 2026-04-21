package com.edwidge_jason.minimoodle.dao;

import com.edwidge_jason.minimoodle.services.EcouteurDeDonnees;
import com.edwidge_jason.minimoodle.services.HttpJsonService;

import java.io.IOException;

public class QuizDao {

    public static void getQuizByCourseId(String coursId, EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getQuizByCourseId(coursId, ecouteur);
    }

    public static void getQuizParId(String quizId, EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getQuizParId(quizId, ecouteur);
    }

    public static void getTousQuiz(EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getTousQuiz(ecouteur);
    }
}
