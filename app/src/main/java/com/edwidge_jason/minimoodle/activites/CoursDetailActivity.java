package com.edwidge_jason.minimoodle.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.edwidge_jason.minimoodle.R;

import java.util.ArrayList;

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
        ArrayList<String> annonces = getIntent().getStringArrayListExtra("COURS_ANNONCES");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(coursSigle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView txtTitre = findViewById(R.id.txt_titre_cours);
        TextView txtSigle = findViewById(R.id.txt_sigle_cours);
        TextView txtEnseignant = findViewById(R.id.txt_enseignant);
        TextView txtSession = findViewById(R.id.txt_session);
        TextView txtDescription = findViewById(R.id.txt_description);
        LinearLayout llAnnonces = findViewById(R.id.ll_annonces_cours);

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

        // Affichage des annonces du cours
        afficherAnnonces(llAnnonces, annonces);

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

    private void afficherAnnonces(LinearLayout container, ArrayList<String> annonces) {
        container.removeAllViews();

        if (annonces == null || annonces.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText(getString(R.string.aucune_annonce));
            tv.setTextSize(13f);
            tv.setTextColor(ContextCompat.getColor(this, R.color.texte_secondaire));
            container.addView(tv);
            return;
        }

        int dp8 = (int) (8 * getResources().getDisplayMetrics().density);
        int dp4 = dp8 / 2;

        for (String annonce : annonces) {
            CardView card = new CardView(this);
            CardView.LayoutParams cardParams = new CardView.LayoutParams(
                    CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(0, dp4, 0, dp4);
            card.setLayoutParams(cardParams);
            card.setRadius(6f);
            card.setCardElevation(2f);
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gris_clair));

            TextView tv = new TextView(this);
            tv.setText("• " + annonce);
            tv.setTextSize(13f);
            tv.setTextColor(ContextCompat.getColor(this, R.color.texte_principal));
            tv.setPadding(dp8, dp8, dp8, dp8);

            card.addView(tv);
            container.addView(card);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
