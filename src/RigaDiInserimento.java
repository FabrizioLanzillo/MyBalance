
import java.util.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/*
    RigaDiInserimento si occupa della gestione frontend dei campi della riga, grazie
    ai quali l'utente puo' inserire una nuova spesa
*/
public class RigaDiInserimento {
    private final AnchorPane rigaPerInserimentoDati;    // 1)
    private ChoiceBox<String> categoria;
    private ChoiceBox<String> metodoDiPagamento;
    private DatePicker data;
    private TextField importo;
    private TextField descrizione;
    
    /*
        Inizializza la riga, instanziando i nodi grafici e configurandone l'aspeto grafico
    */
    public RigaDiInserimento(ParametriDiConfigurazione parametriDiConfigurazione){
        HBox campiRigaPerInserimentoDati = new HBox(creazioneCampoCategoria(parametriDiConfigurazione), creazioneCampoMetodoDiPagamento(parametriDiConfigurazione),
                                                    creazioneCampoData(), creazioneCampoImporto(parametriDiConfigurazione),
                                                    creazioneCampoDescrizione(parametriDiConfigurazione));      // 2)
        
        campiRigaPerInserimentoDati.setAlignment(Pos.CENTER_RIGHT);
        AnchorPane.setRightAnchor(campiRigaPerInserimentoDati, 36.0);

        rigaPerInserimentoDati = new AnchorPane(campiRigaPerInserimentoDati); // 3)
    }
    
    /*
        Crea il campo per la scelta delle categorie con l'apposita etichetta,
        in disposizione verticale
    */
    private Node creazioneCampoCategoria(ParametriDiConfigurazione parametriDiConfigurazione){
        categoria = new ChoiceBox<>( FXCollections.observableArrayList(Arrays.asList(parametriDiConfigurazione.arrayCategorieSpesa))); // 4)
        categoria.setPrefWidth(250);
        categoria.setStyle("-fx-font-size: 12pt; -fx-border: thin; -fx-border-color: grey; "
                        + "-fx-background-color: #" + parametriDiConfigurazione.coloreRigaDiInserimento.toString().substring(2) + ";");

        Label etichettaCategoria = new Label("Categoria");
        etichettaCategoria.setStyle("-fx-font-size: 10pt; -fx-font-weight: bold; -fx-border: thin; -fx-border-color: grey;");
        etichettaCategoria.setMaxWidth(250);
        etichettaCategoria.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(etichettaCategoria, categoria);
        vbox.setAlignment(Pos.TOP_LEFT);
        return vbox;
    }
    
    /*
        Crea il campo per l'inserimento del metodo di pagamento della spesa con l'apposita etichetta,
        in disposizione verticale
    */
    private Node creazioneCampoMetodoDiPagamento(ParametriDiConfigurazione parametriDiConfigurazione){
        metodoDiPagamento = new ChoiceBox<>( FXCollections.observableArrayList(Arrays.asList(parametriDiConfigurazione.arrayMetodiDiPagamento))); // 4)
        metodoDiPagamento.setPrefWidth(200);
        metodoDiPagamento.setStyle("-fx-font-size: 12pt; -fx-border: thin; -fx-border-color: grey; "
                        + "-fx-background-color: #" + parametriDiConfigurazione.coloreRigaDiInserimento.toString().substring(2) + ";");

        Label etichettaMetodoDiPagamento = new Label("Metodo Di Pagamento");
        etichettaMetodoDiPagamento.setStyle("-fx-font-size: 10pt; -fx-font-weight: bold; -fx-border: thin; -fx-border-color: grey;");
        etichettaMetodoDiPagamento.setMaxWidth(200);
        etichettaMetodoDiPagamento.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(etichettaMetodoDiPagamento, metodoDiPagamento);
        vbox.setAlignment(Pos.TOP_LEFT);
        return vbox;
    }
    
    /*
        Crea il campo per la selezione della data della spesa con l'apposita etichetta,
	in disposizione verticale
    */
    private Node creazioneCampoData() { 
        data = new DatePicker(); // 5)
        data.setPrefWidth(150); 
        data.setStyle("-fx-font-size: 12pt; -fx-border: thin; -fx-border-color: grey;");

        Label etichettaData = new Label("Data");
        etichettaData.setStyle("-fx-font-size: 10pt; -fx-font-weight: bold; -fx-border: thin; -fx-border-color: grey;");
        etichettaData.setMaxWidth(150);
        etichettaData.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(etichettaData, data);
        vbox.setAlignment(Pos.TOP_LEFT);
        return vbox;
    }
    
