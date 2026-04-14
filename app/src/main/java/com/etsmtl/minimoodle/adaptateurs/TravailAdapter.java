package com.etsmtl.minimoodle.adaptateurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.etsmtl.minimoodle.R;
import com.etsmtl.minimoodle.modeles.Travail;

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
        holder.txtDateRemise.setText("Remise : " + travail.getDateRemise());

        Travail.Statut statut = travail.getStatut();
        switch (statut) {
            case CORRIGE:
                holder.txtStatut.setText(holder.itemView.getContext().getString(R.string.statut_corrige));
                holder.txtStatut.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.violet_corrige));
                if (travail.getNote() != null) {
                    holder.txtNote.setText(travail.getNote() + "/" + travail.getNoteMax());
                    holder.txtNote.setVisibility(View.VISIBLE);
                } else {
                    holder.txtNote.setVisibility(View.GONE);
                }
                break;
            case REMIS:
                holder.txtStatut.setText(holder.itemView.getContext().getString(R.string.statut_remis));
                holder.txtStatut.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.vert_succes));
                holder.txtNote.setVisibility(View.GONE);
                break;
            case EN_RETARD:
                holder.txtStatut.setText(holder.itemView.getContext().getString(R.string.statut_en_retard));
                holder.txtStatut.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.orange_retard));
                holder.txtNote.setVisibility(View.GONE);
                break;
            default:
                holder.txtStatut.setText(holder.itemView.getContext().getString(R.string.statut_non_remis));
                holder.txtStatut.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.rouge_erreur));
                holder.txtNote.setVisibility(View.GONE);
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
        TextView txtDateRemise;
        TextView txtStatut;
        TextView txtNote;

        TravailViewHolder(View itemView) {
            super(itemView);
            txtTitre = itemView.findViewById(R.id.txt_titre_travail);
            txtDateRemise = itemView.findViewById(R.id.txt_date_remise);
            txtStatut = itemView.findViewById(R.id.txt_statut);
            txtNote = itemView.findViewById(R.id.txt_note);
        }
    }
}
