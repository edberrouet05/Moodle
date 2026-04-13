package com.etsmtl.minimoodle.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class CoursListActivity extends AppCompatActivity {

    private CoursViewModel coursViewModel;
    private CoursAdapter coursAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cours_list);

        progressBar = findViewById(R.id.progress_bar);
        RecyclerView recyclerView = findViewById(R.id.rv_cours);

        coursAdapter = new CoursAdapter(new ArrayList<>(), cours -> {
            Intent intent = new Intent(this, CoursDetailActivity.class);
            intent.putExtra("COURS_ID", cours.getId());
            intent.putExtra("COURS_TITRE", cours.getTitre());
            intent.putExtra("COURS_SIGLE", cours.getSigle());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(coursAdapter);

        coursViewModel = new ViewModelProvider(this).get(CoursViewModel.class);

        coursViewModel.getListeCours().observe(this, cours -> {
            progressBar.setVisibility(View.GONE);
            coursAdapter.mettreAJour(cours);
        });

        coursViewModel.getErreur().observe(this, message -> {
            progressBar.setVisibility(View.GONE);
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        coursViewModel.chargerCours();

        configurerNavigation();
    }

    private void configurerNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_cours);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_tableau_de_bord) {
                startActivity(new Intent(this, DashboardActivity.class));
                return true;
            } else if (id == R.id.nav_profil) {
                startActivity(new Intent(this, ProfilActivity.class));
                return true;
            }
            return true;
        });
    }
}
