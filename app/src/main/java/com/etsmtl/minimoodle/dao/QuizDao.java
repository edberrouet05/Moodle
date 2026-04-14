package com.etsmtl.minimoodle.dao;

import com.etsmtl.minimoodle.services.EcouteurDeDonnees;
import com.etsmtl.minimoodle.services.HttpJsonService;

import java.io.IOException;

public class QuizDao {

    public static void getQuizByCourseId(int courseId, EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getQuizByCourseId(courseId, ecouteur);
    }

    public static void getQuizParId(int quizId, EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getQuizParId(quizId, ecouteur);
    }

    public static void getTousQuiz(EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getTousQuiz(ecouteur);
    }
}
