package Elements;

/**
 * Ассоциация
 */
public class Association extends Connector{
    public Association() {
        super();
        // Установка стилей элемента
        //addStyle("dashed","1");
        addStyle("dashPattern","6");
        // Задание правил использования
        rules = MySpecRules;
    }
    // Пары элементов, между которыми допустимо проведение ассоциации
    private Object[][] MySpecRules = {
        {Annotation.class.getName(),Gateway.class.getName(),1,1},
        {Annotation.class.getName(),Vertex.class.getName(),1,1},
        {Annotation.class.getName(),Event.class.getName(),1,1},
        {Annotation.class.getName(),Pool.class.getName(),1,1},
        {Pool.class.getName(),Vertex.class.getName(),1,1},
        {Pool.class.getName(),Event.class.getName(),1,1},
        {Vertex.class.getName(),Vertex.class.getName(),1,1}
    };
}
