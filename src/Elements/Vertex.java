package Elements;

import com.mxgraph.model.mxGeometry;

/**
 * Процесс (задание)
 */
public class Vertex extends BasicElement{
    
    public static int globleId = 0;
    public Vertex()
    {
        super();
        // Установка подписи по умолчанию
        this.setValue("A" + globleId++);
        // Установка начальной геометрии
        this.setGeometry(new mxGeometry(50,10,50,50));
        // Установка стилей элемента
        //addStyle("rounded","1");
        addStyle("fillColor","white");
        addStyle("strokeWidth","2");
        addStyle("strokeColor","black");
        addStyle("whiteSpace","wrap");//распределение текста по всему элементу
        addStyle("spacing","10");
        addStyle("verticalAlign","top");
        addStyle("shape","ellipse");
        // Установка максимальной длины отображаемого текста
        visibleTextLenght = 20;
    }
    
    @Override
    public void setGeometry (mxGeometry mxgmtr) {
        if (mxgmtr != null){
            double w = mxgmtr.getWidth();
            double h = mxgmtr.getHeight();
            double d = (w < h ? w : h);
            //mxGeometry g = new mxGeometry(mxgmtr.getX(), mxgmtr.getY(), d, d);
            mxgmtr.setWidth(d);
            mxgmtr.setHeight(d);
        }
        super.setGeometry(mxgmtr);
    }
}
