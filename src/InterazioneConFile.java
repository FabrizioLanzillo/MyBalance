
import java.io.*;
import java.nio.file.*;

/*
    InterazioneConFile si occupa di leggere e scrivere su file utilizzando la codifica ASCII
*/
public class InterazioneConFile {
    
    /*
        Il metodo caricaDaFile si occupa di leggere l'intero contenuto di un file, con il suo percorso come parametro
        e lo restituisce trasformato in un oggetto String
    */
    public static String caricaDaFile(String percorsoFile) {                                                  
        String contenutoDelFile = "";

        try {
            contenutoDelFile = new String(Files.readAllBytes(Paths.get(percorsoFile)));                         // 1)
        } 
        catch (IOException ex) {
            System.err.println("Caricamento da file " + percorsoFile + "non eseguito: " + ex.getMessage());
        }

        return contenutoDelFile;
    }
    
    /*
        Il metodo salvaSuFile si occupa di scrivere il contenuto di un oggetto passato come parametro in uno specifico
        file. La stringa ottenuta dalla conversione di oggetto puo' essere "appesa" al suddetto file (append = true) o
        puo' sovrascrivere il file (append = false)
    */
    public static void salvaSuFile(Object elementoDaSalvare, String percorsoFile, boolean daAggiungere) { 
        try {
            Files.write(
                Paths.get(percorsoFile),                                                                        // 2)
                elementoDaSalvare.toString().getBytes(),
                StandardOpenOption.CREATE,                                                                      // 3)
                (daAggiungere) ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING               // 4)
            );
        } 
        catch (IOException ex) {
            System.err.println("Salvataggio su file " + percorsoFile + "fallito: " + ex.getMessage());
        }
    }
    
}

/* Note:
    1) Legge l'intero contenuto del file
    2) Ricava il percorso del file target
    3) Crea il file se non esiste
    4) Si specifica la modalita' di scrittura in base al valore del parametro "append"
       https://docs.oracle.com/javase/8/docs/api/java/nio/file/StandardOpenOption.html
*/