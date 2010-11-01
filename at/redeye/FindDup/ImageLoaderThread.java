/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FindDup;

import at.redeye.FrameWork.base.AutoLogger;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.imagestorage.ImageUtils;
import at.redeye.FrameWork.utilities.StringUtils;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.apache.log4j.Logger;

/**
 *
 * @author moberza
 */
public class ImageLoaderThread extends Thread implements ImageLoader
{
    private class Content implements Runnable
    {
        File file;
        JLabel target_label;
        Icon icon;

        public Content( File file, JLabel target_label )
        {
            this.file = file;
            this.target_label = target_label;
        }

        public void run() {
            target_label.setIcon(icon);
            parent_container.updateUI();
        }
    }

    private static final Logger logger = Logger.getLogger(ImageLoaderThread.class.getName());
   

    private final List<Content> workque = new LinkedList();

    private boolean cancel_loading = false;

    JComponent parent_container;
    Root root;
    DefaultWidth default_width;

    public ImageLoaderThread( Root root, JComponent parent_container )
    {
        this.parent_container = parent_container;
        this.root = root;
        default_width = new DefaultWidth(root);
    }

    public synchronized void loadIcon(File file, JLabel target_label)
    {
       cancel_loading = false;

       synchronized( workque )
       {
            workque.add( new Content(file, target_label));
            //notify();
       }
    }

    public void cancelLoading()
    {
        cancel_loading = true;
    }

    @Override
    public void run()
    {
       while(true)
       {
           boolean is_empty = false;

           synchronized( workque )
           {
               final int height = default_width.getWidth();

               for( final Content content : workque )
               {
                   if( !cancel_loading )
                   {
                       new AutoLogger(ImageLoaderThread.class.getName()) {

                           @Override
                           public void do_stuff() throws Exception {
                               content.icon = ImageUtils.loadScaledImageIcon(content.file.toString(),height,height,height);
                               java.awt.EventQueue.invokeLater(content);
                           }
                       };
                   }

                   workque.remove(content);
                   break;
               } // for

               is_empty = workque.isEmpty();
           }

           if (is_empty) {
               try {
                   sleep(300);
               } catch (InterruptedException ex) {
                   
               }
           }
       }
    }

    public int getDefaultWidth() {
        return default_width.getWidth();
    }

}
