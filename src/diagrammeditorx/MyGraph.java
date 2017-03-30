package diagrammeditorx;

import Elements.*;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Класс диаграммы
 */
public class MyGraph extends mxGraph{

    MyGraphComponent gc;                    // Рабочая область
    private boolean pasting;                // Флаг вставки
    
    public void setPasting(boolean val) {
        pasting = val;
    }
    
    public boolean isPasting() {
        return pasting;
    }
    
    //Слушатель перемещения ячеек
    mxIEventListener cellMoved = new mxIEventListener(){
        @Override
        public void invoke(Object o, mxEventObject eo) {
            Object [] oss  = (Object[])(eo.getProperty("cells"));
            int l=0;
            for(int i=0;i<oss.length;i++){
                if ((oss[i] instanceof BasicElement)){
                    l++;
                }
            }
            Object [] os = new Object [l]; 
            ArrayList <Object> objects = new ArrayList<Object>();
            int j=0;
            for(int i=0;i<oss.length;i++){
                if ((oss[i] instanceof BasicElement)){
                    os[j]=oss[i];
                    objects.add(os[j]);
                    j++;
                }
            }
            double dx = (double)eo.getProperty("dx"); //Перемещение по оси Х
            double dy = (double)eo.getProperty("dy"); //Перемещение по оси Y
            boolean re = false; //Флаг возврата ячеек на исходную позицию
            for (Object obj : os){
                if (obj instanceof BasicElement && !re){
                    //Пересечения ячеек на поле
                    ArrayList<ArrayList<mxCell>> intersections = MyFrame.getFrame().getGraphComponent().getVerticeIntersections();
                    
                    //Пересечения ячеек с дугами
                    ArrayList<ArrayList<mxCell>> intersectionsBetwenVerticesAndEdges = MyFrame.getFrame().getGraphComponent().getIntersectionsBetwenVerticesAndEdges();
                    
                    //Проверка на возврат ячеек на место
                    if (checkVerticeIntersections(intersections, os)/* || checkVerticesAndEdgesIntersections (intersectionsBetwenVerticesAndEdges, os)*/){
                        re=true;
                    }
                }
            }
            if (re){ 
                //Вернуть все ячейки на исходную позицию
                for (Object obj : os){
                    BasicElement be = ((BasicElement)obj);
                    Point p = be.getGeometry().getPoint();
                    be.setPosition(p.getX() - dx, p.getY() - dy);
                }
                setPasting(false);
                return;
            }
            
            // Проверка на перемещение связей
            HashSet<Connector> changedEdges = new HashSet<Connector>();
            BasicElement el = null;
            Connector c = null;
            for (Object obj : objects) {
                if (obj instanceof BasicElement) {
                    el = (BasicElement) obj;
                    for (j = 0; j < el.getEdgeCount(); ++j) {
                        c = (Connector) el.getEdgeAt(j);
                        if (!(objects.contains(c.getSource()) && objects.contains(c.getTarget())))
                            changedEdges.add(c);
                        else {
                            for (mxPoint p : c.getGeometry().getPoints()) {
                                p.setX(p.getX() + dx/2);
                                p.setY(p.getY() + dy/2);
                            }
                        }                            
                    }
                }
            }
        /*
            if (changedEdges.size() > 0) {
                if (!repaintConnections(changedEdges)) {
                    for (Object obj : os){
                        BasicElement be = ((BasicElement)obj);
                        Point p = be.getGeometry().getPoint();
                        be.setPosition(p.getX() - dx, p.getY() - dy);
                        JOptionPane.showMessageDialog(MyFrame.getFrame(),"Невозможно построить все связи из новой позиции.","Ошибка",JOptionPane.ERROR_MESSAGE);
                    }
                    setPasting(false);
                    return;
                }
            }*/
            setPasting(false);
        }
  
        /**
         * Проверяет пересечения на пренадлежность к объектам
         * @param intersections - пересечения
         * @param objects - ячейки
         * @return в списке пересечений присутствуюят ячейки из списка objects
         */
        private boolean checkVerticeIntersections(ArrayList<ArrayList<mxCell>> intersections, Object [] objects){
            boolean res = false;
            for (ArrayList<mxCell> intersection : intersections){
                for (Object o : objects){
                    if (intersection.contains(o) && intersection.get(0)!=intersection.get(1)){
                        res= true;
                    }
                }
            }
            return res;
        }
        
        /**
         * Проверяет пересечения с дугами на пренадлежность к объектам и то, что объекты не соединины с дугами
         * @param intersections
         * @param objects
         * @return 
         */
        private boolean checkVerticesAndEdgesIntersections(ArrayList<ArrayList<mxCell>> intersections, Object [] objects){
            boolean res = false;
            for (ArrayList<mxCell> intersection : intersections){
                for (Object o : objects){
                    //Если отслеживать появившиеся пересечения даже с теми объектами, кторые не пересекались, то убрать "intersection.get(1)==o && "
                    if (intersection.get(1)==o && (intersection.get(0).getSource()!=o && intersection.get(0).getTarget()!=o)){
                        res= true;
                    }
                }
            }
            return res;
        }
    };
    
