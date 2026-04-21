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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edwidge_jason.minimoodle.R;
import com.edwidge_jason.minimoodle.adaptateurs.ResultatQuizAdapter;
import com.edwidge_jason.minimoodle.bd.MoodleDatabase;
import com.edwidge_jason.minimoodle.modeles.ResultatQuiz;
import com.edwidge_jason.minimoodle.vuemodeles.AuthViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ProfilActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private String userId;
    private EditText champPrenom, champNom, champTelephone, champPhotoProfil, champMotDePasse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        MoodleDatabase db = MoodleDatabase.getInstance(this);
        String[] session = db.getSession();

        if (session == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // session : [0]=idUtilisateur, [1]=nom, [2]=prenom, [3]=courriel, [4]=idsInscrits, [5]=telephone, [6]=photo
        userId = session[0];
        String nom = session[1];
        String prenom = session[2];
        String courriel = session[3];
        String idsInscrits = session[4];
        String telephone = session.length > 5 ? session[5] : "";
        String photo = session.length > 6 ? session[6] : "";

        TextView txtNomComplet = findViewById(R.id.txt_nom_complet);
        TextView txtCourriel = findViewById(R.id.txt_courriel_profil);
        TextView txtProgramme = findViewById(R.id.txt_programme_profil);
        TextView txtInitiales = findViewById(R.id.txt_initiales_avatar);

        champPrenom = findViewById(R.id.champ_edit_prenom);
        champNom = findViewById(R.id.champ_edit_nom);
        champTelephone = findViewById(R.id.champ_edit_telephone);
        champPhotoProfil = findViewById(R.id.champ_edit_photo);
        champMotDePasse = findViewById(R.id.champ_edit_mot_de_passe);

        Button btnModifier = findViewById(R.id.btn_modifier_profil);
        Button btnDeconnexion = findViewById(R.id.btn_deconnexion);
        RecyclerView rvResultats = findViewById(R.id.rv_resultats_quiz);

        txtNomComplet.setText(prenom + " " + nom);
        txtCourriel.setText(courriel);
        txtProgramme.setText("Courriel : " + courriel);

        String initP = (prenom != null && !prenom.isEmpty()) ? String.valueOf(prenom.charAt(0)).toUpperCase() : "";
        String initN = (nom != null && !nom.isEmpty()) ? String.valueOf(nom.charAt(0)).toUpperCase() : "";
        if (txtInitiales != null) txtInitiales.setText(initP + initN);

        if (champPrenom != null) champPrenom.setText(prenom);
        if (champNom != null) champNom.setText(nom);
        if (champTelephone != null && telephone != null) champTelephone.setText(telephone);
        if (champPhotoProfil != null && photo != null) champPhotoProfil.setText(photo);

        List<ResultatQuiz> resultats = db.getResultatsParUser(userId);
        ResultatQuizAdapter adapter = new ResultatQuizAdapter(resultats);
        rvResultats.setLayoutManager(new LinearLayoutManager(this));
        rvResultats.setAdapter(adapter);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getMiseAJourReussie().observe(this, ok -> {
            if (ok != null && ok) {
                Toast.makeText(this, "Profil mis à jour !", Toast.LENGTH_SHORT).show();
                String newPrenom = champPrenom.getText().toString().trim();
                String newNom = champNom != null ? champNom.getText().toString().trim() : "";
                String newTel = champTelephone.getText().toString().trim();
                String newPhoto = champPhotoProfil.getText().toString().trim();
                String savedPrenom = newPrenom.isEmpty() ? prenom : newPrenom;
                String savedNom = newNom.isEmpty() ? nom : newNom;
                db.sauvegarderSession(userId, savedNom, savedPrenom, courriel, idsInscrits, newTel, newPhoto);
                txtNomComplet.setText(savedPrenom + " " + savedNom);
                String initP2 = savedPrenom.isEmpty() ? "" : String.valueOf(savedPrenom.charAt(0)).toUpperCase();
                String initN2 = savedNom.isEmpty() ? "" : String.valueOf(savedNom.charAt(0)).toUpperCase();
                TextView txtInitiales2 = findViewById(R.id.txt_initiales_avatar);
                if (txtInitiales2 != null) txtInitiales2.setText(initP2 + initN2);
            }
        });

        authViewModel.getErreur().observe(this, msg -> {
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        });

        btnModifier.setOnClickListener(v -> {
            String newPrenom = champPrenom.getText().toString().trim();
            String newNom = champNom != null ? champNom.getText().toString().trim() : "";
            String newTel = champTelephone.getText().toString().trim();
            String newPhoto = champPhotoProfil.getText().toString().trim();
            String newMdp = champMotDePasse.getText().toString().trim();

            if (newPrenom.isEmpty()) {
                Toast.makeText(this, "Le prénom ne peut pas être vide.", Toast.LENGTH_SHORT).show();
                return;
            }
            authViewModel.mettreAJourProfil(userId, newPrenom, newNom.isEmpty() ? nom : newNom,
                    newTel, newPhoto, newMdp);
        });

        btnDeconnexion.setOnClickListener(v -> {
            db.supprimerSession();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        configurerNavigation();
    }

    private void configurerNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_profil);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_tableau_de_bord) {
                startActivity(new Intent(this, DashboardActivity.class));
                return true;
            } else if (id == R.id.nav_cours) {
                startActivity(new Intent(this, CoursListActivity.class));
                return true;
            }
            return true;
        });
    }
}
