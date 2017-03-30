package diagrammeditorx;

import Elements.BasicElement;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 * Ячейка таблицы для алгоритма А*
 */
class Cell {

    //координаты клетки в таблице
    public int x = -1;
    public int y = -1;
    //клетка, из которую мы пришли в данную
    public Cell parent = this;
    //заблокирована ли клетка
    public boolean blocked = false;
    //является ли клетка клеткой начала поиска пути
    public boolean start = false;
    //является ли клетка клеткой конца поиска пути
    public boolean finish = false;
    //является ли клетка участком пути
    public boolean road = false;
    //стоимости движени через клетки
    public int F = 0;
    public int G = 0;
    public int H = 0;

    /**
     * Создает клетку с координатами x, y.
     *
     * @param blocked является ли клетка непроходимой
     */
    public Cell(int x, int y, boolean blocked) {
        this.x = x;
        this.y = y;
        this.blocked = blocked;
    }

    /**
     * Функция вычисления манхеттенского расстояния от текущей клетки до finish
     *
     * @param finish конечная клетка
     * @return расстояние
     */
    public int manhattenDistance(Cell finish) {
        return 10 * (Math.abs (this.x - finish.x) + Math.abs (this.y - finish.y));
    }

    /**
     * Вычисление стоимости пути до соседней клетки finish
     *
     * @param finish соседняя клетка
     * @return 10, если клетка по горизонтали или вертикали от текущей, 20, если
     * по диагонали
     */
    public int price(Cell finish) {
        return this.G;
    }

    /**
     * Устанавливает текущую клетку как стартовую
     */
    public void setAsStart() {
        this.start = true;
    }

    /**
     * Устанавливает текущую клетку как конечную
     */
    public void setAsFinish() {
        this.finish = true;
    }

    /**
     * Сравнение клеток
     *
     * @param second сравниваемая клетка
     * @return true, если координаты клеток равны, иначе - false
     */
    public boolean equals(Cell second) {
        return (this.x == second.x) && (this.y == second.y);
    }

    /**
     * Красиво печатаем * - путь (это в конце) + - стартовая или конечная # -
     * непроходимая . - обычная
     *
     * @return строковое представление клетки
     */
    public String toString() {
        if (this.road) {
            return " * ";
        }
        if (this.start || this.finish) {
            return " + ";
        }
        if (this.blocked) {
            return " # ";
        }
        return " . ";
    }
}

/*
 * Класс табилцы для алгоритма А*
 */
class Table<T extends Cell> {

    //ширина поля
    public int width;
    //высота поля
    public int height;
    //клетки на поле
    private Cell[][] table;

    /**
     * Создаем карту игры
     *
     * @param width ширина
     * @param height высота
     */
    public Table(int width, int height) {
        this.width = width;
        this.height = height;
        this.table = new Cell[width][height];
    }

    /**
     * Добавить клетку на карту
     *
     * @param cell добавляемая клетка
     */
    public void add(Cell cell) {
        table[cell.x][cell.y] = cell;
    }

    /**
     * Получить клетку по координатам x, y
     *
     * @return реальная клетка, либо блокированную новую клетку в (0,0)
     */
    public T get(int x, int y) {
        if (x < width && x >= 0 && y < height && y >= 0) {
            return (T) table[x][y];
        }
        return null;
    }

    /**
     * Печать всех клеток поля. Красиво.
     */
    public void printp() {
        for (int i = 0; i < width; i++) {
            System.out.print (i);
            System.out.print (": ");
            for (int j = 0; j < height; j++) {
                System.out.print (this.get (i, j));
            }
            System.out.println ();
            System.out.println ();
        }
        System.out.println ();
        System.out.println ();
    }
}

/*
 * Менеджер стрелок
 */
public class EdgeHandler {

    //размер ячейки таблицы
    static int tableCellSize = 5;
    //минимальное расстояние между объектами
    static int objectOffset = 0;
    // Стоимость последнего пути
    static int lastPathCost = 0;
    // Максимально допустимая стоимость пути
    static int maxPathCost = 100000;
    //
    private static int tableLeftCellX = 0;
    private static int tableLeftCellY = 0;
    //прошлая клетка
    private static Cell _previousCell = null;
    //направление движения
    private static int _currentDirection = -1;

