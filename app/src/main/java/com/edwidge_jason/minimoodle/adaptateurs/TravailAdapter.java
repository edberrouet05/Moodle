package com.edwidge_jason.minimoodle.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.edwidge_jason.minimoodle.R;
import com.edwidge_jason.minimoodle.modeles.Travail;

import java.util.List;

public class TravailAdapter extends RecyclerView.Adapter<TravailAdapter.TravailViewHolder> {

    public interface OnTravailClickListener {
        void onClick(Travail travail);
    }

    private List<Travail> listeTravail;
    private final OnTravailClickListener listener;

    public TravailAdapter(List<Travail> listeTravail, OnTravailClickListener listener) {
        this.listeTravail = listeTravail;
        this.listener = listener;
    }

    public void mettreAJour(List<Travail> nouvelleListe) {
        this.listeTravail = nouvelleListe;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TravailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_travail, parent, false);
        return new TravailViewHolder(vue);
    }

    @Override
    public void onBindViewHolder(@NonNull TravailViewHolder holder, int position) {
        Travail travail = listeTravail.get(position);
        holder.txtTitre.setText(travail.getTitre());

        if (holder.txtCoursId != null && travail.getCoursId() != null) {
            holder.txtCoursId.setText(travail.getCoursId());
        }

        holder.txtDateRemise.setText("Remise : " + travail.getDateRemise());

        Travail.Statut statut = travail.getStatut();

        switch (statut) {
            case EN_RETARD:
                holder.txtUrgent.setVisibility(View.VISIBLE);
                holder.txtStatut.setText("En retard");
                holder.txtStatut.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_chip_rouge));
                holder.txtNote.setVisibility(View.GONE);
                break;
            case A_FAIRE:
                holder.txtUrgent.setVisibility(View.GONE);
                holder.txtStatut.setText("À faire");
                holder.txtStatut.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_chip_orange));
                holder.txtNote.setVisibility(View.GONE);
                break;
            case REMIS:
                holder.txtUrgent.setVisibility(View.GONE);
                holder.txtStatut.setText("Remis");
                holder.txtStatut.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_chip_vert));
                holder.txtNote.setVisibility(View.GONE);
                break;
            case CORRIGE:
                holder.txtUrgent.setVisibility(View.GONE);
                holder.txtStatut.setText("Corrigé");
                holder.txtStatut.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_chip_violet));
                if (travail.getNote() != null) {
                    holder.txtNote.setText(travail.getNote() + "/" + travail.getNoteMax());
                    holder.txtNote.setVisibility(View.VISIBLE);
                } else {
                    holder.txtNote.setVisibility(View.GONE);
                }
                break;
        }

        holder.itemView.setOnClickListener(v -> listener.onClick(travail));
    }

    @Override
    public int getItemCount() {
        return listeTravail.size();
    }

    static class TravailViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitre;
        TextView txtCoursId;
        TextView txtDateRemise;
        TextView txtUrgent;
        TextView txtStatut;
        TextView txtNote;

        TravailViewHolder(View itemView) {
            super(itemView);
            txtTitre = itemView.findViewById(R.id.txt_titre_travail);
            txtCoursId = itemView.findViewById(R.id.txt_cours_id_travail);
            txtDateRemise = itemView.findViewById(R.id.txt_date_remise);
            txtUrgent = itemView.findViewById(R.id.txt_urgent);
            txtStatut = itemView.findViewById(R.id.txt_statut);
            txtNote = itemView.findViewById(R.id.txt_note);
        }
    }
}
