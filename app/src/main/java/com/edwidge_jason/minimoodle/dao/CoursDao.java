package com.edwidge_jason.minimoodle.dao;

import com.edwidge_jason.minimoodle.services.EcouteurDeDonnees;
import com.edwidge_jason.minimoodle.services.HttpJsonService;

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
