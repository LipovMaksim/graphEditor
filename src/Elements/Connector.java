package Elements;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxStylesheet;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Связь на диаграмме
 */
 abstract public class Connector extends MyCell{
    protected Object[][] rules = null;  // Правила, по которым может проходить связь (пары типов объектов, между которыми она допустима)
    private mxPoint fromPoint;           // Точка привязки объекта, от которого идет связь
    private mxPoint toPoint;             // Точка привязки объекта, к которому идет связь
    public Connector()
    {
        super();
        this.setId(null);
        this.setEdge(true);
        this.setVisible(true);
        // Установка начальной геометрии
        this.setGeometry(new mxGeometry(10,10,40,40));
        // Установка стилей элемента
        //addStyle("bendable", "1");
        //addStyle("whiteSpace","wrap");//выравнивание текста - по ширине элемента
        addStyle("fontColor","black");
        addStyle("deletable","1");
        addStyle("fontSize","16");
        addStyle("fontFamily","Times New Roman");
        //addStyle("orthogonal", "0");
        addStyle("strokeWidth", "1"); 
        addStyle("strokeColor","black");
        addStyle("endArrow","none");

        //addStyle("edgeStyle", "elbowEdgeStyle");
        //addStyle("sourcePerimeterSpacing", "0");//расстояние от связи до элемента
        // Установка максимальной длины отображаемого текста
        visibleTextLenght = 20;
        //put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        
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
     * Валидация связи
     * @param source - объект, от которого идет связь
     * @param target - объект, к которому идет связь
     * @return - признак корректности связи
     * @throws ClassNotFoundException - исключение при попытке обращения к несуществующему классу
     */
    public boolean checkConnection(MyCell source, MyCell target) throws ClassNotFoundException
    {
        // Для всех правил текущего типа связи
        for(int i = 0; i < rules.length;i++)
        {
            // Получение названий классов объектов правила
            String firstClassName = (String)rules[i][0];
            String secondClassName = (String)rules[i][1];

            // Получение классов объектов по их названию
            Class firstClass = Class.forName(firstClassName);
            Class secondClass = Class.forName(secondClassName);  
  
            // Проверка наличия допускающего правила для заданных объектов
            boolean ok;
            ok = firstClass.isInstance(source) ;
            ok &= secondClass.isInstance(target);
            ok &= ((int)rules[i][2] == 1);
            if (ok) return true;
            
            ok = firstClass.isInstance(target) ;
            ok &= secondClass.isInstance(source);
            ok &= ((int)rules[i][3] == 1);
            if (ok) return true;
        }
        return false;
    }
    /**
     * Установка точки привязки к объекту, от которого идет связь
     * @param fromPoint - координата точки привязки
     */
    public void setFromPoint(mxPoint fromPoint) {
        this.fromPoint = fromPoint;
        //style += mxConstants.STYLE_EXIT_X + "=" + fromPoint.getX () + ';';
        //style += mxConstants.STYLE_EXIT_Y + "=" + fromPoint.getY () + ';';
    }
    /**
     * Получение точки привязки связи
     * @return - точка привязки
     */
    public mxPoint fromPoint() {
        return fromPoint;
    }
    /**
     * Установка точки привязки к объекту, к которому идет связь
     * @param toPoint - координата точки привязки
     */
    public void setToPoint(mxPoint toPoint) {
        this.toPoint = toPoint;
        //style += mxConstants.STYLE_ENTRY_X + "=" + toPoint.getX () + ';';
        //style += mxConstants.STYLE_ENTRY_Y + "=" + toPoint.getY () + ';';
    }
    /**
     * Получение точки привязки связи
     * @return - точка привязки
     */
    public mxPoint toPoint() {
        return toPoint;
    }
    
    @Override
    public void setGeometry(mxGeometry g) {
        if (getGeometry() != null)
            return;
        super.setGeometry(g);
    }
}