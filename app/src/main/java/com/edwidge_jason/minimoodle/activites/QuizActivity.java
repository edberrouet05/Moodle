package com.edwidge_jason.minimoodle.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.edwidge_jason.minimoodle.R;
import com.edwidge_jason.minimoodle.bd.MoodleDatabase;
import com.edwidge_jason.minimoodle.modeles.Question;
import com.edwidge_jason.minimoodle.modeles.Quiz;
import com.edwidge_jason.minimoodle.modeles.ResultatQuiz;
import com.edwidge_jason.minimoodle.vuemodeles.QuizViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private QuizViewModel quizViewModel;
    private List<Question> questions;
    private int indexQuestion = 0;
    private int score = 0;
    private String quizId;
    private String quizTitre;
    private String userId;

    private TextView txtEnonce;
    private TextView txtProgression;
    private RadioGroup radioGroup;
    private RadioButton[] radioButtons;
    private Button btnSuivant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizId = getIntent().getStringExtra("QUIZ_ID");
        quizTitre = getIntent().getStringExtra("QUIZ_TITRE");

        String[] session = MoodleDatabase.getInstance(this).getSession();
        userId = (session != null) ? session[0] : "";

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(quizTitre);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtEnonce = findViewById(R.id.txt_enonce);
        txtProgression = findViewById(R.id.txt_progression);
        radioGroup = findViewById(R.id.radio_group_choix);
        btnSuivant = findViewById(R.id.btn_suivant);

        radioButtons = new RadioButton[]{
                findViewById(R.id.radio_choix_0),
                findViewById(R.id.radio_choix_1),
                findViewById(R.id.radio_choix_2),
                findViewById(R.id.radio_choix_3)
        };

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        quizViewModel.getQuizActif().observe(this, quiz -> {
            if (quiz != null) {
                questions = quiz.getQuestions();
                afficherQuestion();
            }
        });

        quizViewModel.getErreur().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        quizViewModel.chargerQuizParId(quizId);

        btnSuivant.setOnClickListener(v -> validerEtSuivre());
    }

    private void afficherQuestion() {
        if (questions == null || indexQuestion >= questions.size()) return;

        Question question = questions.get(indexQuestion);
        txtEnonce.setText(question.getEnonce());
        txtProgression.setText((indexQuestion + 1) + "/" + questions.size());

        List<String> choix = question.getChoix();
        for (int i = 0; i < radioButtons.length; i++) {
            if (i < choix.size()) {
                radioButtons[i].setText(choix.get(i));
                radioButtons[i].setVisibility(android.view.View.VISIBLE);
            } else {
                radioButtons[i].setVisibility(android.view.View.GONE);
            }
        }
        radioGroup.clearCheck();

        boolean derniere = indexQuestion == questions.size() - 1;
        btnSuivant.setText(derniere ? getString(R.string.btn_terminer) : getString(R.string.btn_suivant));
    }

    private void validerEtSuivre() {
        if (questions == null) return;

        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Veuillez sélectionner une réponse.", Toast.LENGTH_SHORT).show();
            return;
        }

        Question question = questions.get(indexQuestion);
        int reponseSelectionnee = -1;
        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].getId() == selectedId) {
                reponseSelectionnee = i;
                break;
            }
        }

        if (reponseSelectionnee == question.getBonneReponse()) {
            score++;
        }

        indexQuestion++;

        if (indexQuestion >= questions.size()) {
            terminerQuiz();
        } else {
            afficherQuestion();
        }
    }

    private void terminerQuiz() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA_FRENCH).format(new Date());
        ResultatQuiz resultat = new ResultatQuiz(quizId, quizTitre, score, questions.size(), date);

        MoodleDatabase.getInstance(this).sauvegarderResultat(resultat, userId);

        Intent intent = new Intent(this, QuizResultatActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("TOTAL", questions.size());
        intent.putExtra("TITRE_QUIZ", quizTitre);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
