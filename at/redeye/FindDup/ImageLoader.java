/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FindDup;

import java.io.File;
import javax.swing.JLabel;

/**
 *
 * @author moberza
 */
public interface ImageLoader
{
    void loadIcon( File file, JLabel target_label );
    int getDefaultWidth();
}
