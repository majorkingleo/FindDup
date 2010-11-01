/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FindDup;

import at.redeye.FrameWork.base.Root;
import at.redeye.FrameWork.base.prm.PrmCustomChecksInterface;
import at.redeye.FrameWork.base.prm.PrmDefaultChecksInterface;
import at.redeye.FrameWork.base.prm.PrmListener;
import at.redeye.FrameWork.base.prm.impl.PrmActionEvent;
import at.redeye.FrameWork.base.prm.impl.PrmDefaultCheckSuite;
import at.redeye.FrameWork.utilities.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author moberza
 */
public class DefaultWidth
{
     private static final int DEFAULT_WIDTH = 50;
     private static final Logger logger = Logger.getLogger(DefaultWidth.class.getName());
     private Root root;
     private int last_width = -1;

     public DefaultWidth( Root root )
     {
        this.root = root;

         AppConfigDefinitions.IconSize.addPrmListener(new PrmListener() {

             PrmDefaultCheckSuite checker = new PrmDefaultCheckSuite(
                     PrmDefaultChecksInterface.PRM_IS_LONG);

             void onChange(PrmActionEvent event) {
                 if (checker.doChecks(event) == true) {
                     last_width = -1;
                 }
             }

             public void onChange(
                     PrmDefaultChecksInterface defaultChecks,
                     PrmActionEvent event) {
                 onChange(event);
             }

             public void onChange(PrmCustomChecksInterface customChecks,
                     PrmActionEvent event) {
                 onChange(event);
             }
         });
     }

     int getWidth()
     {
         if( last_width != -1 )
             return last_width;

         int width = DEFAULT_WIDTH;

         try {
             width = Integer.valueOf(root.getSetup().getLocalConfig(AppConfigDefinitions.IconSize));
         } catch (NumberFormatException ex) {
             logger.error(ex);
             logger.error(StringUtils.exceptionToString(ex));
         }

         if (width <= 0) {
             width = DEFAULT_WIDTH;
         }

         last_width = width;

         return width;
     }

}
