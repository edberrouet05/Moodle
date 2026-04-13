package com.etsmtl.minimoodle.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etsmtl.minimoodle.R;
import com.etsmtl.minimoodle.modeles.Cours;

import java.util.List;

public class CoursAdapter extends RecyclerView.Adapter<CoursAdapter.CoursViewHolder> {

    public interface OnCoursClickListener {
        void onClick(Cours cours);
    }

    private List<Cours> listeCours;
    private final OnCoursClickListener listener;

    public CoursAdapter(List<Cours> listeCours, OnCoursClickListener listener) {
        this.listeCours = listeCours;
        this.listener = listener;
    }

    public void mettreAJour(List<Cours> nouvelleListe) {
        this.listeCours = nouvelleListe;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cours, parent, false);
        return new CoursViewHolder(vue);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursViewHolder holder, int position) {
        Cours cours = listeCours.get(position);
        holder.txtSigle.setText(cours.getSigle());
        holder.txtTitre.setText(cours.getTitre());
        holder.txtCredits.setText(cours.getCredits() + " cr.");
        holder.itemView.setOnClickListener(v -> listener.onClick(cours));
    }

    @Override
    public int getItemCount() {
        return listeCours.size();
    }

    static class CoursViewHolder extends RecyclerView.ViewHolder {
        TextView txtSigle;
        TextView txtTitre;
        TextView txtCredits;

        CoursViewHolder(View itemView) {
            super(itemView);
            txtSigle = itemView.findViewById(R.id.txt_sigle);
            txtTitre = itemView.findViewById(R.id.txt_titre);
            txtCredits = itemView.findViewById(R.id.txt_credits);
        }
    }
}
