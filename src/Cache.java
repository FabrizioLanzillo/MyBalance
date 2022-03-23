import java.io.*;
import java.time.*;

public class Cache implements Serializable{         //0
    public String nomeUtente;
    public String categoriaSpesaNonArchiviata;
    public String metodoDiPagamentoNonArchiviato;
    public LocalDate dataNonArchiviata;             //1
    public double importoNonArchiviato;
    public String descrizioneNonArchiviata;
    public LocalDate dataDiInizioPeriodoNonArchiviata;
    public LocalDate dataDiFinePeriodoNonArchiviata;
    
    
    /*
    costruttore della classe Cache
    */
    public Cache(String nomeUtente, String categoriaSpesaNonArchiviata, String metodoDiPagamentoNonArchiviato, LocalDate dataNonArchiviata,
                 double importoNonArchiviato, String descrizioneNonArchiviata, LocalDate dataDiInizioPeriodoNonArchiviata, LocalDate dataDiFinePeriodoNonArchiviata){
        
        this.nomeUtente = nomeUtente;
        this.categoriaSpesaNonArchiviata = categoriaSpesaNonArchiviata;
        this.metodoDiPagamentoNonArchiviato = metodoDiPagamentoNonArchiviato;
        this.dataNonArchiviata = dataNonArchiviata;
        this.importoNonArchiviato = importoNonArchiviato;
        this.descrizioneNonArchiviata = descrizioneNonArchiviata;
        this.dataDiInizioPeriodoNonArchiviata = dataDiInizioPeriodoNonArchiviata;
        this.dataDiFinePeriodoNonArchiviata = dataDiFinePeriodoNonArchiviata;
        
    }
}

/* Note:
    0) La classe Cache rappresenta i dati che devono essere caricati nell'applicazione all'avvio di quest'ultima e salvati
       su file binario alla chiusura. E' serializzabile.
    1) LocalDate e' una classe adottata in Java 8 per memorizzare i timestamp
       https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
*/
