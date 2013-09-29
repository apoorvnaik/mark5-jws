

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import com.webrenderer.swing.*;
import com.webrenderer.swing.IBrowserCanvas;
import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import com.webrenderer.swing.dom.w3c.Node;
import com.webrenderer.swing.event.MouseAdapter;
import com.webrenderer.swing.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;

/**
 *
 * @author Apoorv
 */
public class MainFrame {

    /**
     * @param args the command line arguments
     */
        
	public static void main(String[] args) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse("C://JWebScraper//projects//123//123-pattern.xml");
            NodeList list = doc.getElementsByTagName("Type"),list1;
            org.w3c.dom.Node node;
            //list = doc.getElementsByTagName("FieldName")
            System.out.println(list.getLength());
            for(int i=0; i < list.getLength() ; i++)
            {
                node = list.item(i).getChildNodes().item(i);
                if(node.getNodeType() != 3)
                    System.out.println("Node Name : "+node.getNodeName());
                //System.out.println("Node's Child: "+node.getChildNodes().item(0).getTextContent());
                list1 = doc.getElementsByTagName(node.getNodeName());
                System.out.println(list1.getLength());
            }
            
        } catch (SAXException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (ParserConfigurationException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

	}



}
