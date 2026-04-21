package com.edwidge_jason.minimoodle.services;

public interface EcouteurDeDonnees {
    void onDataLoaded(Object data);
    void onError(String messageErreur);
}
