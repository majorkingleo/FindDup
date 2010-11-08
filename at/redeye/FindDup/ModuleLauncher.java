/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FindDup;

import at.redeye.FrameWork.base.BaseModuleLauncher;
import at.redeye.FrameWork.base.FrameWorkConfigDefinitions;
import at.redeye.FrameWork.base.LocalRoot;
import at.redeye.FrameWork.base.Setup;
import at.redeye.FrameWork.widgets.StartupWindow;
import java.io.File;

/**
 *
 * @author martin
 */
public class ModuleLauncher extends BaseModuleLauncher {


    MainWin mainwin;

    public ModuleLauncher( String args[] )
    {
        super( args );

        root = new LocalRoot("FindDup", "Finde Duplikate");

        root.setBaseLanguage("de");
        root.setDefaultLanguage("en");

        root.setWebStartUlr(getWebStartUrl("http://redeye.hoffer.cx/FindDup/launch.jnlp"));
    }

    public void invoke()
    {
        if (splashEnabled()) {
            splash = new StartupWindow(
                    "/at/redeye/FindDup/resources/images/FindDup.png");
        }

        AppConfigDefinitions.registerDefinitions();
	FrameWorkConfigDefinitions.registerDefinitions();

        if( Setup.is_win_system() )
        {
            root.registerPlugin(new at.redeye.Plugins.ShellExec.Plugin());
        }

        configureLogging();

        setLookAndFeel(root);

        initSystemDirs();

        mainwin = new MainWin(root);

        closeSplash();

        mainwin.setVisible(true);
        mainwin.toFront();

        updateJnlp2();         
    }

    @Override
    public String getVersion() {
        return Version.getVersion();
    }

    void initSystemDirs()
    {
       StringBuilder sb = new StringBuilder();

       if( root.getSetup().initialRun() )
       {
           if( Setup.is_win_system() )
           {
                sb.append("c:\\windows"); sb.append(File.pathSeparator);
                sb.append(root.MlM("c:\\programme")); sb.append(File.pathSeparator);
                sb.append("c:\\syswow64"); sb.append(File.pathSeparator);
                sb.append("c:\\RECYCLED"); sb.append(File.pathSeparator);
                sb.append("c:\\Program Files"); sb.append(File.pathSeparator);
                sb.append("c:\\Program Files (x86)"); sb.append(File.pathSeparator);
           }
           else
           {
               sb.append("/usr"); sb.append(File.pathSeparator);
               sb.append("/opt"); sb.append(File.pathSeparator);
               sb.append("/lib"); sb.append(File.pathSeparator);
               sb.append("/etc"); sb.append(File.pathSeparator);
               sb.append("/boot"); sb.append(File.pathSeparator);
               sb.append("/dev"); sb.append(File.pathSeparator);
               sb.append("/tmp"); sb.append(File.pathSeparator);
               sb.append("/sys"); sb.append(File.pathSeparator);
               sb.append("/selinux"); sb.append(File.pathSeparator);
               sb.append("/lib"); sb.append(File.pathSeparator);
               sb.append("/lib64"); sb.append(File.pathSeparator);
               sb.append("/proc"); sb.append(File.pathSeparator);
               sb.append("/var"); sb.append(File.pathSeparator);
               sb.append("/sbin"); sb.append(File.pathSeparator);
           }

           root.getSetup().setLocalConfig(AppConfigDefinitions.SysDirs.getConfigName(), sb.toString());
       }
    }

    @Override
    public void jnlpUpdated() {
        mainwin.setCreateDesktopIconEnabled(true);
    }
}
