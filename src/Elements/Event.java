package Elements;

import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxRectangle;
import diagrammeditorx.MyFrame;

/**
 * Базовый класс для всех событий
 */
abstract public class Event extends BasicElement{
    public Event()
    {
        super();
        // Установка размеров элемента
        this.setGeometry(new mxGeometry(10,10,40,40));
        // Установка стилей элемента
        addStyle("verticalLabelPosition", "bottom");
        addStyle("verticalAlign", "top");
        addStyle("fillColor", "white");
        addStyle("resizable", "0");
        addStyle("spacingTop", "10");//отступ лейбла от верхней границы
        addStyle("perimeter","ellipsePerimeter");
        addStyle("shape","ellipse");
        addStyle("strokeWidth","2");
        // Установка максимальной длины отображаемого текста
        visibleTextLenght = 15;
    }
    
    /**
     * Получение периметра объекта с учётом подписи
     * @return - геометрия объекта с учётом подписи
     */
    public mxGeometry getFullGeometry(){
        //MyFrame.getFrame().getGraphComponent().getGraph().getView().getState(this,true);
        mxGeometry g = new mxGeometry(geometry.getX(), geometry.getY(), geometry.getWidth(), geometry.getHeight() + 30);
        return g;
    }
}