package Elements;

import com.mxgraph.model.mxGeometry;

/**
 * Таймер
 */
public class Timer extends Event{
    public Timer()
    {
        super();
        // Установка начальной геометрии
        this.setGeometry(new mxGeometry(10,10,44,44));
        // Установка стилей элемента
        addStyle("image", "/icons/timer.png");
        addStyle("shape", "image");
        // Установка подписи по умолчанию
        this.setValue("Таймер");
    }
}
