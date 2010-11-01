/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainWin.java
 *
 * Created on 29.10.2010, 14:47:41
 */

package at.redeye.FindDup;

import at.redeye.FindDup.SearchThread.STATE;
import at.redeye.FrameWork.Plugin.AboutPlugins;
import at.redeye.FrameWork.base.AutoLogger;
import at.redeye.FrameWork.base.BaseDialog;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.base.prm.impl.gui.LocalConfig;
import at.redeye.FrameWork.utilities.StringUtils;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 *
 * @author moberza
 */
public class MainWin extends BaseDialog {

    private static final String SELECTED_VIDEO = "SelectedVideos";
    private static final String SELECTED_AUDIO = "SelectedAudio";
    private static final String SELECTED_IMAGES = "SelectedImages";
    private static final String SELECTED_OFFICE = "SelectedOffice";
    private static final String SELECTED_ALL = "SelectedAll";
    private static final String SELECTED_MASK = "SelectedMask";
    private static final String SELECTED_SYSPATHS = "SelectedSysPaths";
    private static final String SELECTED_ROOTS = "SelectedRoots";
    
    SearchThread search_thread = null;
    ImageLoaderThread image_loader;

    public MainWin(Root root)
    {
        super(root, root.getAppTitle());

        initComponents();

        helper.autoRefreshTimer.schedule(helper.autoRefreshTask, 500, 500);
        jLProgress.setText("");

        jListErg.setCellRenderer(new ImageCellRenderer());

        image_loader = new ImageLoaderThread(root,jListErg);
        image_loader.start();

        jCall.setSelected( StringUtils.isYes(root.getSetup().getLocalConfig(SELECTED_ALL, "false")) );
        jCimages.setSelected( StringUtils.isYes(root.getSetup().getLocalConfig(SELECTED_IMAGES, "true")) );
        jCvideo.setSelected( StringUtils.isYes(root.getSetup().getLocalConfig(SELECTED_VIDEO, "true")) );
        jCMusic.setSelected( StringUtils.isYes(root.getSetup().getLocalConfig(SELECTED_AUDIO, "true")) );
        jCoffice.setSelected( StringUtils.isYes(root.getSetup().getLocalConfig(SELECTED_OFFICE, "true")) );
        jCsystemPaths.setSelected( StringUtils.isYes(root.getSetup().getLocalConfig(SELECTED_SYSPATHS, "true")) );

        jTFileendings.setText(root.getSetup().getLocalConfig(SELECTED_MASK, ""));

        drivePanel.setSelectedDirs(root.getSetup().getLocalConfig(SELECTED_ROOTS, drivePanel.getDirsAsString()));

        if( jTFileendings.getText().trim().isEmpty() )
            updateFileendings();

        new AutoLogger(MainWin.class.getName()) {

            @Override
            public void do_stuff() throws Exception {
                updateErgList(readLastResult());
            }
        };

    }