    //Слушатель добавления ячеек
    mxIEventListener cellAdded = new mxIEventListener(){
        @Override
        public void invoke(Object o, mxEventObject eo) {
            Object [] os  = (Object[])(eo.getProperty("cells")); //Создаваемые объекты
            boolean re = false; //Вернуть диаграмму к исходному состоянию
            for (Object obj : os){
                if (obj instanceof BasicElement && !re){
                    ////Пересечения ячеек на поле
                    ArrayList<ArrayList<mxCell>> intersections = MyFrame.getFrame().getGraphComponent().getVerticeIntersections();
                    
                    //Пересечения ячеек с дугами
                    ArrayList<ArrayList<mxCell>> intersectionsBetwenVerticesAndEdges = MyFrame.getFrame().getGraphComponent().getIntersectionsBetwenVerticesAndEdges();
                    
                    //Проверка на возврат диаграммы к исходному состоянию
                    if (checkVerticeIntersections(intersections, os)/* || checkVerticesAndEdgesIntersections (intersectionsBetwenVerticesAndEdges, os)*/){
                        re=true;
                    }
                }
            }
            if (re){
                removeCells(os); //Удалить ячейки
                repaint();
                //Выдать ошибку
                JOptionPane.showMessageDialog(MyFrame.getFrame(),"Наложение объектов недопустимо.","Ошибка",JOptionPane.ERROR_MESSAGE);
            }
        }

        /**
         * Проверяет пересечения на пренадлежность к объектам
         * @param intersections - пересечения
         * @param objects - ячейки
         * @return в списке пересечений присутствуюят ячейки из списка objects
         */
        private boolean checkVerticeIntersections(ArrayList<ArrayList<mxCell>> intersections, Object [] objects){
            boolean res = false;
            for (ArrayList<mxCell> intersection : intersections){
                for (Object o : objects){
                    if (intersection.contains(o) && intersection.get(0)!=intersection.get(1)){
                        res= true;
                    }
                }
            }
            return res;
        }

        /**
         * Проверяет пересечения с дугами на пренадлежность к объектам и то, что объекты не соединины с дугами
         * @param intersections
         * @param objects
         * @return 
         */
        private boolean checkVerticesAndEdgesIntersections(ArrayList<ArrayList<mxCell>> intersections, Object [] objects){
            boolean res = false;
            for (ArrayList<mxCell> intersection : intersections){
                for (Object o : objects){
                    //Если отслеживать появившиеся пересечения даже с теми объектами, кторые не пересекались, то убрать "intersection.get(1)==o && "
                    if (intersection.get(1)==o && (intersection.get(0).getSource()!=o && intersection.get(0).getTarget()!=o)){
                        res= true;
                    }
                }
            }
            return res;
        }
    };
    
