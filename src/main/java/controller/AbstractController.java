package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Classe base astratta per tutti i controller dell'applicazione.
 * Fornisce metodi comuni utilizzati da più controller.
 * 
 * Questa classe implementa il pattern Template Method fornendo:
 * - Gestione centralizzata della navigazione tra le viste
 * - Gestione uniforme degli alert e delle conferme
 * - Gestione del nome utente e della sua visualizzazione
 * - Metodi di utilità per l'accesso allo Stage corrente
 * 
 * Tutti i controller specifici dell'applicazione devono estendere questa classe
 * per ereditare le funzionalità comuni e mantenere un comportamento coerente.
 */
public abstract class AbstractController {
    
    /**
     * Nome del giocatore corrente.
     * Questa variabile viene utilizzata per tenere traccia dell'utente attualmente loggato
     * e viene visualizzata nelle varie schermate dell'applicazione.
     * Viene inizializzata dal metodo initData quando un utente effettua il login.
     */
    protected String playerName;
    
    /**
     * Inizializza i dati del controller con il nome utente.
     * Questo metodo viene chiamato quando si passa da una vista all'altra per
     * mantenere la persistenza del nome utente attraverso l'applicazione.
     * 
     * @param username Nome utente da utilizzare per l'inizializzazione
     */
    public void initData(String username) {
        this.playerName = username;
        updatePlayerNameLabel();
    }
    
    /**
     * Metodo astratto che deve essere implementato dai controller figli per
     * aggiornare l'etichetta del nome utente nella loro interfaccia grafica.
     * 
     * Questo metodo segue il pattern Template Method, permettendo a ogni controller
     * di gestire in modo specifico la visualizzazione del nome utente mantenendo
     * un'implementazione coerente in tutta l'applicazione.
     */
    protected abstract void updatePlayerNameLabel();
    
    /**
     * Carica una nuova vista FXML e la visualizza nello stage corrente.
     * Questo metodo gestisce il caricamento delle viste FXML e la gestione degli errori,
     * fornendo un punto centralizzato per la navigazione tra le schermate.
     * 
     * @param fxmlPath Percorso del file FXML da caricare
     * @param title Titolo da assegnare alla nuova finestra
     * @param currentStage Stage corrente su cui caricare la nuova vista
     */
    protected void loadView(String fxmlPath, String title, Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            currentStage.setScene(scene);
            currentStage.setTitle(title);
            currentStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare la vista: " + e.getMessage());
        }
    }
    
    /**
     * Mostra un messaggio di alert all'utente.
     * Questo metodo standardizza la visualizzazione dei messaggi di alert
     * in tutta l'applicazione, garantendo una presentazione uniforme
     * di errori, avvisi e informazioni.
     * 
     * @param alertType Tipo di alert (ERROR, INFORMATION, WARNING, CONFIRMATION)
     * @param title Titolo della finestra di alert
     * @param message Messaggio da mostrare all'utente
     */
    protected void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Mostra una finestra di dialogo di conferma all'utente.
     * Utilizzato per richiedere conferma prima di eseguire azioni importanti
     * come logout, cambio utente o uscita dall'applicazione.
     * 
     * @param title Titolo della finestra di conferma
     * @param message Messaggio da mostrare all'utente
     * @return true se l'utente clicca OK/Conferma, false altrimenti
     */
    protected boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    /**
     * Gestisce la navigazione verso la schermata principale (ExerciseListView).
     * Questo metodo si occupa di:
     * - Caricare la vista della lista esercizi
     * - Trasferire il nome utente al nuovo controller
     * - Gestire eventuali errori durante la navigazione
     * 
     * @param currentStage Stage corrente da cui si sta navigando
     */
    protected void goToHome(Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ExerciseListView.fxml"));
            Parent root = loader.load();
            
            // Passa il nome utente al controller se è del tipo corretto
            Object controller = loader.getController();
            if (controller != null && controller instanceof AbstractController) {
                ((AbstractController) controller).initData(playerName);
            }
            
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle("Lista Esercizi");
            currentStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile tornare alla home: " + e.getMessage());
        }
    }
    
    /**
     * Recupera lo Stage corrente a partire da un qualsiasi nodo dell'interfaccia grafica.
     * Questo metodo di utilità permette di accedere allo Stage principale
     * dell'applicazione da qualsiasi punto dell'interfaccia grafica.
     * 
     * @param node Qualsiasi componente JavaFX presente nella scena corrente
     * @return Lo Stage (finestra) che contiene il componente
     */
    protected Stage getCurrentStage(javafx.scene.Node node) {
        return (Stage) node.getScene().getWindow();
    }
    
    /**
     * Carica una nuova vista FXML e restituisce il suo controller.
     * Questo metodo estende loadView() permettendo di accedere al controller
     * della nuova vista per ulteriori inizializzazioni o configurazioni.
     * 
     * @param fxmlPath Percorso del file FXML da caricare
     * @param title Titolo da assegnare alla nuova finestra
     * @param currentStage Stage corrente su cui caricare la vista
     * @return Il controller associato alla vista caricata, o null in caso di errore
     */
    protected Object loadViewAndGetController(String fxmlPath, String title, Stage currentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            currentStage.setScene(scene);
            currentStage.setTitle(title);
            currentStage.show();
            
            return loader.getController();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare la vista: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Ottiene il controller corrente dopo aver caricato una vista.
     * Questo metodo è pensato per essere sovrascritto nelle sottoclassi
     * che necessitano di accedere al proprio controller specifico.
     * 
     * @return Il controller corrente o null se non è stato caricato
     */
    protected Object getCurrentController() {
        return null; // Questo metodo sarà sovrascritto nelle sottoclassi
    }
}