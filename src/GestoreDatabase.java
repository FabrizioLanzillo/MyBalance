
import java.sql.*;
import java.time.*;
import java.util.*;
import javafx.util.*;

/*
    GestoreDatabase è una classe che offre un insieme di metodi utilizzati dalle classi middleware 
    per interagire con il database ed estrarre o immettere  dati necessari
*/
public class GestoreDatabase {
    
    private final String nomeDB;
    private final String indirizzoIp;
    private final int porta;
    private final String username;
    private final String password;
    
    /*
        Costruttore di GestoreDatabase
    */
    public GestoreDatabase (String nomeDB, String indirizzoIp, int porta, String username, String password) {
        
        this.nomeDB = nomeDB;
        this.indirizzoIp = indirizzoIp;
        this.porta = porta;
        this.username = username;
        this.password = password;
    }
    
    /*
        Richied al database tutti i dati di una singola spesa da parte di un utente in un periodo temporale selezionato
    */
    public List<DatiSpesa> richiestaDatiSpesa(String nomeUtente, LocalDate dataDiInizio, LocalDate dataDiFine) {                 
        List<DatiSpesa> elencoSpese = new ArrayList<>();                                                                             // 0)

        if (nomeUtente == null || nomeUtente.equals("")){
            return elencoSpese;
        }                                                                                                                            // 1)
        try (Connection connection = DriverManager.getConnection(
                                        "jdbc:mysql://" + indirizzoIp + ":" + porta + "/" + nomeDB, username, password);             // 2)
                                     PreparedStatement statement = connection.prepareStatement(
                                        "SELECT * FROM spese WHERE NomeUtente = ? AND data BETWEEN ? AND ? ORDER BY data DESC");     // 3)
            ){
            statement.setString(1, nomeUtente);
            statement.setString(2, dataDiInizio.toString());
            statement.setString(3, dataDiFine.toString());
            ResultSet result = statement.executeQuery();	

            while (result.next()){                                                                                                   // 4)
                elencoSpese.add( new DatiSpesa(result.getInt("IdSpesa"), result.getString("Categoria"), 
                                 result.getString("MetodoDiPagamento"), 
                                 result.getDate("Data").toLocalDate(), result.getDouble("Importo"), result.getString("Descrizione"))
                               );
            }
        } 
        catch (SQLException ex) {
            System.err.println("Impossibile ottenere i dati delle spese dell'utente: " + ex.getMessage());
        }
        return elencoSpese;
    }
    
    /*
        Elimina i dati inerenti ad una spesa (identificata dalla chiave NomeUtente-idSpesa) dal database
    */
    public boolean rimozioneDatiSpesa(String nomeUtente, int IdSpesa) {                                                       
        int risultatoOperazione = 0;                                                                                                 

        try (Connection connection = DriverManager.getConnection(
                                        "jdbc:mysql://" + indirizzoIp + ":" + porta + "/" + nomeDB, username, password);
                                     PreparedStatement statement = connection.prepareStatement(
                                        "DELETE FROM spese WHERE NomeUtente = ? AND IdSpesa = ?");                                   // 5)
            ){
            statement.setString(1, nomeUtente);
            statement.setInt(2, IdSpesa);
            risultatoOperazione = statement.executeUpdate();                                                                         // 6)
        } 
        catch (SQLException ex) {
            System.err.println("Impossibile eliminare i dati della spesa selezionata: " + ex.getMessage());
        } 

        return (risultatoOperazione != 0);
    }
    
    /*
        Inserisce nel database dei nuovi dati relativi ad una nuova spesa per un determinato utente. 
        Se l'utente non esiste, sarà compito del database aggiungere in automatico nella tabella utente una nuovo record per l'utente
    */
    public boolean inserimentoDatiNuovaSpesa (String nomeUtente, String categoria, String metodoDiPagamento, LocalDate data, double importo, String descrizione) { 
        int result = 0;                                                                                                              // 7)

        try (Connection connection = DriverManager.getConnection(
                                        "jdbc:mysql://" + indirizzoIp + ":" + porta + "/" + nomeDB, username, password);             // 8)
                                     PreparedStatement statementControllaNomeUtente =
                                        connection.prepareStatement("CALL controllo_esistenza_utente(?);");                          // 9)
                                     PreparedStatement statementInserisciNuovaSpesa = 
                                        connection.prepareStatement("INSERT INTO spese VALUES(?,0,?,?,?,?,?)");                      // 10)
        ) {
            statementControllaNomeUtente.setString(1, nomeUtente);
            statementControllaNomeUtente.execute();

            statementInserisciNuovaSpesa.setString(1, nomeUtente);
            statementInserisciNuovaSpesa.setString(2, categoria);
            statementInserisciNuovaSpesa.setString(3, metodoDiPagamento);
            statementInserisciNuovaSpesa.setString(4, data.toString());
            statementInserisciNuovaSpesa.setDouble(5, importo);
            statementInserisciNuovaSpesa.setString(6, descrizione);
            result = statementInserisciNuovaSpesa.executeUpdate();                                                                   // 11)
        } 
        catch (SQLException ex) {
                System.err.println("Impossibile inserire la nuova spesa: " + ex.getMessage());
        } 

        return (result != 0);
    }
    
