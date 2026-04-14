package com.etsmtl.minimoodle.activites;

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

import com.etsmtl.minimoodle.R;
import com.etsmtl.minimoodle.adaptateurs.ResultatQuizAdapter;
import com.etsmtl.minimoodle.bd.MoodleDatabase;
import com.etsmtl.minimoodle.modeles.ResultatQuiz;
import com.etsmtl.minimoodle.vuemodeles.AuthViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ProfilActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private String userId;
    private EditText champPrenom, champTelephone, champPhotoProfil, champMotDePasse;

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

        // session: [0]=userId, [1]=nom, [2]=prenom, [3]=courriel, [4]=programme, [5]=telephone, [6]=photo
        userId = session[0];
        String nom = session[1];
        String prenom = session[2];
        String courriel = session[3];
        String programme = session[4];
        String telephone = session.length > 5 ? session[5] : "";
        String photo = session.length > 6 ? session[6] : "";

        TextView txtNomComplet = findViewById(R.id.txt_nom_complet);
        TextView txtCourriel = findViewById(R.id.txt_courriel_profil);
        TextView txtProgramme = findViewById(R.id.txt_programme_profil);

        champPrenom = findViewById(R.id.champ_edit_prenom);
        champTelephone = findViewById(R.id.champ_edit_telephone);
        champPhotoProfil = findViewById(R.id.champ_edit_photo);
        champMotDePasse = findViewById(R.id.champ_edit_mot_de_passe);

        Button btnModifier = findViewById(R.id.btn_modifier_profil);
        Button btnDeconnexion = findViewById(R.id.btn_deconnexion);
        RecyclerView rvResultats = findViewById(R.id.rv_resultats_quiz);

        txtNomComplet.setText(prenom + " " + nom);
        txtCourriel.setText(courriel);
        txtProgramme.setText(programme != null ? programme : "");

        if (champPrenom != null) champPrenom.setText(prenom);
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
                String newTel = champTelephone.getText().toString().trim();
                String newPhoto = champPhotoProfil.getText().toString().trim();
                db.sauvegarderSession(userId, nom, newPrenom.isEmpty() ? prenom : newPrenom,
                        courriel, programme, newTel, newPhoto);
                txtNomComplet.setText((newPrenom.isEmpty() ? prenom : newPrenom) + " " + nom);
            }
        });

        authViewModel.getErreur().observe(this, msg -> {
            if (msg != null) Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        });

        btnModifier.setOnClickListener(v -> {
            String newPrenom = champPrenom.getText().toString().trim();
            String newTel = champTelephone.getText().toString().trim();
            String newPhoto = champPhotoProfil.getText().toString().trim();
            String newMdp = champMotDePasse.getText().toString().trim();

            if (newPrenom.isEmpty()) {
                Toast.makeText(this, "Le prénom ne peut pas être vide.", Toast.LENGTH_SHORT).show();
                return;
            }
            authViewModel.mettreAJourProfil(userId, newPrenom, nom, newTel, newPhoto, newMdp);
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
