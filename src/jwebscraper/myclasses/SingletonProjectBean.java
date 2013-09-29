/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jwebscraper.myclasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Apoorv
 */
public class SingletonProjectBean implements Serializable {
    private String projectName;
    private String projectUrl;
    private String projectFolder;
    private String projectConfigFile;
    private transient DocumentBuilderFactory dbf;
    private transient DocumentBuilder db;
    private transient Document document;
    private transient Element rootElement,element[];
    private transient TransformerFactory tf ;
    private transient Transformer t;
    private transient DOMSource source;
    private transient StreamResult result;
    
    public SingletonProjectBean() {
        try {
            projectName = null;
            projectUrl = null;
            projectFolder = null;
            projectConfigFile = null;
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
            document = db.newDocument();
            
            tf = TransformerFactory.newInstance();
            t = tf.newTransformer();
            source = null;
            result = null;

                    } catch (TransformerConfigurationException ex) {
            Logger.getLogger(SingletonProjectBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SingletonProjectBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    public String getProjectConfigFile() {
        return projectConfigFile;
    }

    public void setProjectConfigFile(String projectConfigFile) {
        this.projectConfigFile = projectConfigFile;
    }

    public String getProjectFolder() {
        return projectFolder;
    }

    public void setProjectFolder(String projectFolder) {
        this.projectFolder = projectFolder;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    
    public void initialiseElementArray(int size)
    {
        element = new Element[size];
        //System.out.print(element.length);
    }
    
    public void addExtractionData(String param1, String param2, String param3, String param4, String param5, int index) {
        element[index] = document.createElement("DATASET");
        element[index].appendChild(document.createElement("FieldName")).appendChild(document.createTextNode(param1));
        element[index].appendChild(document.createElement("HTML-Path")).appendChild(document.createTextNode(param2));
        element[index].appendChild(document.createElement("CSS")).appendChild(document.createTextNode(param3));
        element[index].appendChild(document.createElement("Type")).appendChild(document.createTextNode(param4));
        element[index].appendChild(document.createElement("Content")).appendChild(document.createTextNode(param5));
    }
    private  void saveExtractionPattern()
    {
        try {
            
            rootElement = document.createElement("DATA");
            for(int i=0; i< element.length; i++)
            {
                //System.out.println("inside for");
                rootElement.appendChild(element[i]);
            
            }
            tf.setAttribute("indent-number", 2);
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            source = new DOMSource(rootElement);
            result = new StreamResult(new File("C://JWebScraper//projects//" + projectName + "//" + projectName + "-pattern.xml"));
            t.transform(source, result);
        } catch (TransformerException ex) {
            Logger.getLogger(SingletonProjectBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void tabulateExtractionPattern()
    {
        
    }

    public static void saveProject(SingletonProjectBean projectBean ) throws FileNotFoundException, IOException {
            
            FileOutputStream fos = null;
            File file = new File("C://JWebScraper//projects//"+projectBean.getProjectName()+"//");
            file.mkdirs();
            file = new File("C://JWebScraper//projects//"+projectBean.getProjectName()+"//"+projectBean.getProjectConfigFile());
            file.createNewFile();
            fos = new FileOutputStream(file);
            //XMLEncoder encoder = new XMLEncoder(fos);
            ObjectOutputStream writeObj = new ObjectOutputStream(fos);
            writeObj.writeObject(projectBean);
            writeObj.close();
            //encoder.writeObject(projectBean);
            //encoder.close();
            fos.close();
            
            //projectBean.saveExtractionPattern();
    }

    public void updateAndSaveProject() throws FileNotFoundException, IOException
    {
        saveExtractionPattern();
    }
    
}
