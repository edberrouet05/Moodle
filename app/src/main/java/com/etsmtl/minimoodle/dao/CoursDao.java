package com.etsmtl.minimoodle.dao;

import com.etsmtl.minimoodle.services.EcouteurDeDonnees;
import com.etsmtl.minimoodle.services.HttpJsonService;

import java.io.IOException;

public class CoursDao {

    public static void getCours(EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getCours(ecouteur);
    }

    public static void getCoursByUserId(String userId, EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getCoursByUserId(userId, ecouteur);
    }
}
