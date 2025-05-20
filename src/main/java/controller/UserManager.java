package controller;

import java.io.*;
import java.util.*;
import model.User;

/**
 * Classe che gestisce gli utenti del sistema.
 * Si occupa del caricamento, salvataggio e autenticazione degli utenti.
 * Gli utenti vengono memorizzati in un file CSV con formato username,password.
 */
public class UserManager {
    /** Lista degli utenti caricati in memoria */
    private List<User> users;
    
    /** Percorso del file dove vengono salvati gli utenti */
    private String filePath;

    /**
     * Costruttore che inizializza il gestore utenti.
     * @param filePath Percorso del file dove salvare/caricare gli utenti
     */
    public UserManager(String filePath) {
        this.filePath = filePath;
        this.users = new ArrayList<>();
        loadUsers();
    }

    /**
     * Aggiunge un nuovo utente al sistema.
     * L'utente viene aggiunto alla lista in memoria e salvato su file.
     * @param user L'utente da aggiungere
     */
    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    /**
     * Autentica un utente verificando username e password.
     * @param username Nome utente da verificare
     * @param password Password da verificare
     * @return L'utente autenticato o null se l'autenticazione fallisce
     */
    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Carica gli utenti dal file CSV nella lista in memoria.
     * Ogni riga del file rappresenta un utente nel formato username,password.
     */
    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.add(new User(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Salva tutti gli utenti dalla lista in memoria nel file CSV.
     * Sovrascrive completamente il contenuto del file.
     */
    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : users) {
                writer.write(user.getUsername() + "," + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
