package Elements;

/**
 * Инклюзивный шлюз (Включающее ИЛИ)
 */
public class InclusiveGateway extends Gateway{
    
    public InclusiveGateway() {
        super();
        // Установка подписи по умолчанию
        this.setValue("Включающее ИЛИ");
        // Установка стилей элемента
        addStyle("image", "/icons/inclusive.png");
    }
}