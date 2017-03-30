/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagrammeditorx;

import java.awt.Component;
import javax.swing.JScrollPane;

/**
 *
 * @author Elena Sarkisova
 */
public class MyScrollPane extends JScrollPane{
    public MyScrollPane(Component view)
    {
        super(view);
        setSize(600, 600);
    }
}
