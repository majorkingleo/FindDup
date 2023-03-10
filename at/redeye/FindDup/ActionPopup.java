/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FindDup;

import at.redeye.FrameWork.base.AutoMBox;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import at.redeye.Plugins.ShellExec.ShellExec;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 *
 * @author moberza
 */
public class ActionPopup extends JPopupMenu
{
    private Root root;

    public ActionPopup(Root root_, final DisplayEntry display_entry)
    {
        this.root = root_;

        JMenuItem menuItem = new JMenuItem(root.MlM("Alle Verzeichnisse öffnen"));
        
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                new AutoMBox(ActionPopup.class.getName()) {

                    @Override
                    public void do_stuff() throws Exception {


                        for (FileEntry entry : display_entry.getFileEntries()) {


                            String open_command = getOpenCommand();

                            String command = open_command + " \"" + entry.getFile().getParentFile().getPath() + "\"";
                            logger.info(command);

                            String command_array[] = new String[2];

                            command_array[0] = open_command;
                            command_array[1] = entry.getFile().getParentFile().getPath();

                            Process p = Runtime.getRuntime().exec(command_array);
                        } // for
                    }
                };
            }
        });

        add(menuItem);

        JMenu submenu = new JMenu( root.MlM("Verzeichnis Öffnen") );
        add(submenu);

        for (final FileEntry entry : display_entry.getFileEntries()) {
            menuItem = new JMenuItem(entry.getFile().getParentFile().getPath());
            menuItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    new AutoMBox(ActionPopup.class.getName()) {

                        @Override
                        public void do_stuff() throws Exception {

                            String open_command = getOpenCommand();

                            String command =  open_command + " \"" + entry.getFile().getParentFile().getPath() + "\"";
                            logger.info(command);

                            String command_array[] = new String[2];

                            command_array[0] = open_command;                            
                            command_array[1] = entry.getFile().getParentFile().getPath();

                            Process p = Runtime.getRuntime().exec(command_array);
                        }
                    };
                }
            });

            submenu.add(menuItem);
        }

        final JMenu submenu_open = new JMenu( root.MlM("Datei Öffnen") );

        boolean found_something = false;

        for (final FileEntry entry : display_entry.getFileEntries()) {

            if( !entry.getFile().exists() )
                continue;

            final JMenuItem menuItem_open = new JMenuItem(entry.getFile().getPath());
            menuItem_open.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    if( Setup.is_win_system() )
                    {
                        ShellExec exec = new ShellExec();
                        exec.execute(entry.getFile().getPath());
                    }
                    else
                    {
  
                        new AutoMBox(ActionPopup.class.getName()) {

                            @Override
                            public void do_stuff() throws Exception {
                                String open_command = getOpenCommand();

                                String command = open_command + " \"" + entry.getFile().getPath() + "\"";
                                logger.info(command);

                                String command_array[] = new String[2];

                                command_array[0] = open_command;
                                command_array[1] = entry.getFile().getPath();

                                Process p = Runtime.getRuntime().exec(command_array);
                            }
                        };
                    }
                }
            });

            found_something = true;
            submenu_open.add(menuItem_open);
        } // for

        if( found_something )
            add(submenu_open);




        

        final JMenu submenu_del = new JMenu(  root.MlM("Datei Löschen") );

        found_something = false;

        for (final FileEntry entry : display_entry.getFileEntries()) {

            if( !entry.getFile().exists() )
                continue;

            final JMenuItem menuItem_del = new JMenuItem(entry.getFile().getPath());
            menuItem_del.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    if( !entry.getFile().delete() )
                    {
                        JOptionPane.showMessageDialog(display_entry,
                                String.format(root.MlM("Datei %s konnte nicht glöscht werden"),
                                entry.getFile().getPath()) );
                    }
                    else
                    {
                        submenu_del.remove(menuItem_del);
                    }
                }
            });

            found_something = true;
            submenu_del.add(menuItem_del);
        } // for

        if( found_something )
            add(submenu_del);
    }

    private void notImplemented()
    {
        JOptionPane.showMessageDialog(null,
                "Not Implemented yes", "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private String getOpenCommand()
    {
        if( root.getSetup().is_win_system() )
            return "explorer";

        return root.getSetup().getLocalConfig(AppConfigDefinitions.OpenCommand);
    }
}
