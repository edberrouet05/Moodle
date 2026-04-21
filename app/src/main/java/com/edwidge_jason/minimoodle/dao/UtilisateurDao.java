package com.edwidge_jason.minimoodle.dao;

import com.edwidge_jason.minimoodle.modeles.Utilisateur;
import com.edwidge_jason.minimoodle.services.EcouteurDeDonnees;
import com.edwidge_jason.minimoodle.services.HttpJsonService;

import java.io.IOException;

public class UtilisateurDao {

    public static void getUtilisateurs(EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().getUtilisateurs(ecouteur);
    }

    public static void creerUtilisateur(Utilisateur utilisateur, EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().creerUtilisateur(utilisateur, ecouteur);
    }

    public static void mettreAJourUtilisateur(String userId, Utilisateur utilisateur, EcouteurDeDonnees ecouteur) throws IOException {
        new HttpJsonService().mettreAJourUtilisateur(userId, utilisateur, ecouteur);
    }
}
