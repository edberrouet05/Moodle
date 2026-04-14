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
import com.etsmtl.minimoodle.adaptateurs.TravailAdapter;
import com.etsmtl.minimoodle.vuemodeles.TravailViewModel;

import java.util.ArrayList;

public class TravailDetailActivity extends AppCompatActivity {

    private TravailViewModel travailViewModel;
    private TravailAdapter travailAdapter;
    private ProgressBar progressBar;
    private String coursId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travail_detail);

        coursId = getIntent().getStringExtra("COURS_ID");
        String coursTitre = getIntent().getStringExtra("COURS_TITRE");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.titre_travaux));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.progress_bar);
        RecyclerView recyclerView = findViewById(R.id.rv_travaux);

        travailAdapter = new TravailAdapter(new ArrayList<>(), travail -> {
            Intent intent = new Intent(this, TravailInfoActivity.class);
            intent.putExtra("TRAVAIL_ID", travail.getId());
            intent.putExtra("COURS_ID", coursId);
            intent.putExtra("TRAVAIL_TITRE", travail.getTitre());
            startActivity(intent);
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(travailAdapter);

        travailViewModel = new ViewModelProvider(this).get(TravailViewModel.class);

        travailViewModel.getListeTravail().observe(this, travaux -> {
            progressBar.setVisibility(View.GONE);
            travailAdapter.mettreAJour(travaux);
        });

        travailViewModel.getErreur().observe(this, message -> {
            progressBar.setVisibility(View.GONE);
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        travailViewModel.chargerTravailByCourseId(coursId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
