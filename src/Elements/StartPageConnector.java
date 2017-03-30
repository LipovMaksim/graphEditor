package Elements;

import com.mxgraph.model.mxGeometry;

/**
 * Начальный разделитель страниц
 */
public class StartPageConnector extends StartEvent{
    
    public StartPageConnector(){
        super();
        // Установка начальной геометрии
        this.setGeometry(new mxGeometry(10,10,44,44));
        // Установка стилей элемента
        addStyle("image", "/icons/startPageConnector.png");
        addStyle("shape", "image");
        addStyle("strokeWidth","0");
        addStyle("strokeColor","none");
        // Установка подписи по умолчанию
        this.setValue("");
    }
}
