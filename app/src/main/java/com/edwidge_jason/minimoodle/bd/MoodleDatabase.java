package com.edwidge_jason.minimoodle.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.edwidge_jason.minimoodle.modeles.ResultatQuiz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoodleDatabase extends SQLiteOpenHelper {

    private static final String NOM_BD = "minimoodle.db";
    private static final int VERSION_BD = 3;

    // Table résultats quiz
    private static final String TABLE_RESULTATS = "resultats_quiz";
    private static final String COL_ID = "id";
    private static final String COL_QUIZ_ID = "quiz_id";
    private static final String COL_TITRE_QUIZ = "titre_quiz";
    private static final String COL_SCORE = "score";
    private static final String COL_TOTAL = "total";
    private static final String COL_DATE = "date_passage";
    private static final String COL_USER_ID = "user_id";

    // Table soumissions (cache local)
    private static final String TABLE_SOUMISSIONS = "soumissions";
    private static final String COL_TRAVAIL_ID = "travail_id";

    // Table session utilisateur
    private static final String TABLE_SESSION = "session";
    private static final String COL_UTILISATEUR_ID = "utilisateur_id";
    private static final String COL_NOM = "nom";
    private static final String COL_PRENOM = "prenom";
    private static final String COL_COURRIEL = "courriel";
    private static final String COL_ENROLLED_IDS = "enrolled_course_ids";
    private static final String COL_TELEPHONE = "telephone";
    private static final String COL_PHOTO = "photo_profil";

    private static MoodleDatabase instance;

    public static synchronized MoodleDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new MoodleDatabase(context.getApplicationContext());
        }
        return instance;
    }

    private MoodleDatabase(Context context) {
        super(context, NOM_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RESULTATS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_QUIZ_ID + " TEXT NOT NULL, " +
                COL_TITRE_QUIZ + " TEXT, " +
                COL_SCORE + " INTEGER, " +
                COL_TOTAL + " INTEGER, " +
                COL_DATE + " TEXT, " +
                COL_USER_ID + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_SOUMISSIONS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TRAVAIL_ID + " TEXT UNIQUE NOT NULL, " +
                COL_USER_ID + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_SESSION + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                COL_UTILISATEUR_ID + " TEXT, " +
                COL_NOM + " TEXT, " +
                COL_PRENOM + " TEXT, " +
                COL_COURRIEL + " TEXT, " +
                COL_ENROLLED_IDS + " TEXT, " +
                COL_TELEPHONE + " TEXT, " +
                COL_PHOTO + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int ancienne, int nouvelle) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULTATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOUMISSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
        onCreate(db);
    }

    // ─── Session ─────────────────────────────────────────────────────────────────

    public void sauvegarderSession(String userId, String nom, String prenom, String courriel, String idsInscrits, String telephone, String photo) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SESSION, null, null);
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, 1);
        cv.put(COL_UTILISATEUR_ID, userId);
        cv.put(COL_NOM, nom);
        cv.put(COL_PRENOM, prenom);
        cv.put(COL_COURRIEL, courriel);
        cv.put(COL_ENROLLED_IDS, idsInscrits);
        cv.put(COL_TELEPHONE, telephone);
        cv.put(COL_PHOTO, photo);
        db.insert(TABLE_SESSION, null, cv);
    }

    public String[] getSession() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SESSION, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            String[] session = new String[]{
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_UTILISATEUR_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_NOM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PRENOM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_COURRIEL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_ENROLLED_IDS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TELEPHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PHOTO))
            };
            cursor.close();
            return session;
        }
        cursor.close();
        return null;
    }

    public void supprimerSession() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SESSION, null, null);
    }

    // ─── Résultats quiz ──────────────────────────────────────────────────────────

    public void sauvegarderResultat(ResultatQuiz resultat, String userId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_QUIZ_ID, resultat.getQuizId());
        cv.put(COL_TITRE_QUIZ, resultat.getTitreQuiz());
        cv.put(COL_SCORE, resultat.getScore());
        cv.put(COL_TOTAL, resultat.getTotal());
        cv.put(COL_DATE, resultat.getDatePassage());
        cv.put(COL_USER_ID, userId);
        db.insert(TABLE_RESULTATS, null, cv);
    }

    public List<ResultatQuiz> getResultatsParUser(String userId) {
        SQLiteDatabase db = getReadableDatabase();
        List<ResultatQuiz> resultats = new ArrayList<>();
        Cursor cursor = db.query(TABLE_RESULTATS, null,
                COL_USER_ID + "=?", new String[]{userId},
                null, null, COL_DATE + " DESC");

        while (cursor.moveToNext()) {
            ResultatQuiz r = new ResultatQuiz(
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_QUIZ_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_TITRE_QUIZ)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCORE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_TOTAL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
            );
            resultats.add(r);
        }
        cursor.close();
        return resultats;
    }

    public boolean aDejaPasseQuiz(String quizId, String userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESULTATS, new String[]{COL_ID},
                COL_QUIZ_ID + "=? AND " + COL_USER_ID + "=?",
                new String[]{quizId, userId},
                null, null, null);
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    // ─── Soumissions locales ─────────────────────────────────────────────────────

    public void marquerSoumisLocalement(String travailId, String userId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TRAVAIL_ID, travailId);
        cv.put(COL_USER_ID, userId);
        db.insertWithOnConflict(TABLE_SOUMISSIONS, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public boolean estSoumisLocalement(String travailId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SOUMISSIONS, new String[]{COL_ID},
                COL_TRAVAIL_ID + "=?", new String[]{travailId},
                null, null, null);
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }

    public Set<String> getQuizTerminesIds(String userId) {
        SQLiteDatabase db = getReadableDatabase();
        Set<String> ids = new HashSet<>();
        Cursor cursor = db.query(TABLE_RESULTATS, new String[]{COL_QUIZ_ID},
                COL_USER_ID + "=?", new String[]{userId},
                COL_QUIZ_ID, null, null);
        while (cursor.moveToNext()) {
            ids.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_QUIZ_ID)));
        }
        cursor.close();
        return ids;
    }

}