package model;

/**
 * Classe che rappresenta un utente nel sistema.
 * Gestisce le informazioni dell'utente, incluse le credenziali di accesso
 * e mantiene traccia dell'username dell'utente attualmente autenticato.
 */
public class User {
    /** Username dell'utente attualmente autenticato nel sistema */
    private static String currentUsername;
    
    /** Username dell'utente */
    private String username;
    
    /** Password dell'utente */
    private String password;

    /**
     * Costruisce un nuovo oggetto User con le credenziali specificate.
     * Imposta anche l'username corrente del sistema.
     * 
     * @param username L'username dell'utente
     * @param password La password dell'utente
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        currentUsername = username;
    }

    /**
     * Restituisce l'username dell'utente attualmente autenticato nel sistema.
     * 
     * @return L'username corrente
     */
    public static String getUsername() {
        return currentUsername;
    }

    /**
     * Restituisce la password dell'utente.
     * 
     * @return La password dell'utente
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta un nuovo username per l'utente e aggiorna anche
     * l'username corrente del sistema.
     * 
     * @param username Il nuovo username da impostare
     */
    public void setUsername(String username) {
        this.username = username;
        currentUsername = username;
    }

    /**
     * Imposta una nuova password per l'utente.
     * 
     * @param password La nuova password da impostare
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
