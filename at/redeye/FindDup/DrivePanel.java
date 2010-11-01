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

import at.redeye.FrameWork.utilities.StringUtils;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
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

    private Timer autoRefreshTimer = new Timer();
    private TimerTask autoRefreshTask = new TimerTask() {

        @Override
        public void run() {

            doAutoRefresh();
        }
    };

    public DrivePanel() {
        initComponents();

        File roots[] = File.listRoots();

        for( File root : roots )
        {
            logger.info("root: " + root + ( root.exists() ? "exists" : "not exists"));

            if (root.exists()) {
                JCheckBox cb = new JCheckBox(root.getPath());
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

            File roots[] = File.listRoots();

            for (File root : roots) {
                // logger.info("root: " + root + ( root.exists() ? "exists" : "not exists"));

                boolean found = false;

                for (JCheckBox box : boxes) {
                    if (box.getText().equals(root.getPath())) {
                        found = true;
                        // Laufwerk wurde entfernt
                        if (!root.exists()) {
                            remove(box);
                            boxes.remove(box);
                            updateUI();
                        }

                        break;
                    }
                } // for

                // Laufwerk wurde hinzugefügt
                if (!found && root.exists()) {
                    JCheckBox cb = new JCheckBox(root.getPath());
                    cb.setSelected(true);
                    add(cb);
                    boxes.add(cb);
                    updateUI();
                }
            }

            // un wenn einer den USB Stick wieder rausgezogen hat, dann
            // is der nicht mehr in der root liste, also umgekehrt suchen



            for (JCheckBox box : boxes) {
                boolean found = false;

                for (File root : roots) {
                    if (root.getPath().equals(box.getText())) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    remove(box);
                    boxes.remove(box);
                    updateUI();
                    break;
                }
            }
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

        File roots[] = File.listRoots();

        for( File root : roots )
        {
            if( !root.exists() )
                continue;

            boolean found = false;
            for( File dir : getDirs() )
            {
                if( dir.getPath().equals(root.getPath()))
                {
                    found = true;
                    break;
                }
            }

            if( !found )
            {
                res.append(root.getPath());
                res.append("=false");
                res.append(root.pathSeparator);
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

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createTitledBorder("Laufwerke"));
        setLayout(new java.awt.GridLayout());
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
