package com.etsmtl.minimoodle.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.etsmtl.minimoodle.R;

public class CoursDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours_detail);

        String coursId = getIntent().getStringExtra("COURS_ID");
        String coursTitre = getIntent().getStringExtra("COURS_TITRE");
        String coursSigle = getIntent().getStringExtra("COURS_SIGLE");
        String coursDescription = getIntent().getStringExtra("COURS_DESCRIPTION");
        String coursEnseignant = getIntent().getStringExtra("COURS_ENSEIGNANT");
        String coursSession = getIntent().getStringExtra("COURS_SESSION");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(coursSigle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView txtTitre = findViewById(R.id.txt_titre_cours);
        TextView txtSigle = findViewById(R.id.txt_sigle_cours);
        TextView txtEnseignant = findViewById(R.id.txt_enseignant);
        TextView txtSession = findViewById(R.id.txt_session);
        TextView txtDescription = findViewById(R.id.txt_description);

        txtTitre.setText(coursTitre);
        txtSigle.setText(coursSigle);

        if (coursEnseignant != null && !coursEnseignant.isEmpty()) {
            txtEnseignant.setText("Enseignant : " + coursEnseignant);
            txtEnseignant.setVisibility(View.VISIBLE);
        } else {
            txtEnseignant.setVisibility(View.GONE);
        }

        if (coursSession != null && !coursSession.isEmpty()) {
            txtSession.setText("Session : " + coursSession);
            txtSession.setVisibility(View.VISIBLE);
        } else {
            txtSession.setVisibility(View.GONE);
        }

        if (coursDescription != null && !coursDescription.isEmpty()) {
            txtDescription.setText(coursDescription);
        } else {
            txtDescription.setText("Aucune description disponible.");
        }

        findViewById(R.id.btn_voir_travaux).setOnClickListener(v -> {
            Intent intent = new Intent(this, TravailDetailActivity.class);
            intent.putExtra("COURS_ID", coursId);
            intent.putExtra("COURS_TITRE", coursTitre);
            startActivity(intent);
        });

        findViewById(R.id.btn_voir_quiz).setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizListActivity.class);
            intent.putExtra("COURS_ID", coursId);
            intent.putExtra("COURS_TITRE", coursTitre);
            startActivity(intent);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