    @Override
    public void doAutoRefresh()
    {
       if( search_thread == null )
       {           
           jBSearch.setText(MlM("Suche Starten"));
       }
       else if( search_thread.isAlive() )
       {
           STATE thread_state = search_thread.getCurrentState();
           
           switch( thread_state )
           {
               case IDLE:
               case SEARCHING_FOR_FILES:
                   jLProgress.setText(String.format(MlM("%d Dateien gefunden"), search_thread.getNumOfFiles() ) );
                   break;

               case DONE:
                   setWaitCursor(false);
               case ANALYSING_FILES:
                   jLProgress.setText(String.format(MlM("%d Dateien gleichen sich"), search_thread.getNumOfEqualFiles() ) );
                   break;
           }
            
           jBSearch.setText(MlM("Suche Abbrechen"));
       }
       else
       {
           final Map<String, List<FileEntry>> equalFilesByMD5 = search_thread.getEqualFilesByMD5();
           updateErgList(equalFilesByMD5);
           jLProgress.setText(String.format(MlM("%d gleiche Dateien gefunden"), search_thread.getNumOfEqualFiles() ) );
           search_thread = null;

           new AutoLogger(MainWin.class.getName()) {

               @Override
               public void do_stuff() throws Exception {
                   saveResult(equalFilesByMD5);
               }
           };
           setWaitCursor(false);
       }       
    }




    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jBSearch = new javax.swing.JButton();
        jLProgress = new javax.swing.JLabel();
        drivePanel = new at.redeye.FindDup.DrivePanel();
        jPanel1 = new javax.swing.JPanel();
        jTFileendings = new javax.swing.JTextField();
        jCimages = new javax.swing.JCheckBox();
        jCvideo = new javax.swing.JCheckBox();
        jCMusic = new javax.swing.JCheckBox();
        jCoffice = new javax.swing.JCheckBox();
        jCall = new javax.swing.JCheckBox();
        jCsystemPaths = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListErg = new javax.swing.JList();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMSettings = new javax.swing.JMenuItem();
        jMQuit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMAbout = new javax.swing.JMenuItem();
        jMChangeLog = new javax.swing.JMenuItem();
        jMPlugin = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Suche und Finde doppelte Dateien auf deinem Computer");

