package com.etsmtl.minimoodle.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.etsmtl.minimoodle.R;

public class CoursDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours_detail);

        int coursId = getIntent().getIntExtra("COURS_ID", -1);
        String coursTitre = getIntent().getStringExtra("COURS_TITRE");
        String coursSigle = getIntent().getStringExtra("COURS_SIGLE");

        TextView txtTitre = findViewById(R.id.txt_titre_cours);
        TextView txtSigle = findViewById(R.id.txt_sigle_cours);
        Button btnTravaux = findViewById(R.id.btn_voir_travaux);
        Button btnQuiz = findViewById(R.id.btn_voir_quiz);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(coursSigle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtTitre.setText(coursTitre);
        txtSigle.setText(coursSigle);

        btnTravaux.setOnClickListener(v -> {
            Intent intent = new Intent(this, TravailDetailActivity.class);
            intent.putExtra("COURS_ID", coursId);
            intent.putExtra("COURS_TITRE", coursTitre);
            startActivity(intent);
        });

        btnQuiz.setOnClickListener(v -> {
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
