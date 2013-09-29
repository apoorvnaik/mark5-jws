/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

/**
 *
 * @author Apoorv
 */
public class Test {
    JFrame frame = new JFrame("Frame");
    JTree tree = new JTree();
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane;
    JOptionPane jOptionPane1;
    Document document;

    public Test() throws IOException {
            FileReader fr = null;
            frame.setSize(200, 200);
            //frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            File file = new File("C://JWebScraper//temp//page.html");
            //System.out.println(file.createNewFile());
            textArea.append(""+file.createNewFile());
            //StringBuilder sb = new StringBuilder("http://www.google.co.in/images?um=1&hl=en&biw=1024&bih=544&tbs=isch%3A1&sa=1&q=sachin+tendulkar&btnG=Search&aq=f&aqi=&aql=&oq=");
            URL url = new URL("http://watches.shop.ebay.in/Branded-/137495/i.html?_catref=1");
            StringBuilder builder = new StringBuilder();
            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            while(bis.read()!=-1)
                builder.append(bis.read());
            bis.close();
            //url.
            FileWriter writer = new FileWriter(file);
            writer.write(builder.toString());
            writer.close();
            //jOptionPane1.showMessageDialog(frame, builder.toString());
            textArea.append(builder.toString());
            frame.add(textArea);
            frame.setVisible(true);
            /*
            try {
                File file = new File("c://test.html");
                fr = new FileReader(file);
                document = Jsoup.parse(file, fr.getEncoding());
 
                fr.close();
                tree = new JTree(createAndProcessTreeNode(document));
                
                //tree.setScrollsOnExpand(true);
                frame.add(tree);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                //showContents(document);


            } catch (FileNotFoundException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/

    }


    public static void main(String []arg) throws IOException
    {
          Test obj = new Test();
        
    }

    private void showContents(Document document)
    {
        int i=0;
        frame.remove(tree);
        textArea.setLineWrap(true);
        if(document.children().size()>0)
            while(i < document.children().size())
            {
                processElement(document.child(i));
                i++;
            }
        scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
        frame.repaint();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void processElement(Element element)
    {
        int childCount = element.children().size();
        int i=0;
        Element child ;
        textArea.append("\nElement Name: "+element.nodeName());
        textArea.append("\nElement's Child count: "+childCount);
        while(i < childCount)
        {
            child = element.child(i);

            if(child.children().size() > 0)
                processElement(child);

            textArea.append("\n\n#####-- Child of "+child.nodeName()+" --#####");
            textArea.append("\n-------------- Child "+i+" BEGIN's HERE-------------");
            textArea.append("\nChild Count: "+child.children().size());
            textArea.append("\n\nHTML: "+child.html());
            textArea.append("\n\nOWNText: "+child.ownText());
            textArea.append("\n\ntoString : "+child.toString());
            textArea.append("\n-------------- Child "+i+" ENDs HERE---------------");
            i++;
        }
    }
     private MutableTreeNode createTree(Node parentNode){

         DefaultMutableTreeNode  treeNode = null;
         String name;

         name = parentNode.nodeName();
                 
         treeNode = new DefaultMutableTreeNode(name);

         if(parentNode.childNodes().size() > 0)
         {
            List children;
            int  numChildren;
            Node node;
            String data;

            children = parentNode.childNodes();
            

            if( children != null )
            {
                numChildren = children.size();

                for (int i=0; i < numChildren; i++)
                {
                   node = (Node) children.get(i);
                   if( node != null )
                   {
                        treeNode.add( createTree(node) );
                   }
                }
             }
         }
         return treeNode;
    }

    private MutableTreeNode createAndProcessTreeNode(Element docElement)
    {
        DefaultMutableTreeNode treeNode = null;
        String nodeName = docElement.nodeName(), nodeValue = docElement.toString(), nodeContent= docElement.html();
        int childCount = docElement.children().size();

        treeNode = new DefaultMutableTreeNode(nodeName);

        if(childCount > 0)
        {
            int i=0;
            while(i < childCount )
            {
                treeNode.add(createAndProcessTreeNode(docElement.child(i)));
                i++;
            }
        }

        if (childCount == 0)
        {
            treeNode.add(new DefaultMutableTreeNode("HTML Representation: "+nodeValue));
            treeNode.add(new DefaultMutableTreeNode("HTML Content       : "+nodeContent));
        }

        return treeNode;
        
        
    }
}



