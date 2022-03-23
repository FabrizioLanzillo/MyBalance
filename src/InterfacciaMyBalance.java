import java.text.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;

/*
    La classe InterfacciaMyBalance si occupa di costruire e gestire i vari settori e sezioni dell'interfaccia grafica,
    instanziando i vari elementi grafici che la compongono
*/
public class InterfacciaMyBalance {
    
    private final MyBalance myBalance;
    private double totaleSpese;                 // 1)
    private double banca1;                      // 2)
    private double banca2;                      // 3)
    private double contanti;                    // 4)
    
    private final VBox contenitorePrincipale;   // 5)
    private final AnchorPane header;
    private final VBox sezioneTabella;
    private final HBox pulsantiPerTabella;
    private final HBox sezioneAnalisiSpese; 
    

    private final SettoreCaricaDati settoreCaricaDati;  //6)
    private final RigaDiInserimento rigaDiInserimento;
    private final GraficoDelleCategorie grafico;
    private final TabellaSpese tabella;
    private final SettoreCambioPeriodoTemporale settoreCambioPeriodo;
    
    /*
        Costruttore: inizializza l'interfaccia grafica e inserisce il contenitore principale nell'oggetto Group 
        passato come parametro
    */
    public InterfacciaMyBalance(Group root, MyBalance myBalance, ParametriDiConfigurazione parametriDiConfigurazione, String nomeApplicazione){
        
        this.myBalance = myBalance;
        
        header = new AnchorPane();                                                                      // 7)
        pulsantiPerTabella = new HBox();
        sezioneAnalisiSpese = new HBox();
        
        
        tabella = new TabellaSpese(this, parametriDiConfigurazione);                                    // 8)
        settoreCaricaDati = new SettoreCaricaDati(this, parametriDiConfigurazione);
        rigaDiInserimento = new RigaDiInserimento(parametriDiConfigurazione);
        grafico = new GraficoDelleCategorie();
        settoreCambioPeriodo = new SettoreCambioPeriodoTemporale(this, parametriDiConfigurazione);
        
        costruisciHeader(nomeApplicazione);                                                             // 9
        costruisciPulsantiPerTabella(parametriDiConfigurazione);
        costruisciSezioneAnalisiSpese();
        
        sezioneTabella = new VBox(tabella.getTabellaGUI(), rigaDiInserimento.getRigaDiInserimentoGUI());
        sezioneTabella.setPadding(new Insets(0, 0, 0, 44));
        
        contenitorePrincipale = new VBox(header, sezioneTabella, pulsantiPerTabella, sezioneAnalisiSpese);  //10)
        contenitorePrincipale.setAlignment(Pos.TOP_CENTER);
        contenitorePrincipale.setPrefWidth(MyBalance.LARGHEZZA_SCENA);
        contenitorePrincipale.setMaxWidth(MyBalance.LARGHEZZA_SCENA);
        contenitorePrincipale.setMaxHeight(MyBalance.ALTEZZA_SCENA);
        contenitorePrincipale.setSpacing(40);
        contenitorePrincipale.setPadding(new Insets(20, 20, 0, 20));

        root.getChildren().add(contenitorePrincipale);                                                      // 11)   
        
    }
    
    /*
        Costruisce lo header dell'applicazione, composto dal titolo dell'applicazione e dall'area in cui l'utente
        puo' inserire il proprio Nome Utente per caricare i dati
    */
    public void costruisciHeader(String nomeApplicazione){
        header.getChildren().clear(); 
		
        Label titolo = new Label(nomeApplicazione);                                            // 12)
        titolo.setStyle("-fx-font-size: 28pt; -fx-text-fill: red;  -fx-font-weight: bold;"
                        + "-fx-font-family: \"Calibri\", cursive, sans-serif");

        header.getChildren().addAll(titolo, settoreCaricaDati.getSettoreCaricaDatiGUI());
        AnchorPane.setLeftAnchor(titolo, 40.0);
        AnchorPane.setRightAnchor(settoreCaricaDati.getSettoreCaricaDatiGUI(), 40.0);
    }
    
    /*
        configura la grafica dei pulsanti dell'interfaccia
    */
    public void definisciStilePulsanti(Button pulsante, Color colore){
            pulsante.setStyle("-fx-padding: 5px 15px;"
                            + "-fx-text-align: center; -fx-font-size: 12pt; -fx-font-family: sans-serif; "
                            + "-fx-text-fill: white;  -fx-font-weight: bold;"
                            + "-fx-background-color: " + colore.toString().replace("0x", "#"));
            pulsante.setPrefSize(200, 60);
    }
    
