

package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale dell'applicazione che funge da punto di ingresso.
 * Estende la classe Application di JavaFX per inizializzare e avviare
 * l'interfaccia grafica dell'applicazione.
 *
 * Questa classe si occupa di:
 * - Caricare la vista iniziale di login
 * - Gestire la configurazione base della finestra principale
 * - Gestire eventuali errori durante l'avvio dell'applicazione
 */
public class Main extends Application {
    
    /** Percorso del file FXML per la vista di login */
    private static final String LOGIN_FXML_PATH = "/view/Login.fxml";
    
    /** Titolo dell'applicazione mostrato nella finestra principale */
    private static final String APP_TITLE = "Esercizi di Programmazione";

    /**
     * Metodo principale per l'avvio dell'applicazione JavaFX.
     * Carica la vista di login e configura la finestra principale.
     *
     * @param primaryStage Lo stage principale fornito dal framework JavaFX
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = loadFXML(LOGIN_FXML_PATH);
            Scene scene = new Scene(root);
            
            primaryStage.setScene(scene);
            primaryStage.setTitle(APP_TITLE);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Errore durante l'avvio dell'applicazione: " + e.getMessage());
            e.printStackTrace();
            Platform.exit();
        }
    }
    
    /**
     * Carica un file FXML e restituisce il nodo radice della vista.
     *
     * @param fxmlPath Il percorso del file FXML da caricare
     * @return Il nodo radice (Parent) della vista caricata
     * @throws Exception Se si verifica un errore durante il caricamento del file FXML
     */
    private Parent loadFXML(String fxmlPath) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        return loader.load();
    }

    /**
     * Punto di ingresso principale dell'applicazione.
     * Avvia l'applicazione JavaFX chiamando il metodo launch.
     *
     * @param args Argomenti della riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
