import java.time.*;
import java.util.*;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

/*
    TabellaSpese si occupa dei dati delle spese dell'utente sotto forma di tabella. Permette di selezionare una
    riga della tabella per eliminare la relativa spesa dal database
*/
public class TabellaSpese {
    
    private final TableView<DatiSpesaBean> tabella;     // 1)
    private final ObservableList<DatiSpesaBean> spese;  // 2)
    
    private final InterfacciaMyBalance interfacciaGrafica;
    
    /*
        Inizializza la TabellaSpese secondo i parametri di configurazione forniti
    */
    public TabellaSpese(InterfacciaMyBalance interfacciaGrafica, ParametriDiConfigurazione parametriDiConfigurazione){
        
        tabella = new TableView<>();
        spese = FXCollections.observableArrayList();
        this.interfacciaGrafica = interfacciaGrafica;
        
        tabella.setFixedCellSize(32);   // 3)
        tabella.setPrefSize(1000, parametriDiConfigurazione.numeroRigheTabella*tabella.getFixedCellSize());
        tabella.setMaxSize(tabella.getPrefWidth(), tabella.getPrefHeight());
        
        TableColumn<DatiSpesaBean, String> categoria = new TableColumn<>("Categoria");  // 4)
        categoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        categoria.setPrefWidth(250); 
        categoria.setMaxWidth(categoria.getPrefWidth());
        
        TableColumn<DatiSpesaBean, String> metodoDiPagamento = new TableColumn<>("MetodoDiPagamento");  // 4)
        metodoDiPagamento.setCellValueFactory(new PropertyValueFactory<>("metodoDiPagamento"));
        metodoDiPagamento.setPrefWidth(200); 
        metodoDiPagamento.setMaxWidth(metodoDiPagamento.getPrefWidth());

        TableColumn<DatiSpesaBean, LocalDate> data = new TableColumn<>("Data");  // 4)
        data.setCellValueFactory(new PropertyValueFactory<>("data"));
        data.setPrefWidth(150); 
        data.setMaxWidth(data.getPrefWidth());
        
        TableColumn<DatiSpesaBean, Double> importo = new TableColumn<>("Importo");  // 4)
        importo.setCellValueFactory(new PropertyValueFactory<>("importo"));
        importo.setPrefWidth(100); 
        importo.setMaxWidth(importo.getPrefWidth());

        TableColumn<DatiSpesaBean, String> descrizione = new TableColumn<>("Descrizione");  // 4)
        descrizione.setCellValueFactory(new PropertyValueFactory<>("descrizione"));
        descrizione.setPrefWidth(tabella.getPrefWidth() - categoria.getWidth() - metodoDiPagamento.getWidth() - data.getWidth() - importo.getWidth());
        descrizione.setMaxWidth(descrizione.getPrefWidth());

        tabella.setItems(spese);  // 5)
        tabella.getColumns().add(categoria);	
        tabella.getColumns().add(metodoDiPagamento);	
        tabella.getColumns().add(data);
        tabella.getColumns().add(importo);   
        tabella.getColumns().add(descrizione);
        
    }
    
    /*
        Svuota la tabella e la riempie con i nuovi dati di spesa
    */
    public void caricaSpese(List<DatiSpesa> elencoSpese) { // 07)
        spese.clear();

        for (DatiSpesa dati : elencoSpese) {
            spese.add(new DatiSpesaBean(dati));
        }
    }
    
    /*
        Preleva i dati della riga selezionata dall'utente e li passa al metodo middleware responsabile 
        all'eliminazione delle spese
    */
    public void rimuoviSpesaSelezionata() { // 08)
        interfacciaGrafica.getMyBalance().rimuoviSpesa(tabella.getSelectionModel().getSelectedItem().covertiInDatiSpesa());
    }
    
    /*
        Restituisce l'oggetto grafico della classe, ovvero la TableView, cosi' da poter essere inserita nell'interfaccia
    */
    public Node getTabellaGUI() { 
        return tabella;
    }
            
}

/* Note:
    1) E' la tabella che si occupa di mostrare i dati
    2) Contiene la parte di "backend" della tabella, ovvero la lista dei beans che contengono i dati mostrati
    3) Configura l'aspetto grafico della tabella: altezza delle righe e dimensioni della tabella
    4) Crea le colonne della tabella, configurandone il nome, la factory delle celle e la larghezza
    5) Associa l'observable list e le colonne alla tabella
*/