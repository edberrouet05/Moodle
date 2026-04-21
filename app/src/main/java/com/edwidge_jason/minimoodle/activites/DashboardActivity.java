package com.edwidge_jason.minimoodle.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edwidge_jason.minimoodle.R;
import com.edwidge_jason.minimoodle.adaptateurs.CoursAdapter;
import com.edwidge_jason.minimoodle.adaptateurs.QuizAdapter;
import com.edwidge_jason.minimoodle.adaptateurs.TravailAdapter;
import com.edwidge_jason.minimoodle.bd.MoodleDatabase;
import com.edwidge_jason.minimoodle.modeles.Cours;
import com.edwidge_jason.minimoodle.modeles.Travail;
import com.edwidge_jason.minimoodle.vuemodeles.CoursViewModel;
import com.edwidge_jason.minimoodle.vuemodeles.QuizViewModel;
import com.edwidge_jason.minimoodle.vuemodeles.TravailViewModel;
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
    private TextView txtNbAFaire;
    private TextView txtNbEnRetard;
    private TextView txtNbRemis;
    private TextView txtNbCorriges;

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

        txtNbAFaire = findViewById(R.id.txt_nb_a_faire);
        txtNbEnRetard = findViewById(R.id.txt_nb_en_retard);
        txtNbRemis = findViewById(R.id.txt_nb_remis);
        txtNbCorriges = findViewById(R.id.txt_nb_corriges);

        RecyclerView rvCours = findViewById(R.id.rv_cours_recents);
        RecyclerView rvTravaux = findViewById(R.id.rv_travaux_recents);
        RecyclerView rvQuiz = findViewById(R.id.rv_quiz_recents);

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

        LinearLayout llAnnonces = findViewById(R.id.ll_annonces);

        coursViewModel.getListeCours().observe(this, cours -> {
            coursAdapter.mettreAJour(cours);
            llAnnonces.removeAllViews();
            for (Cours c : cours) {
                if (c.getAnnonces() == null || c.getAnnonces().isEmpty()) continue;
                for (String annonce : c.getAnnonces()) {
                    CardView card = new CardView(this);
                    CardView.LayoutParams cardParams = new CardView.LayoutParams(
                            CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
                    cardParams.setMargins(0, 0, 0, 8);
                    card.setLayoutParams(cardParams);
                    card.setRadius(8f);
                    card.setCardElevation(3f);

                    LinearLayout inner = new LinearLayout(this);
                    inner.setOrientation(LinearLayout.VERTICAL);
                    inner.setPadding(32, 24, 32, 24);

                    TextView tvSigle = new TextView(this);
                    tvSigle.setText(c.getSigle());
                    tvSigle.setTextSize(12f);
                    tvSigle.setTextColor(getColor(R.color.couleur_primaire));
                    tvSigle.setTypeface(null, android.graphics.Typeface.BOLD);

                    TextView tvAnnonce = new TextView(this);
                    tvAnnonce.setText(annonce);
                    tvAnnonce.setTextSize(14f);
                    tvAnnonce.setPadding(0, 4, 0, 0);

                    inner.addView(tvSigle);
                    inner.addView(tvAnnonce);
                    card.addView(inner);
                    llAnnonces.addView(card);
                }
            }
        });
        coursViewModel.getErreur().observe(this, msg -> {
            if (msg != null) android.widget.Toast.makeText(this, "Cours : " + msg, android.widget.Toast.LENGTH_LONG).show();
        });

        travailViewModel.getErreur().observe(this, msg -> {
            if (msg != null) android.widget.Toast.makeText(this, "Travaux : " + msg, android.widget.Toast.LENGTH_LONG).show();
        });

        travailViewModel.getListeTravail().observe(this, travaux -> {
            List<Travail> urgents = new ArrayList<>();
            int aFaire = 0, enRetard = 0, remis = 0, corriges = 0;
            for (Travail t : travaux) {
                boolean soumisLocal = MoodleDatabase.getInstance(this).estSoumisLocalement(t.getId());
                Travail.Statut s = soumisLocal ? Travail.Statut.REMIS : t.getStatut();
                switch (s) {
                    case A_FAIRE: aFaire++; break;
                    case EN_RETARD: enRetard++; break;
                    case REMIS: remis++; break;
                    case CORRIGE: corriges++; break;
                }
                if (s == Travail.Statut.A_FAIRE || s == Travail.Statut.EN_RETARD) {
                    if (urgents.size() < 3) urgents.add(t);
                }
            }
            travailAdapter.mettreAJour(urgents);
            txtNbAFaire.setText(String.valueOf(aFaire));
            txtNbEnRetard.setText(String.valueOf(enRetard));
            txtNbRemis.setText(String.valueOf(remis));
            txtNbCorriges.setText(String.valueOf(corriges));
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