    public MyGraph()
    {
        super();       
        setAllowDanglingEdges(false); //запрет непривязанных связей
        setCellsSelectable(true); //возможность выделения объектов
        setAutoOrigin(true); // автоматическое смещение рабочейобласти при переносе объектов за левую или верхнюю границы рабочей области
        setDropEnabled(false); //запрет на вклинивание в связь между элементами другого элемента
        setBorder(10); //отступ от краев рабочей области
        addListener(mxEvent.CELLS_MOVED, cellMoved);
        addListener(mxEvent.CELLS_ADDED, cellAdded);
        
    }
    /**
     * Получить изображение диаграммы в виде картинки.
     */
    public Image getImage()
    {
        return mxCellRenderer.createBufferedImage(this, null, 1, Color.WHITE, false, null);
    }
    /**
     * Добавить связь между элементами.
     */
    public void addConnection(Connector connection, MyCell source, MyCell target)
    {
        getModel ().beginUpdate ();
        // Точки привязки первого объекта
        //ArrayList<mxPoint> fromPoints = VertexHandler.FindPlacesOnPerimetrForEdge ((mxCell) source, (mxCell) target);
        // Точки привязки второго объекта
        //ArrayList<mxPoint> toPoints = VertexHandler.FindPlacesOnPerimetrForEdge ((mxCell) target, (mxCell) source);
        addEdge(connection, getDefaultParent(), source, target, null);
        //connection.setFromPoint(fromPoints.get(0));
        //connection.setToPoint(toPoints.get(0));
        getModel ().endUpdate ();
    }
    public void addConnection(Connector connection, MyCell source, MyCell target, boolean findpath)
    {
        getModel ().beginUpdate ();
        // Точки привязки первого объекта
        //ArrayList<mxPoint> fromPoints = VertexHandler.FindPlacesOnPerimetrForEdge ((mxCell) source, (mxCell) target);
        // Точки привязки второго объекта
        //ArrayList<mxPoint> toPoints = VertexHandler.FindPlacesOnPerimetrForEdge ((mxCell) target, (mxCell) source);
        /*    
        int i = 0;
        boolean found = false;
        // Ищем пару точек привязки для проведения связи
        while (!found && i < Math.min(fromPoints.size(), toPoints.size())) {
            // Попробовать построить связь минимальной длины между несколькими парами точек
            Object[] objects = getChildCells (getDefaultParent());
            ArrayList<mxPoint> minLengthPoints = null;
            int minLengthIndex = -1;
            int j = i;
            for (; j < i + 2 && j < Math.min(fromPoints.size(), toPoints.size()); j++) {
                ArrayList<mxPoint> points = EdgeHandler.FindEdgePoints ((mxCell) source, (mxCell) target, fromPoints.get(j), toPoints.get(j), objects);
                if (points != null && EdgeHandler.lastPathCost <= EdgeHandler.maxPathCost)
                    if (minLengthPoints == null || connectionLength(points) < connectionLength(minLengthPoints)) {
                        minLengthPoints = points;
                        minLengthIndex = j;
                    }
            }
            i = j;
            // Если удалось найти путь - задать его связи и завершить поиск
            if (minLengthPoints != null) {
                found = true;
                addEdge(connection, getDefaultParent(), source, target, null);
                connection.setFromPoint(fromPoints.get(minLengthIndex));
                connection.setToPoint(toPoints.get(minLengthIndex));
                mxGeometry geometry = connection.getGeometry ();
                if (geometry != null) {
                    geometry.setPoints (minLengthPoints);
                }
            }
            i++;
        }
        // Если не удалось найти путь - выдать сообщение об ошибке
        if (!found) {
            clearSelection ();
            addSelectionCell(connection);
            deleteSelectedObjects ();
            JOptionPane.showMessageDialog(MyFrame.getFrame(), "Невозможно проложить путь для связи", "Ошибка", JOptionPane.ERROR_MESSAGE);
        }*/
        addEdge(connection, getDefaultParent(), source, target, null);
        getModel ().endUpdate ();
    }
    
