package com.etsmtl.minimoodle.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.etsmtl.minimoodle.R;

public class QuizResultatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_resultat);

        int score = getIntent().getIntExtra("SCORE", 0);
        int total = getIntent().getIntExtra("TOTAL", 1);
        String titreQuiz = getIntent().getStringExtra("TITRE_QUIZ");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.titre_resultat));
        }

        TextView txtTitreQuiz = findViewById(R.id.txt_titre_quiz_resultat);
        TextView txtScore = findViewById(R.id.txt_score);
        TextView txtPourcentage = findViewById(R.id.txt_pourcentage);
        TextView txtMessage = findViewById(R.id.txt_message_resultat);
        Button btnRetour = findViewById(R.id.btn_retour_cours);

        txtTitreQuiz.setText(titreQuiz);
        txtScore.setText(score + " / " + total);

        int pourcentage = (total > 0) ? (int) Math.round((score * 100.0) / total) : 0;
        txtPourcentage.setText(pourcentage + "%");

        if (pourcentage >= 80) {
            txtMessage.setText("Excellent travail !");
        } else if (pourcentage >= 60) {
            txtMessage.setText("Bon résultat, continuez !");
        } else {
            txtMessage.setText("Continuez vos efforts !");
        }

        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(this, CoursListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
