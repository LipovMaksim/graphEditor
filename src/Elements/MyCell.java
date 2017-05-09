package Elements;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import diagrammeditorx.MyFrame;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Класс, определяющий объект диаграммы
 */
abstract public class MyCell extends mxCell{
    // Полная и отображаемая подпись. элемента
    protected String fullText = "",visibleText = "";
    /**
     * Получение полной подписи элемента
     * @return - полная подпись элемента
     */
    public String getFullText(){
        if (fullText == null)
            fullText = (String) value;
        if (fullText.isEmpty())
            return (String)getValue();
        return fullText;
    }
    // Ограничения на максимальный размер полной и отображаемой подписи
    protected int fullTextLenght = 0, visibleTextLenght = 0;
    public MyCell()
    {
        super();
        this.setId(null);
        
        // Установка стилей элемента
        addStyle("whiteSpace","wrap");//выравнивание текста - по ширине элемента
        addStyle("fontColor","black");
        addStyle("deletable","1");
        addStyle("perimeterSpacing","0");
        addStyle("fontSize","16");
        addStyle("fontFamily","Times New Roman");
        addStyle("cloneable","0");
        
        // Максимальный размеры полной и отображаемой подписи по умолчанию
        fullTextLenght = 500;
        visibleTextLenght = 40;
    }
    /**
     * Функция добавления стиля элементу
     * @param attr - атрибут
     * @param value - значение атрибута
     */
    public void addStyle(String attr, String value)
    {
        String cellStyle = this.getStyle();
        cellStyle += (attr + "=" + value + ";");
        this.setStyle(cellStyle);
    }
    
    /**
     * Установка позиции элемента
     * @param p - точка на диаграмме, в которую надо установить элемент
     */
    public void setPosition(Point p){
        mxGeometry g = getGeometry();
        g.setX(p.getX());
        g.setY(p.getY());
    }
    
    /**
     * Установка позиции элемента
     * @param x - координата х точки, в которую надо установить элемент
     * @param y - координата у точки, в которую надо установить элемент
     */
    public void setPosition(double x, double y){
        mxGeometry g = getGeometry();
        g.setX(x);
        g.setY(y);
    }
    
    /**
     * Валидация размеров подписи
     * @param input - подпись
     */
    public void ValidateText(String input) {
        // При пустой подписи валидация не нужна
        if(input == null) return;
        // Если дляна подписи больше максимальной - обрезать подпись до максимального размера
        if (input.length() <= fullTextLenght)
            fullText = input;
        else
            fullText = input.substring(0, fullTextLenght-1);
        // Если дляна подписи больше максимальной отображаемой - обрезать подпись до максимального отображаемого размера
        if (input.length() <= visibleTextLenght)
            visibleText = input;
        else
            visibleText = input.substring(0, visibleTextLenght-1);
        // Если часть подписи скрывается - добавить к отображаемой части троеточие
        if (visibleText.length() < fullText.length())
            setValue(visibleText+"...");
        else
            setValue(visibleText);
    }
    /**
     * Установка полной подписи объекту
     */
    public void setFullText()
    {
        setValue(fullText);
    }
    
    /**
     * Установка полной подписи объекту
     */
    public void setFullText(String text)
    {
        fullText = text;
        ValidateText(text);
    }
    
    /**
     * Получение максимально допустимой длины подписи
     * @return - максимально допустимая длина подписи
     */
    public int getMaxTextLenght()
    {
        return fullTextLenght;
    }  
    /**
     * Получение полной геометрии элемента
     * @return - полная геометрия объекта
     */
    public mxGeometry getFullGeometry(){
        return getGeometry();
    }
}
