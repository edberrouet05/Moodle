package com.etsmtl.minimoodle.dao;

import com.etsmtl.minimoodle.services.EcouteurDeDonnees;
import com.etsmtl.minimoodle.services.HttpJsonService;

import java.io.IOException;

public class TravailDao {

    public static void getTravaux(EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getTravaux(ecouteur);
    }

    public static void getTravailByCourseId(String coursId, EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getTravailByCourseId(coursId, ecouteur);
    }

    public static void soumettreTravail(String travailId, EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().soumettreTravail(travailId, ecouteur);
    }
}
