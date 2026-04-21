package com.edwidge_jason.minimoodle.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;

import com.edwidge_jason.minimoodle.R;
import com.edwidge_jason.minimoodle.bd.MoodleDatabase;
import com.edwidge_jason.minimoodle.modeles.Utilisateur;
import com.edwidge_jason.minimoodle.vuemodeles.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText champCourriel;
    private EditText champMotDePasse;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vérifier si une session existe déjà
        String[] session = MoodleDatabase.getInstance(this).getSession();
        if (session != null) {
            allerAuTableauDeBord();
            return;
        }

        setContentView(R.layout.activity_login);

        champCourriel = findViewById(R.id.champ_courriel);
        champMotDePasse = findViewById(R.id.champ_mot_de_passe);
        Button btnConnexion = findViewById(R.id.btn_connexion);
        TextView lienInscription = findViewById(R.id.lien_inscription);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getUtilisateurConnecte().observe(this, utilisateur -> {
            if (utilisateur != null) {
                String idsInscrits = "";
                if (utilisateur.getCoursInscrits() != null) {
                    idsInscrits = TextUtils.join(",", utilisateur.getCoursInscrits());
                }
                MoodleDatabase.getInstance(this).sauvegarderSession(
                        utilisateur.getId(),
                        utilisateur.getNom(),
                        utilisateur.getPrenom(),
                        utilisateur.getCourriel(),
                        idsInscrits,
                        utilisateur.getTelephone(),
                        utilisateur.getPhotoProfil()
                );
                allerAuTableauDeBord();
            }
        });

        authViewModel.getErreur().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        btnConnexion.setOnClickListener(v -> {
            String courriel = champCourriel.getText().toString().trim();
            String motDePasse = champMotDePasse.getText().toString().trim();

            if (courriel.isEmpty() || motDePasse.isEmpty()) {
                Toast.makeText(this, getString(R.string.erreur_champs_vides), Toast.LENGTH_SHORT).show();
                return;
            }

            authViewModel.connecter(courriel, motDePasse);
        });

        lienInscription.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void allerAuTableauDeBord() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