    /*
        Interroga il database il quale restituirà una lista di coppie {Categoria, TotaleSpesePerCategoria} 
        relative all'utente indicato nel periodo temporale scelto.
        Per ogni categoria, quindi verranno sommate tutte le spese sostenute dall'utente per quella determinata categoria
    */
    public List<Pair<String, Double>> calcoloSpesePerCategoria (String nomeUtente, LocalDate dataDiInizio, LocalDate dataDiFine, int numeroMaxCategorie) { 
        
        List<Pair<String, Double>> elencoCategorie = new ArrayList<>();                                                              //12)
        if (nomeUtente == null || nomeUtente.equals("")){
            return elencoCategorie;
        }

        try (Connection connection = DriverManager.getConnection(
                                        "jdbc:mysql://" + indirizzoIp + ":" + porta + "/" + nomeDB, username, password);
                                     PreparedStatement statement = connection.prepareStatement(
                                        "SELECT Categoria, ROUND(SUM(Importo), 2) AS TotaleSpesePerCategoria FROM spese WHERE NomeUtente = ? "
                                        + "AND data BETWEEN ? AND ? GROUP BY Categoria ORDER BY TotaleSpesePerCategoria DESC "
                                        + "LIMIT ?");                                                                                // 13)
            ){
                statement.setString(1, nomeUtente);
                statement.setString(2, dataDiInizio.toString());
                statement.setString(3, dataDiFine.toString());
                statement.setInt(4, numeroMaxCategorie);
                ResultSet result = statement.executeQuery();                                                                         // 14)

                while (result.next()) {
                        elencoCategorie.add(new Pair<>(result.getString("Categoria"), result.getDouble("TotaleSpesePerCategoria"))); // 15)
                }
        } 
        catch (SQLException ex) {
                System.err.println("Impossibile ottenere l'elenco contentente le spese totali per ogni categoria: " + ex.getMessage());
        }

        return elencoCategorie;
    }
}

/* Note:
	
	0) E' una lista che contiene i dati delle spese che verranno visualizzate nell'interfaccia e che verra' restituita.
        1) Se l'utente non e' indicato, si restituisce la lista vuota
	2) Instaura una connessione con il database MySQL passando i parametri relativi al db importati dai paramtri di configurazione
	3) Prepara uno statement MySQL per ottenere i dati delle spese dell'utente nomeUtente nel periodo temporale 
           compreso tra DataDiInizio e DataDiFine
	4) Estrae i risultati della query creando un oggetto DatiSpesa per ogni tupla, aggiungendola alla lista elencoSpese che
           verra' poi restituita al chiamante
        5) Inserisce i valori nello statement MySQL ed esegue la DELETE.
	6) Variabile che conterrà il risultato della query. Se e' uguale a zero, allora l'operazione di DELETE
           e' fallita
        07) Variabile che conterra' il risultato della query. Se e' uguale a zero, allora l'operazione di INSERT
            e' fallita	
	8) Instaura una connessione con il database MySQL passando i parametri relativi al db importati dai paramtri di configurazione
	9) Questo statement chiama una procedure del db che controlla l'esistenza dell'utente.
           In caso contrario, l'utente viene automaticamente creato
	10) Questo statement inserisce i dati relativi ad una nuova spesa nel database
	11) Inserisce i valori nello statement ed esegue la INSERT.
            Il metodo executeUpdate() restituisce il numero di righe influenzate dall'operazione. 
            Se uguale a zero, significa che la INSERT e' fallita.
	12) E' la lista che verra' restituita.
            Pair<K,V> e' una classe Generics usata per rappresentare in maniera	conveniente 
            coppie chiave-valore
            https://docs.oracle.com/javafx/2/api/javafx/util/Pair.html
	13) Apre la connessione e prepara la query
	14) Compila i campi della query e la esegue
	15) Scorre il result set e inserisce ogni coppia di dati nella lista, creando un oggetto Pair<>
*/