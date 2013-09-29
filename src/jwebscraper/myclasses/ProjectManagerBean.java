/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jwebscraper.myclasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;

/**
 *
 * @author Apoorv
 */
public class ProjectManagerBean {
    public static boolean firstRun = false;
    public static ProjectListBean loadProjects() throws FileNotFoundException, IOException, ClassNotFoundException{

        ProjectListBean listBean = new ProjectListBean();
        File file = new File("C://JWebScraper//projects//");
        int projectCount = file.listFiles().length;
        File[] projectFilePath = file.listFiles();
        String[] projectNames = file.list();
        for (int count = 0 ; count < projectCount; count++) {
            listBean.addProject(projectNames[count],projectFilePath[count].getAbsolutePath());
        }

        return listBean;

       }

    public static SingletonProjectBean loadProjectBean(ProjectListBean listBean , String name){
        FileInputStream fis = null;
        SingletonProjectBean tempObject = null;
        try {
            File file = new File("C://JWebScraper//projects//"+name.replaceFirst(".xml", "")+"//"+name);
            fis = new FileInputStream(file);
            ObjectInputStream readObj = new ObjectInputStream(fis);
            tempObject = (SingletonProjectBean) readObj.readObject();
            readObj.close();
            return tempObject;

        } catch (Exception ex) {
            Logger.getLogger(ProjectManagerBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ProjectManagerBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            return tempObject;
        }

    }

    public static JTable createTabularData(){
        return new JTable() ;
    }

    public static boolean isFirstRun()
    {
        File dir = new File("C://JWebScraper//projects//");
        if(dir.list().length==0) {
            firstRun = true;
        }
        else {
            firstRun = false;
        }

        return firstRun;
    }
}
