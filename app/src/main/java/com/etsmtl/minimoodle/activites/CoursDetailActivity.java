package com.etsmtl.minimoodle.activites;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.etsmtl.minimoodle.R;

public class CoursDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours_detail);

        String coursTitre = getIntent().getStringExtra("COURS_TITRE");
        String coursSigle = getIntent().getStringExtra("COURS_SIGLE");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(coursSigle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView txtTitre = findViewById(R.id.txt_titre_cours);
        TextView txtSigle = findViewById(R.id.txt_sigle_cours);
        txtTitre.setText(coursTitre);
        txtSigle.setText(coursSigle);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
