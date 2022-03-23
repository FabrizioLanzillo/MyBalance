import com.thoughtworks.xstream.*;
import java.util.*;
import javafx.scene.paint.*;

/*
    ParametriDiConfigurazione e' la classe che preleva i parametri inseriti dall'utente
    nel file di configurazione XML e li rende disponibili alle classi frontend 
    e middleware. La classe presenta una serie di attributi statici di default,
    il cui valore verra' usato nel caso di eventuali parametri di configurazione 
    mancanti, secondo le opzioni scelte per la configurazione
*/
public class ParametriDiConfigurazione { // 00)

    private static final int DEFAULT_NUMERO_RIGHE_TABELLA = 5;
    private static final int DEFAULT_PERIODO_IN_ESAME_INIZIALE = 60;
    private static final String[] DEFAULT_ARRAY_CATEGORIE_SPESA = {
                                                                    "Abbigliamento",
                                                                    "Animali domestici",
                                                                    "Casa & Giardino",
                                                                    "Generi alimentari",
                                                                    "Hobbies e tempo libero",
                                                                    "Istruzione",
                                                                    "Mezzi Di Transporto",
                                                                    "Sanità",
                                                                    "Sport",
                                                                    "Tasse",
                                                                    "Utenze",
                                                                    "Varie",
                                                                    "Veicoli"
                                                                  };

    private static final String[] DEFAULT_ARRAY_METODI_DI_PAGAMENTO = {
                                                                        "Contanti",
                                                                        "Banca #1",
                                                                        "Banca #2"
                                                                      };

    private static final int DEFAULT_NUMERO_CATEGORIE_GRAFICO = 5;
    private static final Color[] DEFAULT_COLORI_GRAFICO = {
                                                            new Color(1, 0, 0, 1),	// RED
                                                            new Color(0, 1, 0, 1),	// LIME
                                                            new Color(0, 0, 1, 1),	// BLUE
                                                            new Color(1, 1, 0, 1),	// YELLOW
                                                            new Color(1, 0.64705884, 0, 1), // ORANGE
                                                            new Color(0.5019608, 0, 0.5019608, 1) // PURPLE
                                                          };

    private static final Color DEFAULT_COLORE_CARICA_DATI_SELEZIONA_PERIODO = Color.rgb(0, 204, 51); // BLUE
    private static final Color DEFAULT_COLORE_INSERISCI = new Color(0, 0.5019608, 0, 1); // GREEN
    private static final Color DEFAULT_COLORE_ELIMINA = new Color(0, 0, 0, 1); // BLACK
    private static final Color DEFAULT_COLORE_ANNULLA = new Color(1, 0, 0, 1); // RED
    private static final Color DEFAULT_COLORE_CAMBIA_PERIODO = new Color(1, 0.64705884, 0, 1); // ORANGE
    private static final Color DEFAULT_COLORE_RIGA_DI_INSERIMENTO = new Color(1, 1, 0.8784314, 1); // LIGHT YELLOW
    private static final String DEFAULT_INDIRIZZO_IP_LOG = "127.0.0.1";
    private static final int DEFAULT_PORTA_LOG = 8080;
    private static final String DEFAULT_INDIRIZZO_IP_DATABASE = "127.0.0.1";
    private static final int DEFAULT_PORTA_DATABASE = 3306;
    private static final String DEFAULT_NOME_DATABASE = "my_balance";
    private static final String DEFAULT_USERNAME_DATABASE = "root";
    private static final String DEFAULT_PASSWORD_DATABASE = "";

    public int numeroRigheTabella;
    public int periodoInEsameleIniziale;
    public String[] arrayCategorieSpesa;
    public String[] arrayMetodiDiPagamento;
    public int numeroCategorieGrafico;
    public Color[] coloriGrafico;
    public Color coloreCaricaDatiSelezionaPeriodo;
    public Color coloreInserisci;
    public Color coloreElimina;
    public Color coloreAnnulla;
    public Color coloreCambiaPeriodo;
    public Color coloreRigaDiInserimento;
    public String indirizzoIpLog;
    public int portaLog;
    public String indirizzoIpDatabase;
    public int portaDatabase;
    public String nomeDatabase;
    public String usernameDatabase;
    public String passwordDatabase;

