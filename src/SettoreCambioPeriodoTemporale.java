import java.time.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

/*
    SettoreCambioPeriodoTemporale e' la classe che gestisce il lato frontend 
    del cambio del periodo temporale in analisi
*/
public class SettoreCambioPeriodoTemporale {
    
    private final VBox contenitore;
    private final DatePicker dataDiInizioDatePicker;	// 1)
    private final DatePicker dataDiFineDatePicker;	// 1)
    private LocalDate dataDiInizio;
    private LocalDate dataDiFine;
    
    private final InterfacciaMyBalance interfacciaGrafica;
    
    /*
        Costruisce e inizializza il settore di cambio del periodo
    */
    public SettoreCambioPeriodoTemporale(InterfacciaMyBalance interfacciaGrafica, ParametriDiConfigurazione parametriDiConfigurazione){
        this.interfacciaGrafica = interfacciaGrafica;
        
        Label titoloPeriodoDiEsame = new Label("Periodo In Esame"); 
        titoloPeriodoDiEsame.setFont(new Font("sans-serif", 20));
        titoloPeriodoDiEsame.setMinWidth(300);
        titoloPeriodoDiEsame.setPadding(new Insets(0, 0, 0, 0));
        titoloPeriodoDiEsame.setAlignment(Pos.TOP_CENTER);
        titoloPeriodoDiEsame.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
        
        Label etichettaDataDiInizio = new Label("Data Di Inizio:"); 
        configuraEtichetta(etichettaDataDiInizio);
        VBox.setMargin(etichettaDataDiInizio, new Insets(15, 0, 10, 0));
        
        Label etichettaDataDiFine = new Label("Data Di Fine:"); 
        configuraEtichetta(etichettaDataDiFine);
        VBox.setMargin(etichettaDataDiFine, new Insets(15, 0, 10, 0));
        
                
        dataDiInizio = LocalDate.now().minusDays(parametriDiConfigurazione.periodoInEsameleIniziale);   // 2)
        dataDiFine = LocalDate.now(); 
        
        dataDiInizioDatePicker = new DatePicker(dataDiInizio);                                          // 3)
        dataDiFineDatePicker = new DatePicker(dataDiFine);  
        
        Button pulsante = new Button("SELEZIONA PERIODO");                                              // 4)
        configuraPulsante(pulsante, parametriDiConfigurazione);

        contenitore = new VBox(titoloPeriodoDiEsame, etichettaDataDiInizio, dataDiInizioDatePicker, etichettaDataDiFine, 
                                                        dataDiFineDatePicker, pulsante);                // 5)
        contenitore.setAlignment(Pos.TOP_CENTER);
    }
    
    /*
        Configura lo stile dell'etichetta
    */
    public void configuraEtichetta(Label etichetta){
        etichetta.setStyle("-fx-font-size: 12pt; -fx-text-fill: black; -fx-font-weight: bold;" + "-fx-font-family: sans-serif;");    
    }
    
    /*
        Configura il pulsante sia dal punto di vista grafico sia del suo comportamento (se premuto causa 
        l'aggiornamento dell'interfaccia grafica con i dati delle spese del periodo selezionato e invia
        un log al server log;)
    */
    public void configuraPulsante(Button pulsante, ParametriDiConfigurazione parametriDiConfigurazione){
        pulsante.setOnAction((ActionEvent ev)-> {
            dataDiInizio = dataDiInizioDatePicker.getValue();
            dataDiFine = dataDiFineDatePicker.getValue();
            interfacciaGrafica.getMyBalance().aggiornaDatiSpeseInterfaccia();
            interfacciaGrafica.getMyBalance().inviaEventoNavigazione("SELEZIONA PERIODO");
        });
        
        pulsante.setStyle("-fx-padding: 5px 20px; -fx-text-align: center; "
				+ "-fx-font-size: 12pt; -fx-font-family: sans-serif; "
				+ "-fx-text-fill: white;  -fx-font-weight: bold;"
				+ "-fx-background-color: #" + parametriDiConfigurazione.coloreCaricaDatiSelezionaPeriodo.toString().substring(2));
        pulsante.setPrefHeight(50);

        VBox.setMargin(pulsante, new Insets(30, 0, 0, 0));
    }
    
    
    public LocalDate getDataDiInizio() {
        return dataDiInizio;
    }

    public LocalDate getDataDiFine() {
        return dataDiFine;
    }

    public void setDataDiInizio(LocalDate dataDiInizioPeriodoTemporale) {
        dataDiInizio = dataDiInizioPeriodoTemporale;
        dataDiInizioDatePicker.setValue(dataDiInizioPeriodoTemporale);
    }

    public void setDataDiFine(LocalDate dataDiFinePeriodoTemporale) {
        dataDiFine = dataDiFinePeriodoTemporale;
        dataDiFineDatePicker.setValue(dataDiFinePeriodoTemporale);
    }
    
    /*
        Restituisce il nodo grafico principale della classe, così da poter essere aggiunto
        all' interfaccia grafica
    */
    public Node getSettoreCambioPeriodoTemporaleGUI() {
        return contenitore;
    }
}

/* Note:

    1) DatePicker e' un elemento di controllo che permette la selezione di una data
        utilizzando una GUi user-friendly simile ad un calendario
        https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/DatePicker.html

    2) Il periodo iniziale di analisi e' configurabile dall'utente attraverso il file
        di configurazione XML. Si assume come data di fine periodo la data corrente.
    3) I DatePicker vengono inzializzati con le date del periodo
    4) Configurazione degli elementi grafici della classe (il pulsante e le due etichette)
    5) Inserimento nel nodo principale della classe di tutti gli elementi grafici da mostrare
*/
