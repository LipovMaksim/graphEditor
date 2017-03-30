package Elements;

import com.mxgraph.model.mxGeometry;

/**
 * Конечный разделитель страниц
 */
public class EndPageConnector extends EndEvent{
    
    public EndPageConnector(){
        super();
        // Установка размеров элемента
        this.setGeometry(new mxGeometry(10,10,44,44));
        // Установка стилей элемента
        addStyle("image", "/icons/endPageConnector.png");
        addStyle("shape", "image");
        addStyle("strokeWidth","0");
        addStyle("strokeColor","none");
        // Установка подписи по умолчанию
        this.setValue("");
    }
}
