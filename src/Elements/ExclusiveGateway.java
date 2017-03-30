package Elements;

/**
 * Эксклюзивный шлюз (Исключающее ИЛИ)
 */
public class ExclusiveGateway extends Gateway{
    
    public ExclusiveGateway() {
        super(); 
        // Установка стилей элемента
        addStyle("image", "/icons/exclusive.png");
        // Установка подписи по умолчанию
        this.setValue("Исключающее ИЛИ");
    }
}
