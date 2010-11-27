/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.redeye.FindDup;

import at.redeye.FrameWork.base.BaseAppConfigDefinitions;
import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import at.redeye.FrameWork.base.prm.impl.GlobalConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.LocalConfigDefinitions;
import at.redeye.FrameWork.base.prm.impl.PrmDefaultCheckSuite;
import java.io.File;

/**
 *
 * @author martin
 */
public class AppConfigDefinitions extends BaseAppConfigDefinitions {

    public static DBConfig IconSize = new DBConfig("IconSize","100","Icon Größe", new PrmDefaultCheckSuite(PrmDefaultChecksInterface.PRM_IS_LONG));
    public static DBConfig FileTypesImages = new DBConfig("FileTypesImages","*.jpg *.jpeg *.bmp *.png *.gif *.tiff","Dateiendungen für Bilder" );
    public static DBConfig FileTypesVideo = new DBConfig("FileTypesVideo","*.avi *.mpg *.mpeg *.mkv *.vob","Dateiendungen für Videos" );
    public static DBConfig FileTypesAudio = new DBConfig("FileTypesAudio","*.ogg *.mp3 *.wav","Dateiendungen für Musik" );
    public static DBConfig FileTypesOffice = new DBConfig("FileTypesOffice","*.doc *.pps *.xls","Dateiendungen für Office Dokumente" );
    public static DBConfig SysDirs = new DBConfig("SystemDirs","c:\\windows" + File.pathSeparator + "c:\\programme","Systemverzeichnisse getrennt durch " + File.pathSeparator );
    public static DBConfig OpenCommand = new DBConfig("OpenCommand", "kde-open", "Kommando für das öffnen einer Datei, oder eines Verzeichnisses");

    public static void registerDefinitions() {

        BaseRegisterDefinitions();

        addLocal(IconSize);
        addLocal(FileTypesAudio);
        addLocal(FileTypesImages);
        addLocal(FileTypesOffice);
        addLocal(FileTypesVideo);
        addLocal(SysDirs);
        addLocal(OpenCommand);

        GlobalConfigDefinitions.add_help_path("/at/redeye/FindDup/resources/Help/Params/");
        LocalConfigDefinitions.add_help_path("/at/redeye/FindDup/resources/Help/Params/");
    }    
}
