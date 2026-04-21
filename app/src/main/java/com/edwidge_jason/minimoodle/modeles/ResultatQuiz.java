package com.edwidge_jason.minimoodle.modeles;

public class ResultatQuiz {

    private String quizId;
    private String titreQuiz;
    private int score;
    private int total;
    private String datePassage;

    public ResultatQuiz() {}

    public ResultatQuiz(String quizId, String titreQuiz, int score, int total, String datePassage) {
        this.quizId = quizId;
        this.titreQuiz = titreQuiz;
        this.score = score;
        this.total = total;
        this.datePassage = datePassage;
    }

    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }

    public String getTitreQuiz() { return titreQuiz; }
    public void setTitreQuiz(String titreQuiz) { this.titreQuiz = titreQuiz; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public String getDatePassage() { return datePassage; }
    public void setDatePassage(String datePassage) { this.datePassage = datePassage; }

    public int getPourcentage() {
        if (total == 0) return 0;
        return (int) Math.round((score * 100.0) / total);
    }
}
