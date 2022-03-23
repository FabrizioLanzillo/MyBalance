import java.io.*;
import java.time.*;

/*
    La classe GestoreCache si occupa di gestire le operazioni di salvataggio in cache e di caricamento dalla cache
    dei dati inseriti dall'utente e ancora non salvati nell'interfaccia
*/
public class GestoreCache {
    
    private static final String PERCORSO_FILE_CACHE = "files/cache.bin";
    
    /*
        Metodo che estrae i dati della cache e li carica nell'interfaccia grafica
    */
    public static void estraiDatiSpesaNonArchiaviataDallaCache(InterfacciaMyBalance interfacciaGrafica) { 
        
        Cache cache;

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(PERCORSO_FILE_CACHE))){  // 0)
            cache = (Cache)input.readObject();                                                            // 1)
        } 
        catch (IOException | ClassNotFoundException ex) {
            System.err.println("Estrazione Dati dalla cache fallito: " + ex.getMessage());
            return;
        }
        caricaDatiSpesaNonArchiaviataEstrattiDallaCache(interfacciaGrafica, cache);
    }
    
    /*
        Metodo che carica sui vari elementi grafici dell'interfaccia i dati letti dall'oggetto Cache
    */
    private static void caricaDatiSpesaNonArchiaviataEstrattiDallaCache(InterfacciaMyBalance interfacciaGrafica, Cache cache) { 
        
        interfacciaGrafica.getSettoreCaricaDati().setUtente(cache.nomeUtente);                                                  // 2)
        DatiSpesa spesa = new DatiSpesa(0, cache.categoriaSpesaNonArchiviata, cache.metodoDiPagamentoNonArchiviato, 
                                        cache.dataNonArchiviata, cache.importoNonArchiviato, cache.descrizioneNonArchiviata);   // 3)
        interfacciaGrafica.getRigaDiInserimento().setSpesaInserita(spesa);                                                      // 4)
        interfacciaGrafica.getSettoreCambioPeriodoTemporale().setDataDiInizio(cache.dataDiInizioPeriodoNonArchiviata);          // 5)
        interfacciaGrafica.getSettoreCambioPeriodoTemporale().setDataDiFine(cache.dataDiFinePeriodoNonArchiviata);              // 5)
    }
    
    /*
        Metodo che si occuoa di salvare in cache i dati inseriti dall'utente nell'interfaccia grafica
    */
    public static void salvaDatiSpesaNonArchiaviataEstrattiNellaCache(InterfacciaMyBalance interfacciaGrafica) {               
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(PERCORSO_FILE_CACHE))){                    // 6)
            output.writeObject(creaCache(interfacciaGrafica));                                                                  // 7)
        } 
        catch (IOException ex) {
            System.err.println("Salvataggio dei dati nella cache fallito: " + ex.getMessage());
        }
    }
    
    /*
        Metodo che crea un oggetto Cache, prelevando le informazioni necessarie dal'interfaccia Grafica
    */
    public static Cache creaCache(InterfacciaMyBalance interfacciaGrafica){                                                     
        
        DatiSpesa spesa = interfacciaGrafica.getRigaDiInserimento().getSpesaInserita();                                         // 8)
        SettoreCambioPeriodoTemporale settoreCambioPeriodo = interfacciaGrafica.getSettoreCambioPeriodoTemporale();             // 9)
        
        String nomeUtente = interfacciaGrafica.getSettoreCaricaDati().getUtente();                                              // 10)
        String categoriaSpesaNonArchiviata = spesa.getCategoria();
        String metodoDiPagamentoNonArchiviato = spesa.getMetodoDiPagamento();
        LocalDate dataNonArchiviata = spesa.getData();
        double importoNonArchiviato = spesa.getImporto();
        String descrizioneSpesaInserita = spesa.getDescrizione();
        LocalDate dataDiInizioPeriodoNonArchiviata = settoreCambioPeriodo.getDataDiInizio();
        LocalDate dataDiFinePeriodoNonArchiviata = settoreCambioPeriodo.getDataDiFine();

        return new Cache(nomeUtente, categoriaSpesaNonArchiviata, metodoDiPagamentoNonArchiviato, dataNonArchiviata, importoNonArchiviato,
                                        descrizioneSpesaInserita, dataDiInizioPeriodoNonArchiviata, dataDiFinePeriodoNonArchiviata);
        
    }
}


/* Note:
    0) Apre uno strem oggetto di input per leggere un oggetto Cache serializzato su file
    1) Legge l'oggetto dal file binario 
    2) Carica il nome utente nel campo input del settore carica dati
    3) Crea un oggetto DatiSpesa con i dati contenuti nella cache
    4) passa l'oggetto precedente alla riga di inserimento cosi' che possa aggiornare i campi con i dati della cache
    5) Inizializza le date di inizio e fine del periodo temporale
    6) Apre uno strem oggetto di output per scrivere un oggetto Cache su file
    7) Serializza l'oggetto Cache scrivendolo su file
    8) Preleva i dati inseriti nella riga di Inserimento come oggetto DatiSpesa e ne preleva i singoli attributi
    9) Prelava la date di inizio e di fine del periodo temporale impostate dall'utente
    10) Preleva il nome dell'utente
*/