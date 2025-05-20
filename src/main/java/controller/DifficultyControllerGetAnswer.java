package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DifficultyControllerGetAnswer extends AbstractController {

    @FXML
    private Label playerNameLabel;
    
    @Override
    protected void updatePlayerNameLabel() {
        if (playerNameLabel != null) {
            playerNameLabel.setText("Nome del giocatore: " + playerName);
        }
    }

    @FXML
    private void openEasyLevel() {
        openQuiz("/view/GetAnswerViewEasy.fxml", "Scrivi l'Output - Livello Facile");
    }

    @FXML
    private void openMediumLevel() {
        openQuiz("/view/GetAnswerViewMedium.fxml", "Scrivi l'Output - Livello Medio");
    }

    @FXML
    private void openHardLevel() {
        openQuiz("/view/GetAnswerViewHard.fxml", "Scrivi l'Output - Livello Difficile");
    }

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
