/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package at.redeye.FindDup;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author martin
 */
public class ImageCellRenderer implements ListCellRenderer {

    private static Color highlightColor = new Color(50,70,229);
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel component = (JLabel) value;

        if( isSelected )
        {
            component.setBackground(highlightColor);
            component.setForeground(Color.WHITE);
        }
        else
        {
            component.setBackground(Color.WHITE);
            component.setForeground(Color.BLACK);
        }

        return component;
    }
}
