package com.etsmtl.minimoodle.dao;

import com.etsmtl.minimoodle.services.EcouteurDeDonnees;
import com.etsmtl.minimoodle.services.HttpJsonService;

import java.io.IOException;

public class TravailDao {

    public static void getTravaux(EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getTravaux(ecouteur);
    }

    public static void getTravailByCourseId(int courseId, EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getTravailByCourseId(courseId, ecouteur);
    }

    public static void soumettreTravail(int travailId, EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().soumettreTravail(travailId, ecouteur);
    }
}
