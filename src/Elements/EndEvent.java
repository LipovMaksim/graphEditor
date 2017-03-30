package Elements;

import com.mxgraph.model.mxGeometry;

/**
 * Конечное событие
 */
public class EndEvent extends Event{
    public EndEvent()
    {
        super();
        // Установка подписи по умолчанию
        this.setValue("Конец");
        // Установка стилей элемента
        addStyle("strokeColor","red");
    }
}
