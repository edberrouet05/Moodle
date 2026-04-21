package com.edwidge_jason.minimoodle.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edwidge_jason.minimoodle.R;
import com.edwidge_jason.minimoodle.adaptateurs.CoursAdapter;
import com.edwidge_jason.minimoodle.bd.MoodleDatabase;
import com.edwidge_jason.minimoodle.modeles.Cours;
import com.edwidge_jason.minimoodle.vuemodeles.CoursViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class CoursListActivity extends AppCompatActivity {

    private CoursViewModel coursViewModel;
    private CoursAdapter coursAdapter;
    private ProgressBar progressBar;
    private List<Cours> tousLesCours = new ArrayList<>();
    private int ongletActif = 0; // 0=Tous, 1=Actifs, 2=Terminés

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
            intent.putExtra("COURS_DESCRIPTION", cours.getDescription());
            intent.putExtra("COURS_ENSEIGNANT", cours.getEnseignant());
            intent.putExtra("COURS_SESSION", cours.getSession());
            if (cours.getAnnonces() != null) {
                intent.putStringArrayListExtra("COURS_ANNONCES", new ArrayList<>(cours.getAnnonces()));
            }
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(coursAdapter);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrerCours(newText);
                return true;
            }
        });

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ongletActif = tab.getPosition();
                filtrerCours(searchView.getQuery().toString());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        coursViewModel = new ViewModelProvider(this).get(CoursViewModel.class);

        coursViewModel.getListeCours().observe(this, cours -> {
            progressBar.setVisibility(View.GONE);
            tousLesCours = cours;
            filtrerCours(searchView.getQuery().toString());
        });

        coursViewModel.getErreur().observe(this, message -> {
            progressBar.setVisibility(View.GONE);
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        MoodleDatabase db = MoodleDatabase.getInstance(this);
        String[] session = db.getSession();
        String idsInscrits = (session != null && session.length > 4) ? session[4] : null;

        progressBar.setVisibility(View.VISIBLE);
        coursViewModel.chargerCoursParIds(idsInscrits);

        configurerNavigation();
    }

    private void filtrerCours(String requete) {
        String q = (requete != null) ? requete.trim().toLowerCase() : "";
        List<Cours> filtre = new ArrayList<>();
        for (Cours c : tousLesCours) {
            if (!q.isEmpty()) {
                boolean correspondRecherche = (c.getSigle() != null && c.getSigle().toLowerCase().contains(q))
                        || (c.getTitre() != null && c.getTitre().toLowerCase().contains(q));
                if (!correspondRecherche) continue;
            }
            if (ongletActif == 1 && !estCoursActif(c)) continue;
            if (ongletActif == 2 && estCoursActif(c)) continue;
            filtre.add(c);
        }
        coursAdapter.mettreAJour(filtre);
    }

    private boolean estCoursActif(Cours c) {
        String session = c.getSession();
        if (session == null) return true;
        return session.contains("2026") || session.contains("2025");
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
