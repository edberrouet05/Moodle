package com.etsmtl.minimoodle.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etsmtl.minimoodle.R;
import com.etsmtl.minimoodle.adaptateurs.TravailAdapter;
import com.etsmtl.minimoodle.bd.MoodleDatabase;
import com.etsmtl.minimoodle.vuemodeles.TravailViewModel;

import java.util.ArrayList;

public class TravailDetailActivity extends AppCompatActivity {

    private TravailViewModel travailViewModel;
    private TravailAdapter travailAdapter;
    private ProgressBar progressBar;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travail_detail);

        int coursId = getIntent().getIntExtra("COURS_ID", -1);
        String coursTitre = getIntent().getStringExtra("COURS_TITRE");

        String[] session = MoodleDatabase.getInstance(this).getSession();
        userId = (session != null) ? session[0] : "";

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.titre_travaux));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.progress_bar);
        RecyclerView recyclerView = findViewById(R.id.rv_travaux);

        travailAdapter = new TravailAdapter(new ArrayList<>(), travail -> {
            if (!travail.isRemis() && !MoodleDatabase.getInstance(this).estSoumisLocalement(travail.getId())) {
                travailViewModel.soumettreTravail(travail.getId());
            } else {
                Toast.makeText(this, "Ce travail a déjà été remis.", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(travailAdapter);

        travailViewModel = new ViewModelProvider(this).get(TravailViewModel.class);

        travailViewModel.getListeTravail().observe(this, travaux -> {
            progressBar.setVisibility(View.GONE);
            travailAdapter.mettreAJour(travaux);
        });

        travailViewModel.getTravailSoumis().observe(this, travail -> {
            if (travail != null) {
                MoodleDatabase.getInstance(this).marquerSoumisLocalement(travail.getId(), userId);
                Toast.makeText(this, "Travail remis avec succès !", Toast.LENGTH_SHORT).show();
                travailViewModel.chargerTravailByCourseId(coursId);
            }
        });

        travailViewModel.getErreur().observe(this, message -> {
            progressBar.setVisibility(View.GONE);
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        travailViewModel.chargerTravailByCourseId(coursId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
