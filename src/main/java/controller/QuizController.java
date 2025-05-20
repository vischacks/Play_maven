package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.FileUtils;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller per la gestione del quiz interattivo.
 * Gestisce la logica per la presentazione delle domande, la validazione delle risposte
 * e il tracciamento del progresso dell'utente.
 * 
 * Caratteristiche principali:
 * - Gestione delle domande e risposte del quiz
 * - Validazione delle risposte dell'utente
 * - Tracciamento del progresso
 * - Navigazione tra le domande
 * - Salvataggio dei risultati
 */
public class QuizController extends AbstractController {

   

    /** Barra di progresso che mostra il completamento complessivo del quiz */
    @FXML
    public ProgressBar completionProgressBar;
    /** Pannello a schede per la navigazione tra le domande del quiz */
    @FXML
    private TabPane tabPane;
    @FXML
    private TextArea userAnswersTextArea;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label completionPercentageLabel;
    @FXML
    private Label playerNameLabel;
    @FXML
    public Button HomeButton;

    /** Lista delle risposte fornite dall'utente */
    private List<String> userAnswers = new ArrayList<>();
    /** Array delle risposte corrette caricate dal file */
    private String[] correctAnswers;
    /** Livello di difficoltà selezionato */
    private String difficulty;
    /** Nome del giocatore corrente */
    private String playerName;

    @Override
    protected void updatePlayerNameLabel() {
        if (playerNameLabel != null) {
            playerNameLabel.setText("Nome del giocatore: " + playerName);
        }
    }

