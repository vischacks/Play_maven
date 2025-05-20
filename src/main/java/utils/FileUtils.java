package utils;

// Import necessari per la gestione dei file e delle operazioni di I/O
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import model.User;

/**
 * Classe di utilità per la gestione dei file del gioco.
 * Questa classe fornisce metodi statici per:
 * - Gestione dei risultati del gioco (lettura/scrittura)
 * - Gestione degli utenti (autenticazione e registrazione)
 * - Lettura delle risposte corrette per gli esercizi
 * - Gestione dei file CSV per il salvataggio dei dati
 * 
 * La classe utilizza file CSV per memorizzare sia i risultati del gioco
 * che le informazioni degli utenti, garantendo la persistenza dei dati
 * tra le sessioni di gioco.
 */
public class FileUtils {
    
    /** File per il salvataggio dei risultati del gioco in formato CSV */
    private static final String RESULTS_FILE = "game_results.csv";
    
    /** File per il salvataggio delle credenziali degli utenti */
    private static final String USERS_FILE = "users.csv";
    
    /** Percorso base per i file degli esercizi */
    private static final String EXERCISES_PATH = "src/main/resources/exercises/";
    
    /**
     * Scrive i risultati del gioco in un file CSV.
     * Questo metodo salva i progressi del giocatore per un determinato esercizio,
     * aggiornando il file dei risultati. Se esiste già un risultato per lo stesso
     * giocatore ed esercizio, viene mantenuto il punteggio più alto.
     * 
     * @param playerName Nome del giocatore che ha completato l'esercizio
     * @param exerciseName Nome dell'esercizio completato
     * @param completionPercentage Percentuale di completamento dell'esercizio (0-100)
     */
    public static void writeGameResults(String playerName, String exerciseName, double completionPercentage) {
        try {
            // Usa il metodo per aggiornare i risultati
            updateCompletionPercentage(playerName, exerciseName, completionPercentage);
        } catch (Exception e) {
            System.err.println("Errore durante la scrittura dei risultati: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Legge i risultati del gioco da un file CSV.
     * @return Array di stringhe contenenti i risultati
     */
    public static String[] readGameResults() {
        try {
            if (!Files.exists(Paths.get(RESULTS_FILE))) {
                return new String[0];
            }
            
            return Files.readAllLines(Paths.get(RESULTS_FILE)).toArray(new String[0]);
        } catch (IOException e) {
            System.err.println("Errore durante la lettura dei risultati: " + e.getMessage());
            e.printStackTrace();
            return new String[0];
        }
    }
    
    /**
     * Legge un utente dal file degli utenti.
     * @param username Nome utente
     * @return Oggetto User o null se non trovato
     */
    public static User readUserFromFile(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length >= 2 && userData[0].equals(username)) {
                    return new User(userData[0], userData[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Scrive un utente nel file degli utenti.
     * @param user Oggetto User da scrivere
     */
    public static void writeUserToFile(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(user.getUsername() + "," + user.getPassword());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Aggiorna la percentuale di completamento per un utente e un esercizio.
     * @param username Nome utente
     * @param gameName Nome dell'esercizio
     * @param percentage Percentuale di completamento
     */
    private static void updateCompletionPercentage(String username, String gameName, double percentage) {
        String line;
        List<String[]> data = new ArrayList<>();
        boolean updated = false;
        boolean fileExists = new File(RESULTS_FILE).exists();

        // Se il file non esiste, crea una nuova lista con intestazione
        if (!fileExists) {
            data.add(new String[]{"PlayerName", "ExerciseName", "CompletionPercentage"});
        } else {
            // Leggi i dati esistenti
            try (BufferedReader br = new BufferedReader(new FileReader(RESULTS_FILE))) {
                while ((line = br.readLine()) != null) {
                    String[] row = line.split(",");
                    if (row.length == 3 && row[0].equals(username) && row[1].equals(gameName)) {
                        // Se esiste già una riga per lo stesso utente e gioco, aggiorna con la percentuale più alta
                        double existingPercentage = Double.parseDouble(row[2]);
                        row[2] = String.valueOf(Math.max(existingPercentage, percentage)); // Mantieni la percentuale più alta
                        updated = true;
                    }
                    data.add(row); // Aggiungi la riga ai dati
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Se non è stato aggiornato, aggiungi una nuova voce
        if (!updated) {
            data.add(new String[]{username, gameName, String.valueOf(percentage)});
        }

        // Scrivi i dati aggiornati nel file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RESULTS_FILE))) {
            for (String[] row : data) {
                // Solo scrivi righe valide con 3 campi
                if (row.length == 3) {
                    bw.write(String.join(",", row));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Legge le risposte corrette dal file corrispondente all'esercizio e al livello di difficoltà.
     * @param exName Nome dell'esercizio (es. "Fill")
     * @param difficulty Livello di difficoltà (Easy, Medium, Hard)
     * @return Array delle risposte corrette
     */
    public static String[] readCorrectAnswers(String exName, String difficulty) {
        String fileName = exName + difficulty + ".csv";
        String filePath = "/exercises/" + fileName;
        List<String> answers = new ArrayList<>();
        
        try (InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(filePath.substring(1))) {
            if (is == null) {
                // Fallback to relative path
                String relativePath = EXERCISES_PATH + fileName;
                File file = new File(relativePath);
                if (file.exists()) {
                    System.out.println("File found at absolute path");
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            if (!line.isEmpty()) {
                                System.out.println("Read line: " + line);
                                answers.add(line);
                            }
                        }
                    }
                } else {
                    System.err.println("File non trovato neanche con percorso assoluto");
                    return new String[0];
                }
            } else {
                // Read from classpath
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        System.out.println("Read line: " + line);
                        answers.add(line);
                    }
                }
            }
            
            System.out.println("Total answers read: " + answers.size());
            return answers.toArray(new String[0]);
            
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file: " + filePath);
            e.printStackTrace();
            return new String[0];
        }
    }
    
    // Definisci una costante per il percorso del file dei risultati
    private static final String RESULTS_FILE_PATH = "results.csv";
    
    /**
     * Scrive i risultati del gioco su un file CSV, includendo il livello di difficoltà.
     * 
     * @param playerName Il nome del giocatore
     * @param exerciseName Il nome dell'esercizio
     * @param difficulty Il livello di difficoltà
     * @param completionPercentage La percentuale di completamento
     * @throws IOException Se si verifica un errore durante la scrittura del file
     */
    public static void writeGameResultsWithDifficulty(String playerName, String exerciseName, String difficulty, double completionPercentage) throws IOException {
        File file = new File(RESULTS_FILE_PATH);
        
        // Crea il file se non esiste
        if (!file.exists()) {
            System.out.println("Il file results.csv non esiste, lo creo in: " + RESULTS_FILE_PATH);
            file.createNewFile();
        } else {
            System.out.println("File results.csv trovato in: " + RESULTS_FILE_PATH);
        }
        
        // Leggi il contenuto esistente
        List<String> lines = new ArrayList<>();
        if (file.length() > 0) {
            lines = Files.readAllLines(file.toPath());
            System.out.println("Lette " + lines.size() + " righe dal file results.csv");
        }
        
        // Controlla se esiste già una riga per questo utente, esercizio e difficoltà
        boolean updated = false;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");
            if (parts.length >= 4 && 
                parts[0].equals(playerName) && 
                parts[1].equals(exerciseName) && 
                parts[2].equals(difficulty)) {
                // Aggiorna solo se la nuova percentuale è maggiore
                double existingPercentage = Double.parseDouble(parts[3]);
                if (completionPercentage > existingPercentage) {
                    lines.set(i, playerName + "," + exerciseName + "," + difficulty + "," + completionPercentage);
                    System.out.println("Aggiornato record esistente per " + playerName + ", " + exerciseName + ", " + difficulty);
                }
                updated = true;
                break;
            }
        }
        
        // Se non è stato aggiornato, aggiungi una nuova riga
        if (!updated) {
            String newLine = playerName + "," + exerciseName + "," + difficulty + "," + completionPercentage;
            lines.add(newLine);
            System.out.println("Aggiunta nuova riga per " + playerName + ", " + exerciseName + ", " + difficulty);
        }
        
        // Scrivi tutte le righe nel file
        Files.write(file.toPath(), lines);
        
        System.out.println("Risultati salvati con successo in: " + RESULTS_FILE_PATH);
    }
    
    /**
     * Legge i risultati del gioco dal file CSV.
     * @return Lista di stringhe contenenti i risultati
     */
    public static List<String> readGameResultsWithDifficulty() {
        try {
            File file = new File(RESULTS_FILE_PATH);
            if (!file.exists()) {
                System.out.println("Il file results.csv non esiste ancora");
                return new ArrayList<>();
            }
            
            List<String> lines = Files.readAllLines(file.toPath());
            System.out.println("Lette " + lines.size() + " righe dal file results.csv");
            return lines;
        } catch (IOException e) {
            System.err.println("Errore durante la lettura dei risultati: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
