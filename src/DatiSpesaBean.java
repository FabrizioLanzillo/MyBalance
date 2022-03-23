import java.time.*;
import javafx.beans.property.*;

/*
    DatiSpesaBean e' il bean utilizzato per incapsulare i dati di una singola spesa che saranno visualizzati nella tabella dell'applicazione.
*/
@SuppressWarnings("unchecked")
public class DatiSpesaBean { 
    
    private SimpleStringProperty categoria;
    private SimpleStringProperty metodoDiPagamento;
    private SimpleObjectProperty<LocalDate> data;
    private SimpleDoubleProperty importo;
    private SimpleStringProperty descrizione;
    
    private int idSpesa = -1; // 1)
    
    public DatiSpesaBean(String categoria, String metodoDiPagamento, LocalDate data, Double importo, String descrizione){
        this.categoria = new SimpleStringProperty(categoria);
        this.metodoDiPagamento = new SimpleStringProperty(metodoDiPagamento);
        this.data = new SimpleObjectProperty(data);
        this.importo = new SimpleDoubleProperty(importo);
        this.descrizione = new SimpleStringProperty(descrizione);    
    }
    

    public DatiSpesaBean(DatiSpesa dati){   // 2)
        this(dati.getCategoria(), dati.getMetodoDiPagamento(), dati.getData(), dati.getImporto(), dati.getDescrizione());
        this.idSpesa = dati.getIdSpesa();
    }
    
    public String getCategoria() {
        return categoria.getValue();
    }
    public String getMetodoDiPagamento() {
        return metodoDiPagamento.getValue();
    }
    public Double getImporto() {
        return importo.getValue();
    }
    public LocalDate getData() {
        return data.getValue();
    }
    public String getDescrizione() {
        return descrizione.getValue();
    }

    public void setCategoria (String cat) {
        categoria.setValue(cat);
    }
    public void setMetodoDiPagamento (String cat) {
        metodoDiPagamento.setValue(cat);
    }
    public void setImporto (Double imp){
        importo.setValue(imp);
    }
    public void setData (LocalDate data) {
        this.data.setValue(data);
    }
    public void setDescrizione (String desc) {
        descrizione.setValue(desc);
    }
    
    public DatiSpesa covertiInDatiSpesa(){  // 3)
        return new DatiSpesa(idSpesa, getCategoria(),getMetodoDiPagamento(), getData(), getImporto(), getDescrizione());
    }
}


/* Note:
    1) E' l'idSpesa che contiene tutti i dati di una singola spesa. Non viene mostrata negli elementi grafici che usano questo bean,
       perchè l'utente non utilizza e non ha bisogno di questa informazione. E' tuttavia necessaria quest'informazione 
       per selezionare quale record eliminare dal database.
       Ha come valore di default -1, dove viene utilizzato nella TableView responsabile dell'inserimento di una nuova spesa,
       in quanto la gestione dell'assegnazione dei prossimi id è gestita da un trigger nel db.
    2) Costruitore per costruire DatiSpesaBean a partire da DatiSpesa
    3) Converte un DatiSpesaBean in un DatiSpesa
*/