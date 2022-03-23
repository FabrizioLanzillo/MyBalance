import java.io.*;
import java.net.*;

/*
    ServerLogNavigazioneGUI ha il compito di ricevere, validare e (se validi) memorizzare i log inviati dall' applicativi client MyBalance. 
    I log vengono ricevuti in formato XML. 
    Questi log vengono raccolti per permettere ai progettisti software di valutare le prestazioni 
    e la facilita' d'uso dell' interfaccia grafica.
*/
public class ServerLogNavigazioneGUI {
    
    private static final int PORTA = 8080; // 1)
    private static final String FILE_DI_LOG = "../files/server_log.txt"; // 2)
    private static final String SCHEMA_XML_LOG = "../files/schema_di_log.xsd"; // 3)
    
    
    public static void main(String args[]) {
        System.out.println("Server per la ricezione di file Log XML avviato correttamente\n");

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) { // 4)
            while (true) {
                try (Socket socket = serverSocket.accept(); // 5)
                     DataInputStream input = new DataInputStream(socket.getInputStream());){    // 6)
                        
                    String logXML = input.readUTF(); // 7)
                    System.out.println("Log XML ricevuto con successo \n" + logXML + "\n"); 
                    if (ValidatoreXML.valida(logXML, SCHEMA_XML_LOG)) { // 8)
                        InterazioneConFile.salvaSuFile(logXML + "\n", FILE_DI_LOG, true);
                    }
                }
            }
        } 
        catch(IOException ex) {
            System.err.println("Errore Rilevato: " + ex.getMessage() + "\n");
        }
    }
}

/* Note:
    1) Porta su cui il server rimane in ascolto
    2) File dove il server memorizzera' i log XML ricevuti
    3) File di schema per la validazione dei log XML ricevuti
    4) Il server apre un socket e si mette in ascolto di connessioni in ingresso
    5) Il server accetta una connessione in ingresso. Nel caso in cui non vi siano connessioni,
            il server si blocca in attesa di una richiesta di connessione
    6) Crea uno stream per tipi primitivi, concatenato allo stream input del socket, per leggere il log inviato dal
            client come stringa
    7) Legge il log in ingresso e notifica la corretta ricezione stampando un messaggio sullo schermo
    8) Valida il log ricevuto secondo lo schema predefinito. Qualora sia valido, lo memorizza sul file txt
*/