    /*
        Costruisce i pulsanti INSERISCI, ANNULLA e ELIMINA, ne configura la grafica e gli handler degli eventi
        di interazione e li inserisce nel contenitore apposito.
    */
    public void costruisciPulsantiPerTabella(ParametriDiConfigurazione parametriDiConfigurazione){
        
        pulsantiPerTabella.getChildren().clear();
        pulsantiPerTabella.setAlignment(Pos.CENTER);

        Button inserisci = new Button("INSERISCI");                                     // 13)
        definisciStilePulsanti(inserisci, parametriDiConfigurazione.coloreInserisci);
        inserisci.setOnAction((ActionEvent ev)-> {
            myBalance.inserisciNuovaSpesa(rigaDiInserimento.getSpesaInserita());
            rigaDiInserimento.cancellaRiga();
            myBalance.inviaEventoNavigazione("INSERISCI");
        });
        
        Button annulla = new Button("ANNULLA");                                         // 14)
        definisciStilePulsanti(annulla, parametriDiConfigurazione.coloreAnnulla);
        annulla.setOnAction((ActionEvent ev)-> {
            rigaDiInserimento.cancellaRiga();
            myBalance.inviaEventoNavigazione("ANNULLA");
        });
        
        Button elimina = new Button("ELIMINA");                                         // 15)
        definisciStilePulsanti(elimina, parametriDiConfigurazione.coloreElimina);
        elimina.setOnAction((ActionEvent ev)-> {
            tabella.rimuoviSpesaSelezionata();
            myBalance.inviaEventoNavigazione("ELIMINA");
            
        });
        
        pulsantiPerTabella.getChildren().addAll(inserisci, annulla, elimina);
        pulsantiPerTabella.setSpacing(150);

    }
    
    /*
        Metodo che modifica lo stile dei display dei valori contabili delle spese
    */
    public void definisciStileDisplay(Label display){
        display.setFont(new Font("sans-serif",20));
        display.setMinSize(150, 20);
        display.setAlignment(Pos.CENTER);
        display.setStyle("-fx-border-width: 2.5px; -fx-border-color:black;"
                        + "-fx-text-fill: black;");
    }
    
    /*
        Metodo che modifica lo stile delle etichette dei display dei valori contabili delle spese
    */
    public void definisciStileEtichettaDisplay(Label etichettaDisplay){
        etichettaDisplay.setFont(new Font("sans-serif",20));
        etichettaDisplay.setMinSize(130, 20);
        etichettaDisplay.setAlignment(Pos.CENTER);
    }
    
    /*
        Costruisce la parte inferiore dell'interfaccia grafica, ovvero l'area contenente il settore per cambiare il periodo temporale, 
        il settore cotabile delle spese e il grafico delle categorie.
    */
    public void costruisciSezioneAnalisiSpese(){
        
        sezioneAnalisiSpese.getChildren().clear();
        
        HBox voceContanti = new HBox();
        HBox voceBanca1 = new HBox();
        HBox voceBanca2 = new HBox();
        HBox voceTotaleSpese = new HBox();
		
        Label titoloContatori = new Label("Spese Complessive");                 
        titoloContatori.setFont(new Font("sans-serif", 20));
        titoloContatori.setMinWidth(300);
        titoloContatori.setPadding(new Insets(0, 0, 0, 0));
        titoloContatori.setAlignment(Pos.TOP_CENTER);
        titoloContatori.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
        
        Label contanti = new Label("Contanti: ");                               // 16)
        Label displayContanti = new Label("-");                                 // 17)
        definisciStileEtichettaDisplay(contanti);
        definisciStileDisplay(displayContanti);
        voceContanti.getChildren().addAll(contanti, displayContanti);
        voceContanti.setAlignment(Pos.TOP_CENTER);
        
        Label banca1 = new Label("Banca#1: ");                                  // 16)
        Label displayBanca1 = new Label("-");                                   // 17)
        definisciStileEtichettaDisplay(banca1);
        definisciStileDisplay(displayBanca1);
        voceBanca1.getChildren().addAll(banca1, displayBanca1);
        voceBanca1.setAlignment(Pos.TOP_CENTER);
        
        Label banca2 = new Label("Banca#2: ");                                  // 16)
        Label displayBanca2 = new Label("-");                                   // 17)           
        definisciStileEtichettaDisplay(banca2);
        definisciStileDisplay(displayBanca2);
        voceBanca2.getChildren().addAll(banca2, displayBanca2);
        voceBanca2.setAlignment(Pos.TOP_CENTER);
        
        Label totaleSpese = new Label("Totale Spese: ");                        // 16)
        Label displayTotaleSpese = new Label("-");                              // 17)
        definisciStileEtichettaDisplay(totaleSpese);
        definisciStileDisplay(displayTotaleSpese);
        voceTotaleSpese.getChildren().addAll(totaleSpese, displayTotaleSpese);
        voceTotaleSpese.setAlignment(Pos.TOP_CENTER);
        
        VBox contatoreTotaleSpese = new VBox();                                 // 18)
        contatoreTotaleSpese.getChildren().addAll(titoloContatori, voceContanti, voceBanca1, voceBanca2, voceTotaleSpese);
        contatoreTotaleSpese.setSpacing(20);
        
        sezioneAnalisiSpese.getChildren().addAll(settoreCambioPeriodo.getSettoreCambioPeriodoTemporaleGUI(), contatoreTotaleSpese, grafico.getGraficoGUI());
        sezioneAnalisiSpese.setAlignment(Pos.TOP_CENTER);
        sezioneAnalisiSpese.setSpacing(15);
    
    }
    
