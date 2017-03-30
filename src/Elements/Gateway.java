package Elements;

import com.mxgraph.model.mxGeometry;

/**
 * Базовый класс для всех шлюзов
 */
abstract public class Gateway extends BasicElement{
    public Gateway()
    {
        super();
        this.setId(null);
        // Установка размеров элемента
        this.setGeometry(new mxGeometry(10,10,60,60));
        // Установка стилей элемента
        addStyle("verticalLabelPosition", "bottom");
        addStyle("verticalAlign", "top");
        addStyle("resizable", "0");
        addStyle("spacingTop", "7");
        addStyle("shape", "image");
        addStyle("whiteSpace", "noWrap");
        addStyle("perimeter", "rhombusPerimeter");
        // Установка максимальной длины отображаемого текста
        visibleTextLenght = 15;
    }
    
    /**
     * Получение периметра объекта с учётом подписи
     * @return - геометрия объекта с учётом подписи
     */    
    public mxGeometry getFullGeometry(){
        mxGeometry g = new mxGeometry(geometry.getX(), geometry.getY(), geometry.getWidth(), geometry.getHeight() + 30);
        return g;
    }
}
