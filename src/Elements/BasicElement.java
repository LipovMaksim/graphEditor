package Elements;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import diagrammeditorx.MyFrame;
import diagrammeditorx.MyGraph;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.JOptionPane;

/**
 * Базовый элемент диаграммы
 */
public class BasicElement extends MyCell{
    // Минимальные и максимальные размеры элемента
    private int maxH = 500, maxW = 500, minH = 50, minW = 50;
    public BasicElement()
    {
        super();
        setVertex(true);
    }
    /**
     * Функция проверки минимальных и максимальных размеров элемента
     */
    public void validateCellSize()
    {
        // Получить текущие размеры элемента
        mxGeometry geo = getGeometry();
        double H = geo.getHeight();
        double W = geo.getWidth();
        // Если высота больше максимально допустимой - установить её равной максимальной
        H = H > maxH ? maxH : H;
        // Если ширина больше максимально допустимой - установить её равной максимальной
        W = W > maxW ? maxW : W;
        // Если высота больше минимально допустимой - установить её равной минимальной
        H = H < minH ? minH : H;
        // Если ширина больше минимально допустимой - установить её равной минимальной
        W = W < minW ? minW : W;
        // Установить новые размеры элементов
        geo.setHeight(H);
        geo.setWidth(W);
        setGeometry(geo);
    }
    
    /**
     * Функция проверяет, был ли элемент масштабирован
     * @param r1 - новые размеры элемента
     * @param r2 - старые размеры элемента
     * @return - флаг наличия масштабировани
     */
    boolean isResized(Rectangle r1, Rectangle r2){
        // Если ширина или высота различается - элемент был масштабирован
        return r1.getHeight()!=r2.getHeight() || r1.getWidth()!=r2.getWidth();
    }
    
    /**
     * Функция установки новой геометрии элемента
     * @param mxgmtr - новая геометрия элемента
     */
    @Override
    public void setGeometry(mxGeometry mxgmtr) {
        if (mxgmtr!=null && getGeometry()!=null){
            mxGeometry g = getGeometry();
            g = new mxGeometry(g.getX(), g.getY(), g.getWidth(), g.getHeight());
            // Если имело место масштабирование
            boolean resized = isResized(mxgmtr.getRectangle(), this.getGeometry().getRectangle());
            super.setGeometry(mxgmtr); 
            if (mxgmtr!=null && getGeometry()!=null && resized){
                // Проверить наложение объекта на другие
                ArrayList<ArrayList<mxCell>> intersections = MyFrame.getFrame().getGraphComponent().getVerticeIntersections();
                ArrayList<ArrayList<mxCell>> intersectionsBetwenVerticesAndEdges = MyFrame.getFrame().getGraphComponent().getIntersectionsBetwenVerticesAndEdges();
                if (checkVerticeIntersections(intersections, new Object[] {this}) || checkVerticesAndEdgesIntersections (intersectionsBetwenVerticesAndEdges, new Object[] {this})){
                    super.setGeometry(g);
                    //JOptionPane.showMessageDialog(MyFrame.getFrame(),"Наложение объектов недопустимо.","Ошибка",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            if (!resized) {
                return;
            }
            HashSet<Connector> changedEdges = new HashSet<Connector>();
            for (int j = 0; j < getEdgeCount(); ++j) {
                changedEdges.add((Connector) getEdgeAt(j));
            }
        
            if (changedEdges.size() > 0) {
                if (!((MyGraph)MyFrame.getFrame().getGraphComponent().getGraph()).repaintConnections(changedEdges)) {
                    super.setGeometry(g);
                    JOptionPane.showMessageDialog(MyFrame.getFrame(),"Невозможно построить все связи из новой позиции.","Ошибка",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else
            super.setGeometry(mxgmtr); 
    }
    
    /**
     * Функция проверки наличия пересечения объектов с другими объектами
     * @param intersections - пересечения на графе
     * @param objects - элементы, для которых проверяется наличие наложения
     * @return признак наличия наложения элементов
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
     * Функция проверки наличия пересечения объектов с дугами
     * @param intersections - пересечения на графе дуг с объектами
     * @param objects - элементы, для которых проверяется наличие наложения
     * @return признак наличия наложения элементов
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
}
