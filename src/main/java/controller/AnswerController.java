package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.FileUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller per la gestione degli esercizi di risposta.
 * Gestisce la logica per la presentazione degli esercizi, la validazione degli output
 * e il tracciamento del progresso dell'utente.
 * 
 * Caratteristiche principali:
 * - Gestione degli esercizi di output
 * - Validazione degli output dell'utente
 * - Tracciamento del progresso
 * - Navigazione tra gli esercizi
 * - Salvataggio dei risultati
 */
public class AnswerController extends AbstractController {

    @FXML
    public ProgressBar completionProgressBar;
    @FXML
    public Button homeButton;
    public TextArea userInput1, userInput2, userInput3, userInput4, userInput5,
            userInput6, userInput7, userInput8, userInput9, userInput10, userInput11;

    @FXML
    private TabPane tabPane;
    @FXML
    private TextArea userOutputTextArea;
    @FXML
    private Label completionPercentageLabel;
    @FXML
    private Label playerNameLabel;

    /** Lista degli output forniti dall'utente */
    private List<String> userOutputs = new ArrayList<>();
    /** Array degli output attesi caricati dal file */
    private String[] expectedOutputs;  // Rimuoviamo l'inizializzazione diretta
    /** Livello di difficoltà selezionato */
    private String difficulty;  // Aggiungiamo la variabile per la difficoltà

    @Override
    protected void updatePlayerNameLabel() {
        if (playerNameLabel != null) {
            playerNameLabel.setText("Nome del giocatore: " + playerName);
        }
    }

    /**
     * Inizializza il controller con i dati dell'utente e il livello di difficoltà.
     * Carica gli output attesi dal file appropriato e configura l'interfaccia.
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
            System.out.println("Tentativo di caricare le risposte corrette per Output" + difficulty);
            this.expectedOutputs = FileUtils.readCorrectAnswers("Output", difficulty);
            
            if (expectedOutputs == null || expectedOutputs.length == 0) {
                throw new RuntimeException("Failed to load correct answers");
            }
            
            System.out.println("Caricate " + expectedOutputs.length + " risposte corrette");
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
     * Inizializza l'esercizio creando la lista degli output utente
     * e configurando il listener per il cambio di scheda.
     */
    private void initializeExercise() {
        // Inizializza gli output degli utenti con stringhe vuote
        userOutputs = new ArrayList<>();
        for (int i = 0; i < expectedOutputs.length; i++) {
            userOutputs.add("");
        }

        // Listener per gestire il cambio di scheda
        tabPane.getSelectionModel().selectedIndexProperty().addListener(
            (observable, oldValue, newValue) -> saveCurrentOutput());
    }

    /**
     * Ottiene l'input corrente dell'utente in base alla scheda selezionata.
     *
     * @return TextArea corrente.
     */
    private TextArea getCurrentUserInput() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        return (TextArea) tabPane.getTabs().get(currentIndex).getContent().lookup("#userInput" + (currentIndex + 1));
    }

    /**
     * Salva l'output dell'utente corrente e aggiorna i risultati.
     */
    /**
     * Salva l'output corrente dell'utente e aggiorna la visualizzazione dei risultati.
     * Viene chiamato automaticamente quando l'utente cambia scheda.
     */
    private void saveCurrentOutput() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        TextArea currentTextArea = getCurrentUserInput();
        if (currentTextArea != null) {
            String userOutput = currentTextArea.getText().trim();
            userOutputs.set(currentIndex, userOutput);
        }
        updateResults();
    }

    @FXML
    private void handleCheckButtonAction() {
        saveCurrentOutput();
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        TextArea currentTextArea = getCurrentUserInput();
        if (currentTextArea != null) {
            String userOutput = currentTextArea.getText().trim();
            if (userOutput.equalsIgnoreCase(expectedOutputs[currentIndex])) {
                showAlert(Alert.AlertType.INFORMATION, "Corretto", "L'output inserito è corretto!");
                if (currentIndex < tabPane.getTabs().size() - 1) {
                    handleNextButtonAction();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Errato", "L'output inserito è errato. Riprova.");
            }
        }
    }

    
    /**
     * Aggiorna la visualizzazione dei risultati degli esercizi.
     * Calcola la percentuale di output corretti e aggiorna la barra di progresso
     * e l'etichetta con la percentuale di completamento.
     */
    private void updateResults() {
        StringBuilder userOutputsBuilder = new StringBuilder();
        int correctCount = 0;

        for (int i = 0; i < userOutputs.size(); i++) {
            userOutputsBuilder.append("Domanda ").append(i + 1).append(": ").append(userOutputs.get(i)).append("\n");
            if (userOutputs.get(i).equalsIgnoreCase(expectedOutputs[i])) {
                correctCount++;
            }
        }

        if (userOutputTextArea != null) {
            userOutputTextArea.setText(userOutputsBuilder.toString());
        }

        // Aggiorna la progressione dell'utente
        double completionPercentage = ((double) correctCount / expectedOutputs.length) * 100;
        completionPercentageLabel.setText(String.format("Completamento: %.2f%%", completionPercentage));
        completionProgressBar.setProgress(correctCount / (double) expectedOutputs.length);
    }

    @FXML
    private void handleNextButtonAction() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (currentIndex < tabPane.getTabs().size() - 1) {
            saveCurrentOutput();
            tabPane.getSelectionModel().select(currentIndex + 1);
        }
    }

    @FXML
    private void handleBackButtonAction() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (currentIndex > 0) {
            saveCurrentOutput();
            tabPane.getSelectionModel().select(currentIndex - 1);
        }
    }

    // Semplifichiamo il metodo handleHomeButtonAction utilizzando i metodi della classe base
    @FXML
    private void handleHomeButtonAction() {
        try {
            // Formattazione della percentuale di completamento
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse(completionPercentageLabel.getText().replace("Completamento: ", "").replace("%", ""));
            double completionPercentage = number.doubleValue();

            // Salva i risultati in un file CSV con la difficoltà
            String exerciseName = "OutputExercise";
            FileUtils.writeGameResultsWithDifficulty(playerName, exerciseName, difficulty, completionPercentage);

            // Torna alla home
            goToHome(getCurrentStage(homeButton));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore: " + e.getMessage());
        }
    }


    // Semplifichiamo il metodo showNextExerciseConfirmation utilizzando il metodo della classe base
    private void showNextExerciseConfirmation() {
        if (showConfirmation("Prossimo Esercizio", "Vuoi passare all'esercizio successivo?")) {
            handleNextButtonAction();
        }
    }
}