        jBSearch.setText("Suche Starten");
        jBSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSearchActionPerformed(evt);
            }
        });

        jLProgress.setText("XXX");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Dateiendungen"));

        jTFileendings.setText("*.mp3 *.jpg *.jpeg");
        jTFileendings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFileendingsActionPerformed(evt);
            }
        });

        jCimages.setText("Bilder");
        jCimages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCimagesActionPerformed(evt);
            }
        });

        jCvideo.setText("Videos");
        jCvideo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCvideoActionPerformed(evt);
            }
        });

        jCMusic.setText("Musik");
        jCMusic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCMusicActionPerformed(evt);
            }
        });

        jCoffice.setText("Office");
        jCoffice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCofficeActionPerformed(evt);
            }
        });

        jCall.setText("Alles");
        jCall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCallActionPerformed(evt);
            }
        });

        jCsystemPaths.setText("Systempfade Überspringen");
        jCsystemPaths.setToolTipText("Windows Interne Verzeichnisse nicht auswerten.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jCall)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCimages)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCvideo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCMusic)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCoffice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                        .addComponent(jCsystemPaths))
                    .addComponent(jTFileendings, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCall)
                    .addComponent(jCimages)
                    .addComponent(jCvideo)
                    .addComponent(jCMusic)
                    .addComponent(jCoffice)
                    .addComponent(jCsystemPaths))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTFileendings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Ergebnisse"));

        jListErg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jListErgMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jListErg);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setText("Programm");

        jMSettings.setText("Einstellungen");
        jMSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMSettingsActionPerformed(evt);
            }
        });
        jMenu1.add(jMSettings);

        jMQuit.setText("Beenden");
        jMQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMQuitActionPerformed(evt);
            }
        });
        jMenu1.add(jMQuit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Info");

        jMAbout.setText("Über");
        jMAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMAboutActionPerformed(evt);
            }
        });
        jMenu2.add(jMAbout);

        jMChangeLog.setText("Änderungsprotokoll");
        jMChangeLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMChangeLogActionPerformed(evt);
            }
        });
        jMenu2.add(jMChangeLog);

        jMPlugin.setText("Plugins");
        jMPlugin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMPluginActionPerformed(evt);
            }
        });
        jMenu2.add(jMPlugin);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLProgress, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                        .addGap(125, 125, 125)
                        .addComponent(jBSearch))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(drivePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(drivePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLProgress)
                    .addComponent(jBSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMQuitActionPerformed

        close();
    }//GEN-LAST:event_jMQuitActionPerformed

    private void jMAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMAboutActionPerformed
        invokeDialogUnique(new About(root));
    }//GEN-LAST:event_jMAboutActionPerformed

    private void jTFileendingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFileendingsActionPerformed
        jBSearchActionPerformed(evt);        
    }//GEN-LAST:event_jTFileendingsActionPerformed

    private void jBSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSearchActionPerformed

        if( search_thread == null )
        {
            List<File> dirs = drivePanel.getDirs();
            jListErg.setListData(new Vector());

            String endings = jTFileendings.getText().trim();

            if( endings.isEmpty() )
            {
                JOptionPane.showMessageDialog(this, 
                        MlM("Bitte wählen sie einen Dateifilter aus."),
                        MlM("Fehler"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            search_thread = new SearchThread(root, endings, dirs,jCsystemPaths.isSelected());

            search_thread.start();
            setWaitCursor();

        } else {
            image_loader.cancelLoading();
            search_thread.stopWorking();
        }

    }//GEN-LAST:event_jBSearchActionPerformed

    private void jMSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMSettingsActionPerformed
        invokeDialogUnique(new LocalConfig(root));
    }//GEN-LAST:event_jMSettingsActionPerformed

    private void jCallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCallActionPerformed

        if( jCall.isSelected() )
        {
            jCvideo.setSelected(true);
            jCMusic.setSelected(true);
            jCoffice.setSelected(true);
            jCimages.setSelected(true);       
        }

        updateFileendings();

    }//GEN-LAST:event_jCallActionPerformed

    private void jCimagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCimagesActionPerformed
        updateFileendings();
    }//GEN-LAST:event_jCimagesActionPerformed

    private void jCvideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCvideoActionPerformed
        updateFileendings();
    }//GEN-LAST:event_jCvideoActionPerformed

    private void jCMusicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCMusicActionPerformed
        updateFileendings();
    }//GEN-LAST:event_jCMusicActionPerformed

    private void jCofficeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCofficeActionPerformed
        updateFileendings();
    }//GEN-LAST:event_jCofficeActionPerformed

    private void jListErgMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListErgMousePressed

        logger.info("Button: " + evt.getButton() );

        if (evt.getButton() == MouseEvent.BUTTON3)
        {
            int idx = jListErg.locationToIndex(evt.getPoint());

            if (idx >= 0) {
                jListErg.setSelectedIndex(idx);

                JPopupMenu popup = new ActionPopup(root, (DisplayEntry)jListErg.getSelectedValue());

                popup.show(evt.getComponent(), evt.getX(), evt.getY());
            }           
        } // if

    }//GEN-LAST:event_jListErgMousePressed

    private void jMChangeLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMChangeLogActionPerformed

        invokeDialogUnique(new LocalHelpWin(root, "ChangeLog"));
    }//GEN-LAST:event_jMChangeLogActionPerformed

    private void jMPluginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMPluginActionPerformed

        invokeDialogUnique(new AboutPlugins(root));
    }//GEN-LAST:event_jMPluginActionPerformed


    private void updateFileendings()
    {
        if( jCall.isSelected() )
        {
            jTFileendings.setText("*.*");
            return;
        }
        else
        {
            removeEndings("*.*");
        }

        if( jCvideo.isSelected() )
            addEndings(root.getSetup().getLocalConfig(AppConfigDefinitions.FileTypesVideo));
        else
            removeEndings(root.getSetup().getLocalConfig(AppConfigDefinitions.FileTypesVideo));

        if( jCMusic.isSelected() )
            addEndings(root.getSetup().getLocalConfig(AppConfigDefinitions.FileTypesAudio));
        else
            removeEndings(root.getSetup().getLocalConfig(AppConfigDefinitions.FileTypesAudio));

        if( jCoffice.isSelected() )
            addEndings(root.getSetup().getLocalConfig(AppConfigDefinitions.FileTypesOffice));
        else
            removeEndings(root.getSetup().getLocalConfig(AppConfigDefinitions.FileTypesOffice));

        if( jCimages.isSelected() )
            addEndings(root.getSetup().getLocalConfig(AppConfigDefinitions.FileTypesImages));
        else
            removeEndings(root.getSetup().getLocalConfig(AppConfigDefinitions.FileTypesImages));
    }

    private void addEndings( String endings )
    {
        String ends[] = endings.split("[ \t;,]");

        String current = jTFileendings.getText();

        for( String ending : ends )
        {
            if( !current.contains(ending) )
            {
                current += " " + ending;
            }
        }

        jTFileendings.setText(current);
    }

    private void removeEndings( String endings )
    {
        String ends[] = endings.split("[ \t;,]");

        String current = jTFileendings.getText();

        for( String ending : ends )
        {
            if( current.contains(ending) )
            {
                current = current.replace(ending, "");
            }
        }

        jTFileendings.setText(current.trim());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private at.redeye.FindDup.DrivePanel drivePanel;
    private javax.swing.JButton jBSearch;
    private javax.swing.JCheckBox jCMusic;
    private javax.swing.JCheckBox jCall;
    private javax.swing.JCheckBox jCimages;
    private javax.swing.JCheckBox jCoffice;
    private javax.swing.JCheckBox jCsystemPaths;
    private javax.swing.JCheckBox jCvideo;
    private javax.swing.JLabel jLProgress;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jListErg;
    private javax.swing.JMenuItem jMAbout;
    private javax.swing.JMenuItem jMChangeLog;
    private javax.swing.JMenuItem jMPlugin;
    private javax.swing.JMenuItem jMQuit;
    private javax.swing.JMenuItem jMSettings;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTFileendings;
    // End of variables declaration//GEN-END:variables

    private void updateErgList(final Map<String, List<FileEntry>> equalFilesByMD5)
    {
        Vector<DisplayEntry> res = new Vector();

        for( Entry<String,List<FileEntry>> entry : equalFilesByMD5.entrySet() )
        {
            DisplayEntry de = new DisplayEntry(entry.getValue(),image_loader);
            res.add(de);
        }

        jListErg.setListData(res);
    }

    @Override
    public void close()
    {
        root.getSetup().setLocalConfig(SELECTED_ALL, String.valueOf(jCall.isSelected()));
        root.getSetup().setLocalConfig(SELECTED_VIDEO, String.valueOf(jCvideo.isSelected()));
        root.getSetup().setLocalConfig(SELECTED_AUDIO, String.valueOf(jCMusic.isSelected()));
        root.getSetup().setLocalConfig(SELECTED_OFFICE, String.valueOf(jCoffice.isSelected()));
        root.getSetup().setLocalConfig(SELECTED_IMAGES, String.valueOf(jCimages.isSelected()));
        root.getSetup().setLocalConfig(SELECTED_MASK, jTFileendings.getText());
        root.getSetup().setLocalConfig(SELECTED_SYSPATHS, String.valueOf(jCsystemPaths.isSelected()));
        root.getSetup().setLocalConfig(SELECTED_ROOTS, drivePanel.getDirsAsString());

        super.close();
    }

    private String getLastResultFile()
    {
        return Setup.getAppConfigDir(root.getAppName()) + "/last_result.dat";
    }

    private void saveResult(Map<String, List<FileEntry>> equalFilesByMD5) throws FileNotFoundException, IOException
    {
        String last_result = getLastResultFile();

        ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream(last_result));
        out.writeObject(equalFilesByMD5);
        out.close();
    }

    private Map<String, List<FileEntry>> readLastResult() throws IOException, ClassNotFoundException
    {
        String last_result = getLastResultFile();

        ObjectInputStream in = new ObjectInputStream( new FileInputStream(last_result));
        Map<String, List<FileEntry>> equalFilesByMD5  = (Map<String, List<FileEntry>>) in.readObject();
        in.close();

        return equalFilesByMD5;
    }

}
