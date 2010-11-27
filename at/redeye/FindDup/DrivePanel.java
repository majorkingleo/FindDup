/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DrivePanel.java
 *
 * Created on 31.10.2010, 10:10:39
 */

package at.redeye.FindDup;

import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.utilities.StringUtils;
import java.awt.GridLayout;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JCheckBox;
import org.apache.log4j.Logger;

/**
 *
 * @author moberza
 */
public class DrivePanel extends javax.swing.JPanel {

    private static final Logger logger = Logger.getLogger(DrivePanel.class.getName());
    private final List<JCheckBox> boxes = new LinkedList();
    private boolean show_system_drives = true; 
    private Root root;
    private final Set<String> system_dirs = new HashSet();

    private Timer autoRefreshTimer = new Timer();
    private TimerTask autoRefreshTask = new TimerTask() {

        @Override
        public void run() {

            doAutoRefresh();
        }
    };

    public DrivePanel() {
        initComponents();

        initCommon();
    }
    
    public DrivePanel(Root root) {
        initComponents();

        this.root = root;
        
        initCommon();
    }    
    
    public void setRoot( Root root )
    {
        this.root = root;
    }

    private void initCommon()
    {
        File roots[] = getRoots();

        if( roots.length > 8 )
            setLayout(new GridLayout(0, 8));

        for( File root_file : roots )
        {
            logger.info("root: " + root_file + ( root_file.exists() ? " exists" : " not exists"));

            if (root_file.exists()) {
                JCheckBox cb = new JCheckBox(root_file.getPath());
                cb.setSelected(true);
                add(cb);
                boxes.add(cb);
            }
        }

        autoRefreshTimer.schedule(autoRefreshTask, 3000, 3000);
    }

    private void doAutoRefresh()
    {
        synchronized (boxes) {

            File roots[] = getRoots();

            for (File root_file : roots) {
                // logger.info("root: " + root + ( root.exists() ? "exists" : "not exists"));

                boolean found = false;


                for (JCheckBox box : boxes) {
                    if (box.getText().equals(root_file.getPath())) {
                        found = true;
                        // Laufwerk wurde entfernt
                        if (!root_file.exists()) {
                            remove(box);
                            boxes.remove(box);
                            updateUI();                            
                        }

                        break;
                    }
                } // for
                
     

                // Laufwerk wurde hinzugefügt
                if (!found && root_file.exists()) {
                    JCheckBox cb = new JCheckBox(root_file.getPath());
                    cb.setSelected(true);
                    add(cb);
                    boxes.add(cb);
                    updateUI();
                }
            }

            // und wenn einer den USB Stick wieder rausgezogen hat, dann
            // is der nicht mehr in der root liste, also umgekehrt suchen

            boolean do_it_again = false;

            do {
                do_it_again = false;
                
                for (JCheckBox box : boxes) {
                    boolean found = false;

                    for (File root_file : roots) {
                        if (root_file.getPath().equals(box.getText())) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        remove(box);
                        boxes.remove(box);
                        updateUI();
                        do_it_again = true;
                        break;
                    }
                }
            } while (do_it_again);
        }
    }

    public List<File> getDirs()
    {
        List<File> res = new LinkedList();

        for( JCheckBox cb : boxes )
        {
            if( cb.isSelected() )
                res.add(new File(cb.getText()));
        }

        return res;
    }

    public String getDirsAsString()
    {
        StringBuilder res  = new StringBuilder();

        for( File dir : getDirs() )
        {
            res.append(dir.getPath());
            res.append("=true");
            res.append(dir.pathSeparator);
        }

        File roots[] = getRoots();

        for( File root_file : roots )
        {
            if( !root_file.exists() )
                continue;

            boolean found = false;
            for( File dir : getDirs() )
            {
                if( dir.getPath().equals(root_file.getPath()))
                {
                    found = true;
                    break;
                }
            }

            if( !found )
            {
                res.append(root_file.getPath());
                res.append("=false");
                res.append(root_file.pathSeparator);
            }
        }

        return res.toString();
    }

    public void setSelectedDirs( String dirs )
    {
        synchronized (boxes) {

            String dir_array[] = dirs.split(File.pathSeparator);
            List<String> dir_list = new LinkedList();


            for (String dir_string : dir_array) {

                String dir = dir_string;
                String peaces[] = dir_string.split("=");

                boolean selected = true;

                if( peaces.length == 2 )
                {
                    dir = peaces[0];
                    selected = StringUtils.isYes(peaces[1]);
                }

                dir_list.add(dir);
                
                for (JCheckBox box : boxes) {
                    if( box.getText().equals(dir) )
                    {                        
                        box.setSelected(selected);
                        break;
                    }
                }
            }

            for (JCheckBox box : boxes) {
                boolean found = false;

                for (String dir : dir_list) {
                    if (box.getText().equals(dir)) {
                        found = true;
                        break;
                    }
                }

                if( !found ) {
                    box.setSelected(true);
                }
            } // for
        }
    }

    private File[] getRoots()
    {
        if( Setup.is_win_system() )
            return File.listRoots();

        List<File> roots = new LinkedList();

        File root_file = File.listRoots()[0];

        synchronized (system_dirs) {

            for (File sub : root_file.listFiles()) {
                if (sub.exists() && sub.isDirectory() && sub.canRead()) {
                    if (!show_system_drives) {
                        if (!system_dirs.contains(sub.toString())) {
                            roots.add(sub);
                        }
                    } else {
                        roots.add(sub);
                    }
                }
            }
        }

        File root_array[] = new File[roots.size()];

        for( int i = 0; i < root_array.length; i++ )
            root_array[i] = roots.get(i);

        return root_array;
    }

    public void showSystemDrives( boolean state )
    {
        if( root == null )
            throw new RuntimeException("Root is null");

        synchronized (system_dirs) {

            system_dirs.clear();

            String dirs = root.getSetup().getLocalConfig(AppConfigDefinitions.SysDirs);

            if (dirs != null) {
                for (String dir : dirs.split(File.pathSeparator)) {
                    if (Setup.is_win_system()) {
                        system_dirs.add(dir.toLowerCase());
                    } else {
                        system_dirs.add(dir);
                    }
                }
            }

            show_system_drives = state;

        }

        doAutoRefresh();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createTitledBorder("Laufwerke"));
        setLayout(new java.awt.GridLayout(2, 0));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
