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
import com.etsmtl.minimoodle.adaptateurs.QuizAdapter;
import com.etsmtl.minimoodle.bd.MoodleDatabase;
import com.etsmtl.minimoodle.vuemodeles.QuizViewModel;

import java.util.ArrayList;
import java.util.HashSet;

public class QuizListActivity extends AppCompatActivity {

    private QuizViewModel quizViewModel;
    private QuizAdapter quizAdapter;
    private ProgressBar progressBar;
    private String userId;
    private String coursId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        coursId = getIntent().getStringExtra("COURS_ID");
        String coursTitre = getIntent().getStringExtra("COURS_TITRE");

        String[] session = MoodleDatabase.getInstance(this).getSession();
        userId = (session != null) ? session[0] : "";

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.titre_quiz_list));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.progress_bar);
        RecyclerView recyclerView = findViewById(R.id.rv_quiz);

        quizAdapter = new QuizAdapter(new ArrayList<>(), new HashSet<>(), quiz -> {
            if (MoodleDatabase.getInstance(this).aDejaPasseQuiz(quiz.getId(), userId)) {
                Toast.makeText(this, "Quiz déjà terminé.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("QUIZ_ID", quiz.getId());
            intent.putExtra("QUIZ_TITRE", quiz.getTitre());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(quizAdapter);

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        quizViewModel.getListeQuiz().observe(this, quizList -> {
            progressBar.setVisibility(View.GONE);
            quizAdapter.mettreAJour(quizList,
                    MoodleDatabase.getInstance(this).getQuizTerminesIds(userId));
        });

        quizViewModel.getErreur().observe(this, message -> {
            progressBar.setVisibility(View.GONE);
            if (message != null) Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });

        progressBar.setVisibility(View.VISIBLE);
        if (coursId != null) {
            quizViewModel.chargerQuizByCourseId(coursId);
        } else {
            quizViewModel.chargerTousQuiz();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (coursId != null) {
            quizViewModel.chargerQuizByCourseId(coursId);
        } else {
            quizViewModel.chargerTousQuiz();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