    /**
     * Вычислить прохождение стрелки
     *
     * @param from откуда ведется стрелка
     * @param to куда ведется стрелка
     * @param fromPoint точка привязки объекта from
     * @param toPoint точка привязки объекта from
     * @param objects все объекты на поле
     * @param cost возвращаемое значение - стоимость построенного пути
     * @return
     */
    public static ArrayList<mxPoint> FindEdgePoints(mxCell from, mxCell to, mxPoint fromPoint, mxPoint toPoint, Object[] objects) {
        // Заполним карту клетками, учитывая объекты и линии, уже нарисованы
        Table<Cell> cellList = CreateCoordinateTableWithObjects (objects);

        // Вычислить стартовую и конесную ячейчку таблицы
        mxPoint realStartPoint = VertexHandler.ToRelativeCoordinates (fromPoint, from.getGeometry ().getRectangle ());
        Cell start = FindTableCellForPoint (realStartPoint, cellList);
        mxPoint realFinishPoint = VertexHandler.ToRelativeCoordinates (toPoint, to.getGeometry ().getRectangle ());
        Cell finish = FindTableCellForPoint (realFinishPoint, cellList);
        if (finish != null && start != null) {
            start.setAsStart ();
            finish.setAsFinish ();
            return FindEdgePoints (start, finish, cellList);
        }
        return null;
    }

    /**
     * Найти соседнюю свободную ячейку в таблице для реальной позиции
     *
     * @param point реальная позиция
     * @param cellList таблица ячеек
     * @return
     */
    private static Cell FindTableCellForPoint(mxPoint point, Table<Cell> cellList) {
        LinkedList<Cell> cells = FindNeighbours (PointToCell (point), cellList);
        for (Cell cell : cells) {
            if (cell != null && !cell.blocked) {
                return cell;
            }
        }
        return null;
    }

    /**
     * Перевести реальные координаты в координаты ячейки в таблице
     *
     * @param point реальные координаты
     * @return
     */
    private static Cell PointToCell(mxPoint point) {
        int y = (int) (point.getX () / tableCellSize - tableLeftCellX + 1);
        int x = (int) (point.getY () / tableCellSize - tableLeftCellY + 1);
        return new Cell (x, y, false);
    }

    /**
     * Создание таблицы и заполенение ее исходя из объектов, которые стоят на
     * поле
     *
     * @param objects
     * @return
     */
    private static Table<Cell> CreateCoordinateTableWithObjects(Object[] objects) {
        ArrayList<Cell> blockedCells = new ArrayList<Cell> ();
        Cell cell;
        mxGeometry g;

        int tableRightCellX = 0,
                tableRightCellY = 0;

        int currentLeftCellX = 0,
                currentLeftCellY = 0,
                currentRightCellX = 0,
                currentRightCellY = 0;

        if (objects.length > 0) {
            g = ((BasicElement) objects[0]).getGeometry ();
            tableLeftCellX = (int) ((g.getX () - objectOffset) / tableCellSize);
            tableLeftCellY = (int) ((g.getY () - objectOffset) / tableCellSize);
            tableRightCellX = (int) ((g.getX () + g.getWidth () + objectOffset) / tableCellSize);
            tableRightCellY = (int) ((g.getY () + g.getHeight () + objectOffset) / tableCellSize);
        }

        //занести в таблицу закрытие ячейки - на которых стоят объекты
        for (int i = 0; i < objects.length; ++i) {
            g = ((mxCell) objects[i]).getGeometry ();
            //если текущий объект - элемент на поле
            if (objects[i] instanceof BasicElement) {

                currentLeftCellX = (int) ((g.getX () - objectOffset) / tableCellSize);
                currentLeftCellY = (int) ((g.getY () - objectOffset) / tableCellSize);
                currentRightCellX = (int) (((g.getX () + g.getWidth () + objectOffset)) / tableCellSize);
                currentRightCellY = (int) (((g.getY () + g.getHeight () + objectOffset)) / tableCellSize);

                //вычислить границы, в которых нужно заблокировать элементы
                tableLeftCellX = Math.min (tableLeftCellX, currentLeftCellX);
                tableLeftCellY = Math.min (tableLeftCellY, currentLeftCellY);
                tableRightCellX = Math.max (tableRightCellX, currentRightCellX);
                tableRightCellY = Math.max (tableRightCellY, currentRightCellY);

                //заблокировать все элементы в этиъ границах
                for (int j = currentLeftCellX; j <= currentRightCellX; ++j) {
                    for (int k = currentLeftCellY; k <= currentRightCellY; ++k) {
                        cell = new Cell (j, k, true);
                        if (!blockedCells.contains (cell)) {
                            blockedCells.add (cell);
                        }
                    }
                }
            }
            else {
                List<mxPoint> p = g == null ? null : g.getPoints ();
                if (p != null) {
                    for (int l = 1; l < p.size (); ++l) {
                        currentLeftCellX = (int) (Math.min (p.get (l - 1).getX (), p.get (l).getX ()) / tableCellSize);
                        currentLeftCellY = (int) (Math.min (p.get (l - 1).getY (), p.get (l).getY ()) / tableCellSize);
                        currentRightCellX = (int) (Math.max (p.get (l - 1).getX (), p.get (l).getX ()) / tableCellSize);
                        currentRightCellY = (int) (Math.max (p.get (l - 1).getY (), p.get (l).getY ()) / tableCellSize);
                        
                        //вычислить границы, в которых нужно заблокировать элементы
                        tableLeftCellX = Math.min (tableLeftCellX, currentLeftCellX);
                        tableLeftCellY = Math.min (tableLeftCellY, currentLeftCellY);
                        tableRightCellX = Math.max (tableRightCellX, currentRightCellX);
                        tableRightCellY = Math.max (tableRightCellY, currentRightCellY);
                        
                        //заблокировать все элементы в этиъ границах
                        //TODO: разблокировать и присвоить большую цену прохода
                        for (int j = currentLeftCellX; j <= currentRightCellX; ++j) {
                            for (int k = currentLeftCellY; k <= currentRightCellY; ++k) {
                                cell = new Cell (j, k, false);
                                cell.G = 5000;
                                if (!blockedCells.contains (cell)) {
                                    blockedCells.add (cell);
                                }
                            }
                        }
                    }
                }
            }
        }

        //определить размеры таблицы
        int tableWidth = tableRightCellX - tableLeftCellX + 3,
                tableHeight = tableRightCellY - tableLeftCellY + 3;

        //заполнить таблицу полученными значениями
        Table<Cell> result = new Table<Cell> (tableHeight, tableWidth);
        for (Cell c : blockedCells) {
            int tmp = c.x - tableLeftCellX + 1;
            c.x = c.y - tableLeftCellY + 1;
            c.y = tmp;
            result.add (c);
        }
        for (int i = 0; i < tableHeight; i++) {
            for (int j = 0; j < tableWidth; j++) {
                if (result.get (i, j) == null) {
                    result.add (new Cell (i, j, false));
                }
            }
        }

        return result;
    }

