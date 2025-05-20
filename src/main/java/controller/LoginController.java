package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.User;
import utils.FileUtils;

import java.io.IOException;

/**
 * Controller che gestisce la schermata di login e registrazione utenti.
 * Permette agli utenti di autenticarsi con credenziali esistenti o
 * di registrarsi come nuovi utenti nel sistema.
 */
public class LoginController extends AbstractController {
    /** Username dell'utente corrente */
    private String username;

    /** Campo per l'inserimento username nel login */
    @FXML
    private TextField usernameField;

    /** Campo per l'inserimento password nel login */
    @FXML
    private PasswordField passwordField;

    /** Campo per l'inserimento username nella registrazione */
    @FXML
    private TextField registerUsernameField;

    /** Campo per l'inserimento password nella registrazione */
    @FXML
    private PasswordField registerPasswordField;

    /** Campo per la conferma password nella registrazione */
    @FXML
    private PasswordField confirmPasswordField;
    
    /** Label che mostra il nome del giocatore */
    @FXML
    private Label playerNameLabel;
    
    @Override
    protected void updatePlayerNameLabel() {
        if (playerNameLabel != null) {
            playerNameLabel.setText("Login");
        }
    }

    /**
     * Gestisce il click sul pulsante di login.
     * Verifica le credenziali inserite e, se valide, reindirizza l'utente
     * alla schermata degli esercizi.
     */
    @FXML
    private void handleLoginButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (isValidLogin(username, password)) {
            try {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Object controller = loadViewAndGetController("/view/ExerciseListView.fxml", "Lista Esercizi", stage);
                
                if (controller instanceof AbstractController) {
                    ((AbstractController) controller).initData(username);
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore durante il caricamento della vista: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Errore di login", "Username o password non validi");
        }
    }

    /**
     * Gestisce il click sul pulsante di registrazione.
     * Verifica che i dati inseriti siano validi e crea un nuovo utente
     * se tutte le validazioni sono superate.
     */
    @FXML
    private void handleCreateUserButtonAction() {
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Tutti i campi devono essere compilati");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Le password non corrispondono");
            return;
        }

        if (FileUtils.readUserFromFile(username) != null) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Nome utente già in uso");
            return;
        }

        User user = new User(username, password);
        FileUtils.writeUserToFile(user);
        showAlert(Alert.AlertType.INFORMATION, "Successo", "Utente creato con successo");
    }

    /**
     * Verifica se le credenziali di login sono valide.
     * @param username Username da verificare
     * @param password Password da verificare
     * @return true se le credenziali sono valide, false altrimenti
     */
    private boolean isValidLogin(String username, String password) {
        User user = FileUtils.readUserFromFile(username);
        return user != null && user.getPassword().equals(password);
    }
}