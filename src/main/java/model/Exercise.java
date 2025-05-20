package model;

/**
 * Classe che rappresenta un esercizio nel sistema.
 * Questa classe mantiene le informazioni relative a un singolo esercizio,
 * incluso il suo titolo, livello di difficoltà e stato di avanzamento.
 */
public class Exercise {

    /** Il titolo dell'esercizio */
    private String title;
    
    /** Il livello di difficoltà dell'esercizio (es. "Easy", "Medium", "Hard") */
    private String difficulty;
    
    /** Lo stato di avanzamento dell'esercizio */
    private String progress;

    /**
     * Costruisce un nuovo oggetto Exercise con i parametri specificati.
     * 
     * @param title Il titolo dell'esercizio
     * @param difficulty Il livello di difficoltà dell'esercizio
     * @param progress Lo stato di avanzamento dell'esercizio
     */
    public Exercise(String title, String difficulty, String progress) {
        this.title = title;
        this.difficulty = difficulty;
        this.progress = progress;
    }

    /**
     * Restituisce il titolo dell'esercizio.
     * 
     * @return Il titolo dell'esercizio
     */
    public String getTitle() {
        return title;
    }

    /**
     * Imposta il titolo dell'esercizio.
     * 
     * @param title Il nuovo titolo dell'esercizio
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Restituisce il livello di difficoltà dell'esercizio.
     * 
     * @return Il livello di difficoltà dell'esercizio
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Imposta il livello di difficoltà dell'esercizio.
     * 
     * @param difficulty Il nuovo livello di difficoltà dell'esercizio
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Restituisce lo stato di avanzamento dell'esercizio.
     * 
     * @return Lo stato di avanzamento dell'esercizio
     */
    public String getProgress() {
        return progress;
    }

    /**
     * Imposta lo stato di avanzamento dell'esercizio.
     * 
     * @param progress Il nuovo stato di avanzamento dell'esercizio
     */
    public void setProgress(String progress) {
        this.progress = progress;
    }
}
