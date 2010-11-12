/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FindDup;

import at.redeye.FindDup.lib.FileExtFilter;
import at.redeye.FindDup.lib.FileFoundInterface;
import at.redeye.FindDup.lib.SearchForFiles;
import at.redeye.FrameWork.base.AutoLogger;
import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.Setup;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author moberza
 */
public class SearchThread extends Thread implements FileFoundInterface
{
    private Boolean stop_working;
    private Root root;
    private String fileendings;
    private final Map<Long,List<FileEntry>> results = new HashMap();
    private List<File> directories;
    private int fileCounter=0;
    private Map<String,FileEntry> equal_files = new HashMap();
    private Set<String> system_dirs;
    private String progress_info = "";

    public enum STATE
    {
        IDLE,
        SEARCHING_FOR_FILES,
        ANALYSING_FILES,
        DONE
    };

    private STATE currentState;
    private boolean ignoreSystemDirs;

    public SearchThread( Root root,
                         String fileendings,
                         List<File> directories,
                         boolean ignoreSystemDirs )
    {
        this.root = root;
        this.fileendings = fileendings;
        this.directories = directories;
        currentState = STATE.IDLE;
        this.ignoreSystemDirs = ignoreSystemDirs;
    }

    public STATE getCurrentState()
    {
        return currentState;
    }

    public synchronized boolean continueWorking()
    {
        return !stop_working;
    }

    public synchronized void stopWorking()
    {
        stop_working = true;
    }

    public String getProgressInfo()
    {
        return progress_info;
    }

    @Override
    public void run()
    {
        stop_working = false;
        results.clear();
        fileCounter = 0;
        equal_files = new HashMap();
        currentState = STATE.SEARCHING_FOR_FILES;

        final SearchForFiles ssf = new SearchForFiles(root, new FileExtFilter(fileendings), this);

        new AutoLogger(SearchThread.class.getName()) {

            @Override
            public void do_stuff() throws Exception {

                for (File dir : directories) {
                    ssf.findFiles(dir);
                }

                int equalSizes = 0;

                for( Entry<Long,List<FileEntry>> entry : results.entrySet() )
                {
                    List<FileEntry> files = entry.getValue();

                    if( files.size() > 1 )
                    {
                        equalSizes++;
                    }
                }

                logger.info(String.format("%d Dateien gleicher Größe gefunden", equalSizes));

                currentState = STATE.ANALYSING_FILES;

                for( Entry<Long,List<FileEntry>> entry : results.entrySet() )
                {
                     if( !continueWorking() )
                         break;
                     
                    List<FileEntry> files = entry.getValue();

                    if( files.size() > 1 )
                    {
                        compareFiles(files);
                    }
                }

                logger.info(String.format("%d Dateien gleiche Dateien gefunden", equal_files.size()));

                /*
                for( Entry<String,FileEntry> entry : equal_files.entrySet() )
                {
                    logger.info("entry: " + entry.getKey() + " " + entry.getValue().getMD5Sum() );
                }
                 */
            }
        };

        currentState = STATE.DONE;
    }

    public int getNumOfFiles()
    {
        return fileCounter;
    }

    public int getNumOfEqualFiles()
    {
        return equal_files.size();
    }

    public boolean fileFound(File file)
    {
        if( !continueWorking() )
            return false;

        // skip files we can't read
        if( !file.canRead() )
            return true;

        // ignore directories that are named like the macting file ending
        if( file.isDirectory() )
            return true;

        synchronized( results )
        {
            List<FileEntry> entries = results.get(file.length());

            if( entries == null ) {
                entries = new LinkedList<FileEntry>();
                results.put(Long.valueOf(file.length()), entries);
            }

            entries.add(new FileEntry(file));
        }

        fileCounter++;

        return true;
    }

    void compareFiles( List<FileEntry> files ) throws IOException
    {
        for( FileEntry first : files )
        {
            if( !continueWorking() )
               break;

            progress_info = first.getPath();            

            for( FileEntry second : files )
            {
               if( !continueWorking() )
                   break;              

                if( first.getName().equals(second.getName()) )
                    continue;

                if( first.isEqual(second) )
                {
                    equal_files.put(second.getName(), second);
                }
            }
        }
    }
    
    public Map<String,FileEntry> getEqualFiles()
    {
        return equal_files;
    }

    public Map<String,List<FileEntry>> getEqualFilesByMD5()
    {
        Map<String,List<FileEntry>> ergs = new HashMap();

        try {
            for (Entry<String, FileEntry> entry : equal_files.entrySet()) {
                List<FileEntry> entries = ergs.get(entry.getValue().getMD5Sum());

                if( entries == null ) {
                    entries = new LinkedList();
                    ergs.put(entry.getValue().getMD5Sum(), entries);
                }

                entries.add(entry.getValue());
            }
        } catch( IOException ex ) {
            // will not happen, cause we requested md5 sums already before
            throw new RuntimeException(ex.toString());
        }

        return ergs;
    }

    public boolean diveIntoSubDir(File file)
    {
        if( !continueWorking() )
            return false;

        if( !ignoreSystemDirs )
            return true;

        progress_info = file.getPath();

        if( system_dirs == null )
        {
            system_dirs = new HashSet();

            String dirs = root.getSetup().getLocalConfig(AppConfigDefinitions.SysDirs);

            if( dirs == null )
                return true;

            for( String dir : dirs.split(File.pathSeparator) )
            {
                if( Setup.is_win_system() )
                    system_dirs.add(dir.toLowerCase());
                else
                    system_dirs.add(dir);
            }
        }

        if( Setup.is_win_system() )
        {
            if( system_dirs.contains(file.getPath().toLowerCase()) )
                return false;
        }
        else
        {
            if( system_dirs.contains(file.getPath()) )
                return false;
        }

        return true;
    }
}
