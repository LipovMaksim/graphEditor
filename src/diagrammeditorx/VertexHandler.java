package diagrammeditorx;

import Elements.BasicElement;
import Elements.Connector;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

/**
 * Менеджер точек привязок к объектам
 */
public class VertexHandler {

    /**
     * Получить координату X верхнего левого угла исходя из ширины и координаты
     * центра
     *
     * @param pos
     * @param width
     * @return
     */
    public static int GetAdjustX(int pos, int width) {
        int x = pos - width / 2;
        return x;
    }

    /**
     * Получить координату Y верхнего левого угла исходя из высоты и координаты
     * центра
     *
     * @param pos
     * @param width
     * @return
     */
    public static int GetAdjustY(int pos, int height) {
        int y = pos - height / 2;
        return y;
    }
	
    /**
     * Найти свободную точку привязки на периметре объекта from при проведении
     * стрелки к объекту to
     *
     * @param from от которого ведется стрелка
     * @param to к которому ведется стрелка
     * @return
     */
    public static ArrayList<mxPoint> FindPlacesOnPerimetrForEdge(mxCell from, mxCell to) {
        //получить центры объектов
        mxGeometry geometryFrom = from.getGeometry ();
        mxPoint centerFrom = new mxPoint (geometryFrom.getCenterX (), geometryFrom.getCenterY ());

        mxGeometry geometryTo = to.getGeometry ();
        mxPoint centerTo = new mxPoint (geometryTo.getCenterX (), geometryTo.getCenterY ());

        //определить угол стрелки, если бы она рисовалась по прямой
        int direction = FindEdgeDirection (centerFrom, centerTo);

        //найти точки привязок других стрелок к объекту from
        ArrayList<mxPoint> connectedPoints = GetEdgeConnectedPoints (from);
        ArrayList<mxPoint> result = null;

        result = GetFreePointsOnRectanglePerimeter (geometryFrom.getRectangle (), connectedPoints, direction);
        return result;
    }

    /**
     * Определить угол наклона стрелки между объектами from и to в градусах
     *
     * @param from от которого ведется стрелка
     * @param to к которому ведется стрелка
     * @return
     */
    private static int FindEdgeDirection(mxPoint from, mxPoint to) {
        //проверка граничных точек арктангенса
        if (Math.abs (from.getX () - to.getX ()) == 0) {
            return from.getY () > to.getY () ? 90 : -90;
        }
        //получения угла вычислением арктангенса по координатам
        int dergees = (int) Math.toDegrees (Math.atan ((to.getY () - from.getY ()) / (from.getX () - to.getX ())));
        if (from.getX () > to.getX ()) {
            dergees += dergees < 0 ? 180 : -180;
        }
        return dergees;
    }

    /**
     * Получить точки присоединения уже созданных стрелок к объекту cell
     *
     * @param cell
     * @return
     */
    private static ArrayList<mxPoint> GetEdgeConnectedPoints(mxCell cell) {
        Object[] edges = MyFrame.getFrame().getGraphComponent().getGraph ().getEdges (cell);
        ArrayList<mxPoint> edgePoints = new ArrayList<> ();
        for (Object currentCell : edges) {
            Connector edge = (Connector) currentCell;
            if (edge.getSource ().equals (cell)) {
                edgePoints.add (edge.fromPoint());
            }
            else {
                edgePoints.add (edge.toPoint());
            }
        }
        return edgePoints;
    }
	
