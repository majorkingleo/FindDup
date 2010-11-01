/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FindDup;

import at.redeye.FrameWork.utilities.MD5Calc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author moberza
 */
public class FileEntry implements Serializable
{
    private File file;
    private String md5_sum;
    private static final transient MD5Calc md5Calc = new MD5Calc();

    public FileEntry( File file )
    {
        this.file = file;
    }

    long getSize()
    {
        return file.length();
    }

    boolean isEqual( FileEntry other ) throws FileNotFoundException, IOException
    {
        if( getSize() != other.getSize() )
            return false;

        if( !getMD5Sum().equals(other.getMD5Sum()) )
            return false;

        return true;
    }

    public String getMD5Sum() throws FileNotFoundException, IOException
    {
        if( md5_sum == null )
        {
            md5_sum = md5Calc.calcCheckSum(file);
        }

        return md5_sum;
    }

    public String getName() throws IOException
    {
        return file.getCanonicalPath();
    }

    public String getFileName()
    {
        return file.getName();
    }

    public String getPath()
    {
        return file.getParent();
    }

    public File getFile()
    {
        return file;
    }
}
