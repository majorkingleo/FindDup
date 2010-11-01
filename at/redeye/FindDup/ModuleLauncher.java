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

        // root.setWebStartUlr(getWebStartUrl("http://172.28.16.55/hotline/launch_new2.jnlp"));
    }

    public void invoke()
    {
        if (splashEnabled()) {
            splash = new StartupWindow(
                    "/at/redeye/FrameWork/base/resources/pictures/redeye.png");
        }

        AppConfigDefinitions.registerDefinitions();
	FrameWorkConfigDefinitions.registerDefinitions();

        root.registerPlugin(new at.redeye.Plugins.ShellExec.Plugin());

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
                sb.append("c:\\windows ");
                sb.append(root.MlM("c:\\programme") + " ");
                sb.append("c:\\syswow64 ");
                sb.append("c:\\RECYCLED ");
           }
           else
           {
               sb.append("/usr ");
               sb.append("/opt ");
               sb.append("/lib ");
               sb.append("/etc ");
           }

           root.getSetup().setLocalConfig(AppConfigDefinitions.SysDirs.getConfigName(), sb.toString());
       }
    }
}
