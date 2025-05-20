package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Controller per la selezione della difficoltà degli esercizi di debug del codice.
 * Gestisce l'interfaccia utente per la scelta del livello di difficoltà
 * (Facile, Medio, Difficile) per la modalità di debug del codice.
 * 
 * Questa modalità permette agli utenti di esercitarsi nel trovare e correggere
 * errori in snippets di codice di diversa complessità.
 * 
 * Il controller estende AbstractController ereditando le funzionalità base
 * come la gestione della navigazione e la visualizzazione del nome utente.
 */
public class DifficultyControllerCodeDebug extends AbstractController {

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
     * Gestisce l'apertura del livello facile degli esercizi di debug.
     * Carica gli esercizi di debug più semplici, adatti a utenti principianti.
     */
    @FXML
    private void openEasyLevel() {
        openQuiz("/view/CodeDebugViewEasy.fxml", "Debug Code - Livello Facile");
    }

    /**
     * Gestisce l'apertura del livello medio degli esercizi di debug.
     * Carica esercizi di debug di media complessità, con errori più sottili
     * e codice più articolato.
     */
    @FXML
    private void openMediumLevel() {
        openQuiz("/view/CodeDebugViewMedium.fxml", "Debug Code - Livello Medio");
    }

    /**
     * Gestisce l'apertura del livello difficile degli esercizi di debug.
     * Carica esercizi di debug avanzati con errori complessi da individuare
     * e correggere, richiedendo una comprensione approfondita del codice.
     */
    @FXML
    private void openHardLevel() {
        openQuiz("/view/CodeDebugViewHard.fxml", "Debug Code - Livello Difficile");
    }

    /**
     * Metodo privato che gestisce il caricamento della vista degli esercizi di debug.
     * Si occupa di:
     * - Caricare il file FXML appropriato per il livello selezionato
     * - Configurare la nuova scena
     * - Inizializzare il controller di debug con il nome utente e la difficoltà
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
                if (controller instanceof CodeDebugController) {
                    ((CodeDebugController) controller).initData(playerName, difficulty);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Errore", 
                    "Impossibile caricare il livello: " + e.getMessage());
        }
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
