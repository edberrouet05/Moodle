package com.etsmtl.minimoodle.dao;

import com.etsmtl.minimoodle.modeles.Utilisateur;
import com.etsmtl.minimoodle.services.EcouteurDeDonnees;
import com.etsmtl.minimoodle.services.HttpJsonService;

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
