package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.FileUtils;
import java.util.List;

/**
 * Controller che gestisce la schermata principale con la lista degli esercizi disponibili.
 * Mostra i progressi dell'utente per ogni tipo di esercizio e permette di selezionare
 * l'esercizio da svolgere. Gestisce anche il logout e il cambio utente.
 */
public class ExerciseListController extends AbstractController {
    /** Label che mostra il nome del giocatore corrente */
    @FXML
    private Label playerNameLabel;

    /**
     * Aggiorna la label con il nome del giocatore.
     * Implementazione del metodo astratto della classe padre.
     */
    @Override
    protected void updatePlayerNameLabel() {
        if (playerNameLabel != null) {
            playerNameLabel.setText("Nome del giocatore: " + playerName);
        }
    }

    /** Pulsante per accedere all'esercizio di debug del codice */
    @FXML
    public Button DifficultySelectionCodeDebug;
    
    /** Lista degli esercizi disponibili */
    @FXML
    private ListView<String> exerciseListView;

    /** Pulsante per accedere all'esercizio di riempimento spazi */
    @FXML
    private Button DifficultySelectionFillSpaces;

    /** Barre di progresso per i vari tipi di esercizi */
    @FXML
    private ProgressBar completionProgressBar1; // Progresso Fill Spaces
    @FXML
    private ProgressBar completionProgressBar2; // Progresso Output Exercise
    @FXML
    private ProgressBar completionProgressBar3; // Progresso Code Debug
    @FXML
    private ProgressBar completionProgressBar4; // Progresso Quiz
    
    /** Label che mostra la percentuale totale di completamento */
    @FXML
    private Label progressBarLabel;

    /** Nome del giocatore corrente */
    private String playerName;

    /**
     * Inizializza il controller con il nome del giocatore.
     * @param username Il nome utente del giocatore
     */
    @Override
    public void initData(String username) {
        this.playerName = username;
        super.initData(username);
        System.out.println("ExerciseListController inizializzato con username: " + username);
        updateProgressBars();
    }

