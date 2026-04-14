package com.etsmtl.minimoodle.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etsmtl.minimoodle.R;
import com.etsmtl.minimoodle.modeles.ResultatQuiz;

import java.util.List;

public class ResultatQuizAdapter extends RecyclerView.Adapter<ResultatQuizAdapter.ResultatViewHolder> {

    private final List<ResultatQuiz> listeResultats;

    public ResultatQuizAdapter(List<ResultatQuiz> listeResultats) {
        this.listeResultats = listeResultats;
    }

    @NonNull
    @Override
    public ResultatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resultat_quiz, parent, false);
        return new ResultatViewHolder(vue);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultatViewHolder holder, int position) {
        ResultatQuiz resultat = listeResultats.get(position);
        holder.txtTitreQuiz.setText(resultat.getTitreQuiz());
        holder.txtScore.setText(resultat.getScore() + "/" + resultat.getTotal()
                + "  (" + resultat.getPourcentage() + "%)");
        holder.txtDate.setText(resultat.getDatePassage());
    }

    @Override
    public int getItemCount() {
        return listeResultats.size();
    }

    static class ResultatViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitreQuiz;
        TextView txtScore;
        TextView txtDate;

        ResultatViewHolder(View itemView) {
            super(itemView);
            txtTitreQuiz = itemView.findViewById(R.id.txt_titre_quiz_item);
            txtScore = itemView.findViewById(R.id.txt_score_item);
            txtDate = itemView.findViewById(R.id.txt_date_item);
        }
    }
}
