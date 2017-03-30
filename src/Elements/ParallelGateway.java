package Elements;

/**
 * Параллельный шлюз (И)
 */
public class ParallelGateway extends Gateway{
    
    public ParallelGateway() {
        super();
        // Установка подписи по умолчанию
        this.setValue("И");
        // Установка стилей элемента
        addStyle("image","/icons/parallel.png");
    }
}
