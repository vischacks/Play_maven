package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import utils.FileUtils;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller per la gestione degli esercizi di riempimento (fill-in-the-blank).
 * Gestisce la logica per la presentazione degli esercizi, la validazione delle risposte
 * e il tracciamento del progresso dell'utente.
 * 
 * Caratteristiche principali:
 * - Gestione degli esercizi di riempimento
 * - Validazione delle risposte dell'utente
 * - Tracciamento del progresso
 * - Navigazione tra gli esercizi
 * - Salvataggio dei risultati
 */
public class FillController{

    /** Barra di progresso che mostra il completamento complessivo degli esercizi */
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

    /** Lista delle risposte fornite dall'utente */
    private List<String> userAnswers = new ArrayList<>();
    /** Array delle risposte corrette caricate dal file */
    private String[] correctAnswers;  // Rimuovi l'inizializzazione diretta
    /** Nome del giocatore corrente */
    private String playerName;
    /** Livello di difficoltà selezionato */
    private String difficulty;

    /**
     * Inizializza il controller con i dati dell'utente e il livello di difficoltà.
     * Carica le risposte corrette e imposta l'esercizio.
     * @param username Il nome utente del giocatore
     * @param difficulty Il livello di difficoltà selezionato
     */
    /**
     * Stampa il contenuto dell'array delle risposte corrette per debug.
     */
    /**
     * Stampa il contenuto dell'array delle risposte corrette per debug.
     * Utile per la verifica del corretto caricamento delle risposte.
     */
    private void printCorrectAnswers() {
        System.out.println("=== Debug: Contenuto di correctAnswers ===");
        if (correctAnswers == null) {
            System.out.println("correctAnswers è null!");
            return;
        }
        for (int i = 0; i < correctAnswers.length; i++) {
            System.out.println("Risposta " + (i + 1) + ": " + correctAnswers[i]);
        }
        System.out.println("=======================================");
    }

    /**
     * Inizializza il controller con i dati dell'utente e il livello di difficoltà.
     * Carica le risposte corrette dal file appropriato e configura l'interfaccia.
     * 
     * @param username Nome del giocatore
     * @param difficulty Livello di difficoltà selezionato
     */
    public void initData(String username, String difficulty) {
        System.out.println("initData chiamato con username: " + username + ", difficulty: " + difficulty);
        this.playerName = username;
        this.difficulty = difficulty;
        
        try {
            // Aggiungi log prima di caricare le risposte
            System.out.println("Tentativo di caricare le risposte corrette per Fill" + difficulty);
            this.correctAnswers = FileUtils.readCorrectAnswers("Fill", difficulty);
            
            if (correctAnswers == null || correctAnswers.length == 0) {
                throw new RuntimeException("Failed to load correct answers");
            }
            
            System.out.println("Caricate " + correctAnswers.length + " risposte corrette");
            playerNameLabel.setText("Nome del giocatore: " + playerName);
            printCorrectAnswers(); // Debug print
            initializeExercise();
            
        } catch (Exception e) {
            System.err.println("Errore durante l'inizializzazione: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare l'esercizio. Riprova più tardi.");
            // Return to previous screen or handle error
            try {
                Stage stage = (Stage) playerNameLabel.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ExerciseListView.fxml"));
                Parent root = loader.load();
                stage.setScene(new Scene(root));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Inizializza l'esercizio creando risposte utente vuote
     * e impostando il listener per il cambio di scheda.
     */
    /**
     * Inizializza l'esercizio creando la lista delle risposte utente
     * e configurando il listener per il cambio di scheda.
     */
    private void initializeExercise() {
        // Inizializza la lista con il numero corretto di elementi
        userAnswers = new ArrayList<>();
        for (int i = 0; i < correctAnswers.length; i++) {
            userAnswers.add("");
        }
                
        // Aggiungi listener per il cambio di scheda
        tabPane.getSelectionModel().selectedIndexProperty().addListener(
            (observable, oldValue, newValue) -> saveCurrentAnswerAndRefresh());
    }
    
    /**
     * Gestisce il click sul pulsante home.
     * Salva i progressi correnti e torna alla vista dell'elenco esercizi.
     */
    @FXML
    /**
     * Gestisce il ritorno alla schermata principale.
     * Salva i progressi correnti e naviga verso la vista dell'elenco esercizi.
     */
    private void handleHomeButtonAction() {
        try {
            // Formattazione della percentuale di completamento
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse(completionPercentageLabel.getText().replace("Completamento: ", "").replace("%", ""));
            double completionPercentage = number.doubleValue();

            // Salva i risultati su un file CSV includendo la difficoltà
            // Usa solo "FillView" come nome dell'esercizio per mantenere la coerenza con i record esistenti
            String exerciseName = "FillView";
            
            // Aggiungi log per debug
            System.out.println("Salvataggio risultati: " + playerName + ", " + exerciseName + ", " + difficulty + ", " + completionPercentage);
            
            // Chiama il metodo corretto per salvare i risultati con la difficoltà
            FileUtils.writeGameResultsWithDifficulty(playerName, exerciseName, difficulty, completionPercentage);

            // Cambia la vista per tornare a ExerciseListView
            System.out.println("Tornando alla vista ExerciseListView...");
            Stage stage = (Stage) HomeButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ExerciseListView.fxml"));
            Parent root = loader.load();

            // Passa il nome utente al controller ExerciseListController
            ExerciseListController controller = loader.getController();
            controller.initData(playerName);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore: " + e.getMessage());
        }
    }

    /**
     * Mostra una finestra di dialogo di avviso con il tipo, titolo e messaggio specificati.
     */
    /**
     * Mostra una finestra di dialogo di avviso con il tipo, titolo e messaggio specificati.
     * 
     * @param alertType Tipo di avviso da mostrare
     * @param title Titolo della finestra di dialogo
     * @param message Messaggio da mostrare all'utente
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Mostra una finestra di dialogo di conferma per procedere all'esercizio successivo.
     */
    private void showNextExerciseConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Prossimo Esercizio");
        alert.setHeaderText(null);
        alert.setContentText("Vuoi passare all'esercizio successivo?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                handleNextButtonAction();
            }
        });
    }

