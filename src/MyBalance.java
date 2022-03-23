import java.time.*;
import java.util.*;
import javafx.application.*;
import javafx.util.*;
import javafx.scene.*;
import javafx.stage.*;

/*
    MyBalance e' il controller applicativo: estrae e valida i parametri di configurazione,
    instanzia e configura il gestore del database e il gestore degli eventi di log,
    costruisce l'interfaccia grafica e la inizializza con i dati della cache, configura un
    apposito handler per il salvataggio della cache.
    Inoltre offre alle classi di frontend, metodi per la corretta interazione con il database e con i log
*/
public class MyBalance extends Application {
    
    private static final String NOME_APPLICAZIONE = "MyBalance";
    private static final String FILE_DI_CONFIGURAZIONE = "files/configurazione.xml";
    private static final String SCHEMA_FILE_DI_CONFIGURAZIONE_XML = "files/schema_di_configurazione.xsd";
    
    public static final int LARGHEZZA_SCENA = 1120;
    public static final int ALTEZZA_SCENA = 900;
    
    private InterfacciaMyBalance interfacciaGrafica;
    
    private ParametriDiConfigurazione parametriDiConfigurazione;
    private EventoNavigazioneGUI eventoNavigazioneGUI;
    private GestoreDatabase gestoreDatabase;     
    
    private Scene scene;
    
    @Override
    /*
        Il metodo start e' il primo metodo applicativo che viene eseguito. Si occupa delle operazioni di
        inizializzazione dell'applicazione
    */
    public void start(Stage stage) {
       
        String cofigurazioneXML = InterazioneConFile.caricaDaFile(FILE_DI_CONFIGURAZIONE);                      // 1)
        parametriDiConfigurazione = new ParametriDiConfigurazione(                                              // 2)
            ValidatoreXML.valida(cofigurazioneXML, SCHEMA_FILE_DI_CONFIGURAZIONE_XML)?cofigurazioneXML : null   // 3)
        );
        
        gestoreDatabase = new GestoreDatabase(parametriDiConfigurazione.nomeDatabase, parametriDiConfigurazione.indirizzoIpDatabase, 
				parametriDiConfigurazione.portaDatabase, parametriDiConfigurazione.usernameDatabase, parametriDiConfigurazione.passwordDatabase); // 4)
        eventoNavigazioneGUI = new EventoNavigazioneGUI(parametriDiConfigurazione.indirizzoIpLog, parametriDiConfigurazione.portaLog);                            // 4)

        Group root = new Group();
    
        interfacciaGrafica = new InterfacciaMyBalance(root, this, parametriDiConfigurazione, NOME_APPLICAZIONE);    // 5)
        GestoreCache.estraiDatiSpesaNonArchiaviataDallaCache(interfacciaGrafica);                                   // 6)
        aggiornaDatiSpeseInterfaccia();                                                                             // 7)
        
        inviaEventoNavigazione("AVVIO");
        stage.setOnCloseRequest((WindowEvent we) -> {                                                               // 8)
                                    inviaEventoNavigazione("TERMINE");
                                    GestoreCache.salvaDatiSpesaNonArchiaviataEstrattiNellaCache(interfacciaGrafica);
				});

        stage.setTitle(NOME_APPLICAZIONE);
        scene = new Scene(root, LARGHEZZA_SCENA, ALTEZZA_SCENA);
        stage.setScene(scene);
        stage.show();
    
    }
    
    /*
        Aggiorna i dati mostrati nell'interfaccia grafica con le spese effettuate dall'utente
        nel periodo da lui selezionato
    */
    public void aggiornaDatiSpeseInterfaccia() { 
       
        LocalDate dataDiInizio = interfacciaGrafica.getSettoreCambioPeriodoTemporale().getDataDiInizio();
        LocalDate dataDiFine = interfacciaGrafica.getSettoreCambioPeriodoTemporale().getDataDiFine();
        String nomeUtente = interfacciaGrafica.getSettoreCaricaDati().getUtente();                          // 9)
        
        List<DatiSpesa> spese = gestoreDatabase.richiestaDatiSpesa(nomeUtente, dataDiInizio, dataDiFine);   
        interfacciaGrafica.getTabellaSpese().caricaSpese(spese);                                            // 10)

        double totaleSpese = 0;
        double contanti = 0;
        double banca1 = 0;
        double banca2 = 0;
        for (DatiSpesa dati : spese) { 
            if(dati.getMetodoDiPagamento().equals("Contanti")){
                contanti += dati.getImporto();
            }
            else if(dati.getMetodoDiPagamento().equals("Banca #1")){
                banca1 += dati.getImporto();   
            }
            else{
                banca2 += dati.getImporto();   
            }
            totaleSpese += dati.getImporto();
        }
        interfacciaGrafica.aggiornaSettoreContabileSpese(contanti, banca1, banca2, totaleSpese);            // 11)
          
        List<Pair<String, Double>> elencoCategoriePerGrafico = 
            gestoreDatabase.calcoloSpesePerCategoria(
                            nomeUtente, dataDiInizio, dataDiFine, parametriDiConfigurazione.numeroCategorieGrafico  // 12)
            ); 
        double totaleSpeseCategorieMostrateInGrafico = 0;
        for (Pair<String, Double> coppia : elencoCategoriePerGrafico) {                                             // 13)
                totaleSpeseCategorieMostrateInGrafico += coppia.getValue();
        }
        if (totaleSpese > totaleSpeseCategorieMostrateInGrafico) { 
            Pair<String, Double> altro = new Pair<>("Altre Categorie", totaleSpese - totaleSpeseCategorieMostrateInGrafico); 
            elencoCategoriePerGrafico.add(altro);
        }

        interfacciaGrafica.getGraficoDelleCategorie().aggiornaGrafico(elencoCategoriePerGrafico, totaleSpese);     // 14)
  
    }
    
