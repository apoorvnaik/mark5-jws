/*
 * JWebScraperView.java
 */

package jwebscraper;

import com.webrenderer.swing.*;
import com.webrenderer.swing.dom.IDocument;
import com.webrenderer.swing.dom.IElement;
import com.webrenderer.swing.dom.IElementCollection;
import com.webrenderer.swing.event.MouseAdapter;
import com.webrenderer.swing.event.MouseEvent;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.xml.parsers.DocumentBuilderFactory;
import jwebscraper.myclasses.ProjectListBean;
import jwebscraper.myclasses.ProjectManagerBean;
import jwebscraper.myclasses.SingletonProjectBean;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * The application's main frame.
 */
public class JWebScraperView extends FrameView {

    public JWebScraperView(SingleFrameApplication app) {
        super(app);

        createFolderStructure();
        
        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = JWebScraperApp.getApplication().getMainFrame();
            aboutBox = new JWebScraperAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        JWebScraperApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        projectPanel = new javax.swing.JPanel();
        vertSplitPane = new javax.swing.JSplitPane();
        existingProjectPanel = new org.jdesktop.swingx.JXTitledPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        browserPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        importProjectItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jToolBar = new javax.swing.JToolBar();
        newProjectButton = new javax.swing.JButton();
        openProjectButton = new javax.swing.JButton();
        saveProjectButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        prjExecute = new javax.swing.JButton();
        prjPause = new javax.swing.JButton();
        prjStop = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        helpButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jFileChooser = new javax.swing.JFileChooser();
        newProjectDialog = new javax.swing.JDialog();
        vertSplitPane1 = new javax.swing.JSplitPane();
        newProjectPanel1 = new org.jdesktop.swingx.JXTitledPanel();
        jLabel8 = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        projectNameTextField = new javax.swing.JTextField();
        startUrlTextField = new javax.swing.JTextField();
        projectPathTextField = new javax.swing.JTextField();
        configFilePathTextField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        browseFolderButton = new javax.swing.JButton();
        browseFileButton = new javax.swing.JButton();
        extractionButton = new javax.swing.JButton();
        projectDefintionDialog = new javax.swing.JDialog();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jXTitledPanel2 = new org.jdesktop.swingx.JXTitledPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        domtree = new javax.swing.JTree();
        jSplitPane3 = new javax.swing.JSplitPane();
        jXTitledPanel1 = new org.jdesktop.swingx.JXTitledPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        extractionFieldTable = new javax.swing.JTable();
        jXTitledPanel3 = new org.jdesktop.swingx.JXTitledPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        elementDetailArea = new javax.swing.JTextArea();
        jOptionPane1 = new javax.swing.JOptionPane();
        currentProjectPanel = new javax.swing.JPanel();
        vertSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane4 = new javax.swing.JSplitPane();
        resultsPanel = new org.jdesktop.swingx.JXTitledPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        resultsTable = new javax.swing.JTable();
        projectConfigPanel = new org.jdesktop.swingx.JXTitledPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        projectConfigTable = new javax.swing.JTable();
        browserPanel1 = new javax.swing.JPanel();

        mainPanel.setMinimumSize(new java.awt.Dimension(800, 600));
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        projectPanel.setName("projectPanel"); // NOI18N

        vertSplitPane.setDividerLocation(200);
        vertSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        vertSplitPane.setDividerLocation(mainPanel.getHeight()*0.5);
        vertSplitPane.setLastDividerLocation(200);
        vertSplitPane.setName("vertSplitPane"); // NOI18N
        vertSplitPane.setPreferredSize(new java.awt.Dimension(800, 600));
        vertSplitPane.setTopComponent(existingProjectPanel);
        vertSplitPane.setBottomComponent(browserPanel);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jwebscraper.JWebScraperApp.class).getContext().getResourceMap(JWebScraperView.class);
        existingProjectPanel.setTitle(resourceMap.getString("existingProjectPanel.title")); // NOI18N
        existingProjectPanel.setTitleForeground(resourceMap.getColor("existingProjectPanel.titleForeground")); // NOI18N
        existingProjectPanel.setName("existingProjectPanel"); // NOI18N
        existingProjectPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        existingProjectPanel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                existingProjectPanelFocusGained(evt);
            }
        });

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Project Name", "Seed URL", "Status"
            }
        ));
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout existingProjectPanelLayout = new javax.swing.GroupLayout(existingProjectPanel.getContentContainer());
        existingProjectPanel.getContentContainer().setLayout(existingProjectPanelLayout);
        existingProjectPanelLayout.setHorizontalGroup(
            existingProjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 793, Short.MAX_VALUE)
        );
        existingProjectPanelLayout.setVerticalGroup(
            existingProjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
        );

        vertSplitPane.setLeftComponent(existingProjectPanel);

        browserPanel.setToolTipText(resourceMap.getString("browserPanel.toolTipText")); // NOI18N
        browserPanel.setMaximumSize(new java.awt.Dimension(400, 400));
        browserPanel.setMinimumSize(new java.awt.Dimension(100, 100));
        browserPanel.setName("browserPanel"); // NOI18N
        browserPanel.setLayout(new java.awt.BorderLayout());
        BrowserFactory.setLicenseData( "shock2k7" , "0000EOLVVUB6UTIL2LE6A9M5" );
        //browser = BrowserFactory.spawnMozilla();
        //browser.setProxyProtocol(new ProxySetting(ProxySetting.PROTOCOL_ALL, "172.16.1.100", 8080));
        //browser.enableProxy();
        //browser1.loadURL("http://www.google.com");
        browser1.loadURL("file://C://rwh.html");
        //browser1.loadURL("file:///C://JWebScraper//temp//cleanedXml.xml");
        /*try{
            File file = new File("C://JWebScraper//temp//cleanedXml.xml");
            StringBuilder html = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null)
            {
                html.append(line);
                //html.
            }
            browser1.loadHTML(html.toString(), "");
        }
        catch(IOException ex)
        {
            System.out.println(ex.getLocalizedMessage());
        }*/
        browserPanel.add(browser1.getComponent());

        try{
            browser1.addMouseListener(new MouseAdapter()
                {
                    public void onClick(MouseEvent e) {
                        //Getting the IElement from the corresponding click
                        browser1.setHTMLEditingMode(Boolean.TRUE);
                        IDocument iDocument = browser1.getDocument();
                        IElement element = iDocument.elementFromPoint(e.getX(), e.getY());

                        statusMessageLabel.setText("ParentName:" + element.getParentElement().getOuterHTML());
                    }});
                }
                catch(Exception e){}

                vertSplitPane.setRightComponent(browserPanel);

                javax.swing.GroupLayout projectPanelLayout = new javax.swing.GroupLayout(projectPanel);
                projectPanel.setLayout(projectPanelLayout);
                projectPanelLayout.setHorizontalGroup(
                    projectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vertSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
                );
                projectPanelLayout.setVerticalGroup(
                    projectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(vertSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
                );

                jTabbedPane1.addTab(resourceMap.getString("projectPanel.TabConstraints.tabTitle"), projectPanel); // NOI18N

                javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
                mainPanel.setLayout(mainPanelLayout);
                mainPanelLayout.setHorizontalGroup(
                    mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE)
                );
                mainPanelLayout.setVerticalGroup(
                    mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
                );

                menuBar.setName("menuBar"); // NOI18N

                fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
                fileMenu.setName("fileMenu"); // NOI18N

                importProjectItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
                importProjectItem.setText(resourceMap.getString("importProjectItem.text")); // NOI18N
                importProjectItem.setName("importProjectItem"); // NOI18N
                fileMenu.add(importProjectItem);

                javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(jwebscraper.JWebScraperApp.class).getContext().getActionMap(JWebScraperView.class, this);
                exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
                exitMenuItem.setName("exitMenuItem"); // NOI18N
                fileMenu.add(exitMenuItem);

                menuBar.add(fileMenu);

                helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
                helpMenu.setName("helpMenu"); // NOI18N

                aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
                aboutMenuItem.setName("aboutMenuItem"); // NOI18N
                helpMenu.add(aboutMenuItem);

                menuBar.add(helpMenu);

                statusPanel.setName("statusPanel"); // NOI18N

                statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

                statusMessageLabel.setName("statusMessageLabel"); // NOI18N

                statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

                progressBar.setName("progressBar"); // NOI18N

                javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
                statusPanel.setLayout(statusPanelLayout);
                statusPanelLayout.setHorizontalGroup(
                    statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(statusMessageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 634, Short.MAX_VALUE)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusAnimationLabel)
                        .addContainerGap())
                );
                statusPanelLayout.setVerticalGroup(
                    statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusMessageLabel)
                            .addComponent(statusAnimationLabel)
                            .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                );

                jToolBar.setFloatable(false);
                jToolBar.setRollover(true);
                jToolBar.setName("jToolBar"); // NOI18N

                newProjectButton.setText(resourceMap.getString("newProjectButton.text")); // NOI18N
                newProjectButton.setFocusable(false);
                newProjectButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                newProjectButton.setName("newProjectButton"); // NOI18N
                newProjectButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                newProjectButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        newProjectButtonActionPerformed(evt);
                    }
                });
                jToolBar.add(newProjectButton);

                openProjectButton.setText(resourceMap.getString("openProjectButton.text")); // NOI18N
                openProjectButton.setFocusable(false);
                openProjectButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                openProjectButton.setName("openProjectButton"); // NOI18N
                openProjectButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                jToolBar.add(openProjectButton);

                saveProjectButton.setText(resourceMap.getString("saveProjectButton.text")); // NOI18N
                saveProjectButton.setFocusable(false);
                saveProjectButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                saveProjectButton.setName("saveProjectButton"); // NOI18N
                saveProjectButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                jToolBar.add(saveProjectButton);

                jSeparator1.setName("jSeparator1"); // NOI18N
                jToolBar.add(jSeparator1);

                prjExecute.setText(resourceMap.getString("prjExecute.text")); // NOI18N
                prjExecute.setFocusable(false);
                prjExecute.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                prjExecute.setName("prjExecute"); // NOI18N
                prjExecute.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                prjExecute.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        prjExecuteActionPerformed(evt);
                    }
                });
                jToolBar.add(prjExecute);

                prjPause.setText(resourceMap.getString("prjPause.text")); // NOI18N
                prjPause.setFocusable(false);
                prjPause.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                prjPause.setName("prjPause"); // NOI18N
                prjPause.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                prjPause.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        prjPauseActionPerformed(evt);
                    }
                });
                jToolBar.add(prjPause);

                prjStop.setText(resourceMap.getString("prjStop.text")); // NOI18N
                prjStop.setFocusable(false);
                prjStop.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                prjStop.setName("prjStop"); // NOI18N
                prjStop.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                prjStop.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        prjStopActionPerformed(evt);
                    }
                });
                jToolBar.add(prjStop);

                jSeparator2.setName("jSeparator2"); // NOI18N
                jToolBar.add(jSeparator2);

                helpButton.setText(resourceMap.getString("helpButton.text")); // NOI18N
                helpButton.setFocusable(false);
                helpButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
                helpButton.setName("helpButton"); // NOI18N
                helpButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
                helpButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        helpButtonActionPerformed(evt);
                    }
                });
                jToolBar.add(helpButton);

                jSeparator3.setName("jSeparator3"); // NOI18N
                jToolBar.add(jSeparator3);

                jPanel1.setName("jPanel1"); // NOI18N

                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
                jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
                jLabel1.setName("jLabel1"); // NOI18N

                jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
                jComboBox1.setName("jComboBox1"); // NOI18N
                jComboBox1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jComboBox1ActionPerformed(evt);
                    }
                });

                jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
                jLabel2.setName("jLabel2"); // NOI18N

                jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
                jLabel3.setName("jLabel3"); // NOI18N

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                );
                jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                try{
                    if(!ProjectManagerBean.isFirstRun())
                    {
                        ProjectListBean projectListBean = ProjectManagerBean.loadProjects();
                        int projects = projectListBean.getProjectList().size();
                        String[] comboList = new String[projects+1];
                        comboList[0]="--Select--";
                        for(int i=0; i< projects ;i++)
                        {
                            comboList[i+1] = projectListBean.getProjectList().get(i).replaceAll(".xml", "");
                        }
                        jComboBox1.setModel(new DefaultComboBoxModel(comboList));
                    }
                    else
                    jComboBox1.setModel(new DefaultComboBoxModel(new String[] {"No Projects Yet"}));
                }
                catch(Exception exception)
                {
                    jOptionPane1.showMessageDialog(JWebScraperApp.getApplication().getMainFrame().getFocusOwner(), exception.getMessage(),"Error !!!" ,JOptionPane.ERROR_MESSAGE);
                }

                jToolBar.add(jPanel1);

                jFileChooser.setDialogTitle(resourceMap.getString("jFileChooser.dialogTitle")); // NOI18N
                jFileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
                jFileChooser.setName("jFileChooser"); // NOI18N

                newProjectDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                newProjectDialog.setTitle(resourceMap.getString("newProjectDialog.title")); // NOI18N
                newProjectDialog.setName("newProjectDialog"); // NOI18N

                vertSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
                vertSplitPane1.setLastDividerLocation(200);
                vertSplitPane1.setName("vertSplitPane1"); // NOI18N

                newProjectPanel1.setTitle(resourceMap.getString("newProjectPanel1.title")); // NOI18N
                newProjectPanel1.setName("newProjectPanel1"); // NOI18N

                jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
                jLabel8.setName("jLabel8"); // NOI18N

                saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
                saveButton.setName("saveButton"); // NOI18N
                saveButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        saveButtonActionPerformed(evt);
                    }
                });

                jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
                jLabel9.setName("jLabel9"); // NOI18N

                jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
                jLabel10.setName("jLabel10"); // NOI18N

                jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
                jLabel11.setName("jLabel11"); // NOI18N

                projectNameTextField.setName("projectNameTextField"); // NOI18N
                projectNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyReleased(java.awt.event.KeyEvent evt) {
                        projectNameTextFieldKeyReleased(evt);
                    }
                });

                startUrlTextField.setText(resourceMap.getString("startUrlTextField.text")); // NOI18N
                startUrlTextField.setName("startUrlTextField"); // NOI18N
                startUrlTextField.addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        startUrlTextFieldFocusLost(evt);
                    }
                });

                projectPathTextField.setEnabled(false);
                projectPathTextField.setName("projectPathTextField"); // NOI18N

                configFilePathTextField.setEnabled(false);
                configFilePathTextField.setName("configFilePathTextField"); // NOI18N

                cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
                cancelButton.setName("cancelButton"); // NOI18N
                cancelButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        cancelButtonActionPerformed(evt);
                    }
                });

                browseFolderButton.setText(resourceMap.getString("browseFolderButton.text")); // NOI18N
                browseFolderButton.setName("browseFolderButton"); // NOI18N
                browseFolderButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        browseFolderButtonActionPerformed(evt);
                    }
                });

                browseFileButton.setText(resourceMap.getString("browseFileButton.text")); // NOI18N
                browseFileButton.setName("browseFileButton"); // NOI18N
                browseFileButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        browseFileButtonActionPerformed(evt);
                    }
                });

                extractionButton.setText(resourceMap.getString("extractionButton.text")); // NOI18N
                extractionButton.setName("extractionButton"); // NOI18N
                extractionButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        extractionButtonActionPerformed(evt);
                    }
                });

                javax.swing.GroupLayout newProjectPanel1Layout = new javax.swing.GroupLayout(newProjectPanel1.getContentContainer());
                newProjectPanel1.getContentContainer().setLayout(newProjectPanel1Layout);
                newProjectPanel1Layout.setHorizontalGroup(
                    newProjectPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(newProjectPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(newProjectPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(newProjectPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(newProjectPanel1Layout.createSequentialGroup()
                                .addGroup(newProjectPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(projectPathTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
                                    .addComponent(configFilePathTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, newProjectPanel1Layout.createSequentialGroup()
                                        .addComponent(saveButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(extractionButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cancelButton)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(newProjectPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(browseFolderButton)
                                    .addComponent(browseFileButton)))
                            .addComponent(startUrlTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                            .addComponent(projectNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE))
                        .addContainerGap())
                );
                newProjectPanel1Layout.setVerticalGroup(
                    newProjectPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(newProjectPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(newProjectPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(projectNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(newProjectPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(startUrlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(newProjectPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(browseFolderButton)
                            .addComponent(projectPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(newProjectPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(browseFileButton)
                            .addComponent(configFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(newProjectPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(saveButton)
                            .addComponent(extractionButton)
                            .addComponent(cancelButton))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );

                if(projectSaved == true)
                extractionButton.setEnabled(true);
                else
                extractionButton.setEnabled(false);

                vertSplitPane1.setTopComponent(newProjectPanel1);

                //browser1.setProxyProtocol(new ProxySetting(ProxySetting.PROTOCOL_ALL, "172.16.1.100", 8080));
                //browser1.enableProxy();
                browser2.loadURL("file:///C://rwh.html");

                javax.swing.GroupLayout newProjectDialogLayout = new javax.swing.GroupLayout(newProjectDialog.getContentPane());
                newProjectDialog.getContentPane().setLayout(newProjectDialogLayout);
                newProjectDialogLayout.setHorizontalGroup(
                    newProjectDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 800, Short.MAX_VALUE)
                    .addGroup(newProjectDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(vertSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE))
                );
                newProjectDialogLayout.setVerticalGroup(
                    newProjectDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 600, Short.MAX_VALUE)
                    .addGroup(newProjectDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(vertSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
                );

                vertSplitPane1.setBottomComponent(browser2.getComponent());

                newProjectDialog.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);

                projectDefintionDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                projectDefintionDialog.setTitle(resourceMap.getString("projectDefintionDialog.title")); // NOI18N
                projectDefintionDialog.setName("projectDefintionDialog"); // NOI18N
                projectDefintionDialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowActivated(java.awt.event.WindowEvent evt) {
                        projectDefintionDialogWindowActivated(evt);
                    }
                    public void windowClosed(java.awt.event.WindowEvent evt) {
                        projectDefintionDialogWindowClosed(evt);
                    }
                });

                jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
                jSplitPane1.setName("jSplitPane1"); // NOI18N

                jSplitPane2.setName("jSplitPane2"); // NOI18N

                jXTitledPanel2.setTitle(resourceMap.getString("jXTitledPanel2.title")); // NOI18N
                jXTitledPanel2.setName("jXTitledPanel2"); // NOI18N

                jScrollPane1.setName("jScrollPane1"); // NOI18N

                javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("HTML");
                javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Head");
                javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Nothing");
                treeNode2.add(treeNode3);
                treeNode1.add(treeNode2);
                treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Body");
                treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Nothing");
                treeNode2.add(treeNode3);
                treeNode1.add(treeNode2);
                domtree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
                domtree.setAutoscrolls(true);
                domtree.setName("domtree"); // NOI18N
                domtree.setShowsRootHandles(true);
                domtree.setToggleClickCount(1);
                domtree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
                    public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                        domtreeValueChanged(evt);
                    }
                });
                jScrollPane1.setViewportView(domtree);

                javax.swing.GroupLayout jXTitledPanel2Layout = new javax.swing.GroupLayout(jXTitledPanel2.getContentContainer());
                jXTitledPanel2.getContentContainer().setLayout(jXTitledPanel2Layout);
                jXTitledPanel2Layout.setHorizontalGroup(
                    jXTitledPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
                );
                jXTitledPanel2Layout.setVerticalGroup(
                    jXTitledPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                );

                jSplitPane2.setRightComponent(jXTitledPanel2);

                jSplitPane2.setLeftComponent(browser3.getComponent());
                browser3.disableCache();
                browser3.setJavascriptEnabled(false);

                browser3.addMouseListener(new MouseAdapter(){
                    public void onClick(MouseEvent e) {
                        //Getting the IElement from the corresponding click
                        browser3.setHTMLEditingMode(Boolean.TRUE);
                        IDocument iDocument = browser3.getDocument();
                        IElement element = iDocument.elementFromPoint(e.getX(), e.getY());
                        //elementList.add(element);
                        //String path = obtainXPathOfElement(element);
                        //System.out.println(obtainXPathOfElement(element));
                        TableColumnModel columnModel = extractionFieldTable.getColumnModel();
                        dtm = (DefaultTableModel)extractionFieldTable.getModel();
                        rowData = new String[]{"Data Field "+(++rowCount1),obtainXPathOfElement(element),(element.getClassName().equals("")?"-":element.getClassName()),(element.getClassName().equals("")?"HTML":"CSS"),element.getInnerHTML()};
                        elementDetailArea.append("Row Data = "+rowData[0]+"\n"+rowData[1]+"\n"+rowData[2]+"\n"+rowData[3]);
                        dtm.insertRow(rowCount, rowData);
                        rowCount++;
                        extractionFieldTable.setColumnModel(columnModel);
                        extractionFieldTable.setModel(dtm);
                        extractionFieldTable.repaint();

                    }});

                    jSplitPane1.setTopComponent(jSplitPane2);

                    jSplitPane3.setName("jSplitPane3"); // NOI18N

                    jXTitledPanel1.setTitle(resourceMap.getString("jXTitledPanel1.title")); // NOI18N
                    jXTitledPanel1.setAutoscrolls(true);
                    jXTitledPanel1.setName("jXTitledPanel1"); // NOI18N
                    jXTitledPanel1.setScrollableHeightHint(org.jdesktop.swingx.ScrollableSizeHint.NONE);
                    jXTitledPanel1.setScrollableWidthHint(org.jdesktop.swingx.ScrollableSizeHint.NONE);

                    jScrollPane3.setName("jScrollPane3"); // NOI18N

                    extractionFieldTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                            "Field Name ", "HTML Path", "CSS Class", "Type(Auto Detection)", "Content"
                        }
                    ) {
                        Class[] types = new Class [] {
                            java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
                        };
                        boolean[] canEdit = new boolean [] {
                            true, false, false, false, false
                        };

                        public Class getColumnClass(int columnIndex) {
                            return types [columnIndex];
                        }

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            return canEdit [columnIndex];
                        }
                    });
                    extractionFieldTable.setName("extractionFieldTable"); // NOI18N
                    extractionFieldTable.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            extractionFieldTableMouseClicked(evt);
                        }
                    });
                    extractionFieldTable.addKeyListener(new java.awt.event.KeyAdapter() {
                        public void keyPressed(java.awt.event.KeyEvent evt) {
                            extractionFieldTableKeyPressed(evt);
                        }
                    });
                    jScrollPane3.setViewportView(extractionFieldTable);
                    extractionFieldTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("extractionFieldTable.columnModel.title0")); // NOI18N
                    extractionFieldTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("extractionFieldTable.columnModel.title1")); // NOI18N
                    extractionFieldTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("extractionFieldTable.columnModel.title4")); // NOI18N
                    extractionFieldTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("extractionFieldTable.columnModel.title2")); // NOI18N
                    extractionFieldTable.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("extractionFieldTable.columnModel.title3")); // NOI18N

                    javax.swing.GroupLayout jXTitledPanel1Layout = new javax.swing.GroupLayout(jXTitledPanel1.getContentContainer());
                    jXTitledPanel1.getContentContainer().setLayout(jXTitledPanel1Layout);
                    jXTitledPanel1Layout.setHorizontalGroup(
                        jXTitledPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                    );
                    jXTitledPanel1Layout.setVerticalGroup(
                        jXTitledPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    );

                    jSplitPane3.setLeftComponent(jXTitledPanel1);

                    jXTitledPanel3.setTitle(resourceMap.getString("jXTitledPanel3.title")); // NOI18N
                    jXTitledPanel3.setName("jXTitledPanel3"); // NOI18N

                    jScrollPane5.setName("jScrollPane5"); // NOI18N

                    elementDetailArea.setColumns(20);
                    elementDetailArea.setEditable(false);
                    elementDetailArea.setRows(5);
                    elementDetailArea.setName("elementDetailArea"); // NOI18N
                    jScrollPane5.setViewportView(elementDetailArea);

                    javax.swing.GroupLayout jXTitledPanel3Layout = new javax.swing.GroupLayout(jXTitledPanel3.getContentContainer());
                    jXTitledPanel3.getContentContainer().setLayout(jXTitledPanel3Layout);
                    jXTitledPanel3Layout.setHorizontalGroup(
                        jXTitledPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                    );
                    jXTitledPanel3Layout.setVerticalGroup(
                        jXTitledPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                    );

                    jSplitPane3.setRightComponent(jXTitledPanel3);

                    jSplitPane1.setRightComponent(jSplitPane3);

                    extractionFieldTable.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void onClick(MouseEvent event){
                            elementDetailArea.setText("Selected Row = "+extractionFieldTable.getSelectedRow());
                        }
                    });

                    javax.swing.GroupLayout projectDefintionDialogLayout = new javax.swing.GroupLayout(projectDefintionDialog.getContentPane());
                    projectDefintionDialog.getContentPane().setLayout(projectDefintionDialogLayout);
                    projectDefintionDialogLayout.setHorizontalGroup(
                        projectDefintionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPane1)
                    );
                    projectDefintionDialogLayout.setVerticalGroup(
                        projectDefintionDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSplitPane1)
                    );

                    jOptionPane1.setName("jOptionPane1"); // NOI18N

                    currentProjectPanel.setName("currentProjectPanel"); // NOI18N

                    vertSplitPane2.setDividerLocation(200);
                    vertSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
                    vertSplitPane2.setLastDividerLocation(200);
                    vertSplitPane2.setName("vertSplitPane2"); // NOI18N
                    vertSplitPane2.setPreferredSize(new java.awt.Dimension(800, 600));

                    jSplitPane4.setDividerLocation(700);
                    jSplitPane4.setName("jSplitPane4"); // NOI18N

                    resultsPanel.setTitle(resourceMap.getString("resultsPanel.title")); // NOI18N
                    resultsPanel.setName("resultsPanel"); // NOI18N
                    resultsPanel.setPreferredSize(new java.awt.Dimension(800, 600));
                    resultsPanel.addFocusListener(new java.awt.event.FocusAdapter() {
                        public void focusGained(java.awt.event.FocusEvent evt) {
                            resultsPanelFocusGained(evt);
                        }
                    });
                    resultsPanel.addAncestorListener(new javax.swing.event.AncestorListener() {
                        public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                        }
                        public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                            resultsPanelAncestorAdded(evt);
                        }
                        public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                            resultsPanelAncestorRemoved(evt);
                        }
                    });

                    jScrollPane4.setName("jScrollPane4"); // NOI18N

                    resultsTable.setAutoCreateRowSorter(true);
                    resultsTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {

                        }
                    ));
                    resultsTable.setName("resultsTable"); // NOI18N
                    resultsTable.setRowSelectionAllowed(false);
                    jScrollPane4.setViewportView(resultsTable);

                    javax.swing.GroupLayout resultsPanelLayout = new javax.swing.GroupLayout(resultsPanel.getContentContainer());
                    resultsPanel.getContentContainer().setLayout(resultsPanelLayout);
                    resultsPanelLayout.setHorizontalGroup(
                        resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
                    );
                    resultsPanelLayout.setVerticalGroup(
                        resultsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                    );

                    jSplitPane4.setLeftComponent(resultsPanel);

                    projectConfigPanel.setTitle(resourceMap.getString("projectConfigPanel.title")); // NOI18N
                    projectConfigPanel.setName("projectConfigPanel"); // NOI18N

                    jScrollPane6.setName("jScrollPane6"); // NOI18N

                    projectConfigTable.setModel(new javax.swing.table.DefaultTableModel(
                        new Object [][] {

                        },
                        new String [] {
                            "Data Field Name", "CSS / HTML Value", "Selector Type", "Sample Conten"
                        }
                    ) {
                        boolean[] canEdit = new boolean [] {
                            false, false, false, false
                        };

                        public boolean isCellEditable(int rowIndex, int columnIndex) {
                            return canEdit [columnIndex];
                        }
                    });
                    projectConfigTable.setEnabled(false);
                    projectConfigTable.setName("projectConfigTable"); // NOI18N
                    jScrollPane6.setViewportView(projectConfigTable);
                    projectConfigTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("projectConfigTable.columnModel.title0")); // NOI18N
                    projectConfigTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("projectConfigTable.columnModel.title1")); // NOI18N
                    projectConfigTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("projectConfigTable.columnModel.title2")); // NOI18N
                    projectConfigTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("projectConfigTable.columnModel.title3")); // NOI18N

                    javax.swing.GroupLayout projectConfigPanelLayout = new javax.swing.GroupLayout(projectConfigPanel.getContentContainer());
                    projectConfigPanel.getContentContainer().setLayout(projectConfigPanelLayout);
                    projectConfigPanelLayout.setHorizontalGroup(
                        projectConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                    );
                    projectConfigPanelLayout.setVerticalGroup(
                        projectConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                    );

                    jSplitPane4.setRightComponent(projectConfigPanel);

                    vertSplitPane2.setTopComponent(jSplitPane4);

                    browserPanel1.setToolTipText(resourceMap.getString("browserPanel1.toolTipText")); // NOI18N
                    browserPanel1.setMaximumSize(new java.awt.Dimension(400, 400));
                    browserPanel1.setMinimumSize(new java.awt.Dimension(100, 100));
                    browserPanel1.setName("browserPanel1"); // NOI18N
                    browserPanel1.setLayout(new java.awt.BorderLayout());
                    //browser.setProxyProtocol(new ProxySetting(ProxySetting.PROTOCOL_ALL, "172.16.1.100", 8080));
                    //browser.enableProxy();
                    //browser.loadURL("http://www.google.com");
                    //browser1.loadURL("file://C://rwh.html");
                    browserPanel1.add(browser4.getComponent());
                    vertSplitPane2.setRightComponent(browserPanel1);

                    javax.swing.GroupLayout currentProjectPanelLayout = new javax.swing.GroupLayout(currentProjectPanel);
                    currentProjectPanel.setLayout(currentProjectPanelLayout);
                    currentProjectPanelLayout.setHorizontalGroup(
                        currentProjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(vertSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
                    );
                    currentProjectPanelLayout.setVerticalGroup(
                        currentProjectPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(vertSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
                    );

                    setComponent(mainPanel);
                    setMenuBar(menuBar);
                    setStatusBar(statusPanel);
                    setToolBar(jToolBar);
                }// </editor-fold>//GEN-END:initComponents

    private void newProjectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newProjectButtonActionPerformed
        // TODO add your handling code here:
        newProjectDialog.pack();
        newProjectDialog.setModal(true);
        newProjectDialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/10, Toolkit.getDefaultToolkit().getScreenSize().height/10);
        newProjectDialog.setVisible(true);

    }//GEN-LAST:event_newProjectButtonActionPerformed

    private void prjExecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prjExecuteActionPerformed
            // TODO add your handling code here:
            project = ProjectManagerBean.loadProjectBean(projectList, "watches.xml");
            projectDefintionDialog.setModal(true);
            projectDefintionDialog.pack();
            projectDefintionDialog.setLocation(0, 0);
            projectDefintionDialog.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height - 50);
            //browser3.loadURL("file:///C://JWebScraper//temp//temp.html");
            //startUrlTextField.setText("file:///C://JwebScraper//temp//temp.html");
            browser3.loadURL("file:///C://jwebscraper//projects//watch//watch-dump.html");
            //startUrlTextField.setText("http://watches.shop.ebay.in/Branded-/137495/i.html?_catref=1");
            jSplitPane1.setDividerLocation(500);
            jSplitPane2.setDividerLocation(0.95);
            jSplitPane3.setDividerLocation(0.75);
            projectDefintionDialog.repaint();
            projectDefintionDialog.setVisible(true);


    }//GEN-LAST:event_prjExecuteActionPerformed

    private void browseFolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseFolderButtonActionPerformed
        // TODO add your handling code here:
        jFileChooser.setDialogTitle("Select Project Folder");
        jFileChooser.showDialog(newProjectDialog, "Set Folder");
        projectPathTextField.setText(jFileChooser.getSelectedFile().toString());
    }//GEN-LAST:event_browseFolderButtonActionPerformed

    private void browseFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseFileButtonActionPerformed
        // TODO add your handling code here:
        jFileChooser.showDialog(newProjectDialog, "Save File");
        jFileChooser.setDialogTitle("Select File Location");
        jFileChooser.setSelectedFile(new File(projectNameTextField.getText()+".xml"));
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        configFilePathTextField.setText(jFileChooser.getSelectedFile().toString());

    }//GEN-LAST:event_browseFileButtonActionPerformed

    private void extractionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extractionButtonActionPerformed
        // TODO add your handling code here:
        startUrlTextField.setText(browser2.getURL());
        project.setProjectUrl(startUrlTextField.getText());
        try {
            SingletonProjectBean.saveProject(project);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingWorker<Void,Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                generateDOMTree(browser2.getURL());
                return null;

            }
        };
        worker.execute();
        newProjectDialog.setModal(false);
        projectDefintionDialog.setModal(true);
        projectDefintionDialog.setLocation(0, 0);
        projectDefintionDialog.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height-50);
        browser3.loadURL("file:///C://JWebScraper//projects//"+project.getProjectName()+"//"+project.getProjectName()+"-dump.html");
        projectDefintionDialog.setVisible(true);
        newProjectDialog.dispose();
        
    }//GEN-LAST:event_extractionButtonActionPerformed

    private void startUrlTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_startUrlTextFieldFocusLost
        // TODO add your handling code here:
        browser2.loadURL(startUrlTextField.getText());
    }//GEN-LAST:event_startUrlTextFieldFocusLost

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // TODO add your handling code here:
        newProjectDialog.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {
            // TODO add your handling code here:
            if(!ProjectManagerBean.isFirstRun())
                projectList = ProjectManagerBean.loadProjects();
            
            project.setProjectName(projectNameTextField.getText());
            project.setProjectUrl(startUrlTextField.getText());
            project.setProjectFolder(projectPathTextField.getText());
            project.setProjectConfigFile(configFilePathTextField.getText());
            projectList.addProject(project.getProjectName(), project.getProjectConfigFile());
            try {
                SingletonProjectBean.saveProject(project);
                //ProjectListBean.saveProjectList(projectList);
                //ProjectManagerBean.saveProjects(projectList);
            } catch (IOException iox) {
                jOptionPane1.showMessageDialog(newProjectDialog, iox.getMessage(), "Exception Occured", JOptionPane.ERROR_MESSAGE);
            }
            extractionButton.setEnabled(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_saveButtonActionPerformed

    private void projectNameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_projectNameTextFieldKeyReleased
        // TODO add your handling code here:
        configFilePathTextField.setText(projectNameTextField.getText()+".xml");
    }//GEN-LAST:event_projectNameTextFieldKeyReleased

    private void existingProjectPanelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_existingProjectPanelFocusGained
        // TODO add your handling code here:
        statusMessageLabel.setText(statusMessageLabel.getText()+" TEST ");
}//GEN-LAST:event_existingProjectPanelFocusGained

    private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
        // TODO add your handling code here:
        //JPanel tempJPanel = cloneJPanel(currentProjectPanel);
        jSplitPane4.setDividerLocation(700);
        currentProjectPanel.repaint();
        jTabbedPane1.add("Active Project Details", currentProjectPanel);
        jTabbedPane1.setSelectedIndex(1);
        //statusMessageLabel.setText("Tab Count: "+jTabbedPane1.getTabCount());
        //jTabbedPane1.repaint();
        //jTabbedPane1.
    }//GEN-LAST:event_helpButtonActionPerformed

    private void resultsPanelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_resultsPanelFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_resultsPanelFocusGained

    private void resultsPanelAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_resultsPanelAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_resultsPanelAncestorAdded

    private void resultsPanelAncestorRemoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_resultsPanelAncestorRemoved
        // TODO add your handling code here:
    }//GEN-LAST:event_resultsPanelAncestorRemoved

    private void domtreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_domtreeValueChanged
        // TODO add your handling code here:
        elementDetailArea.append(evt.getPath().toString()+"\n");
        
    }//GEN-LAST:event_domtreeValueChanged

    private void projectDefintionDialogWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_projectDefintionDialogWindowActivated
        // TODO add your handling code here:
        jSplitPane1.setDividerLocation(500);
        jSplitPane2.setDividerLocation(0.85);
        jSplitPane3.setDividerLocation(0.75);
        projectDefintionDialog.repaint();
        generateDOMTree(startUrlTextField.getText());
        statusMessageLabel.setText(startUrlTextField.getText());
        browser3.allowPopups(false);
    }//GEN-LAST:event_projectDefintionDialogWindowActivated

    private void prjStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prjStopActionPerformed
        // TODO add your handling code here:
        JFrame frame = new JFrame();
        JScrollPane pane = new JScrollPane();
        JTextArea textArea = new JTextArea();
        ProjectListBean listBean ;
        SingletonProjectBean projectBean;
        try {
            listBean = ProjectManagerBean.loadProjects();
            projectBean = ProjectManagerBean.loadProjectBean(listBean, "123");

            textArea.setEditable(false);
            pane.setViewportView(textArea);
            textArea.append("List Bean Values");
            textArea.append("\nTotal Projects: "+listBean.getProjectCount());
            Iterator iterator = listBean.getProjectList().iterator();
            while(iterator.hasNext())
            {
                String name = iterator.next().toString();
                textArea.append("\n" + name);
                textArea.append("\nConfig File: "+listBean.getConfigFile(name));
            }
            textArea.append("\n--------------------------------------------");
            textArea.append("\nProject Bean Values");
            textArea.append("\nName: "+projectBean.getProjectName());
            textArea.append("\nConfig File: "+projectBean.getProjectConfigFile());
            textArea.append("\nSave Folder: "+projectBean.getProjectFolder());
            textArea.append("\nURL: "+projectBean.getProjectUrl());
            textArea.append("\n--------------------------------------------");
            frame.setSize(500,500);
            frame.add(pane);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_prjStopActionPerformed

    private void extractionFieldTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_extractionFieldTableMouseClicked
        // TODO add your handling code here:
        elementDetailArea.setText("Selected Row: "+extractionFieldTable.getSelectedRow());
    }//GEN-LAST:event_extractionFieldTableMouseClicked

    private void extractionFieldTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_extractionFieldTableKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_DELETE)
        {
            dtm = (DefaultTableModel) extractionFieldTable.getModel();
            dtm.removeRow(extractionFieldTable.getSelectedRow());
            //dtm.addRow(new String[]{"","","",""});
            if(rowCount==0)
                rowCount=0;
            else
                rowCount--;
            extractionFieldTable.setModel(dtm);
            extractionFieldTable.repaint();
        }
    }//GEN-LAST:event_extractionFieldTableKeyPressed

    private void projectDefintionDialogWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_projectDefintionDialogWindowClosed
        // TODO add your handling code here:
        dtm = (DefaultTableModel) extractionFieldTable.getModel();
        System.out.print("DTM Row COunt: "+dtm.getRowCount());
        try{
            if(dtm.getRowCount() > 0)
            {
                project.initialiseElementArray(dtm.getRowCount());
                for (int i = 0; i < dtm.getRowCount(); i++)
                {
                    project.addExtractionData(dtm.getValueAt(i, 0).toString(), dtm.getValueAt(i, 1).toString(), dtm.getValueAt(i, 2).toString(), dtm.getValueAt(i, 3).toString(),dtm.getValueAt(i, 4).toString(),i);
                    
                }
                
            }
            System.out.println("Before Saving");

            project.updateAndSaveProject();
            System.out.println("After Saving");
        }catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }//GEN-LAST:event_projectDefintionDialogWindowClosed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
            loadProjectInPanel(jComboBox1.getSelectedItem().toString());
            
        
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void prjPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prjPauseActionPerformed
        // TODO add your handling code here:
            activeProject = ProjectManagerBean.loadProjectBean(projectList, jComboBox1.getSelectedItem().toString()+".xml");
            JFrame frame = new JFrame();
            JScrollPane pane = new JScrollPane();
            JTextArea textArea = new JTextArea();

            textArea.setEditable(false);
            pane.setViewportView(textArea);
            textArea.append("\n--------------------------------------------");
            textArea.append("\nProject Bean Values");
            textArea.append("\nName: "+activeProject.getProjectName());
            textArea.append("\nConfig File: "+activeProject.getProjectConfigFile());
            textArea.append("\nSave Folder: "+activeProject.getProjectFolder());
            textArea.append("\nURL: "+activeProject.getProjectUrl());
            textArea.append("\n--------------------------------------------");
            frame.setSize(500,500);
            frame.add(pane);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);
    }//GEN-LAST:event_prjPauseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseFileButton;
    private javax.swing.JButton browseFolderButton;
    private javax.swing.JPanel browserPanel;
    private javax.swing.JPanel browserPanel1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField configFilePathTextField;
    private javax.swing.JPanel currentProjectPanel;
    private javax.swing.JTree domtree;
    private javax.swing.JTextArea elementDetailArea;
    private org.jdesktop.swingx.JXTitledPanel existingProjectPanel;
    private javax.swing.JButton extractionButton;
    private javax.swing.JTable extractionFieldTable;
    private javax.swing.JButton helpButton;
    private javax.swing.JMenuItem importProjectItem;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JFileChooser jFileChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar;
    private org.jdesktop.swingx.JXTitledPanel jXTitledPanel1;
    private org.jdesktop.swingx.JXTitledPanel jXTitledPanel2;
    private org.jdesktop.swingx.JXTitledPanel jXTitledPanel3;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton newProjectButton;
    private javax.swing.JDialog newProjectDialog;
    private org.jdesktop.swingx.JXTitledPanel newProjectPanel1;
    private javax.swing.JButton openProjectButton;
    private javax.swing.JButton prjExecute;
    private javax.swing.JButton prjPause;
    private javax.swing.JButton prjStop;
    private javax.swing.JProgressBar progressBar;
    private org.jdesktop.swingx.JXTitledPanel projectConfigPanel;
    private javax.swing.JTable projectConfigTable;
    private javax.swing.JDialog projectDefintionDialog;
    private javax.swing.JTextField projectNameTextField;
    private javax.swing.JPanel projectPanel;
    private javax.swing.JTextField projectPathTextField;
    private org.jdesktop.swingx.JXTitledPanel resultsPanel;
    private javax.swing.JTable resultsTable;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton saveProjectButton;
    private javax.swing.JTextField startUrlTextField;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JSplitPane vertSplitPane;
    private javax.swing.JSplitPane vertSplitPane1;
    private javax.swing.JSplitPane vertSplitPane2;
    // End of variables declaration//GEN-END:variables
    private IBrowserCanvas browser1 = BrowserFactory.spawnMozilla();
    private IBrowserCanvas browser2 = BrowserFactory.spawnMozilla();
    private IBrowserCanvas browser3 = BrowserFactory.spawnMozilla();
    private IBrowserCanvas browser4 = BrowserFactory.spawnMozilla();
    private SystemTray tray = SystemTray.getSystemTray();
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private int colIndex = 0;
    private int rowIndex = 0;

    private JDialog aboutBox;
    private Document document;
    private TreeModel model;
    private Boolean projectSaved = false;
    private SingletonProjectBean project = new SingletonProjectBean();
    private SingletonProjectBean activeProject;
    private ProjectListBean projectList = new ProjectListBean();
    private int rowCount = 0, rowCount1 = 0;
    private DefaultTableModel dtm;
    private String rowData[] = null;
    private List<IElement> elementList = new ArrayList();
    private List parentList = new ArrayList();
    
    /* Method To Generate a DOM(Document Object Model) Tree of given URL*/
    private void generateDOMTree(String url) {
        try {
            parseURL(url);
            JTree tree = new JTree(createAndProcessTreeNode(document.child(0), "[0]"));
            model = tree.getModel();
            domtree.setModel(model);
            jXTitledPanel2.remove(jScrollPane1);
            jXTitledPanel2.setTitle("DOM Structure");
            jScrollPane1.setViewportView(domtree);
            jXTitledPanel2.add(jScrollPane1);

                       
            } catch (ParserConfigurationException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
   
    /* Method To Clean the HTML and Produce Well Formatted Document */
    private void parseURL(String url) throws ParserConfigurationException, SAXException
    {
        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties cleanerProperties = cleaner.getProperties();
            TagNode tagNode = cleaner.clean(new URL(url));
            PrettyHtmlSerializer htmlSerializer = new PrettyHtmlSerializer(cleanerProperties);
            PrettyXmlSerializer xmlSerializer = new PrettyXmlSerializer(cleanerProperties);
            htmlSerializer.writeToFile(tagNode, "C://JWebScraper//projects//"+project.getProjectName()+"//"+project.getProjectName()+"-dump.html");
            xmlSerializer.writeToFile(tagNode, "C://JWebScraper//projects//"+project.getProjectName()+"//"+project.getProjectName()+"-dump.xml");
            document = Jsoup.parse(new File("C://JWebScraper//projects//"+project.getProjectName()+"//"+project.getProjectName()+"-dump.html"),"UTF-8");
            
            
        } catch (IOException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
   
    /* Method To Create A JTree form of the HTML Document */
    private MutableTreeNode createAndProcessTreeNode(Element docElement, String index)
    {
        DefaultMutableTreeNode treeNode = null;
        String nodeName = docElement.nodeName(), nodeValue = docElement.toString(), nodeContent= docElement.html();
        int childCount = docElement.children().size();

        treeNode = new DefaultMutableTreeNode(nodeName+index);

        if(childCount > 0)
        {
            int i=0;
            while(i < childCount )
            {
                treeNode.add(createAndProcessTreeNode(docElement.child(i),"["+i+"]"));
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
    
           
    private String obtainXPathOfElement(IElement element)
    {
        IElement currentElement = element;
        IElementCollection children = currentElement.getParentElement().getChildElements();
        int count=0;
        StringBuilder path = new StringBuilder();
        //parentList.add(current.getTagName()+((current.getClassName().equals("")||(current.getClassName()==null)||(current.getClassName().equals("null")))?"":"[@class=\""+current.getClassName()+"\"]"));
        try{
            
            while(currentElement != null)
            {
                // Break if null node
                if(currentElement == null)
                    break ;
                // calculate position amongst siblings
                for(int i=0;i < children.length() ; i++){
                    if(!children.item(i).isTextNode())
                        count++;
                    if(children.item(i).equals(currentElement)){
                           parentList.add(currentElement.getTagName().toLowerCase()+"["+count+"]");//+(currentElement.getClassName().equals("")?"":"[@class = \""+currentElement.getClassName()+"\"]"));
                           break;
                    }
                }
                currentElement = currentElement.getParentElement();
                children = currentElement.getParentElement().getChildElements();
                count=0;
                //}
            }
            
        }
        catch(NullPointerException ex){
            ex.printStackTrace();
        }
        finally{
            parentList.add("html");
            Collections.reverse(parentList);
            for(int i=1; i< parentList.size(); i++)
            {
                path.append("/").append(parentList.get(i));
                //if()
            }
            System.out.println(path.toString());
            parentList = new ArrayList();
            return path.toString();
        }
        
                
    }

    /* Method to Load Project Config and details into currentProjectPanel*/
    private void loadProjectInPanel(String projectName)
    {
        try {
            activeProject = ProjectManagerBean.loadProjectBean(projectList, projectName + ".xml");
            browser4.loadURL(activeProject.getProjectUrl());
            browser4.setHTMLEditingMode(Boolean.TRUE);
            browserPanel1.setEnabled(false);
            final org.w3c.dom.Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("C://JWebScraper//projects//" + projectName + "//" + projectName + "-pattern.xml");
            final org.w3c.dom.NodeList list1 = document.getElementsByTagName("DATASET"),list2;
            
            dtm = (DefaultTableModel) projectConfigTable.getModel();
            
            SwingWorker<Void,Void> worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {

                    for(int i=0 ; i < ((DefaultTableModel)resultsTable.getModel()).getRowCount(); i++)
                        ((DefaultTableModel)resultsTable.getModel()).removeRow(i);
                    populateRows(dtm,document);
                    
                    
                    return null;
                }

                private void populateRows(DefaultTableModel dtm, org.w3c.dom.Document document1) {
                    Node node ;
                    NodeList list[] ={
                        document.getElementsByTagName("FieldName"),
                        document.getElementsByTagName("Path-CSS"),
                        document.getElementsByTagName("Type"),
                        document.getElementsByTagName("Content")
                    };
                    for(int i =0 ; i < document.getElementsByTagName("DATASET").getLength();i++)
                        dtm.addRow(new String[]{"","","",""});
                    
                    for(int i=0 ; i< list.length; i++)
                    {
                        //dtm.addRow(new String[]{"","","",""});
                        for(int j=0; j< list[i].getLength();j++)
                        {
                            node = list[i].item(j);
                            
                            dtm.setValueAt(node.getTextContent(), j, i);
                            //dtm.removeRow(dtm.getRowCount()-1);
                        }
                    }

                    fetchExtractionResults(activeProject,(DefaultTableModel)resultsTable.getModel());
                }

                private void fetchExtractionResults(SingletonProjectBean activeProject, DefaultTableModel resultTableModel) {
                    try {
                        DefaultTableModel projectConfigTableModel = (DefaultTableModel) projectConfigTable.getModel();
                        //HtmlCleaner cleaner = new HtmlCleaner();
                        //PrettyHtmlSerializer htmlSerializer = new PrettyHtmlSerializer(cleaner.getProperties());
                        //TagNode node = cleaner.clean(new URL(activeProject.getProjectUrl()));
                        //htmlSerializer.writeToFile(node, "C://JWebScraper//projects//"+activeProject.getProjectName()+"//"+activeProject.getProjectName()+"-dump.html");
                        Document parsedDocument = Jsoup.parse(new File("C://JWebScraper//projects//"+activeProject.getProjectName()+"//"+activeProject.getProjectName()+"-dump.html"), null);
                        Elements fetchedElements ;
                        for(int j=0; j < projectConfigTableModel.getRowCount();j++)
                                resultTableModel.addColumn(projectConfigTableModel.getValueAt(j, 0));
                        
                        for(int i=0; i< projectConfigTableModel.getRowCount(); i++)
                        {
                            
                            if(projectConfigTableModel.getValueAt(i, 2).equals("CSS"))
                            {
                                fetchedElements = parsedDocument.getElementsByClass(projectConfigTableModel.getValueAt(i, 1).toString());
                                System.out.println("i: "+i+" \nSize: "+fetchedElements.size()+"\n Value: "+projectConfigTableModel.getValueAt(i, 1));
                                //fetchedElements.
                                Iterator iterator = fetchedElements.iterator();
                                while(iterator.hasNext())
                                    resultTableModel.addRow(new String[]{"",iterator.next().toString(),"",""});
                                
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            worker.execute();
            currentProjectPanel.repaint();
            jTabbedPane1.add("Current Project/Task : " + projectName, currentProjectPanel);
            jTabbedPane1.setSelectedIndex(1);
            
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JWebScraperView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }   

    private void createFolderStructure() {
        File[] dirs = new File[3];
        dirs[0] = new File("C://JWebScraper//config//");
        dirs[1] = new File("C://JWebScraper//projects//");
        dirs[2] = new File("C://JWebScraper//temp//");

        dirs[0].mkdirs();
        dirs[1].mkdirs();
        dirs[2].mkdirs();

    }
    
}

