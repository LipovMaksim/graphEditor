package Elements;

import com.mxgraph.model.mxGeometry;

/**
 * Аннотация
 */
public class Annotation extends BasicElement{
    private double ratio = 48/128.0; // Соотношение сторон элемента
    public Annotation()
    {
        super();
        // Установка подписи по умолчанию
        this.setValue("Аннотация");
        // Установка размеров элемента
        mxGeometry geometry = new mxGeometry(10,10,200,75);
        this.setGeometry(geometry);
        // Установка стилей элемента
        addStyle("strokeWidth","0;");
        addStyle("image", "/icons/annotation2.png");
        addStyle("imageAligh", "left");
        addStyle("shape", "image");
        addStyle("portConstraint", "west");//связь всегда только справа
        addStyle("imageBackground", "#CFCFCF");
        addStyle("opacity", "30");
        addStyle("whiteSpace","wrap");//распределение текста по всему элементу
        addStyle("spacing","10");
        addStyle("verticalAlign","top");
        addStyle("align","left");
        // Установка максимальной длины отображаемого текста
        visibleTextLenght = 30;
    }
    /**
     * Функция,сохраняющая соотношение сторон элемента для корректного отображения
     */
    @Override
    public void validateCellSize()
    {
        super.validateCellSize();
        // Получение текущей ширины объекта
        mxGeometry geo = getGeometry();
        double W = geo.getWidth();
        // Усьаглвка высоты объекта в соответствии с соотношением сторон
        double H = W*ratio;
        // Установка новых размеров
        geo.setHeight(H);
        setGeometry(geo);
    }
    /**
     * Функция поворота элемента вправо
     */
    public void flipToRight()
    {
        addStyle("imageFlipH","0");
    }
    /**
     * Функция поворота элемента влево
     */
    public void flipToLeft()
    {
        addStyle("imageFlipH","1");
    }
}