    /**
     * Aggiorna le barre di progresso in base ai dati salvati nel file results.csv
     */
    /**
     * Aggiorna le barre di progresso per ogni tipo di esercizio.
     * Legge i risultati salvati e calcola le percentuali di completamento
     * per ogni categoria di esercizio.
     */
    private void updateProgressBars() {
        try {
            System.out.println("Aggiornamento barre di progresso per l'utente: " + playerName);
            
            // Leggi i risultati dal file
            List<String> results = FileUtils.readGameResultsWithDifficulty();
            System.out.println("Letti " + results.size() + " risultati dal file");
            
            // Calcola il completamento per ogni esercizio
            double fillSpacesCompletion = calculateExerciseCompletion(results, "FillView");
            double outputExerciseCompletion = calculateExerciseCompletion(results, "OutputExercise");
            double codeDebugCompletion = calculateExerciseCompletion(results, "CodeDebug");
            double quizCompletion = calculateExerciseCompletion(results, "QuizView");  // Modificato da "Quiz" a "QuizView"
            
            System.out.println("Completamento calcolato: FillView=" + fillSpacesCompletion + 
                              ", OutputExercise=" + outputExerciseCompletion + 
                              ", CodeDebug=" + codeDebugCompletion + 
                              ", Quiz=" + quizCompletion);
            
            // Aggiorna le barre di progresso
            if (completionProgressBar1 != null) completionProgressBar1.setProgress(fillSpacesCompletion);
            if (completionProgressBar2 != null) completionProgressBar2.setProgress(outputExerciseCompletion);
            if (completionProgressBar3 != null) completionProgressBar3.setProgress(codeDebugCompletion);
            if (completionProgressBar4 != null) completionProgressBar4.setProgress(quizCompletion);
            
            // Aggiorna l'etichetta con la percentuale totale di completamento
            double totalCompletion = (fillSpacesCompletion + outputExerciseCompletion + 
                                     codeDebugCompletion + quizCompletion) / 4.0;
            if (progressBarLabel != null) {
                progressBarLabel.setText(String.format("%.0f%%", totalCompletion * 100));
            }
            
        } catch (Exception e) {
            System.err.println("Errore durante l'aggiornamento delle barre di progresso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Calcola la percentuale di completamento per un esercizio specifico.
     * @param results Lista di risultati dal file CSV
     * @param exerciseName Nome dell'esercizio
     * @return Percentuale di completamento (0.0 - 1.0)
     */
    /**
     * Calcola la percentuale di completamento per un tipo specifico di esercizio.
     * Considera completato un livello quando la percentuale supera una soglia minima.
     * @param results Lista dei risultati dal file CSV
     * @param exerciseName Nome dell'esercizio da valutare
     * @return Percentuale di completamento (0.0 - 1.0)
     */
    private double calculateExerciseCompletion(List<String> results, String exerciseName) {
        boolean easyCompleted = false;
        boolean mediumCompleted = false;
        boolean hardCompleted = false;
        
        // Soglia minima per considerare un livello completato (ad es. 50%)
        double completionThreshold = 50.0;
        
        for (String result : results) {
            String[] parts = result.split(",");
            if (parts.length >= 4 && parts[0].equals(playerName) && parts[1].equals(exerciseName)) {
                String difficulty = parts[2];
                double percentage = Double.parseDouble(parts[3]);
                
                System.out.println("Trovato risultato per " + playerName + ", " + exerciseName + 
                                  ", " + difficulty + ": " + percentage + "%");
                
                if (percentage >= completionThreshold) {
                    if (difficulty.equals("Easy")) {
                        easyCompleted = true;
                    } else if (difficulty.equals("Medium")) {
                        mediumCompleted = true;
                    } else if (difficulty.equals("Hard")) {
                        hardCompleted = true;
                    }
                }
            }
        }
        
        // Calcola la percentuale di completamento (ogni livello vale 1/3)
        int completedLevels = 0;
        if (easyCompleted) completedLevels++;
        if (mediumCompleted) completedLevels++;
        if (hardCompleted) completedLevels++;
        
        System.out.println("Livelli completati per " + exerciseName + ": " + completedLevels + "/3");
        
        return completedLevels / 3.0;
    }

    /**
     * Initializes the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        System.out.println("Inizializzazione ExerciseListController");
        
        // Verifica che le ProgressBar siano state inizializzate correttamente
        if (completionProgressBar1 == null) System.err.println("completionProgressBar1 è null");
        if (completionProgressBar2 == null) System.err.println("completionProgressBar2 è null");
        if (completionProgressBar3 == null) System.err.println("completionProgressBar3 è null");
        if (completionProgressBar4 == null) System.err.println("completionProgressBar4 è null");
        
        // Aggiorna le barre di progresso se il nome utente è già impostato
        if (playerName != null) {
            System.out.println("Nome utente già impostato: " + playerName);
            updateProgressBars();
        } else {
            System.out.println("Nome utente non ancora impostato");
        }
    }

    @FXML
    private void DifficultySelectionCodeDebug() {
        System.out.println("CodeDebug button clicked!");
        switchScene("/view/DifficultySelectionCodeDebug.fxml");
    }

    @FXML
    private void DifficultySelectionGetAnswer() {
        System.out.println("GetAnswer button clicked!");
        switchScene("/view/DifficultySelectionGetAnswer.fxml");
    }

    @FXML
    private void DifficultySelectionFillSpaces() {
        System.out.println("FillSpaces button clicked!");
        switchScene("/view/DifficultySelectionFillSpaces.fxml");
    }

    @FXML
    private void DifficultySelectionQuiz() {
        System.out.println("Quiz button clicked!");
        switchScene("/view/DifficultySelectionQuiz.fxml");
    }

    /**
     * Switches to a different scene.
     *
     * @param fxmlPath The path to the FXML file to load.
     */
    private void switchScene(String fxmlPath) {
        try {
            // Usa il metodo della classe base per caricare la vista
            Stage currentStage = getCurrentStage(playerNameLabel != null ? playerNameLabel : DifficultySelectionFillSpaces);
            
            // Carica la vista e ottieni il controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            // Passa il nome utente al controller se è del tipo corretto
            Object controller = loader.getController();
            if (controller instanceof AbstractController) {
                ((AbstractController) controller).initData(playerName);
            }
            
            // Imposta la nuova scena
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", 
                    "Si è verificato un errore durante il caricamento della vista: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleLogout() {
        if (showConfirmation("Logout", "Sei sicuro di voler effettuare il logout?")) {
            switchScene("/view/Login.fxml");
        }
    }
    
    @FXML
    public void ChangeUser(ActionEvent actionEvent) {
        if (showConfirmation("Cambio Utente", "Sei sicuro di voler cambiare utente?")) {
            switchScene("/view/Login.fxml");
        }
    }
}