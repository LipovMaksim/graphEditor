package Elements;

/**
 * Поток управления
 */
public class SequenceFlow extends Connector{
    public SequenceFlow()
    {
        super();
        // Установка стилей элемента
        addStyle("endArrow", "classic");
        addStyle("endFill", "1");
        // Задание правил использования
        rules = MySpecRules; 
    }
    // Пары элементов, между которыми допустимо проведение ассоциации
    private Object[][] MySpecRules = {
        {Vertex.class.getName(),Gateway.class.getName(),1,1},
        {StartEvent.class.getName(),Vertex.class.getName(),1,0},
        {StartEvent.class.getName(),Gateway.class.getName(),1,0},
        {StartEvent.class.getName(),Timer.class.getName(),1,0},
        {StartEvent.class.getName(),EndEvent.class.getName(),1,0},
        {EndEvent.class.getName(),Vertex.class.getName(),0,1},
        {EndEvent.class.getName(),Gateway.class.getName(),0,1},
        {EndEvent.class.getName(),Timer.class.getName(),0,1},
        {Vertex.class.getName(),Vertex.class.getName(),1,1},
        {Gateway.class.getName(),Gateway.class.getName(),1,1},
        {Timer.class.getName(),Vertex.class.getName(),1,1},
        {Timer.class.getName(),Gateway.class.getName(),1,1}   
        
    };
}
