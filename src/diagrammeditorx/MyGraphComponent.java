package diagrammeditorx;

import Elements.BasicElement;
import Elements.Connector;
import Elements.Tooltip;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.awt.Point;
import static java.lang.System.out;
import java.util.List;
import javax.swing.JScrollBar;

/**
 * Рабочее поле
 */
public class MyGraphComponent extends mxGraphComponent{

    private double minSize=1;
    
    public MyGraphComponent(MyGraph graph) {
        super(graph);
        setConnectable(false);
        new mxRubberband(this);
        
        //Слушатель клавиатуры
        KeyListener keyListner = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_DELETE: //По кнопке Delete
                        //Получить выделенные объекты
                        Object[] cells = getGraph().getSelectionCells();
                        //Удалить объекты
                        getGraph().removeCells(cells);
                        repaint();
                        break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
            @Override
            public void keyTyped(KeyEvent e) {
            }    
        };
        addKeyListener(keyListner); //Добавить слушателя клавиатуры
    }

    /**
     * Возвращает позицию мыши на графе
     * @return позиция мыши на графе
     */
    public Point getTrueMousePosition(){
        mxPoint origin = getGraph().getOrigin(); //Сдвиг координат (в минус)
        //Получает позицию мыши, прибавляет значения скролов и сдвиг (origin)
        return new Point((int)(getMousePosition().getX()+getHorizontalScrollBar().getValue() + origin.getX()), (int)(getMousePosition().getY()+getVerticalScrollBar().getValue() + origin.getY()));
    }
	
    /**
     * Вывод на печать
     */
    public void print()
    {
        PrinterJob pj = PrinterJob.getPrinterJob();
        if (pj.printDialog())
        {
                PageFormat pf = getPageFormat();
                Paper paper = new Paper();
                double margin = 36;
                paper.setImageableArea(margin, margin, paper.getWidth()
                                - margin * 2, paper.getHeight() - margin * 2);
                pf.setPaper(paper);
                pj.setPrintable(this, pf);

                try
                {
                        pj.print();
                }
                catch (PrinterException e2)
                {
                    System.out.println(e2);
                }
        }
    }
    
    /**
     * Возвращает пересечения объектов (наследников BasicElement) с др объектами
     * @return список пар пересекающихся объектов
     */
    public ArrayList <ArrayList<mxCell>> getVerticeIntersections(){
        ArrayList <ArrayList<mxCell>> res = new ArrayList <ArrayList<mxCell>>();
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent()); //Получить все вершины на графе
        for (int i=0; i<vertices.length; i++){ //Для всех вершин
            if (!(vertices[i] instanceof Tooltip)) //Если i-ая вершина не всплывающая подсказка
                for (int j=i+1;j<vertices.length;j++){ //Для всех вершин после i-той
                    if (!(vertices[j] instanceof Tooltip)){ //Если j-ая вершина не всплывающая подсказка
                        int step = 3; //шаг контура вокруг объекта
                        Rectangle r1 = ((BasicElement)vertices[i]).getFullGeometry().getRectangle(); //Прямоугольник контура i-ой вершины
                        //Увеличиваем контур на step пикселей
                        r1 = new Rectangle((int)(r1.getX()+step), (int)(r1.getY()+step), (int)(r1.getWidth()+step*2), (int)(r1.getHeight()+step*2));
                        Rectangle r2 = ((BasicElement)vertices[j]).getFullGeometry().getRectangle(); //Прямоугольник контура j-ой вершины
                        //Увеличиваем контур на step пикселей
                        r2 = new Rectangle((int)(r2.getX()+step), (int)(r2.getY()+step), (int)(r2.getWidth()+step*2), (int)(r2.getHeight()+step*2));
                        if (isIntersectoin(r1, r2)){ //Если контуры пересекаются, создаем из вершин пару и добавляем ее к результату
                            ArrayList<mxCell> arr = new ArrayList<mxCell>();
                            arr.add((mxCell)vertices[i]);
                            arr.add((mxCell)vertices[j]);
                            res.add(arr);
                        }
                    }
                }
        }
        return res;
    }
    
    /**
     * Возвращает пересечения объектов (наследников BasicElement) с дугами
     * @return список пар пересекающихся mxCell (дуга, объект)
     */
    public ArrayList <ArrayList<mxCell>> getIntersectionsBetwenVerticesAndEdges(){
        ArrayList <ArrayList<mxCell>> res = new ArrayList <ArrayList<mxCell>>();
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent()); //Получить все вершины на графе
        for (Object v : vertices){ //Для всех вершин
            if (!(v instanceof Tooltip)){ //Если вершина не всплывающая подсказка
                Object[] edges = getGraph().getIncomingEdges(v); //Получить список входящих дуг
                for (Object e : edges){ //Для всех дуг
                    List<mxPoint> absolutePoints = graph.getView().getState(e,true).getAbsolutePoints(); //Получить список точек дуги
                    if (absolutePoints != null)
                    for(int i=1; i<absolutePoints.size(); i++){ //Для всех пар последующих точек
                        for (Object ver : vertices){ //Для всех вершин
                            Rectangle edgeSectionRectangle = getEdgeSectionRectangle(absolutePoints.get(i-1),absolutePoints.get(i),3); //Получить контур для секции дуги
                            //Если контур объекта пересекается с контуром дуги создать и добавить в результат пересечение
                            if (ver != v && ver instanceof BasicElement && !(ver instanceof Tooltip) && isIntersectoin(((BasicElement)ver).getFullGeometry().getRectangle(),edgeSectionRectangle)){
                                ArrayList<mxCell> arr = new ArrayList<mxCell>();
                                arr.add((mxCell)e);
                                arr.add((mxCell)ver);
                                res.add(arr);
                            }
                        }
                    }
                }
            }
        }
        return res;
    }
    
    /**
     * Создает контур для ортогонального отрезка (прямой секции дуги)
     * @param p1 - начало отрезка
     * @param p2 - конец отрезка
     * @param step - отступ вокруг отрезка
     * @return - контур вокруг отрезка
     */
    private static Rectangle getEdgeSectionRectangle(mxPoint p1, mxPoint p2, double step){
        double  x1,x2,y1,y2;
        
        /*
            x1 - наименьшая координата по оси Х
            x2 - наибольшая координата по оси Х
            y1 - наименьшая координата по оси Y
            y2 - наибольшая координата по оси Y
        */
        if (p1.getX()<p2.getX()){
            x1 = p1.getX();
            x2 = p2.getX();
        }
        else{
            x2 = p1.getX();
            x1 = p2.getX();
        }
        
        if (p1.getY()<p2.getY()){
            y1 = p1.getY();
            y2 = p2.getY();
        }
        else{
            y2 = p1.getY();
            y1 = p2.getY();
        }  
        //Создается и возвращается прямоугольник по заданным координатам с заданным отступом
        return new Rectangle((int)(x1-step), (int)(y1-step), (int)(x2-x1+step*2), (int)(y2-y1+step*2));
    }
    
    /**
     * Возвращает пересекаются ли прямоугольники
     * @param r1 - первый прямоугольник
     * @param r2 - второй прямоугольник
     * @return - признак пересечения
     */
    static public boolean isIntersectoin(Rectangle r1, Rectangle r2){
        double  r1x1 = r1.getX(),
                r1x2 = r1.getX()+r1.getWidth(),
                r1y1 = r1.getY(),
                r1y2 = r1.getY()+r1.getHeight(),
                r2x1 = r2.getX(),
                r2x2 = r2.getX()+r2.getWidth(),
                r2y1 = r2.getY(),
                r2y2 = r2.getY()+r2.getHeight();
        return !(r1x1 > r2x2 || r1x2 < r2x1 || r1y2 < r2y1 || r1y1 > r2y2);
    }
}
