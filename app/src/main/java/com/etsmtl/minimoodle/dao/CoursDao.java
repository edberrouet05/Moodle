package com.etsmtl.minimoodle.dao;

import com.etsmtl.minimoodle.services.EcouteurDeDonnees;
import com.etsmtl.minimoodle.services.HttpJsonService;

import java.io.IOException;
import java.util.List;

public class CoursDao {

    public static void getCours(EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getCours(ecouteur);
    }

    public static void getCoursParIds(List<String> ids, EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getCoursParIds(ids, ecouteur);
    }
}
