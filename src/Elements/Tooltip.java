/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Elements;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import static java.awt.SystemColor.text;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.paint.Color;
import javax.swing.JTextField;

/**
 * Всплывающая подсказка
 * @author Lipov Maksim
 */
public class Tooltip extends BasicElement{
    
    int width = 250; //Максимальная ширина
    String lable;
    public Tooltip(String val, Graphics g)
    {
        super();
        visibleTextLenght=fullTextLenght;
        //int h=rebieldText(val, g);
        FontMetrics fm = g.getFontMetrics(new Font("Normal", 0, 17)); //Метрики текста
        int size = fm.stringWidth(val); //Длина текста
        this.setValue(val);
        //Задаем размер элемента, согласно длине текста
        this.setGeometry(new mxGeometry(10,10, (width<size ? width : size)+10,(((int)size/width)+1)*24+2));
        
        //Задаем стили
        addStyle("gradientDirection","west");
        addStyle("overflow","hidden;");
        addStyle("strokeWidth","1;");
        addStyle("strokeColor","#000000;");
        addStyle("fillColor","ffff99");
        addStyle("align","left");
        addStyle("whiteSpace","wrap");//распределение текста по всему элементу
        addStyle("verticalAlign","top"); 
        addStyle("editable","0");
        addStyle("resizable","0");
        addStyle("movable","0");
    }
}