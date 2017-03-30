package diagrammeditorx;

import Elements.*;

/**
 * Фабрика элементов диаграммы
 */
public class CellFactory {
    /**
     * Возвращает новый элемент указанного типа
     * @param tool - тип создаваемого элемента из перечисления
     * @return - новый экземпляр элемента указанного типа
     */
    public static MyCell create (Tool tool){
        switch (tool){
            // Процесс (задание)
            case TASK:
                return new Vertex();
            // Начальное событие
            case START_EVENT: 
                return new StartEvent();
            // Конечное событие
            case END_EVENT: 
                return new EndEvent();
            // Таймер
            case TIMER: 
                return new Timer();
            // Аннотация
            case ANNOTATION: 
                return new Annotation();
            // Пул
            case POOL: 
                return new Pool();
            // Эксклюзивный шлюз (Исключающее ИЛИ)
            case EXCLUSIVE: 
                return new ExclusiveGateway();
            // Инклюзивный шлюз (Включающее ИЛИ)
            case INCLUSIVE: 
                return new InclusiveGateway();
            // Параллельный шлюз (И)
            case PARALLEL: 
                return new ParallelGateway();
            // Ассоциация
            case ASSOCIATION:
                return new Association();
            // Поток управления
            case FLOW:
                return new SequenceFlow();
            // Начальный разделитель страниц
            case START_PAGE_CONNECTOR:
                return new StartPageConnector();
            // Конечный разделитель страниц
            case END_PAGE_CONNECTOR:
                return new EndPageConnector();
       }
        return null;
    }
}