    /**
     * Получить точку привязки на периметр, если объект - прямоугольник
     *
     * @param borders - границы объекта
     * @param alreadyConnectedEdges - точки присоединения существующих стрелок
     * @param direction - направление рисования стрелки, если бы она проходила
     * по прямой
     * @return
     */
    private static ArrayList<mxPoint> GetFreePointsOnRectanglePerimeter(Rectangle borders, ArrayList<mxPoint> alreadyConnectedEdges, int direction) {
        // Вычислить начальную точку на периметре в зависимости от угла прямой между центрами соединяемых объектов
        int p = 0;
        
        //вправо
        if (direction >= -45 && direction <= 45) {
            p = borders.width + borders.height / 2;
        }
        //вниз
        else if (direction >= -135 && direction <= -45) {
            p = (int) (borders.width * 1.5 + borders.height);
        }
        //вверх
        else if (direction >= 45 && direction <= 135) {
            p = borders.width / 2;
        }
        //влево
        else {
            p = (int) (borders.width * 2 + borders.height * 1.5);
        }

        // Получить массив занятых точек на периметре объекта
        int perimeter = borders.width * 2 + borders.height * 2;
        ArrayList <Integer> realBusyPoints = new ArrayList <Integer>();
        
        for (mxPoint curP : alreadyConnectedEdges) {
            int tmp = 0;
            if (curP.getX() == 1)
                tmp += borders.getWidth() + curP.getY() * borders.getHeight();
            else if (curP.getX() == 0)
                tmp += 2 * borders.getWidth() + (1 - curP.getY()) * borders.getHeight();
            else if (curP.getY() == 1) 
                tmp += borders.getHeight() + (2 - curP.getX()) * borders.getWidth();
            else
                tmp += curP.getX() * borders.getWidth();
            realBusyPoints.add(tmp);
        }
        
        // Вычислить все пары доступных точек на периметре
        ArrayList<mxPoint> result = new ArrayList<mxPoint>();
        int toLeft = 0; int toRight = 5;
        
        boolean l = false, r = false;
        int newL, newR;
        do {
            l = false;
            r = false;
            newL = (p - toLeft + perimeter) % perimeter;
            newR = (p + toRight) % perimeter;
            for (int j : realBusyPoints) {
                if (Math.abs(j - newL) <= 4 || (Math.min(j, newL) + Math.max(j, newL) + 4) % perimeter < 4) {
                    l = true;
                    toLeft = p - Math.min(newL, j);
                }
            }
            if (!l)
                result.add(PerimeterPointToObjectCoordinates(newL, borders));
            for (int j : realBusyPoints) {
                if (Math.abs(j - newR) <= 4 || (Math.min(j, newR) + Math.max(j, newR) + 4) % perimeter < 4) {
                    r = true;
                    toRight = Math.max(newR, j) - p;
                }
            }
            if (!r)
                result.add(PerimeterPointToObjectCoordinates(newR, borders));
            toLeft += 5;
            toRight += 5;
        } while (toLeft + toRight <= perimeter);
        
        return result;
    }
    
    /**
     * Получить точки привязки уже созданных линий на текущей линии
     *
     * @param line - линия, на которой требуется определить созданные точки
     * @param allPoints - все точки привязки всех линий
     * @return
     */
    private static ArrayList<mxPoint> GetPointsOnSide(Pair<mxPoint, mxPoint> line, ArrayList<mxPoint> allPoints) {
        ArrayList<mxPoint> result = new ArrayList<> ();
        //вычислить координаты линии для создания ее уравнения
        int x1 = (int) line.getKey ().getX (),
                y1 = (int) line.getKey ().getY (),
                x2 = (int) line.getValue ().getX (),
                y2 = (int) line.getValue ().getY ();
        //проверка по уравнению линии каждой созданной точик привязки
        for (mxPoint point : allPoints) {
            int x = (int) point.getX (),
                    y = (int) point.getY ();
            if ((y1 - y2) * x + (x2 - x1) * y == x2 * y1 - x1 * y2) {
                result.add (point);
            }
        }
        return result;
    }

    /**
     * Перевести в реальные координаты из точки на периметре
     *
     * @param p точка на перметре объекта
     * @param borders границы объекта
     * @return
     */
    public static mxPoint PerimeterPointToRelativeCoordinates(int p, Rectangle borders) {
            
        int x = 0, y = 0;
        if (p < borders.getWidth()) {
            x += p + borders.getX();
            y += borders.getY();
        }
        else if (p < borders.getWidth() + borders.getHeight()) {
            x += borders.getX() + borders.getWidth();
            y += p - borders.getWidth() + borders.getY();
        }
        else if (p < borders.getWidth() * 2 + borders.getHeight()) {
            x += borders.getX() + borders.getWidth() * 2 + borders.getHeight() - p;
            y += borders.getY() + borders.getHeight();
        }
        else {
            x += borders.getX();
            y += borders.getY() + borders.getWidth() * 2 + borders.getHeight() * 2 - p;
        }
        return new mxPoint(x, y);
    }
    
    public static mxPoint PerimeterPointToObjectCoordinates(int p, Rectangle borders) {
        return FromRelativeCoordinates (PerimeterPointToRelativeCoordinates(p, borders), borders);
    }

    /**
     * Перевести в реальные координаты из относительных
     *
     * @param old реальные коориданты
     * @param borders границы объекта
     * @return
     */
    public static mxPoint ToRelativeCoordinates(mxPoint old, Rectangle borders) {
        return new mxPoint (
                (old.getX () * borders.width + borders.getX ()),
                (old.getY () * borders.height + borders.getY ()));
    }

    /**
     * Перевести в относительные координаты из реальных
     *
     * @param old относительные коориданты
     * @param borders границы объекта
     * @return
     */
    public static mxPoint FromRelativeCoordinates(mxPoint old, Rectangle borders) {
        return new mxPoint (
                (Math.abs (old.getX () - borders.getX ()) / borders.width),
                (Math.abs (old.getY () - borders.getY ()) / borders.height));
    }
}
