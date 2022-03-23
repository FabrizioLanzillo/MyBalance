import java.time.*;

/*
    DatiSpesa raccoglie l'insieme dei dati relativi ad una spesa sostenuta da parte di un utente che sta usando l'applicazione
*/
public class DatiSpesa {
    
    private final int idSpesa;
    private final String categoria;
    private final String metodoDiPagamento;
    private final LocalDate data;           // 0)
    private final double importo;
    private final String descrizione;
    
    public DatiSpesa(int idSpesa, String categoria, String metodoDiPagamento, LocalDate data, double importo, String descrizione) {
        this.idSpesa = idSpesa;
        this.categoria = categoria;
        this.metodoDiPagamento = metodoDiPagamento;
        this.importo = importo;
        this.data = data;
        this.descrizione = descrizione;
    }
    
    public int getIdSpesa() {
        return idSpesa;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getMetodoDiPagamento() {
        return metodoDiPagamento;
    }
    
    public LocalDate getData() {
        return data;
    }
    
    public double getImporto() {
        return importo;
    }

    public String getDescrizione() {
        return descrizione;
    }
}

/* Note:
    01)	LocalDate e' una classe utilizzata per rappresentare i timestamp senza fuso orario,
        basandosi sullo standard ISO-8601
        https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html
*/