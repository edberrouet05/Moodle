package com.etsmtl.minimoodle.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etsmtl.minimoodle.R;
import com.etsmtl.minimoodle.adaptateurs.CoursAdapter;
import com.etsmtl.minimoodle.bd.MoodleDatabase;
import com.etsmtl.minimoodle.vuemodeles.CoursViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private String userId;
    private CoursViewModel coursViewModel;
    private CoursAdapter coursAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        MoodleDatabase db = MoodleDatabase.getInstance(this);
        String[] session = db.getSession();
        if (session == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        userId = session[0];
        String prenom = session[2];

        TextView txtBienvenue = findViewById(R.id.txt_bienvenue);
        txtBienvenue.setText("Bonjour, " + prenom + " !");

        RecyclerView rvCours = findViewById(R.id.rv_cours_recents);

        coursAdapter = new CoursAdapter(new ArrayList<>(), cours -> {
            Intent intent = new Intent(this, CoursDetailActivity.class);
            intent.putExtra("COURS_ID", cours.getId());
            intent.putExtra("COURS_TITRE", cours.getTitre());
            intent.putExtra("COURS_SIGLE", cours.getSigle());
            startActivity(intent);
        });

        rvCours.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCours.setAdapter(coursAdapter);

        coursViewModel = new ViewModelProvider(this).get(CoursViewModel.class);
        coursViewModel.getListeCours().observe(this, cours -> coursAdapter.mettreAJour(cours));
        coursViewModel.chargerCoursByUserId(userId);

        configurerNavigation();
    }

    private void configurerNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_tableau_de_bord);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_cours) {
                startActivity(new Intent(this, CoursListActivity.class));
                return true;
            }
            return true;
        });
    }
}
