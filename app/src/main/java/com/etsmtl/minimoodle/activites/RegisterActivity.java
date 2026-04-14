package com.etsmtl.minimoodle.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.etsmtl.minimoodle.R;
import com.etsmtl.minimoodle.vuemodeles.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText champNom;
    private EditText champPrenom;
    private EditText champCourriel;
    private EditText champMotDePasse;
    private EditText champProgramme;
    private EditText champTelephone;
    private EditText champPhotoProfil;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        champNom = findViewById(R.id.champ_nom);
        champPrenom = findViewById(R.id.champ_prenom);
        champCourriel = findViewById(R.id.champ_courriel);
        champMotDePasse = findViewById(R.id.champ_mot_de_passe);
        champProgramme = findViewById(R.id.champ_programme);
        champTelephone = findViewById(R.id.champ_telephone);
        champPhotoProfil = findViewById(R.id.champ_photo_profil);
        Button btnInscrire = findViewById(R.id.btn_inscription);
        TextView lienConnexion = findViewById(R.id.lien_connexion);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getInscriptionReussie().observe(this, reussie -> {
            if (reussie != null && reussie) {
                Toast.makeText(this, "Compte créé avec succès !", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });

        authViewModel.getErreur().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        btnInscrire.setOnClickListener(v -> {
            String nom = champNom.getText().toString().trim();
            String prenom = champPrenom.getText().toString().trim();
            String courriel = champCourriel.getText().toString().trim();
            String motDePasse = champMotDePasse.getText().toString().trim();
            String telephone = champTelephone.getText().toString().trim();
            String photoProfil = champPhotoProfil.getText().toString().trim();

            if (nom.isEmpty() || prenom.isEmpty() || courriel.isEmpty() || motDePasse.isEmpty()) {
                Toast.makeText(this, getString(R.string.erreur_champs_vides), Toast.LENGTH_SHORT).show();
                return;
            }

            authViewModel.inscrire(nom, prenom, courriel, motDePasse, telephone, photoProfil);
        });

        lienConnexion.setOnClickListener(v -> finish());
    }
}
