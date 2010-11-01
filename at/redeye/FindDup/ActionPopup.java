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
    public ActionPopup(final Root root, final DisplayEntry display_entry)
    {
        JMenuItem menuItem = new JMenuItem(root.MlM("Alle Verzeichnisse öffnen"));
        
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                new AutoMBox(ActionPopup.class.getName()) {

                    @Override
                    public void do_stuff() throws Exception {
                        for (FileEntry entry : display_entry.getFileEntries()) {
                            Process p = Runtime.getRuntime().exec("explorer \"" + entry.getFile().getParentFile().getPath() + "\"");
                        }
                    }
                };
            }
        });

        add(menuItem);

        JMenu submenu = new JMenu( "Verzeichnis Öffnen" );
        add(submenu);

        for (final FileEntry entry : display_entry.getFileEntries()) {
            menuItem = new JMenuItem(entry.getFile().getParentFile().getPath());
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    new AutoMBox(ActionPopup.class.getName()) {

                        @Override
                        public void do_stuff() throws Exception {
                            Process p = Runtime.getRuntime().exec("explorer \"" + entry.getFile().getParentFile().getPath() + "\"");
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

                public void actionPerformed(ActionEvent e) {

                    if( Setup.is_win_system() )
                    {
                        ShellExec exec = new ShellExec();
                        exec.execute(entry.getFile().getPath());
                    }
                    else
                    {
                        notImplemented();
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
}
