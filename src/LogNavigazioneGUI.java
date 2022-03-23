import com.thoughtworks.xstream.*;
import com.thoughtworks.xstream.converters.basic.*;
import java.util.*;

/*
    La classe LogNavigazioneGUI rappresenta i log da inviare al server di log, al verificarsi di alcuni eventi
    come l'avvio o la chiusura dell'applicazione o l'interazione dell'utente con alcuni elementi dell'interfaccia grafica
*/
public class LogNavigazioneGUI {
    
    public final String nomeApplicativo;
    public final String indirizzoIP;
    public final Date timestamp;
    public final String tipologiaEvento;

    public LogNavigazioneGUI (String nomeApplicativo, String indirizzoIP, Date timestamp, String tipologiaEvento) { // 1)
        this.nomeApplicativo = nomeApplicativo;
        this.indirizzoIP = indirizzoIP;
        this.timestamp = timestamp;
        this.tipologiaEvento = tipologiaEvento;
    }
    
    /*
        Converte un oggetto LogNavigazioneGUI in una stringa XML, secondo le regole di buona progettazione XML
        (tutti gli attributi della classe possono assumere una moltitudine di valori)
    */
    public String convertiInXML() {                                                                                
        XStream xs = new XStream();
        xs.registerConverter(new DateConverter("yyyy-MM-dd HH:mm:ss", null, TimeZone.getDefault()));                // 2)
        return xs.toXML(this);
    }
}

/* Note:
    01) E' necessario utilizzare la classe java.util.Date per rappresentare il timestamp dell'evento scatenante,
        poiche' la versione XStream 1.4.7 non supporta i converter la nuova API LocalDateTime di Java 8
        (utilizzata in altre classi dell'applicazione)

    2) Configura il formato della conversione del timestamp (java.util.Date) in un oggetto String, utilizzando
        il fuso orario locale
        TimeZone e' la classe utilizzata per rappresentare i fusi orari 
        https://docs.oracle.com/javase/8/docs/api/java/util/TimeZone.html
*/