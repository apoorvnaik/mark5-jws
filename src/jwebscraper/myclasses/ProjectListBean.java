/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jwebscraper.myclasses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Apoorv
 */
public class ProjectListBean implements Serializable {
    private List<String> ProjectList ;
    private Map<String, String> ConfigFileMap ;
    
    public ProjectListBean() {
        ProjectList = new ArrayList<String>();
        ConfigFileMap = new HashMap<String, String>();
    }

    public void addProject(String name, String configFile)
    {
        ProjectList.add(name);
        ConfigFileMap.put(name, configFile);
    }

    public List<String> getProjectList()
    {
        return ProjectList;
    }

    public int getProjectCount()
    {
        return ProjectList.size();
    }
    
    public String getConfigFile(String projectName)
    {
        return ConfigFileMap.get(projectName);
    }

   
}
