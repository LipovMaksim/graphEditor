/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagrammeditorx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Elena Sarkisova
 */
public class MyButton extends JButton{
    private Color activeColor = new Color (160,160,160), notActiveColor = new Color (200,200,200);
    public MyButton(String iconPath,String text) throws IOException
    {
        super();
        Image img=ImageIO.read(getClass().getResource(iconPath));
        setIcon(new ImageIcon(img));
        setMaximumSize(new Dimension(48,48));
        active(false);
        setToolTipText(text);
    }
    /**
     * Установить кнопку как активную.
     * @param active 
     */
    public void active(boolean active)
    {
        if(active)
            setBackground(activeColor);
        else
            setBackground(notActiveColor);
    }
}