    /*
        Costruisce un oggetto ParametriDiConfigurazione a partire da una stringa
        XML gia' validata (passata come parametro)
    */
    public ParametriDiConfigurazione (String configurazioneXML) { 
        if (configurazioneXML != null && !configurazioneXML.equals("")) {
            ParametriDiConfigurazione parametri = (ParametriDiConfigurazione)creaXStream().fromXML(configurazioneXML); // 1)
            numeroRigheTabella = parametri.numeroRigheTabella;
            periodoInEsameleIniziale = parametri.periodoInEsameleIniziale; 
            arrayCategorieSpesa = parametri.arrayCategorieSpesa;
            arrayMetodiDiPagamento = parametri.arrayMetodiDiPagamento;
            numeroCategorieGrafico = parametri.numeroCategorieGrafico;
            coloriGrafico = parametri.coloriGrafico;
            coloreCaricaDatiSelezionaPeriodo = parametri.coloreCaricaDatiSelezionaPeriodo;
            coloreInserisci = parametri.coloreInserisci;
            coloreElimina = parametri.coloreElimina;
            coloreAnnulla = parametri.coloreAnnulla;
            coloreCambiaPeriodo = parametri.coloreCambiaPeriodo;
            coloreRigaDiInserimento = parametri.coloreRigaDiInserimento;
            indirizzoIpLog = parametri.indirizzoIpLog;
            portaLog = parametri.portaLog;
            indirizzoIpDatabase = parametri.indirizzoIpDatabase;
            portaDatabase = parametri.portaDatabase;
            nomeDatabase = parametri.nomeDatabase;
            usernameDatabase = parametri.usernameDatabase;
            passwordDatabase = parametri.passwordDatabase;
        }

        impostaParametriDiDefault();
    }
    
    /*
        Ricerca se vi sono dei parametri non inizializzati nel file di configurazione, ovvero che sono rimasti
        al valore Java di default. In tal caso, inizializza l'attributo con il valore
        del parametro statico di default. Se la stringa xml non era valida,
        tutti gli attributi verranno settati
    */
    private void impostaParametriDiDefault () { 
            if (numeroRigheTabella == 0) {
                    numeroRigheTabella = DEFAULT_NUMERO_RIGHE_TABELLA;
            }
            if (periodoInEsameleIniziale == 0) {
                    periodoInEsameleIniziale = DEFAULT_PERIODO_IN_ESAME_INIZIALE;
            }
            if (arrayCategorieSpesa == null) {
                    arrayCategorieSpesa = Arrays.copyOf(DEFAULT_ARRAY_CATEGORIE_SPESA, DEFAULT_ARRAY_CATEGORIE_SPESA.length);
            }
            if (arrayMetodiDiPagamento == null) {
                    arrayMetodiDiPagamento = Arrays.copyOf(DEFAULT_ARRAY_METODI_DI_PAGAMENTO, DEFAULT_ARRAY_METODI_DI_PAGAMENTO.length);
            }
            if (numeroCategorieGrafico == 0) {
                    numeroCategorieGrafico = DEFAULT_NUMERO_CATEGORIE_GRAFICO;
            }
            if (coloriGrafico == null) {
                    coloriGrafico = Arrays.copyOf(DEFAULT_COLORI_GRAFICO, DEFAULT_COLORI_GRAFICO.length);
            }
            if (coloreCaricaDatiSelezionaPeriodo == null) {
                    coloreCaricaDatiSelezionaPeriodo = DEFAULT_COLORE_CARICA_DATI_SELEZIONA_PERIODO;
            }
            if (coloreInserisci == null) {
                    coloreInserisci = DEFAULT_COLORE_INSERISCI;
            }
            if (coloreElimina == null) {
                    coloreElimina = DEFAULT_COLORE_ELIMINA;
            }
            if (coloreAnnulla == null) {
                    coloreAnnulla = DEFAULT_COLORE_ANNULLA;
            }
            if (coloreCambiaPeriodo == null) {
                    coloreCambiaPeriodo = DEFAULT_COLORE_CAMBIA_PERIODO;
            }
            if (coloreRigaDiInserimento == null) {
                    coloreRigaDiInserimento = DEFAULT_COLORE_RIGA_DI_INSERIMENTO;
            }
            if (indirizzoIpLog == null) {
                    indirizzoIpLog = DEFAULT_INDIRIZZO_IP_LOG;
            }
            if (portaLog == 0) {
                    portaLog = DEFAULT_PORTA_LOG;
            }
            if (indirizzoIpDatabase == null) {
                    indirizzoIpDatabase = DEFAULT_INDIRIZZO_IP_DATABASE;
            }
            if (portaDatabase == 0) {
                    portaDatabase = DEFAULT_PORTA_DATABASE;
            }
            if (nomeDatabase == null) {
                    nomeDatabase = DEFAULT_NOME_DATABASE;
            }
            if (usernameDatabase == null) {
                    usernameDatabase = DEFAULT_USERNAME_DATABASE;
            }
            if (passwordDatabase == null) {
                    passwordDatabase = DEFAULT_PASSWORD_DATABASE;
            }
    }
    
    /*
        Crea un flusso XStream per la conversione da XML a ParametriDiConfigurazione e viceversa
    */
    public static XStream creaXStream() {
            return new XStream();
    }
    
    /*
        Converte l'oggetto ParametriDiConfigurazione in XML
    */
    public String convertiInStringa() { 
            return creaXStream().toXML(this);
    }
}

/* Note:

    1) Se la stringa e' valida, converte la stringa XML in un oggetto ParametriDiConfigurazione
        ed effettua la copia elemento a elemento di tutti gli attributi

*/