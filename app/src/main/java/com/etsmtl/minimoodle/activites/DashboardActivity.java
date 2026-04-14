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
import com.etsmtl.minimoodle.adaptateurs.QuizAdapter;
import com.etsmtl.minimoodle.adaptateurs.TravailAdapter;
import com.etsmtl.minimoodle.bd.MoodleDatabase;
import com.etsmtl.minimoodle.modeles.Travail;
import com.etsmtl.minimoodle.vuemodeles.CoursViewModel;
import com.etsmtl.minimoodle.vuemodeles.QuizViewModel;
import com.etsmtl.minimoodle.vuemodeles.TravailViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardActivity extends AppCompatActivity {

    private String userId;
    private CoursViewModel coursViewModel;
    private TravailViewModel travailViewModel;
    private QuizViewModel quizViewModel;
    private CoursAdapter coursAdapter;
    private TravailAdapter travailAdapter;
    private QuizAdapter quizAdapter;

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
        String idsInscrits = session[4];
        String prenom = session[2];

        TextView txtBienvenue = findViewById(R.id.txt_bienvenue);
        txtBienvenue.setText("Bonjour, " + prenom + " !");

        RecyclerView rvCours = findViewById(R.id.rv_cours_recents);
        RecyclerView rvTravaux = findViewById(R.id.rv_travaux_recents);
        RecyclerView rvQuiz = findViewById(R.id.rv_quiz_recents);

        coursAdapter = new CoursAdapter(new ArrayList<>(), cours -> {
            Intent intent = new Intent(this, CoursDetailActivity.class);
            intent.putExtra("COURS_ID", cours.getId());
            intent.putExtra("COURS_TITRE", cours.getTitre());
            intent.putExtra("COURS_SIGLE", cours.getSigle());
            startActivity(intent);
        });


        travailAdapter = new TravailAdapter(new ArrayList<>(), travail -> {
            Intent intent = new Intent(this, TravailDetailActivity.class);
            intent.putExtra("COURS_ID", travail.getCoursId());
            startActivity(intent);
        });

        quizAdapter = new QuizAdapter(new ArrayList<>(), new HashSet<>(), quiz -> {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("QUIZ_ID", quiz.getId());
            intent.putExtra("QUIZ_TITRE", quiz.getTitre());
            startActivity(intent);
        });


        rvCours.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCours.setAdapter(coursAdapter);

        rvTravaux.setLayoutManager(new LinearLayoutManager(this));
        rvTravaux.setAdapter(travailAdapter);

        rvQuiz.setLayoutManager(new LinearLayoutManager(this));
        rvQuiz.setAdapter(quizAdapter);

        coursViewModel = new ViewModelProvider(this).get(CoursViewModel.class);
        travailViewModel = new ViewModelProvider(this).get(TravailViewModel.class);
        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        coursViewModel.getListeCours().observe(this, cours -> coursAdapter.mettreAJour(cours));

        travailViewModel.getListeTravail().observe(this, travaux -> {
            List<Travail> urgents = travaux.stream()
                    .filter(t -> t.getStatut() == Travail.Statut.A_FAIRE || t.getStatut() == Travail.Statut.EN_RETARD)
                    .limit(3)
                    .collect(Collectors.toList());
            travailAdapter.mettreAJour(urgents);
        });

        quizViewModel.getListeQuiz().observe(this, quizList -> {
            List quizLimites = quizList.stream().limit(3).collect(Collectors.toList());
            quizAdapter.mettreAJour(quizLimites,
                    new HashSet<>(db.getQuizTerminesIds(userId)));
        });

        coursViewModel.chargerCoursParIds(idsInscrits);
        travailViewModel.chargerTousTravaux();
        quizViewModel.chargerTousQuiz();

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
            } else if (id == R.id.nav_profil) {
                startActivity(new Intent(this, ProfilActivity.class));
                return true;
            }
            return true;
        });
    }
}
