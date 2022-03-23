
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/*
    SettoreCaricaDati gestisce l'area di input e il relativo pulsante, attraverso cui l'utente puo' 
    inserire il proprio nome utente e caricare i suoi dati nell'interfaccia grafica
*/
public class SettoreCaricaDati {
    
    private final AnchorPane contenitore; // 1)
    private String nomeUtente;   // 2)           
    private static final String LABEL_PULSANTE_CARICA_DATI = "CARICA DATI";	
    private final TextField campoTestoUtente;  
    
    private final InterfacciaMyBalance interfacciaGrafica;
    
    /*
        Crea un'etichetta per indicare all'utente cosa inserire nel campo di input presente 
        e la definizione del campo input stesso con annesso pulsante
    */
    public SettoreCaricaDati(InterfacciaMyBalance interfacciaGrafica, ParametriDiConfigurazione parametriDiConfigurazione){
        
        this.interfacciaGrafica = interfacciaGrafica;
        HBox contenitoreInterno = new HBox();
		
        Label etichettaCampo = new Label("Utente: "); 
        etichettaCampo.setStyle("-fx-font-family: sans-serif; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 15pt;");
        etichettaCampo.setMinSize(0, 15);

        campoTestoUtente = new TextField(); // 3)
        campoTestoUtente.setPrefSize(300, 15); 
        campoTestoUtente.setMaxSize(300, 15);
        campoTestoUtente.setStyle("-fx-font-size: 14pt;");
        nomeUtente = "";

        Button caricaDatiUtente = new Button(LABEL_PULSANTE_CARICA_DATI); // 4)
        caricaDatiUtente.setOnAction((ActionEvent ev)-> {	
                nomeUtente = campoTestoUtente.getText();
                this.interfacciaGrafica.getMyBalance().aggiornaDatiSpeseInterfaccia();
                this.interfacciaGrafica.getMyBalance().inviaEventoNavigazione("CARICA DATI");
        });
        
        caricaDatiUtente.setStyle("-fx-padding: 10px 20px; -fx-border-radius: 15px;"
                        + "-fx-text-align: center; -fx-font-size: 12pt; -fx-font-family: sans-serif; "
                        + "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: #" + parametriDiConfigurazione.coloreCaricaDatiSelezionaPeriodo.toString().substring(2) + ";");
        
        contenitoreInterno.getChildren().addAll(etichettaCampo, campoTestoUtente, caricaDatiUtente);
        contenitoreInterno.setSpacing(20);
        contenitore = new AnchorPane(contenitoreInterno); // 5)
    }
    
    /*
        Restituisce l'elemento grafico principale (il contenitore di tutti gli elementi grafici), così che
        possa essere mostrato sull'interfaccia grafica
    */
    public Node getSettoreCaricaDatiGUI() { // 06)
        return contenitore;
    }
	
    public String getUtente() {
        return nomeUtente;
    }
	
    /*
        Imposta il nome utente
    */
    public void setUtente(String nomeUtentePassato) { // 07)
        nomeUtente = nomeUtentePassato;
        campoTestoUtente.setText(nomeUtente);
    }
}

/* Note:
    1) AnchorPane e' un contenitore che permette di ancorare i nodi figli a distanze fisse dai bordi
        https://docs.oracle.com/javafx/2/api/javafx/scene/layout/AnchorPane.html
    2) Contenuto backend della TextArea: contiene il nome utente inserito e validato
    3) Crea il campo di input dove l'utente dovrebbe inserire il proprio nome utente
    4) Crea il pulsante per caricare i dati sull' interfaccia grafica. Configura l'handler dell'evento di selezione
        del pulsante, provocando:
        - l'aggiornamento della variabile backend che contiene il nome utente
        - l'aggiornamento dell' interfaccia con i nuovi dati
        - l'invio di un messaggio di log per segnalare la selezione del pulsante
    5) Crea il contenitore di tutti i precedenti elementi grafici e configura la posizione al suo interno 
        di ogni elemento
*/