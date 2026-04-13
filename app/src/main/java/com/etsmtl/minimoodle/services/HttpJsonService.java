package com.etsmtl.minimoodle.services;

import com.etsmtl.minimoodle.modeles.Cours;
import com.etsmtl.minimoodle.modeles.Quiz;
import com.etsmtl.minimoodle.modeles.Travail;
import com.etsmtl.minimoodle.modeles.Utilisateur;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpJsonService {

    private static final String URL_BASE = "http://10.0.2.2:3000";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // ─── Utilisateurs ───────────────────────────────────────────────────────────

    public void getUtilisateurs(EcouteurDeDonnees ecouteur) {
        Request request = new Request.Builder()
                .url(URL_BASE + "/users")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Utilisateur[] users = mapper.readValue(json, Utilisateur[].class);
                    ecouteur.onDataLoaded(Arrays.asList(users));
                } catch (JsonProcessingException e) {
                    ecouteur.onError("Erreur de lecture JSON");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ecouteur.onError("Problème réseau : " + e.getMessage());
            }
        });
    }

    public void creerUtilisateur(Utilisateur utilisateur, EcouteurDeDonnees ecouteur) throws IOException {
        String jsonBody = mapper.writeValueAsString(utilisateur);
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(URL_BASE + "/users")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Utilisateur cree = mapper.readValue(json, Utilisateur.class);
                    ecouteur.onDataLoaded(cree);
                } catch (JsonProcessingException e) {
                    ecouteur.onError("Erreur de lecture JSON");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ecouteur.onError("Problème réseau : " + e.getMessage());
            }
        });
    }

    // ─── Cours ──────────────────────────────────────────────────────────────────

    public void getCours(EcouteurDeDonnees ecouteur) {
        Request request = new Request.Builder()
                .url(URL_BASE + "/courses")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Cours[] cours = mapper.readValue(json, Cours[].class);
                    ecouteur.onDataLoaded(Arrays.asList(cours));
                } catch (JsonProcessingException e) {
                    ecouteur.onError("Erreur de lecture JSON");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ecouteur.onError("Problème réseau : " + e.getMessage());
            }
        });
    }

    public void getCoursByUserId(String userId, EcouteurDeDonnees ecouteur) {
        Request request = new Request.Builder()
                .url(URL_BASE + "/courses?userId=" + userId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Cours[] cours = mapper.readValue(json, Cours[].class);
                    ecouteur.onDataLoaded(Arrays.asList(cours));
                } catch (JsonProcessingException e) {
                    ecouteur.onError("Erreur de lecture JSON");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ecouteur.onError("Problème réseau : " + e.getMessage());
            }
        });
    }

    // ─── Travaux ─────────────────────────────────────────────────────────────────

    public void getTravaux(EcouteurDeDonnees ecouteur) {
        Request request = new Request.Builder()
                .url(URL_BASE + "/assignments")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Travail[] travaux = mapper.readValue(json, Travail[].class);
                    ecouteur.onDataLoaded(Arrays.asList(travaux));
                } catch (JsonProcessingException e) {
                    ecouteur.onError("Erreur de lecture JSON");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ecouteur.onError("Problème réseau : " + e.getMessage());
            }
        });
    }

    public void getTravailByCourseId(int courseId, EcouteurDeDonnees ecouteur) {
        Request request = new Request.Builder()
                .url(URL_BASE + "/assignments?courseId=" + courseId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Travail[] travaux = mapper.readValue(json, Travail[].class);
                    ecouteur.onDataLoaded(Arrays.asList(travaux));
                } catch (JsonProcessingException e) {
                    ecouteur.onError("Erreur de lecture JSON");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ecouteur.onError("Problème réseau : " + e.getMessage());
            }
        });
    }

    public void soumettreTravail(int travailId, EcouteurDeDonnees ecouteur) throws IOException {
        String jsonBody = "{\"remis\": true}";
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(URL_BASE + "/assignments/" + travailId)
                .patch(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Travail travail = mapper.readValue(json, Travail.class);
                    ecouteur.onDataLoaded(travail);
                } catch (JsonProcessingException e) {
                    ecouteur.onError("Erreur de lecture JSON");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ecouteur.onError("Problème réseau : " + e.getMessage());
            }
        });
    }

    // ─── Quiz ────────────────────────────────────────────────────────────────────

    public void getQuizByCourseId(int courseId, EcouteurDeDonnees ecouteur) {
        Request request = new Request.Builder()
                .url(URL_BASE + "/quizzes?courseId=" + courseId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Quiz[] quizzes = mapper.readValue(json, Quiz[].class);
                    ecouteur.onDataLoaded(Arrays.asList(quizzes));
                } catch (JsonProcessingException e) {
                    ecouteur.onError("Erreur de lecture JSON");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ecouteur.onError("Problème réseau : " + e.getMessage());
            }
        });
    }

    public void getQuizParId(int quizId, EcouteurDeDonnees ecouteur) {
        Request request = new Request.Builder()
                .url(URL_BASE + "/quizzes/" + quizId)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Quiz quiz = mapper.readValue(json, Quiz.class);
                    ecouteur.onDataLoaded(quiz);
                } catch (JsonProcessingException e) {
                    ecouteur.onError("Erreur de lecture JSON");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ecouteur.onError("Problème réseau : " + e.getMessage());
            }
        });
    }
}

    // Ajout en bas de la classe — à insérer avant le dernier 
    public void mettreAJourUtilisateur(String userId, Utilisateur utilisateur, EcouteurDeDonnees ecouteur) throws IOException {
        String jsonBody = mapper.writeValueAsString(utilisateur);
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(URL_BASE + "/users/" + userId)
                .patch(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Utilisateur updated = mapper.readValue(json, Utilisateur.class);
                    ecouteur.onDataLoaded(updated);
                } catch (JsonProcessingException e) {
                    ecouteur.onError("Erreur de lecture JSON");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ecouteur.onError("Problème réseau : " + e.getMessage());
            }
        });
    }

    public void getTousQuiz(EcouteurDeDonnees ecouteur) {
        Request request = new Request.Builder()
                .url(URL_BASE + "/quizzes")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                try {
                    Quiz[] quizzes = mapper.readValue(json, Quiz[].class);
                    ecouteur.onDataLoaded(Arrays.asList(quizzes));
                } catch (JsonProcessingException e) {
                    ecouteur.onError("Erreur de lecture JSON");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ecouteur.onError("Problème réseau : " + e.getMessage());
            }
        });
    }

}