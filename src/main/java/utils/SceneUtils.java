package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

/**
 * Classe di utilità per la gestione delle scene e dei dialoghi in JavaFX.
 * Questa classe fornisce metodi statici per:
 * - Gestione del cambio di scene nell'applicazione
 * - Caricamento di file FXML e relativi controller
 * - Gestione delle finestre di dialogo (alert e conferme)
 * - Manipolazione degli stage JavaFX
 *
 * La classe semplifica le operazioni comuni dell'interfaccia utente
 * fornendo un'API unificata per la gestione delle scene e dei dialoghi,
 * gestendo anche le eccezioni che potrebbero verificarsi durante
 * il caricamento dei file FXML.
 */
public class SceneUtils {

    /**
     * Cambia la scena corrente con una nuova scena caricata da un file FXML.
     * Questo metodo gestisce il passaggio tra diverse viste dell'applicazione,
     * mantenendo lo stesso stage (finestra) ma aggiornando il contenuto.
     *
     * @param currentScene La scena attualmente visualizzata
     * @param fxmlFile     Il percorso del file FXML da caricare
     * @throws IOException Se si verifica un errore durante il caricamento del file FXML
     */
    public static void switchScene(Scene currentScene, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneUtils.class.getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) currentScene.getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Carica il controller associato a un file FXML.
     * Questo metodo è utile quando si necessita di accedere al controller
     * di una vista per inizializzare dati o configurare comportamenti
     * prima di visualizzare la vista stessa.
     *
     * @param fxmlFile Il percorso del file FXML
     * @return Il controller associato al file FXML
     * @throws IOException Se si verifica un errore durante il caricamento del file
     */
    public static Object loadController(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneUtils.class.getResource(fxmlFile));
        loader.load();
        return loader.getController();
    }

    /**
     * Chiude lo stage (finestra) corrente.
     * Questo metodo è utile per chiudere finestre secondarie o
     * per terminare l'applicazione quando si chiude la finestra principale.
     *
     * @param currentScene La scena attualmente visualizzata
     */
    public static void closeStage(Scene currentScene) {
        Stage stage = (Stage) currentScene.getWindow();
        stage.close();
    }

    /**
     * Mostra una nuova finestra (stage) con il contenuto specificato dal file FXML.
     * Questo metodo è utile per creare finestre di dialogo modali o
     * per visualizzare contenuti in finestre separate.
     *
     * @param fxmlFile Il percorso del file FXML da caricare
     * @throws IOException Se si verifica un errore durante il caricamento del file
     */
    public static void showNewStage(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneUtils.class.getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Mostra una finestra di dialogo informativa con un messaggio specifico.
     * Questo metodo crea un alert di tipo INFORMATION che blocca l'interazione
     * con l'applicazione finché l'utente non lo chiude.
     *
     * @param message Il messaggio da visualizzare nella finestra di dialogo
     */
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informazione");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Mostra una finestra di dialogo di conferma con un messaggio specifico.
     * Questo metodo crea un alert di tipo CONFIRMATION che permette all'utente
     * di confermare o annullare un'azione. Il dialogo blocca l'interazione
     * con l'applicazione finché l'utente non fa una scelta.
     *
     * @param message Il messaggio da visualizzare nella finestra di dialogo
     * @return true se l'utente conferma (preme OK), false altrimenti
     */
    public static boolean showConfirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}