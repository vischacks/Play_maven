package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller per la selezione della difficoltà dell'esercizio "Riempi gli Spazi".
 * Gestisce la navigazione tra i diversi livelli di difficoltà.
 */
public class DifficultyControllerFillSpace extends AbstractController {

    @FXML
    private Label playerNameLabel;

    /**
     * Aggiorna l'etichetta con il nome del giocatore.
     * Sovrascrive il metodo della classe AbstractController.
     */
    @Override
    protected void updatePlayerNameLabel() {
        if (playerNameLabel != null) {
            playerNameLabel.setText("Nome del giocatore: " + playerName);
        }
    }

    /**
     * Gestisce l'apertura del livello facile dell'esercizio.
     * Viene chiamato quando l'utente seleziona il livello facile.
     */
    @FXML
    private void openEasyLevel() {
        openQuiz("/view/ExerciseFillViewEasy.fxml", "Riempi gli Spazi - Livello Facile");
    }

    /**
     * Gestisce l'apertura del livello medio dell'esercizio.
     * Viene chiamato quando l'utente seleziona il livello medio.
     */
    @FXML
    private void openMediumLevel() {
        openQuiz("/view/ExerciseFillViewMedium.fxml", "Riempi gli Spazi - Livello Medio");
    }

    /**
     * Gestisce l'apertura del livello difficile dell'esercizio.
     * Viene chiamato quando l'utente seleziona il livello difficile.
     */
    @FXML
    private void openHardLevel() {
        openQuiz("/view/ExerciseFillViewHard.fxml", "Riempi gli Spazi - Livello Difficile");
    }

    /**
     * Apre la vista dell'esercizio con il percorso FXML e il titolo specificati.
     * Inizializza il controller appropriato con i dati necessari.
     * 
     * @param fxmlPath Il percorso del file FXML da caricare
     * @param title Il titolo da impostare per la finestra
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
            
            // Extract difficulty from the title or path
            String difficulty = "";
            if (title.contains("Facile")) {
                difficulty = "Easy";
            } else if (title.contains("Medio")) {
                difficulty = "Medium";
            } else if (title.contains("Difficile")) {
                difficulty = "Hard";
            }
            
            // Get the controller and initialize it properly
            Object controller = loader.getController();
            if (controller != null) {
                if (controller instanceof FillController) {
                    System.out.println("Initializing FillController with: " + playerName + ", " + difficulty);
                    ((FillController) controller).initData(playerName, difficulty);
                } else if (controller instanceof AbstractController) {
                    ((AbstractController) controller).initData(playerName);
                }
            } else {
                System.err.println("Controller is null!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", 
                    "Impossibile caricare il livello: " + e.getMessage());
        }
    }
    
    /**
     * Gestisce il click sul pulsante "Indietro".
     * Torna alla schermata principale dell'applicazione.
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
