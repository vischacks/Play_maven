


          
# Play - Progetto di programmazione 2024/2025


[![Java Version](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build: Maven](https://img.shields.io/badge/Build-Maven-green.svg)](https://maven.apache.org/)

=======

## Descrizione
Play è un'applicazione educativa progettata per aiutare gli studenti ad apprendere la programmazione Java attraverso esercizi interattivi. L'applicazione offre tre tipi di esercizi con diversi livelli di difficoltà:

1. **Completamento di Spazi** - Completa il codice mancante in un programma Java
2. **Risposte a Domande** - Fornisci l'output corretto di un frammento di codice Java
3. **Debug del Codice** - Identifica e correggi gli errori in un frammento di codice Java
4. **Quiz** - Rispondi correttamente alle domande

## Caratteristiche

- 🎨 Interfaccia utente intuitiva basata su JavaFX
- 🎯 Tre livelli di difficoltà per ogni tipo di esercizio (Facile, Medio, Difficile)
- 📊 Tracciamento dei progressi dell'utente
- 💾 Salvataggio dei risultati in formato CSV
- 🔄 Navigazione semplice tra gli esercizi

## Requisiti di Sistema
- Java 17 o superiore
- Maven 3.8+ per la gestione delle dipendenze
- Sistema operativo: Windows 10/11, macOS 10.15+, o Linux (kernel 5.0+)
- Memoria RAM: minimo 4GB
- Spazio su disco: 500MB liberi
=======
- Interfaccia utente intuitiva basata su JavaFX
- Tre livelli di difficoltà per ogni tipo di esercizio (Facile, Medio, Difficile)
- Tracciamento dei progressi dell'utente
- Salvataggio dei risultati in formato CSV
- Navigazione semplice tra gli esercizi

## Requisiti di Sistema
- Java 11 o superiore
- Maven per la gestione delle dipendenze
>>>>>>> f1ba8e097e30858065fa6b1d47e09206419a05a0

## Installazione
1. Clona il repository:
   ```bash
   git clone https://github.com/vischacks/Play_maven.git
   ```
2. Naviga nella directory del progetto:
   ```bash
   cd Play
   ```
3. Compila il progetto con Maven:
   ```bash
   mvn clean compile
   ```
4. Esegui l'applicazione:
   ```bash
   mvn javafx:run
   ```

## Struttura del Progetto
- `src/main/java/controller/` - Controller per le diverse viste dell'applicazione
- `src/main/java/model/` - Classi del modello dati
- `src/main/java/utils/` - Classi di utilità
- `src/main/resources/view/` - File FXML per l'interfaccia utente
- `src/main/resources/exercises/` - File CSV contenenti le risposte corrette per gli esercizi

<<<<<<< HEAD
### Struttura dei File CSV
I file CSV degli esercizi seguono questo formato:
```csv
ID,Domanda,Risposta,Difficoltà,Tipo
1,"Qual è l'output del codice?","Hello World",1,"quiz"
```

=======

## Come Utilizzare
1. Avvia l'applicazione
2. Inserisci il tuo nome utente
3. Seleziona un tipo di esercizio dalla schermata principale
4. Scegli un livello di difficoltà
5. Completa gli esercizi e verifica le tue risposte
6. Visualizza il tuo progresso nella barra di completamento


## Risoluzione dei Problemi Comuni
- **L'applicazione non si avvia**: Verifica di avere Java 17 installato (`java -version`)
- **Errori di compilazione**: Assicurati di avere Maven correttamente configurato
- **File CSV non trovati**: Controlla che la directory `exercises` contenga i file necessari
- **Problemi di JavaFX**: Verifica che il modulo JavaFX sia incluso nel classpath

=======
## Contribuire
Siamo aperti a contributi! Se desideri contribuire al progetto, segui questi passaggi:
1. Fai un fork del repository
2. Crea un branch per la tua feature (`git checkout -b feature/NuovaFeature`)
3. Commit delle tue modifiche (`git commit -m 'Aggiunta nuova feature'`)
4. Push al branch (`git push origin feature/NuovaFeature`)
5. Apri una Pull Request

<<<<<<< HEAD
### Linee Guida per il Contributo
- Segui le convenzioni di codice Java
- Aggiungi test unitari per le nuove funzionalità
- Aggiorna la documentazione quando necessario
- Usa commit semantici

=======

## Licenza
Questo progetto è distribuito con licenza MIT. Vedi il file `LICENSE` per maggiori dettagli.

## Autori
- Giacomo Viscardi
- Elia Cialdini
- Lorenzo Dolceamore


