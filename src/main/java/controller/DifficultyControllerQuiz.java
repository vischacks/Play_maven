package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * Controller per la selezione della difficoltà dei quiz.
 * Gestisce l'interfaccia utente per la scelta del livello di difficoltà
 * (Facile, Medio, Difficile) per la modalità Quiz.
 * 
 * Questo controller estende AbstractController ereditando le funzionalità base
 * come la gestione della navigazione e la visualizzazione del nome utente.
 */
public class DifficultyControllerQuiz extends AbstractController {

    /**
     * Label che mostra il nome del giocatore corrente nell'interfaccia.
     * Viene aggiornata automaticamente quando si inizializza il controller.
     */
    @FXML
    private Label playerNameLabel;
    
    /**
     * Implementazione del metodo astratto per aggiornare l'etichetta del nome utente.
     * Viene chiamato automaticamente quando si imposta un nuovo nome utente.
     */
    @Override
    protected void updatePlayerNameLabel() {
        if (playerNameLabel != null) {
            playerNameLabel.setText("Nome del giocatore: " + playerName);
        }
    }

    /**
     * Gestisce l'apertura del livello facile dei quiz.
     * Viene chiamato quando l'utente clicca sul pulsante del livello facile.
     */
    @FXML
    private void openEasyLevel() {
        openQuiz("/view/QuizViewEasy.fxml", "Quiz - Livello Facile");
    }

    /**
     * Gestisce l'apertura del livello medio dei quiz.
     * Viene chiamato quando l'utente clicca sul pulsante del livello medio.
     */
    @FXML
    private void openMediumLevel() {
        openQuiz("/view/QuizViewMedium.fxml", "Quiz - Livello Medio");
    }

    /**
     * Gestisce l'apertura del livello difficile dei quiz.
     * Viene chiamato quando l'utente clicca sul pulsante del livello difficile.
     */
    @FXML
    private void openHardLevel() { 
        openQuiz("/view/QuizViewHard.fxml", "Quiz - Livello Difficile");
    }

    /**
     * Metodo privato che gestisce il caricamento della vista del quiz.
     * Si occupa di:
     * - Caricare il file FXML appropriato
     * - Configurare la nuova scena
     * - Inizializzare il controller del quiz con il nome utente e la difficoltà
     * - Gestire eventuali errori durante il caricamento
     * 
     * @param fxmlPath Percorso del file FXML da caricare
     * @param title Titolo da mostrare nella finestra
     */
    private void openQuiz(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Stage currentStage = getCurrentStage(playerNameLabel);
            
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle(title);
            currentStage.show();
            
            // Inizializza il controller con il nome utente e la difficoltà
            Object controller = loader.getController();
            if (controller != null && controller instanceof QuizController) {
                String difficulty = getDifficultyFromTitle(title);
                ((QuizController) controller).initData(playerName, difficulty);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", 
                    "Impossibile caricare il livello: " + e.getMessage());
        }
    }
    
    /**
     * Estrae il livello di difficoltà dal titolo della finestra.
     * Converte il testo in italiano nel corrispondente valore in inglese
     * utilizzato internamente dal sistema.
     * 
     * @param title Titolo della finestra contenente il livello di difficoltà
     * @return Stringa rappresentante il livello di difficoltà (Easy, Medium, Hard)
     */
    private String getDifficultyFromTitle(String title) {
        if (title.contains("Facile")) {
            return "Easy";
        } else if (title.contains("Medio")) {
            return "Medium";
        } else if (title.contains("Difficile")) {
            return "Hard";
        }
        return "";
    }
    
    /**
     * Gestisce il click sul pulsante "Indietro".
     * Riporta l'utente alla schermata principale degli esercizi,
     * mantenendo il contesto dell'utente corrente.
     */
    @FXML
    private void handleBackButton() {
        try {
            Stage currentStage = getCurrentStage(playerNameLabel);
            goToHome(currentStage);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", 
                    "Impossibile tornare alla schermata principale: " + e.getMessage());
        }
    }
}
