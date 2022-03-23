import java.text.*;
import java.util.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.util.*;

/*
    GraficoDelleCategorie si occupa di creare e tenere aggiornato il grafico
*/
public class GraficoDelleCategorie {
    
    private final PieChart grafico;     // 1)
    private final ObservableList<PieChart.Data> datiGrafico; // 2)
    
    /*
        Inizializza il grafico, instanziando sia l'observable list dei dati che il grafico stesso
    */
    public GraficoDelleCategorie(){
        datiGrafico = FXCollections.observableArrayList();
        grafico = new PieChart(datiGrafico);

        grafico.setPrefSize(480, 300);
        grafico.setLabelsVisible(false);
        grafico.setLegendSide(Side.RIGHT);
    }
    
    /*
        Aggiorna il grafico sostituendo i dati vecchi con quelli passati dal chiamante.
        La lista datiNuovi (passata per riferimento) contiene una lista di coppie chiave-valore, 
        dove la chiave e' il nome della categoria e il valore e' l'importo totale di tale
        categoria.
        La variabile totaleSpese serve per calcolare la percentuale di ogni categoria
    */
    public void aggiornaGrafico(List<Pair<String, Double>> datiNuovi, double totaleSpese) { 
        this.datiGrafico.clear();

        for (Pair<String, Double> elencoDati : datiNuovi) { 
            this.datiGrafico.add(
                new PieChart.Data(
                        elencoDati.getKey() + " " + 
                        (new DecimalFormat("0.00")).format(elencoDati.getValue()/totaleSpese * 100) + "%", 
                            elencoDati.getValue()
                )
            );
        }
    }
    
    /*
        Restituisce l'oggetto grafico della classe, ovvero la PieChart, 
        cosi' da poter essere inserita nell' interfaccia grafica
    */
    public Node getGraficoGUI() {
        return grafico;
    }
}

/* Note:
    1) La classe PieChart mostra un grafico a torta
        https://docs.oracle.com/javafx/2/api/javafx/scene/chart/PieChart.html
    2) Lista contenente i dati del grafico (coppie chiave-valore)
*/