    /*
        Aggiorna il totale spese mostrato, sia nella variabile backend che nel display frontend 
    */
    public void aggiornaSettoreContabileSpese(double contatoreContanti, double contatoreBanca1, double contatoreBanca2, double contatoreTotaleSpese){
        
        totaleSpese = contatoreTotaleSpese;
        banca1 = contatoreBanca1;
        banca2 = contatoreBanca2;
        contanti = contatoreContanti;
		
        VBox contenitoreDisplay = (VBox)sezioneAnalisiSpese.getChildren().get(1);
        
        HBox contenitoreContanti = (HBox)contenitoreDisplay.getChildren().get(1);
        Label valoreContanti = (Label)contenitoreContanti.getChildren().get(1);
        valoreContanti.setText("\u20ac " + (new DecimalFormat("0.00")).format(contanti));   // 19)
        
        HBox contenitoreBanca1 = (HBox)contenitoreDisplay.getChildren().get(2);
        Label valoreBanca1 = (Label)contenitoreBanca1.getChildren().get(1);
        valoreBanca1.setText("\u20ac " + (new DecimalFormat("0.00")).format(banca1));       // 19)
        
        HBox contenitoreBanca2 = (HBox)contenitoreDisplay.getChildren().get(3);
        Label valoreBanca2 = (Label)contenitoreBanca2.getChildren().get(1);
        valoreBanca2.setText("\u20ac " + (new DecimalFormat("0.00")).format(banca2));       // 19)
        
        HBox contenitoreTotaleSpese = (HBox)contenitoreDisplay.getChildren().get(4);
        Label valoreTotaleSpese = (Label)contenitoreTotaleSpese.getChildren().get(1);
        valoreTotaleSpese.setText("\u20ac " + (new DecimalFormat("0.00")).format(totaleSpese)); // 19)
    }
    
    public MyBalance getMyBalance(){
        return myBalance;
    }
    
    public SettoreCaricaDati getSettoreCaricaDati(){
        return settoreCaricaDati;
    }
    
    public TabellaSpese getTabellaSpese() {
        return tabella;
    } 
    
    public RigaDiInserimento getRigaDiInserimento(){
        return rigaDiInserimento;
    }
    
    public GraficoDelleCategorie getGraficoDelleCategorie() {
        return grafico;
    }
   
    public SettoreCambioPeriodoTemporale getSettoreCambioPeriodoTemporale() {
        return settoreCambioPeriodo;
    }
     
}

/*Note:
    1) Variabile che contiene il valore corrispondente al totale delle spese
    2) Variabile che contiene il valore delle spese sostenute in contanti
    3) Variabile che contiene il valore delle spese sostenute con la banca 1
    4) Variabile che contiene il valore delle spese sostenute con la banca 2
    5) Dichiarazione dei contenitori degli elementi grafici: header e' la parte superiore (titolo e CARICA DATI),
       pulsantiPerTabella contiene i pulsanti sottostanti la tabella e la sezioneAnalisiSpese e' la parte inferiore 
       dell'interfaccia grafica (modifica periodo temporale, settore contabile, grafico)
    6) Dichiarazione degli oggetti grafici
    7) Istanziazione dei contenitori dei vari settori
    8) Istanziazione degli oggetti grafici
    9) Lancia i meotdi che si occupano di costruire e inserire gli elementi grafici appartenenti 
       ai vari contenitori
    10) Inizializza il contenitore principale dell' interfaccia grafica
    11) Inserisce il contenitore principale nell'oggeto Group passato come parametro, che ha il compito di
        contenere tutti gli elementi grafici da mostrare sulla schermata
    12) Crea il titolo dell'applicazione come Label
    13) Creazione del pulsante INSERISCI. Quando l'utente lo seleziona, viene inviato un log dell'evento e viene
        richiamato il metodo per l'inserimento di nuovi dati relativi ad una spesa nel database. Inoltre, viene
        svuotato il form di inserimento
    14) Creazione del pulsante ANNULLA. Quando l'utente lo seleziona, viene inviato un log dell'evento e viene
        svuotato il form d'inserimento
    15) Creazione del pulsante ELIMINA. Quando l'utente lo seleziona, viene inviato un log dell'evento e viene
        richiamato il metodo per la cancellazione dei dati relativi alla spesa selezionata dal database. 

    16) E' l'etichetta presente sopra i vari display contabili
    17) E' il display contabile che mostra i valori riepilogativi
    18) E' il box che contiene tutti i display e le etichette del settore contabile
    19) Aggiorna il totale spese mostrato, sia nella variabile backend che nel display frontend 
    20) La classe java.text.DecimalFormat fornisce metodi per la formattazione di numeri
        con cifre decimale. In questo caso, l'importo totale viene formattato su due cifre
        decimali
        https://docs.oracle.com/javase/8/docs/api/java/text/DecimalFormat.html
        \u20ac e' il simbolo dell'euro in codifica Unicode
*/