    /**
     * Найти путь по ячейкам таблицы от start до finish
     * @param start начальная ячейка
     * @param finish конечная ячейка
     * @param cellList поле, где требуется найти путь
     * @return 
     */
    private static ArrayList<mxPoint> FindEdgePoints(Cell start, Cell finish, Table<Cell> cellList) {
        LinkedList<Cell> openList = new LinkedList<> ();
        LinkedList<Cell> closedList = new LinkedList<> ();

        boolean found = false;
        boolean noroute = false;

        //Добавляем стартовую клетку в открытый список
        openList.push (start);

        //Пока не найден путь или не рассмотрены все возмодные варианты прохода по клеткам
        while (!found && !noroute) {
            //Ищем в открытом списке клетку с наименьшей стоимостью F
            Cell min = GetFirstCellWithMinPrice (openList);
            //Помещаем ее в закрытый список и удаляем с открытого
            closedList.push (min);
            openList.remove (min);

            //Получаем соседние клетки
            LinkedList<Cell> tmpList = FindNeighbours (min, cellList);

            //Для каждой из соседних клеток
            int curInd = -1;
            for (Cell neighbour : tmpList) {
                curInd++;
                //Если клетка непроходимая или она находится в закрытом списке, игнорируем ее
                if (neighbour == null || neighbour.blocked || closedList.contains (neighbour)) {
                    continue;
                }

                //Если клетка еще не в открытом списке
                if (!openList.contains (neighbour)) {
                    //Добавляем ее туда. 
                    openList.add (neighbour);
                    //Делаем текущую клетку родительской для это клетки
                    neighbour.parent = min;
                    //Расчитываем стоимости F, G и H клетки
                    neighbour.H = neighbour.manhattenDistance (finish);
                    //neighbour.G = start.price (min);
                    if (_previousCell != null && _currentDirection != -1 && IsChangingDirection (_previousCell, neighbour, _currentDirection) ||
                            _previousCell == null && tmpList.get((curInd + 2) % 4) != null && !tmpList.get((curInd + 2) % 4).blocked) {
                        neighbour.G += 50;
                    }
                    neighbour.F = neighbour.H + neighbour.G;
                    continue;
                }

                // Если клетка уже в открытом списке, то проверяем по стоимости G, не дешевле ли будет путь через эту клетку
                if (neighbour.G + neighbour.price (min) < min.G) {
                    // Меняем родителя клетки на текущую клетку
                    neighbour.parent = min;
                    //Пересчитываем для нее стоимости H и F.
                    neighbour.H = neighbour.manhattenDistance (finish);
                    neighbour.G = start.price (min);
                    neighbour.F = neighbour.H + neighbour.G;
                    _previousCell = min;
                    _currentDirection = GetNewDirection (_previousCell, neighbour);
                }
            }

            //Целевую клетку в открытом список = путь найден, рассматривать клетки дальше не надо
            if (openList.contains (finish)) {
                found = true;
            }

            //Если нет больше открытых клеток - мы не нашли путь
            if (openList.isEmpty ()) {
                noroute = true;
            }
        }

        //Если нашли путь - выводим его в виде массива mxPoint для стрелки, иначе возвращаем null
        if (found) {
            return GetEdgePoint (finish, start, cellList);
        }
        else {
            return null;
        }
    }

