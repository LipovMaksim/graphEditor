package Elements;

import com.mxgraph.model.mxGeometry;

/**
 * Начальное событие
 */
public class StartEvent extends Event{
    public StartEvent()
    {
        super();
        // Установка подписи по умолчанию
        this.setValue("Начало");
        // Установка стилей элемента
        addStyle("strokeColor","green");
    }
}
