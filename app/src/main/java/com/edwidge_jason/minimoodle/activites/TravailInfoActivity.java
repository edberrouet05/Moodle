package com.edwidge_jason.minimoodle.activites;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.edwidge_jason.minimoodle.R;
import com.edwidge_jason.minimoodle.bd.MoodleDatabase;
import com.edwidge_jason.minimoodle.modeles.Travail;
import com.edwidge_jason.minimoodle.vuemodeles.TravailViewModel;

public class TravailInfoActivity extends AppCompatActivity {

    private TravailViewModel travailViewModel;
    private String travailId;
    private String coursId;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travail_info);

        travailId = getIntent().getStringExtra("TRAVAIL_ID");
        coursId = getIntent().getStringExtra("COURS_ID");
        String titre = getIntent().getStringExtra("TRAVAIL_TITRE");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titre != null ? titre : "Détail du travail");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String[] session = MoodleDatabase.getInstance(this).getSession();
        userId = (session != null) ? session[0] : "";

        TextView txtTitre = findViewById(R.id.txt_titre_travail);
        TextView txtDateRemise = findViewById(R.id.txt_date_remise);
        TextView txtStatut = findViewById(R.id.txt_statut);
        TextView txtNote = findViewById(R.id.txt_note);
        TextView txtDescription = findViewById(R.id.txt_description);
        TextView txtInstructions = findViewById(R.id.txt_instructions);
        TextView txtCommentaire = findViewById(R.id.txt_commentaire);
        CardView cardInstructions = findViewById(R.id.card_instructions);
        CardView cardCommentaire = findViewById(R.id.card_commentaire);
        Button btnSoumettre = findViewById(R.id.btn_soumettre);

        travailViewModel = new ViewModelProvider(this).get(TravailViewModel.class);

        travailViewModel.getListeTravail().observe(this, travaux -> {
            Travail travail = null;
            for (Travail t : travaux) {
                if (t.getId().equals(travailId)) {
                    travail = t;
                    break;
                }
            }
            if (travail == null) return;

            txtTitre.setText(travail.getTitre());
            txtDateRemise.setText("Remise : " + travail.getDateRemise());

            boolean soumisLocalement = MoodleDatabase.getInstance(this).estSoumisLocalement(travailId);

            Travail.Statut statut = soumisLocalement ? Travail.Statut.REMIS : travail.getStatut();
            switch (statut) {
                case CORRIGE:
                    txtStatut.setText(getString(R.string.statut_corrige));
                    txtStatut.setTextColor(ContextCompat.getColor(this, R.color.violet_corrige));
                    if (travail.getNote() != null) {
                        txtNote.setText("Note : " + travail.getNote() + "/" + travail.getNoteMax());
                        txtNote.setVisibility(View.VISIBLE);
                    }
                    break;
                case REMIS:
                    txtStatut.setText(getString(R.string.statut_remis));
                    txtStatut.setTextColor(ContextCompat.getColor(this, R.color.vert_succes));
                    break;
                case EN_RETARD:
                    txtStatut.setText(getString(R.string.statut_en_retard));
                    txtStatut.setTextColor(ContextCompat.getColor(this, R.color.orange_retard));
                    break;
                default:
                    txtStatut.setText(getString(R.string.statut_non_remis));
                    txtStatut.setTextColor(ContextCompat.getColor(this, R.color.rouge_erreur));
                    break;
            }

            if (travail.getDescription() != null && !travail.getDescription().isEmpty()) {
                txtDescription.setText(travail.getDescription());
            } else {
                txtDescription.setText("Aucune description disponible.");
            }

            if (travail.getInstructions() != null && !travail.getInstructions().isEmpty()) {
                txtInstructions.setText(travail.getInstructions());
                cardInstructions.setVisibility(View.VISIBLE);
            } else {
                cardInstructions.setVisibility(View.GONE);
            }

            if (travail.getCommentaire() != null && !travail.getCommentaire().isEmpty()) {
                txtCommentaire.setText(travail.getCommentaire());
                cardCommentaire.setVisibility(View.VISIBLE);
            } else {
                cardCommentaire.setVisibility(View.GONE);
            }

            boolean peutSoumettre = !travail.isRemis() && !soumisLocalement
                    && statut != Travail.Statut.CORRIGE;
            btnSoumettre.setVisibility(peutSoumettre ? View.VISIBLE : View.GONE);
        });

        travailViewModel.getTravailSoumis().observe(this, soumis -> {
            if (soumis != null) {
                MoodleDatabase.getInstance(this).marquerSoumisLocalement(soumis.getId(), userId);
                Toast.makeText(this, "Travail remis avec succès !", Toast.LENGTH_SHORT).show();
                travailViewModel.reinitialiserTravailSoumis();
                travailViewModel.chargerTravailByCourseId(coursId);
            }
        });

        travailViewModel.getErreur().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        btnSoumettre.setOnClickListener(v -> travailViewModel.soumettreTravail(travailId));

        travailViewModel.chargerTravailByCourseId(coursId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