    /**
     * Получить направление движения при движении от previous в current ячейку
     * @param previous откуда движемся
     * @param current куда движемся
     * @return угол = направлению движения
     */
    private static int GetNewDirection(Cell previous, Cell current) {
        if (previous.x == current.x) {
            if (previous.y != current.y) {
                if (previous.y > current.y) {
                    return -90;
                }
                else {
                    return 90;
                }
            }
        }
        else if (previous.y == current.y) {
            if (previous.x > current.x) {
                return 180;
            }
            else {
                return 0;
            }
        }
        return -1;
    }

    /**
     * Проверить, меняется ли направление при движении от previous в current ячейку
     * @param previous откуда движемся
     * @param current куда движемся
     * @param currentDirection текущее направление движения
     * @return 
     */
    private static boolean IsChangingDirection(Cell previous, Cell current, int currentDirection) {
        return GetNewDirection (previous, current) != currentDirection;
    }

    /**
     * Получить ячейку из списка открытых с минимальной ценой F
     * @param openList открытые ячейки
     * @return 
     */
    private static Cell GetFirstCellWithMinPrice(LinkedList<Cell> openList) {
        Cell min = openList.getFirst ();
        for (Cell cell : openList) {
            if (cell.F < min.F) {
                min = cell;
            }
        }
        return min;
    }

    /**
     * Получить соседей ячейки
     * @param cell ячейка, для которой ищутся соседи
     * @param cellList таблица ячеек
     * @return 
     */
    private static LinkedList<Cell> FindNeighbours(Cell cell, Table<Cell> cellList) {
        LinkedList<Cell> neighbours = new LinkedList<> ();
        neighbours.add (cellList.get (cell.x, cell.y - 1));
        neighbours.add (cellList.get (cell.x + 1, cell.y));
        neighbours.add (cellList.get (cell.x, cell.y + 1));
        neighbours.add (cellList.get (cell.x - 1, cell.y));
        return neighbours;
    }

    /**
     * Перевод найденного пути в координатах таблицы в реальные координаты
     * @param finishCell
     * @param start
     * @param _cellList
     * @return 
     */
    private static ArrayList<mxPoint> GetEdgePoint(Cell finishCell, Cell start, Table<Cell> _cellList) {
        ArrayList<mxPoint> points = new ArrayList<mxPoint> ();
        //добавление всего точек и результирующий массив и приведение их к реальным координатам
        points.add (new mxPoint ((finishCell.y - 1 + tableLeftCellX) * tableCellSize, (finishCell.x - 1 + tableLeftCellY) * tableCellSize));
        lastPathCost = finishCell.G;
        Cell rd = finishCell.parent;
        points.add (0, new mxPoint ((rd.y - 1 + tableLeftCellX) * tableCellSize, (rd.x - 1 + tableLeftCellY) * tableCellSize));
        while (!rd.equals (start)) {
            rd.road = true;
            lastPathCost += rd.G >= 5000 ? rd.G : 0;
            rd = rd.parent;
            points.add (0, new mxPoint ((rd.y - 1 + tableLeftCellX) * tableCellSize, (rd.x - 1 + tableLeftCellY) * tableCellSize));
            if (rd == null) {
                break;
            }
        }

        
        for (int i = 1; i < points.size () - 1; ++i) {
            //если текущая точка лежит на линии между двумя другими точками - удаляем ее из списка
            if (points.get (i).getX () == points.get (i - 1).getX () && points.get (i).getX () == points.get (i + 1).getX ()) {
                points.remove (i);
                --i;
                continue;
            }
            if (points.get (i).getY () == points.get (i - 1).getY () && points.get (i).getY () == points.get (i + 1).getY ()) {
                points.remove (i);
                --i;
                continue;
            }
        }

        return points;
    }
}