    @FXML
    public void initialize() {
        // Non chiamare updateProgress() qui, poiché correctAnswers è ancora null
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
            System.out.println("Tentativo di caricare le risposte corrette per Quiz" + difficulty);
            this.correctAnswers = FileUtils.readCorrectAnswers("Quiz", difficulty);
            
            if (correctAnswers == null || correctAnswers.length == 0) {
                throw new RuntimeException("Failed to load correct answers");
            }
            
            System.out.println("Caricate " + correctAnswers.length + " risposte corrette");
            playerNameLabel.setText("Nome del giocatore: " + playerName);
            initializeExercise();
            updateProgress();
            
        } catch (Exception e) {
            System.err.println("Errore durante l'inizializzazione: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare l'esercizio. Riprova più tardi.");
            goToHome(getCurrentStage(playerNameLabel));
        }
    }

    /**
     * Inizializza l'esercizio creando la lista delle risposte utente
     * e configurando il listener per il cambio di scheda.
     */
    private void initializeExercise() {
        userAnswers = new ArrayList<>();
        for (int i = 0; i < correctAnswers.length; i++) {
            userAnswers.add("");
        }
        
        tabPane.getSelectionModel().selectedIndexProperty().addListener(
            (observable, oldValue, newValue) -> saveCurrentAnswerAndRefresh());
    }

    /**
     * Gestisce la verifica della risposta corrente.
     * Confronta la risposta dell'utente con quella corretta e mostra il feedback appropriato.
     */
    @FXML
    private void handleCheckButtonAction() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        TextArea currentInput = (TextArea) tabPane.getScene().lookup("#userInput2" + (currentIndex + 1));
        
        if (currentInput != null && currentIndex < correctAnswers.length) {
            String userAnswer = currentInput.getText().trim();
            if (userAnswer.equals(correctAnswers[currentIndex])) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Corretto!");
                alert.setHeaderText("La risposta è corretta!");
                alert.setContentText("Vuoi passare alla prossima domanda?");
                
                ButtonType buttonTypeYes = new ButtonType("Sì");
                ButtonType buttonTypeNo = new ButtonType("No");
                
                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
                
                alert.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeYes) {
                        handleNextButtonAction();
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Errato", "La risposta non è corretta. Riprova!");
            }
        }
        updateProgress();
    }

    @FXML
    private void handleBackButtonAction() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (currentIndex > 0) {
            tabPane.getSelectionModel().select(currentIndex - 1);
        }
    }

    @FXML
    private void handleNextButtonAction() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (currentIndex < tabPane.getTabs().size() - 1) {
            tabPane.getSelectionModel().select(currentIndex + 1);
        }
    }

    @FXML
    private void handleHomeButtonAction() {
        try {
            // Formattazione della percentuale di completamento
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse(completionPercentageLabel.getText().replace("Completamento: ", "").replace("%", ""));
            double completionPercentage = number.doubleValue();
    
            // Usa "QuizView" come nome dell'esercizio per mantenere la coerenza
            String exerciseName = "QuizView";
            
            // Aggiungi log per debug
            System.out.println("Salvataggio risultati: " + playerName + ", " + exerciseName + ", " + difficulty + ", " + completionPercentage);
            
            // Chiama il metodo corretto per salvare i risultati con la difficoltà
            FileUtils.writeGameResultsWithDifficulty(playerName, exerciseName, difficulty, completionPercentage);
    
            // Cambia la vista per tornare a ExerciseListView
            System.out.println("Tornando alla vista ExerciseListView...");
            Stage stage = (Stage) playerNameLabel.getScene().getWindow();  // Usa playerNameLabel invece di HomeButton
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
     * Salva la risposta corrente dell'utente e aggiorna la visualizzazione del progresso.
     * Viene chiamato automaticamente quando l'utente cambia scheda.
     */
    private void saveCurrentAnswerAndRefresh() {
        int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
        if (currentIndex >= 0 && currentIndex < correctAnswers.length) {
            TextArea currentInput = (TextArea) tabPane.getScene().lookup("#userInput2" + (currentIndex + 1));
            if (currentInput != null) {
                String answer = currentInput.getText().trim();
                userAnswers.set(currentIndex, answer);
                // Aggiorniamo immediatamente lo stato della risposta
                if (answer.equals(correctAnswers[currentIndex])) {
                    System.out.println("Risposta corretta salvata per la domanda " + (currentIndex + 1));
                }
            }
        }
        updateProgress();
    }

    /**
     * Aggiorna la visualizzazione del progresso del quiz.
     * Calcola la percentuale di risposte corrette e aggiorna la barra di progresso
     * e l'etichetta con la percentuale di completamento.
     */
    private void updateProgress() {
        // Verifichiamo che i componenti siano inizializzati prima di procedere
        if (completionProgressBar == null) {
            System.out.println("completionProgressBar è null");
            return;
        }
        if (completionPercentageLabel == null) {
            System.out.println("completionPercentageLabel è null");
            return;
        }
        if (correctAnswers == null) {
            System.out.println("correctAnswers è null");
            return;
        }
        if (userAnswers == null || userAnswers.isEmpty()) {
            System.out.println("userAnswers è null o vuoto");
            return;
        }
    
        StringBuilder userAnswersBuilder = new StringBuilder();
        int correctCount = 0;
    
        // Verifica le risposte corrette
        for (int i = 0; i < userAnswers.size(); i++) {
            userAnswersBuilder.append("Domanda ").append(i + 1).append(": ").append(userAnswers.get(i)).append("\n");
            if (userAnswers.get(i).equals(correctAnswers[i])) {
                correctCount++;
            }
        }
    
        // Aggiorna la TextArea delle risposte utente se presente
        if (userAnswersTextArea != null) {
            userAnswersTextArea.setText(userAnswersBuilder.toString());
        }
    
        // Calcola e aggiorna la percentuale di completamento
        double completionPercentage = ((double) correctCount / correctAnswers.length) * 100;
        completionProgressBar.setProgress(correctCount / (double) correctAnswers.length);
        completionPercentageLabel.setText(String.format("Completamento: %.2f%%", completionPercentage));
    
        System.out.println("Progresso aggiornato: " + completionPercentage + "% (" + correctCount + "/" + correctAnswers.length + " risposte corrette)");
    }
}
