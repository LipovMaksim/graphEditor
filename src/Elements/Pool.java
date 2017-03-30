package Elements;

import com.mxgraph.model.mxGeometry;

/**
 * Пул
 */
public class Pool extends BasicElement{
    public Pool()
    {
        super();
        // Установка подписи по умолчанию
        this.setValue("Пул");
        // Установка начальной геометрии
        this.setGeometry(new mxGeometry(10,10,200,50));
        // Установка стилей элемента
        addStyle("fillColor", "white");
        addStyle("strokeWidth", "2");
        addStyle("strokeColor", "black");
        addStyle("whiteSpace","wrap");//распределение текста по всему элементу
        addStyle("spacing","10");
        addStyle("verticalAlign","top");
        // Установка максимальной длины отображаемого текста
        visibleTextLenght = 20;
    }
}