    /*
        Inserisce una nuova spesa nel database e aggiorna l'interfaccia Grafica
    */
    public void inserisciNuovaSpesa(DatiSpesa spesa){
        gestoreDatabase.inserimentoDatiNuovaSpesa(interfacciaGrafica.getSettoreCaricaDati().getUtente(), spesa.getCategoria(),
                                                  spesa.getMetodoDiPagamento(), spesa.getData(), spesa.getImporto(), spesa.getDescrizione());
        aggiornaDatiSpeseInterfaccia();
        
    }
    
    /*
        Elimina i Dati di una spesa (il cui id e' passato come parametro) effettuata dall'utente 
        ed aggiorna l'interfaccia grafica
    */
    public void rimuoviSpesa (DatiSpesa spesa) {
        gestoreDatabase.rimozioneDatiSpesa(interfacciaGrafica.getSettoreCaricaDati().getUtente(),spesa.getIdSpesa());
        aggiornaDatiSpeseInterfaccia();
    }
    
    /*
        Invia un log di evento al server log
    */
    public void inviaEventoNavigazione(String eventoNavigazione){
        eventoNavigazioneGUI.inviaLogNavigazioneGUIAlServer(NOME_APPLICAZIONE, eventoNavigazione);
    }
	
}


/* Note:
    1) Legge il file XML di configurazione e lo memorizza su una stringa
    2) Costruisce un oggetto ParametriDiConfigurazione dal contenuto del file XML
    3) Valida il file XML di configurazione. In caso sia valido lo passa al costruttore di ParametriDiConfigurazione
    4) Instanzia il gestore del database e il gestore dei log, inizializzandoli con alcuni parametri di configurazioni
    5) Costruisce l'interfaccia grafica. Passa al costruttore un riferimento ad un oggetto Group nel quale
        verranno inseriti da parte dell'interfaccia gli elementi grafici da mostrare sullo schermo
    6) Carica i dati memorizzati in cache sull' interfaccia grafica
    7) Se in cache e' presente il nomeutente, carica sulla schermata iniziale i dati dell'utente inserito.
        Altrimenti mostra la tabella e il grafico vuoti
    8) Configura le operazioni da svolgere alla chiusura dell'applicazione: invio del log di CHIUSURA e
        salvataggio in cache dei dati non ancora archiviati
    9) Preleva dal'interfaccia grafica il nome utente e le date di inizio e fine del periodo da considerare, necessari
        per poter interrogare il database
    10) Recupera dal database le spese dell'utente e le inserisce nella Tabella Spese
    11) Calcola il totale delle spese effettuate dall'utente nel periodo oltre a quelle differenziate
        per metodo di pagamento e le inserisce negli appositi display presenti nell'interfaccia.	
    12) Estrae dal database le coppie Categoria-Spese per categoria relative alle spese effettuate dall'utente
        nel periodo al fine di configurare il grafico a torta.

        Pair<K,V> e' una classe Generics usata per rappresentare in maniera	conveniente 
        coppie chiave-valore
        https://docs.oracle.com/javafx/2/api/javafx/util/Pair.html
    13) Nel grafico a torta vengono mostrate solo un numero ben preciso (configurabile) di categorie. Le altre vengono
        accorpate in una categoria "Altre Categorie".
        Questo for calcola il totale delle spese mostrate, per poter ricavare successivamente le spese appartenenti
        ad "Altre Categorie".
    14) Aggiorna il grafico a torta con i dati ricavati
*/