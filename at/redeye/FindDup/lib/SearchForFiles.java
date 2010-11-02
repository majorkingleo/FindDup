/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FindDup.lib;

import at.redeye.FrameWork.base.Root;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 *
 * @author moberza
 */
public class SearchForFiles
{
    private static class DirFilter implements FileFilter
    {
        public boolean accept(File pathname) {
           return pathname.isDirectory();
        }        
    }
    
    private static final DirFilter directory_filter = new DirFilter();
    FilenameFilter filter;
    FileFoundInterface file_found;
    Root root;

    public SearchForFiles( Root root, FilenameFilter filter, FileFoundInterface file_found )
    {
        this.filter = filter;
        this.file_found = file_found;
        this.root = root;
    }

    public boolean findFiles(File dir) throws FileNotFoundException
    {
       if( !dir.exists() ) {
           throw new FileNotFoundException( 
                   root.MlM(String.format("Das Verzeichnis '%s' existiert nicht",dir.toString()) ));
        }

       File files[] = dir.listFiles(filter);

        if (files != null) {
            for (File file : files) {
                if( !file_found.fileFound(file) )
                    return false;
            }
        }
       File subdirs[] = dir.listFiles(directory_filter);

        if (subdirs != null) {
            for (File subdir : subdirs) {
                try {
                    // do not follow symlinks
                    if (subdir.getAbsolutePath().equals(subdir.getCanonicalPath())) {
                        if (file_found.diveIntoSubDir(subdir)) {
                            if (!findFiles(subdir)) {
                                return false;
                            }
                        }
                    }
                } catch (IOException ex) {
                }
            }
        }

       return true;
    }
}
