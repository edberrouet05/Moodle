package com.edwidge_jason.minimoodle.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.edwidge_jason.minimoodle.R;
import com.edwidge_jason.minimoodle.modeles.Quiz;

import java.util.List;
import java.util.Set;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    public interface OnQuizClickListener {
        void onClick(Quiz quiz);
    }

    private List<Quiz> listeQuiz;
    private Set<String> quizTermines;
    private final OnQuizClickListener listener;

    public QuizAdapter(List<Quiz> listeQuiz, Set<String> quizTermines, OnQuizClickListener listener) {
        this.listeQuiz = listeQuiz;
        this.quizTermines = quizTermines;
        this.listener = listener;
    }

    public void mettreAJour(List<Quiz> nouvelleListe, Set<String> termines) {
        this.listeQuiz = nouvelleListe;
        this.quizTermines = termines;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(vue);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = listeQuiz.get(position);
        holder.txtTitre.setText(quiz.getTitre());
        int nbQuestions = (quiz.getQuestions() != null) ? quiz.getQuestions().size() : 0;
        holder.txtNbQuestions.setText(nbQuestions + " question(s)");

        boolean termine = quizTermines != null && quizTermines.contains(quiz.getId());
        if (termine) {
            holder.txtStatutQuiz.setText("Terminé");
            holder.txtStatutQuiz.setBackground(
                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_chip_vert));
        } else {
            holder.txtStatutQuiz.setText("Non commencé");
            holder.txtStatutQuiz.setBackground(
                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_chip_gris));
        }

        holder.itemView.setOnClickListener(v -> listener.onClick(quiz));
    }

    @Override
    public int getItemCount() {
        return listeQuiz.size();
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitre;
        TextView txtNbQuestions;
        TextView txtStatutQuiz;

        QuizViewHolder(View itemView) {
            super(itemView);
            txtTitre = itemView.findViewById(R.id.txt_titre_quiz);
            txtNbQuestions = itemView.findViewById(R.id.txt_nb_questions);
            txtStatutQuiz = itemView.findViewById(R.id.txt_statut_quiz);
        }
    }
}
