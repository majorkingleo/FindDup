/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FindDup;

import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import java.io.File;

/**
 *
 * @author martin
 */
public class CleanUpThread extends Thread
{
    Root root;


    public CleanUpThread( Root root )
    {
        this.root = root;
    }

    @Override
    public void run()
    {
        File cache_dir  = new File(Setup.getAppConfigDir(root.getAppName())  + "/img_cache" );

        if( !cache_dir.exists() )
            return;

        long timeout = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 14;


        for( File file : cache_dir.listFiles() )
        {
             if( timeout > file.lastModified() )
             {
                 file.delete();
             }
        }
    }

}
