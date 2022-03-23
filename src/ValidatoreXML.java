import java.io.*;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/*
    ValidatoreXML offre metodi per la validazione di file XML. 
    Restituisce true se l'operazione di validazione ha successo, false altrimenti.
*/
public class ValidatoreXML {
    
    /*
        Questo metodo valida un file XML secondo un dato schema XSD
    */
    public static boolean valida(String fileXmlDaValidare, String fileSchemaURI){
        
        try{
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();	// 1)
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);   // 2)
            Document d = db.parse(new InputSource(new StringReader(fileXmlDaValidare)));        // 3)
            Schema s = sf.newSchema(new StreamSource(new File(fileSchemaURI)));                 // 4)
            s.newValidator().validate(new DOMSource(d));                                        // 5)
            
            return true;
        }
        catch(SAXException ex){
            System.err.println("Errore durante la validazione: " + ex.getMessage());
        }
        catch (IOException | ParserConfigurationException ex) {
                System.err.println("Impossibile validare la stringa XML data: " + ex.getMessage());
        }
        return false;
    }
}

/* Note:
    1) Instanzia un parser che produca oggetti DOM da documenti XML
    2) Questa instanza di SchemaFactory legge rappresentazioni esterne di schemi XML, per la validazione
    3) Estrae l'oggetto DOM dalla stringa XML da validare
    4) Estra l'oggetto schema vero e proprio dal file XSD
    5) Crea un oggetto validatore che valida il documento XML sullo schema caricato.
        In caso di un errore nella validazione lancia una SAXException
*/