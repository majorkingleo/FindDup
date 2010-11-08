/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FindDup;

import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.Icon;

/**
 *
 * @author martin
 */
public class ImageCache
{
    Root root;
    File cache_dir;
    long timeout;
    long now;

    public ImageCache( Root root )
    {
        this.root = root;
        cache_dir = new File(Setup.getAppConfigDir(root.getAppName())  + "/img_cache" );

        if( !cache_dir.exists() )
        {
            if( !cache_dir.mkdirs() )
            {
                throw new RuntimeException( "Cannot create directory: " + cache_dir.getPath() );
            }
        }

        now = System.currentTimeMillis();
        timeout = now - 1000 * 60 * 60 * 24 * 7;
    }


    void saveImage(String name, Icon icon) throws FileNotFoundException, IOException
    {
        if( name == null )
            return;

        File img_file = new File( cache_dir.getPath() + "/" + name + ".ser");

        if( img_file.exists() ) {

            if( timeout > img_file.lastModified() )
            {
                img_file.setLastModified(timeout);
            }

            return;
        }

        ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream(img_file));
        out.writeObject(icon);
        out.close();
    }

    public Icon loadImage( String name ) throws IOException, ClassNotFoundException
    {
        if( name == null )
            return null;

        File img_file = new File( cache_dir.getPath() + "/" + name + ".ser");

        if( !img_file.exists() )
            return null;

        ObjectInputStream in = new ObjectInputStream( new FileInputStream(img_file));        
        Icon icon = (Icon)in.readObject();
        in.close();

        return icon;
    }

}
