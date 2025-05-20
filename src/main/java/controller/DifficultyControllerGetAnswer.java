package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller per la gestione della selezione della difficoltà negli esercizi di output.
 * Gestisce l'interfaccia che permette all'utente di scegliere il livello di difficoltà
 * (Facile, Medio, Difficile) per gli esercizi di scrittura dell'output.
 *
 * Caratteristiche principali:
 * - Navigazione ai diversi livelli di difficoltà
 * - Gestione del passaggio dei dati al controller degli esercizi
 * - Mantenimento delle informazioni dell'utente durante la navigazione
 */
public class DifficultyControllerGetAnswer extends AbstractController {

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

    /**
     * Gestisce l'apertura del livello facile degli esercizi di output.
     * Carica la vista corrispondente e inizializza il controller con i dati necessari.
     */
    @FXML
    private void openEasyLevel() {
        openQuiz("/view/GetAnswerViewEasy.fxml", "Scrivi l'Output - Livello Facile");
    }

    /**
     * Gestisce l'apertura del livello medio degli esercizi di output.
     * Carica la vista corrispondente e inizializza il controller con i dati necessari.
     */
    @FXML
    private void openMediumLevel() {
        openQuiz("/view/GetAnswerViewMedium.fxml", "Scrivi l'Output - Livello Medio");
    }

    /**
     * Gestisce l'apertura del livello difficile degli esercizi di output.
     * Carica la vista corrispondente e inizializza il controller con i dati necessari.
     */
    @FXML
    private void openHardLevel() {
        openQuiz("/view/GetAnswerViewHard.fxml", "Scrivi l'Output - Livello Difficile");
    }

    /**
     * Metodo di utilità per aprire la vista dell'esercizio con la difficoltà selezionata.
     * Si occupa di:
     * - Caricare la vista FXML appropriata
     * - Configurare lo stage con la nuova scena
     * - Estrarre il livello di difficoltà dal titolo
     * - Inizializzare il controller degli esercizi con i dati necessari
     *
     * @param fxmlPath Il percorso del file FXML da caricare
     * @param title Il titolo da mostrare nella finestra, che include anche il livello di difficoltà
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
            
            // Estrai la difficoltà dal titolo
            String difficulty = "";
            if (title.contains("Facile")) {
                difficulty = "Easy";
            } else if (title.contains("Medio")) {
                difficulty = "Medium";
            } else if (title.contains("Difficile")) {
                difficulty = "Hard";
            }
            
            // Inizializza il controller con il nome utente e la difficoltà
            Object controller = loader.getController();
            if (controller != null) {
                if (controller instanceof AnswerController) {
                    ((AnswerController) controller).initData(playerName, difficulty);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", 
                    "Impossibile caricare il livello: " + e.getMessage());
        }
    }
    
    /**
     * Gestisce il ritorno alla schermata principale dell'applicazione.
     * Utilizza il metodo goToHome della classe padre per effettuare la navigazione,
     * gestendo eventuali errori durante il processo.
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