    /**
     * Возвращает длину пути по точкам
     * @return длина связи
     */
    public int connectionLength(ArrayList<mxPoint> points) {
        int length = 0;
        for (int i = 1; i < points.size(); ++i) {
            length += Math.abs(points.get(i).getX() - points.get(i-1).getX()) 
                    + Math.abs(points.get(i).getY() - points.get(i-1).getY());
        }
        return length;
    }
    
    /**
     * Перестроить связи для перемещенных стрелок
     */
    public boolean repaintConnections(HashSet<Connector> changedEdges) {
        // Получение всех объектов, кроме перемещенных связей
        Object[] objects = getChildCells (getDefaultParent());
        ArrayList<Object> staticObjects = new ArrayList<Object> ();
        boolean f = false;
        for (Object obj : objects) {
            f = false;
            for (Connector c : changedEdges) {
                if (obj == c) {
                    f = true;
                    break;
                }
            }
            if (!f) {
                staticObjects.add(obj);
            }
        }
        
        // Поиск пути последовательно для каждой связи
        ArrayList<List<mxPoint> > oldPoints = new ArrayList<List<mxPoint> >();
        for (Connector c : changedEdges) {
            // Запоминание старого пути для отката в случае неудачи
            mxGeometry geometry = c.getGeometry ();
            if (geometry != null) {
                oldPoints.add(geometry.getPoints());
            }
            
            // Поиск пути для текущей связи
            int i = 0;
            Object[] newArr = new Object[staticObjects.size()];
            for (Object obj : staticObjects) {
                newArr[i++] = obj;
            }
            MyCell source = (MyCell) c.getSource(), target = (MyCell) c.getTarget();
            mxPoint fromPoint = c.fromPoint();
            mxPoint toPoint = c.toPoint();
            ArrayList<mxPoint> points = EdgeHandler.FindEdgePoints ((mxCell) source, (mxCell) target, fromPoint, toPoint, newArr);
            // Если найти путь не удалось - откатить все предыдущие измененные связи
            if (points == null) {
                Iterator it = changedEdges.iterator();
                for (int j = 0; j < oldPoints.size(); ++j) {
                    geometry = ((Connector) it.next()).getGeometry();
                    if (geometry != null) {
                        geometry.setPoints(oldPoints.get(j));
                    }
                }
                return false;
            }
            // иначе применить новый путь к текущей связи
            else {
                if (geometry != null) {
                    geometry.setPoints(points);
                }
                staticObjects.add(c);
            }
        }
        
        return true;
    }
    
    /**
     * Удалить выделенные объекты
     */
    public void deleteSelectedObjects() {
        Object[] obA = getSelectionCells ();
        for (int i = 0; i < obA.length; i++) {
            setSelectionCell (obA[i]);
            selectChildCell ();
            removeCells ();
        }
    }
    /**
     * Проверка - есть установлена ли связь между вершинами source и target.
     */
    public boolean hasEdgesBetween(MyCell source, MyCell target)
    {
        Object [] connectors = getEdgesBetween(source,target);
        for(int i = 0; i< connectors.length;i++)
        {
            MyCell c = (MyCell) connectors[i];
            if(c instanceof SequenceFlow && c.getSource() == source && c.getTarget()== target)
                return true;
            if(c instanceof Association && (c.getSource() == source && c.getTarget()== target || c.getSource() == target && c.getTarget()== source))
                return true;
        }
        return false;
    }
    
        /**
     * Проверка - есть установлена ли связь между вершинами source и target.
     */
    public MyCell getReverseEdgeBetween(MyCell source, MyCell target)
    {
        Object [] connectors = getEdgesBetween(source,target);
        for(int i = 0; i< connectors.length;i++)
        {
            MyCell c = (MyCell) connectors[i];
            if(c instanceof SequenceFlow && c.getSource() == target && c.getTarget()== source)
                return c;
        }
        return null;
    }
}
