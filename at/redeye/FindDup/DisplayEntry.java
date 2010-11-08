/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FindDup;

import at.redeye.FrameWork.base.imagestorage.ImageUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author moberza
 */
public class DisplayEntry extends JLabel
{
    private List<FileEntry> entries;
    private ImageLoader il;

    private static final Set<String> image_extensions = new HashSet<String>();

    public DisplayEntry(List<FileEntry> entries, ImageLoader il) {
        this.entries = entries;
        this.il = il;

        StringBuilder line = new StringBuilder();

        line.append("<html><body>&nbsp;");
        line.append(entries.get(0).getFileName());
        line.append("<ul>");

        for (FileEntry fe : entries) {
            line.append("<li>");
            line.append(fe.getPath());
            line.append("</li>");
        }

        line.append("</ul>");
        line.append("</body></html>");

        setText( line.toString() );

        setOpaque(true);

        if( isImage(entries.get(0).getFile()) )
        {
           String md5sum = null; 
           
           try {
            md5sum = entries.get(0).getMD5Sum();
           } catch( FileNotFoundException ex ) {

           } catch( IOException ex ) {

           }

           il.loadIcon(entries.get(0).getFile(), this,md5sum);
        }

        setVerticalTextPosition(TOP);
        
        setIconTextGap(il.getDefaultWidth()+4);
    }

    private boolean isImage( File file )
    {
        String ext = ImageUtils.getExtension(entries.get(0).getFile()).toLowerCase();

        if( image_extensions.isEmpty() )
        {
            image_extensions.add("jpg");
            image_extensions.add("jpeg");
            image_extensions.add("png");
            image_extensions.add("gif");
            image_extensions.add("tiff");
            image_extensions.add("xbm");
            image_extensions.add("bmp");
        }

        return image_extensions.contains(ext);
    }

    @Override
    public void setIcon( Icon icon )
    {
        if( icon != null )
        {
            setIconTextGap(getIconTextGap()-icon.getIconWidth());
        }
        
        super.setIcon(icon);
    }

    public List<FileEntry> getFileEntries()
    {
        return entries;
    }
}