    /*
        Crea il campo per l'inserimento dell'importo della spesa con l'apposita etichetta,
        in disposizione verticale
    */
    private Node creazioneCampoImporto (ParametriDiConfigurazione parametriDiConfigurazione) { 
        importo = new TextField();
        importo.setPrefWidth(100);
        importo.setStyle("-fx-font-size: 12pt; -fx-border: thin; -fx-border-color: grey;"
                        + "-fx-background-color: #" + parametriDiConfigurazione.coloreRigaDiInserimento.toString().substring(2) + ";");

        Label etichettaImporto = new Label("Importo");
        etichettaImporto.setStyle("-fx-font-size: 10pt; -fx-font-weight: bold; -fx-border: thin; -fx-border-color: grey;");
        etichettaImporto.setMaxWidth(100);
        etichettaImporto.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(etichettaImporto, importo);
        vbox.setAlignment(Pos.TOP_LEFT);
        return vbox;
    }
    
    /*
        Crea il campo per l'inserimento della descrizione della spesa con l'apposita etichetta,
        in disposizione verticale
    */
    private Node creazioneCampoDescrizione (ParametriDiConfigurazione parametriDiConfigurazione) {
        descrizione = new TextField();
        descrizione.setPrefWidth(300);
        descrizione.setStyle("-fx-font-size: 12pt; -fx-border: thin; -fx-border-color: grey;"
                        + "-fx-background-color: #" + parametriDiConfigurazione.coloreRigaDiInserimento.toString().substring(2) + ";");

        Label etichettaDescrizione = new Label("Descrizione");
        etichettaDescrizione.setStyle("-fx-font-size: 10pt; -fx-font-weight: bold; -fx-border: thin; -fx-border-color: grey;");
        etichettaDescrizione.setMaxWidth(300);
        etichettaDescrizione.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(etichettaDescrizione, descrizione);
        vbox.setAlignment(Pos.TOP_LEFT);
        return vbox;
    }
    
    /*
        Restituisce il nodo grafico principale della riga, cosi' da poter essere aggiunta
        all'interfaccia grafica
    */
    public Node getRigaDiInserimentoGUI(){
        return rigaPerInserimentoDati;
    }
    
    /*
        Restituisce un oggetto DatiSpesa contenente i valori inseriti dall'utente
    */
    public DatiSpesa getSpesaInserita() { 
        double importoConvertito;
        
        try {
            importoConvertito = Double.valueOf(importo.getText()); // 6)
        } 
        catch (NumberFormatException ex) {
            importoConvertito = 0.0;
        }

        return new DatiSpesa(-1, categoria.getValue(), metodoDiPagamento.getValue(), data.getValue(), importoConvertito, descrizione.getText()); // 7)
    }
    
    /*
        Imposta i valori della riga a partire da i dati di una spesa passata per riferimento
    */
    public void setSpesaInserita(DatiSpesa spesa) { 
        categoria.getSelectionModel().select(spesa.getCategoria());
        metodoDiPagamento.getSelectionModel().select(spesa.getMetodoDiPagamento());
        data.setValue(spesa.getData());
        importo.setText(Double.toString(spesa.getImporto()));
        descrizione.setText(spesa.getDescrizione());

        if (importo.getText().equals("0.0")){ // 8)
            importo.setText("");
        }
    }
	
    /*
        Svuota la riga
    */
    public void cancellaRiga() { 
        categoria.getSelectionModel().clearSelection();
        metodoDiPagamento.getSelectionModel().clearSelection();
        importo.setText("");
        data.setValue(null);
        descrizione.setText("");
    }
}

/* Note:
    1) Sono gli elementi grafici della riga il cui riferimento e' necessario per il corretto
        funzionamento dei metodi.
        AnchorPane e' un contenitore che permette di ancorare i nodi figli a distanze fisse dai bordi
        https://docs.oracle.com/javafx/2/api/javafx/scene/layout/AnchorPane.html	
    2) Contenitore dei campi della riga, che vengono disposti orizzontalmente
    3) I campi della riga vengono inseriti in un apposito contenitore cosi' da
        poter essere visualizzati sull' interfaccia grafica
    4) ChoiceBox e' un elemento di controllo usato per permettere la selezione all'utente
        di un unico valore da un insieme limitato di possibili scelte.
        https://docs.oracle.com/javafx/2/api/javafx/scene/control/ChoiceBox.html
        Viene inizializzato con una Observable List costruita a partire dal vettore
        contenente le categorie di spesa selezionabili fornito dai parametri di configurazione XML
    5) DatePicker e' un elemento di controllo che permette la selezione di una data
        utilizzando una GUi user-friendly simile ad un calendario
        https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/DatePicker.html	
    6) Preleva l'importo inserito dall'utente. In caso di fallimento della conversione
        stringa-to-double (ad esempio se il campo e' rimasto vuoto), l'importo
        viene fissato a zero
    7) Poiche' questo oggetto DatiSpesa rappresenta una spesa da inserire e non una
        spesa prelevata dal database, il campo ID non e' significativo e vi viene
        inserito un valore che non crei conflitto (il valore e' negativo, poiche' per progettazione gli id
        sono numeri positivi)
    8) Nel caso in cui l'importo sia zero, il campo testuale viene reso vuoto.
        Questa scelta e' coerente con la scelta del punto 6 di impostare un importo pari
        a zero quando il campo testuale e' vuoto
*/