    /**
     * Inizializza il controller.
     * Imposta il nome del giocatore iniziale e l'etichetta.
     */
    @FXML
    private void initialize() {
        // Imposta il nome del giocatore (se initData non è stato chiamato)
        if (playerName == null) {
            playerName = User.getUsername();
        }
        playerNameLabel.setText("Nome del giocatore: " + playerName);
    }

    /**
     * Ottiene la TextArea per l'input dell'utente nella scheda corrente.
     * @return La TextArea per l'esercizio corrente
     */
    private TextArea getCurrentUserInput() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        return (TextArea) tabPane.getTabs().get(currentIndex).getContent().lookup("#userInput" + (currentIndex + 1));
    }

    /**
     * Salva la risposta corrente e aggiorna la visualizzazione dei risultati.
     */
    private void saveCurrentAnswerAndRefresh() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        TextArea currentTextArea = getCurrentUserInput();
        if (currentTextArea != null && currentIndex < userAnswers.size()) {
            String userAnswer = currentTextArea.getText().trim();
            userAnswers.set(currentIndex, userAnswer);
            updateResults();
        }
    }

    /**
     * Gestisce il click sul pulsante di verifica.
     * Verifica se la risposta corrente è corretta e mostra il feedback appropriato.
     */
    @FXML
    private void handleCheckButtonAction() {
        saveCurrentAnswerAndRefresh();
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        TextArea currentTextArea = getCurrentUserInput();
        if (currentTextArea != null) {
            String userAnswer = currentTextArea.getText().trim();
            if (userAnswer.equalsIgnoreCase(correctAnswers[currentIndex])) {
                showAlert(Alert.AlertType.INFORMATION, "Corretto", "La tua risposta è corretta!");
                if (currentIndex < tabPane.getTabs().size() - 1) {
                    showNextExerciseConfirmation();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Errato", "La tua risposta è errata. Per favore, riprova.");
            }
        }
    }

    /**
     * Aggiorna la visualizzazione dei risultati con i progressi correnti.
     * Calcola la percentuale di completamento e aggiorna gli elementi dell'interfaccia.
     */
    private void updateResults() {
        StringBuilder userAnswersBuilder = new StringBuilder();
        int correctCount = 0;

        for (int i = 0; i < userAnswers.size(); i++) {
            userAnswersBuilder.append("Domanda ").append(i + 1).append(": ").append(userAnswers.get(i)).append("\n");
            if (userAnswers.get(i).equalsIgnoreCase(correctAnswers[i])) {
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

    /**
     * Gestisce il click sul pulsante avanti.
     * Passa all'esercizio successivo se disponibile.
     */
    @FXML
    private void handleNextButtonAction() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (currentIndex < tabPane.getTabs().size() - 1) {
            saveCurrentAnswerAndRefresh();
            tabPane.getSelectionModel().select(currentIndex + 1);
        }
    }

    /**
     * Gestisce il click sul pulsante indietro.
     * Torna all'esercizio precedente se disponibile.
     */
    @FXML
    private void handleBackButtonAction() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (currentIndex > 0) {
            saveCurrentAnswerAndRefresh();
            tabPane.getSelectionModel().select(currentIndex - 1);
        }
    }
}