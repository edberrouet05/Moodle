package com.edwidge_jason.minimoodle.activites;

import android.app.AlertDialog;
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
    private TextView texteErreur;
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
        texteErreur = findViewById(R.id.texte_erreur);
        Button btnConnexion = findViewById(R.id.btn_connexion);
        TextView lienInscription = findViewById(R.id.lien_inscription);
        TextView lienMdpOublie = findViewById(R.id.lien_mdp_oublie);

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
                texteErreur.setText(message);
                texteErreur.setVisibility(View.VISIBLE);
            }
        });

        authViewModel.getMotDePasseRecupere().observe(this, mdp -> {
            if (mdp != null) {
                new AlertDialog.Builder(this)
                        .setTitle("Mot de passe retrouvé")
                        .setMessage("Votre mot de passe est : " + mdp)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        btnConnexion.setOnClickListener(v -> {
            texteErreur.setVisibility(View.GONE);
            String courriel = champCourriel.getText().toString().trim();
            String motDePasse = champMotDePasse.getText().toString().trim();

            if (courriel.isEmpty() || motDePasse.isEmpty()) {
                texteErreur.setText(getString(R.string.erreur_champs_vides));
                texteErreur.setVisibility(View.VISIBLE);
                return;
            }

            authViewModel.connecter(courriel, motDePasse);
        });

        lienMdpOublie.setOnClickListener(v -> {
            EditText champEmail = new EditText(this);
            champEmail.setHint("Votre courriel");
            champEmail.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            new AlertDialog.Builder(this)
                    .setTitle("Mot de passe oublié")
                    .setMessage("Entrez votre courriel pour retrouver votre mot de passe.")
                    .setView(champEmail)
                    .setPositiveButton("Rechercher", (dialog, which) -> {
                        String courriel = champEmail.getText().toString().trim();
                        if (!courriel.isEmpty()) {
                            authViewModel.recupererMotDePasse(courriel);
                        }
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
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
