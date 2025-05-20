package controller;

import javafx.fxml.FXML;
import javafx.stage.*;
import javafx.scene.control.*;
import utils.FileUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller per la gestione degli esercizi di debug del codice.
 * Gestisce la logica per la presentazione degli esercizi di debugging,
 * la validazione delle correzioni e il tracciamento del progresso dell'utente.
 * 
 * Caratteristiche principali:
 * - Gestione degli esercizi di debug
 * - Validazione delle correzioni dell'utente
 * - Tracciamento del progresso
 * - Navigazione tra gli esercizi
 * - Salvataggio dei risultati
 */
public class CodeDebugController extends AbstractController {

    @FXML
    public ProgressBar completionProgressBar;
    @FXML
    public Button HomeButton;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextArea userAnswersTextArea;
    @FXML
    private Label completionPercentageLabel;
    @FXML
    private Label playerNameLabel;
    @FXML
    private TextArea correctAnswersTextArea;

    /** Livello di difficoltà selezionato */
    private String difficulty;
    /** Lista delle correzioni fornite dall'utente */
    private List<String> userAnswers = new ArrayList<>();
    /** Array delle correzioni corrette caricate dal file */
    private String[] correctAnswers;

    /**
     * Inizializza il controller con i dati dell'utente e il livello di difficoltà.
     * Carica le correzioni corrette dal file appropriato e configura l'interfaccia.
     * 
     * @param username Nome del giocatore
     * @param difficulty Livello di difficoltà selezionato
     */
    public void initData(String username, String difficulty) {
        System.out.println("initData chiamato con username: " + username + ", difficulty: " + difficulty);
        this.playerName = username;
        this.difficulty = difficulty;
        
        try {
            // Carica le risposte corrette dal file appropriato
            System.out.println("Tentativo di caricare le risposte corrette per Debug" + difficulty);
            this.correctAnswers = FileUtils.readCorrectAnswers("Debug", difficulty);
            
            if (correctAnswers == null || correctAnswers.length == 0) {
                throw new RuntimeException("Failed to load correct answers");
            }
            
            System.out.println("Caricate " + correctAnswers.length + " risposte corrette");
            playerNameLabel.setText("Nome del giocatore: " + playerName);
            initializeExercise();
            
        } catch (Exception e) {
            System.err.println("Errore durante l'inizializzazione: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare l'esercizio. Riprova più tardi.");
            goToHome(getCurrentStage(playerNameLabel));
        }
    }

    /**
     * Inizializza l'esercizio creando la lista delle correzioni utente
     * e configurando il listener per il cambio di scheda.
     */
    private void initializeExercise() {
        // Inizializza le risposte dell'utente con stringhe vuote
        userAnswers = new ArrayList<>();
        for (int i = 0; i < correctAnswers.length; i++) {
            userAnswers.add("");
        }

        // Aggiungi listener per i cambiamenti di tab
        tabPane.getSelectionModel().selectedIndexProperty().addListener(
            (observable, oldValue, newValue) -> saveCurrentAnswerAndRefresh());
    }

    /**
     * Metodo di inizializzazione FXML.
     * Non è più necessario determinare la difficoltà dal nome della vista
     * poiché viene passata direttamente tramite initData.
     */
    @FXML
    private void initialize() {
        // Non è più necessario determinare la difficoltà qui
        // Il metodo initData si occuperà di impostare la difficoltà e caricare le risposte
    }

    private TextArea getCurrentUserInput() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        return (TextArea) tabPane.getTabs().get(currentIndex).getContent().lookup("#userInput" + (currentIndex + 1));
    }

    /**
     * Salva la correzione corrente dell'utente e aggiorna la visualizzazione dei risultati.
     * Viene chiamato automaticamente quando l'utente cambia scheda.
     */
    private void saveCurrentAnswerAndRefresh() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        TextArea currentTextArea = getCurrentUserInput();
        if (currentTextArea != null) {
            String userAnswer = currentTextArea.getText().trim();
            userAnswers.set(currentIndex, userAnswer);
        }
        updateResults();
    }

    @FXML
    private void handleCheckButtonAction() {
        saveCurrentAnswerAndRefresh();
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        TextArea currentTextArea = getCurrentUserInput();
        
        
        if (currentTextArea != null) {
            String userAnswer = currentTextArea.getText().trim();
            if (userAnswer.equals(correctAnswers[currentIndex])) {
                showAlert(Alert.AlertType.INFORMATION, "Corretto", "La tua correzione è corretta!");
                if (currentIndex < tabPane.getTabs().size() - 1) {
                    showNextExerciseConfirmation();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Errato", "La tua correzione non è corretta. Riprova.");
            }
        }
    }

    @FXML
    private void handleResultsTabAction() {
        if (userAnswersTextArea != null) {
            updateResults();
        }
    }

    /**
     * Aggiorna la visualizzazione dei risultati degli esercizi di debug.
     * Calcola la percentuale di correzioni corrette e aggiorna la barra di progresso
     * e l'etichetta con la percentuale di completamento.
     */
    private void updateResults() {
        StringBuilder userAnswersBuilder = new StringBuilder();
        int correctCount = 0;

        for (int i = 0; i < userAnswers.size(); i++) {
            userAnswersBuilder.append("Esercizio ").append(i + 1).append(":\n");
            userAnswersBuilder.append("La tua correzione: ").append(userAnswers.get(i)).append("\n");
            userAnswersBuilder.append("Correzione corretta: ").append(correctAnswers[i]).append("\n\n");
            
            if (userAnswers.get(i).equals(correctAnswers[i])) {
                correctCount++;
            }
        }

        if (userAnswersTextArea != null) {
            userAnswersTextArea.setText(userAnswersBuilder.toString());
        }

        double completionPercentage = ((double) correctCount / correctAnswers.length) * 100;
        completionPercentageLabel.setText(String.format("Completamento: %.2f%%", completionPercentage));
        completionProgressBar.setProgress(correctCount / (double) correctAnswers.length);
    }

    @FXML
    private void handleNextButtonAction() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (currentIndex < tabPane.getTabs().size() - 1) {
            saveCurrentAnswerAndRefresh();
            tabPane.getSelectionModel().select(currentIndex + 1);
        }
    }

    @FXML
    private void handleHomeButtonAction() {
        try {
            // Parse completion percentage
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse(completionPercentageLabel.getText().replace("Completamento: ", "").replace("%", ""));
            double completionPercentage = number.doubleValue();

            // Save results with difficulty
            String exerciseName = "CodeDebug";
            try {
                FileUtils.writeGameResultsWithDifficulty(playerName, exerciseName, difficulty, completionPercentage);
            } catch (java.io.IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save game results: " + e.getMessage());
            }

            // Navigate to home using the parent class method
            goToHome(getCurrentStage(HomeButton));

        } catch (ParseException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackButton() {
        try {
            Stage currentStage = getCurrentStage(playerNameLabel);
            loadView("/view/DifficultySelectionCodeDebug.fxml", "Selezione Difficoltà", currentStage);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", 
                    "Impossibile tornare alla schermata precedente: " + e.getMessage());
        }
    }

    private void showNextExerciseConfirmation() {
        if (showConfirmation("Prossimo Esercizio", "Vuoi passare all'esercizio successivo?")) {
            handleNextButtonAction();
        }
    }
    
    @Override
    protected void updatePlayerNameLabel() {
        if (playerNameLabel != null) {
            playerNameLabel.setText("Nome del giocatore: " + playerName);
        }
    }
}
