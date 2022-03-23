import java.io.*;
import java.net.*;
import java.util.*;

/*
    La classe EventoNavigazioneGUI si occupa di inviare i lo in formato XML ad un server log, 
    al verificarsi di alcuni eventi scatenati da azioni come l'avvio o la chiusura dell'applicazione 
    o l'interazione dell'utente con alcuni elementi dell' interfaccia grafica come i pulsanti
*/
public class EventoNavigazioneGUI { 	
    
    private final String indirizzoIp;
    private final int porta;

    public EventoNavigazioneGUI(String indirizzoIp, int porta) {
        this.indirizzoIp = indirizzoIp;
        this.porta = porta;
    }
    
    /*
        Metodo che si occupa di inviare un log al server log
    */
    public void inviaLogNavigazioneGUIAlServer(String nomeApplicativo, String tipologiaEvento) { 
        try(Socket socketDiInvio = new Socket(indirizzoIp, porta);                               // 1)
            DataOutputStream output = new DataOutputStream(socketDiInvio.getOutputStream())){    // 2)
               
            LogNavigazioneGUI logXML = new LogNavigazioneGUI(                                    // 3)
                                                            nomeApplicativo,((InetSocketAddress)socketDiInvio.getLocalSocketAddress()).getAddress().getHostAddress(), // 4)
                                                            new Date(), tipologiaEvento
                                                            );       
            output.writeUTF(logXML.convertiInXML());                                             // 5)
        } 
        catch (IOException ex) {
            System.err.println("Impossibile inviare log XML al server di ricezione: " + ex.getMessage());
        }
    }
}

/* Note:
    1) Apre una connessione TCP con il server log, utilizzando una socket
    2) Concatena un flusso oggetto al flusso di uscita del socket, in modo da poter inviare 
       al server il log in formato XML
    3) Crea un oggetto LogNavigazioneGUI con i parametri passati dal chiamante
    4) Ottiene l'indirizzo IP in notazione puntata su cui e' stata aperto la socket di invio.
       InetSocketAddress e' la classe che implementa l'indirizzo di una socket indirizzo IP dell'host + porta)
       https://docs.oracle.com/javase/8/docs/api/java/net/InetSocketAddress.html
    5) Converte il log in una stringa XML attraverso il metodo presente in LogNavigazioneGUI e la invia sulla connessione con il server
*/
