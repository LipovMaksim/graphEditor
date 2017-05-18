/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagrammeditorx;

import Elements.Association;
import Elements.BasicElement;
import Elements.Connector;
import Elements.MyCell;
import Elements.SequenceFlow;
import Elements.Vertex;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Максим
 */
public class GraphWorker {
    
    static JTable matrSmej = new JTable();
    static JTable matrInv = new JTable();
    static JTable matrVesov = new JTable();
    static JTable structSmej = new JTable();
    static JTable spisokReber = new JTable();

    GraphWorker () {
        DefaultTableModel tabModel = (DefaultTableModel)matrSmej.getModel();
        tabModel.setColumnCount(2);
        tabModel.setRowCount(2);
        tabModel.setValueAt("Вершина \\ Вершина", 0, 0);
        tabModel.setValueAt("A0", 1, 0);
        tabModel.setValueAt("A0", 0, 1);
        
        tabModel = (DefaultTableModel)matrVesov.getModel();
        tabModel.setColumnCount(2);
        tabModel.setRowCount(2);
        tabModel.setValueAt("Вершина \\ Вершина", 0, 0);
        tabModel.setValueAt("A0", 1, 0);
        tabModel.setValueAt("A0", 0, 1);
        
        tabModel = (DefaultTableModel)spisokReber.getModel();
        tabModel.setColumnCount(2);
        tabModel.setRowCount(2);
        tabModel.setValueAt("Источник:", 0, 0);
        tabModel.setValueAt("Сток:", 1, 0);
        
        tabModel = (DefaultTableModel)structSmej.getModel();
        tabModel.setColumnCount(2);
        tabModel.setRowCount(2);
        tabModel.setValueAt("Вершина:", 0, 0);
        tabModel.setValueAt("Смежные вершины:", 0, 1);
    }
    
    
    static public void fillSmejMatr (MyGraph graph) {
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        DefaultTableModel tabModel = (DefaultTableModel)matrSmej.getModel(); 
        tabModel.setColumnCount(vertices.length+1);
        tabModel.setRowCount(vertices.length+1);
        for (int i = 0; i < vertices.length; i++){
            tabModel.setValueAt(((BasicElement)vertices[i]).getFullText(), 0, i+1);
            tabModel.setValueAt(((BasicElement)vertices[i]).getFullText(), i+1, 0);
            for (int j = i; j < vertices.length; j++){
                Object[] edgesBetween = graph.getEdgesBetween(vertices[i], vertices[j]);
                float val = 0;
                if (edgesBetween.length > 0){
                    String ageText = ((Connector) edgesBetween[0]).getFullText();
                    if (ageText != null && ageText.matches("[0-9]+\\.?[0-9]*")){
                        float data = Float.parseFloat(ageText);
                        val = data;
                    }
                    tabModel.setValueAt("1", i+1, j+1);
                    if ( edgesBetween[0] instanceof Association){
                        tabModel.setValueAt("1", j+1, i+1);
                    }
                    else
                        tabModel.setValueAt("0", j+1, i+1);
                }
                else {
                    tabModel.setValueAt("0", i+1, j+1);
                    tabModel.setValueAt("0", j+1, i+1);
                }
            }
        }
        int a = 0;
    }
    
    static public void fillVesovMatr (MyGraph graph) {
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        DefaultTableModel tabModel = (DefaultTableModel)matrVesov.getModel(); 
        tabModel.setColumnCount(vertices.length+1);
        tabModel.setRowCount(vertices.length+1);
        for (int i = 0; i < vertices.length; i++){
            tabModel.setValueAt(((BasicElement)vertices[i]).getFullText(), 0, i+1);
            tabModel.setValueAt(((BasicElement)vertices[i]).getFullText(), i+1, 0);
            for (int j = i; j < vertices.length; j++){
                Object[] edgesBetween = graph.getEdgesBetween(vertices[i], vertices[j]);
                float val = 0;
                if (edgesBetween.length > 0){
                    String ageText = ((Connector) edgesBetween[0]).getFullText();
                    if (ageText != null && ageText.matches("[0-9]+\\.?[0-9]*")){
                        float data = Float.parseFloat(ageText);
                        val = data;
                    }
                    tabModel.setValueAt(val, i+1, j+1);
                    if ( edgesBetween[0] instanceof Association){
                        tabModel.setValueAt(val, j+1, i+1);
                    }
                }
                else {
                    tabModel.setValueAt("", i+1, j+1);
                }
            }
        }
        int a = 0;
    }
    
    static public void fillIncMatr (MyGraph graph) {
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        Object[] edges = graph.getChildEdges(graph.getDefaultParent());
        DefaultTableModel tabModel = (DefaultTableModel)matrInv.getModel(); 
        tabModel.setColumnCount(edges.length+1);
        tabModel.setRowCount(vertices.length+1);
        for (int i = 0; i < vertices.length; i++){
            tabModel.setValueAt(((BasicElement)vertices[i]).getFullText(), i+1, 0);
            for (int j = 0; j < edges.length; j++){
                tabModel.setValueAt("e" + j, 0, j + 1); 
                if (((Connector)edges [j]).getSource() == vertices[i])
                    tabModel.setValueAt((((Connector)edges [j]) instanceof Association ? "1" : "-1"), i + 1, j + 1);
                else if (((Connector)edges [j]).getTarget() == vertices[i])
                    tabModel.setValueAt((((Connector)edges [j]) instanceof Association ? "1" : "+1"), i + 1, j + 1);
                else 
                   tabModel.setValueAt("0" , i + 1, j + 1); 
            }
        }
    }
    
    static public void fillStructSmej (MyGraph graph) {
        Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
        Object[] edges = graph.getChildEdges(graph.getDefaultParent());
        DefaultTableModel tabModel = (DefaultTableModel)structSmej.getModel(); 
        tabModel.setRowCount(vertices.length+1);
        for (int i = 0; i < vertices.length; i++){
            tabModel.setValueAt(((BasicElement)vertices[i]).getFullText(), i+1, 0);
            String res = "";
            for (int j = 0; j < edges.length; j++){
                
                if (((Connector)edges [j]).getSource() == vertices[i])
                    res += (res == "" ? "" : ", ") + ((Vertex)((Connector)edges [j]).getTarget()).getFullText();
                else if (((Connector)edges [j]).getTarget() == vertices[i] && ((Connector)edges [j]) instanceof Association)
                    res += (res == "" ? "" : ", ") + ((Vertex)((Connector)edges [j]).getSource()).getFullText();
            }
            tabModel.setValueAt(res, i+1, 1);
        }
    }
    
    static public void fillSpisokReber (MyGraph graph) {
        Object[] edges = graph.getChildEdges(graph.getDefaultParent());
        DefaultTableModel tabModel = (DefaultTableModel)spisokReber.getModel();
        ArrayList <Association> a = new ArrayList<Association> ();
        ArrayList <SequenceFlow> s = new ArrayList<SequenceFlow> ();
        for (int i = 0; i < edges.length; i++) {
            if (edges [i] instanceof Association) {
                a.add((Association)edges[i]);
            }
            else if (edges [i] instanceof SequenceFlow) {
                s.add((SequenceFlow) edges[i]);
            }
        }
        int n = s.size() + a.size() * 2;
        tabModel.setColumnCount(n + 1);
        n = s.size();
        for (int i = 0; i < n; i++){
            tabModel.setValueAt(((BasicElement) s.get(i).getSource()).getFullText(), 0, i + 1);
            tabModel.setValueAt(((BasicElement) s.get(i).getTarget()).getFullText(), 1, i + 1);
        }
        int m = a.size();
        for (int i = n; i < n + m; i++) {
            tabModel.setValueAt(((BasicElement) a.get(i - n).getSource()).getFullText(), 0, i + 1);
            tabModel.setValueAt(((BasicElement) a.get(i - n).getTarget()).getFullText(), 1, i + 1);
        }
        
        for (int i = n + m; i < n + 2 * m; i++) {
            tabModel.setValueAt(((BasicElement) a.get(i - n - m).getSource()).getFullText(), 1, i + 1);
            tabModel.setValueAt(((BasicElement) a.get(i - n - m).getTarget()).getFullText(), 0, i + 1);
        }
    }

    static public MyGraph createGraphFromeSmejMatr () {
        GraphInfo gi = new GraphInfo();
        TableModel tabModel = matrSmej.getModel();
        int n = tabModel.getRowCount() - 1;
        for (int i = 0; i < n; i++){
            gi.addVertex( (String) tabModel.getValueAt(0, i + 1));
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                String val = (String) tabModel.getValueAt(i + 1, j + 1);
                if (val != null && val.equals((Object)"1")) {
                    String v2 = ((String) tabModel.getValueAt(j + 1, i + 1));
                    if (v2 != null && v2.equals((Object)"1")) {
                        if (!gi.hasReversEdge(i, j)){
                            gi.setEdge(i, j, "0", true);
                        }
                    }
                    else {
                        if (!gi.hasReversEdge(i, j)){
                            gi.setEdge(i, j, "0", false);
                        }
                    }
                }
            }
        }
        //graph.refresh();
        return createGraphFromeGraphInfo (gi);
    }
    
    static public MyGraph createGraphFromeStructSmej () {
        GraphInfo gi = new GraphInfo();
        TableModel tabModel = structSmej.getModel(); 
        int n = tabModel.getRowCount() - 1;
        for (int i = 0; i < n; i++) {
            Object o = tabModel.getValueAt(i + 1, 0);
            String val = null;
            if (o != null) {
                val = o.toString();
            }
            
            gi.addVertex(val);
        }
        
        for (int i = 0; i < n; i++) {
            Object o = tabModel.getValueAt(i + 1, 1);
            String val = null;
            if (o != null) {
                val = o.toString();
            
                val = val.replaceAll("\\s+", "");
                //val.replaceAll("    ", "");
                String [] vertexs = val.split(",");

                for (int j = 0; j < vertexs.length; j++) {
                    int ind = gi.getVertexIndex(vertexs[j]);
                    if (ind >= 0 && ind != i) {
                        if (!gi.hasReversEdge(i, ind)) {
                            gi.setEdge(i, ind, "", false);
                        }
                        else {
                            gi.removeReversEdge(i, ind);
                            gi.setEdge(i, ind, "", true);
                        }
                    }
                }
            }
        }
        
        return createGraphFromeGraphInfo (gi);
        }
    
    static public MyGraph createGraphFromeSpisokReber () {
        GraphInfo gi = new GraphInfo();
        TableModel tabModel = spisokReber.getModel(); 
        int n = tabModel.getColumnCount() - 1;
        for (int i = 0; i < n; i++) {
            Object o1 = tabModel.getValueAt(0, i + 1);
            Object o2 = tabModel.getValueAt(1, i + 1);
            String v1 = null;
            String v2 = null;
            if (o1 != null) {
                v1 = o1.toString();
            }
            if (o2 != null) {
                v2 = o2.toString();
            }
            
            if (v1 != null && v2 != null) {
                int ind1 = gi.getVertexIndex(v1);
                if (ind1 < 0) {
                    gi.addVertex(v1);
                    ind1 = gi.getVertexIndex(v1);
                }
                
                int ind2 = gi.getVertexIndex(v2);
                if (ind2 < 0) {
                    gi.addVertex(v2);
                    ind2 = gi.getVertexIndex(v2);
                }
                
                if (ind1 != ind2) {
                    if (!gi.hasEdge(ind1, ind2)){
                        if (!gi.hasReversEdge(ind1, ind2)){
                            gi.setEdge(ind1, ind2, "", false);
                        }
                        else {
                            gi.removeReversEdge(ind1, ind2);
                            gi.setEdge(ind1, ind2, "", true);
                        }
                    }
                }
            }
        }
        
        return createGraphFromeGraphInfo (gi);
    }
    static public MyGraph createGraphFromeIncMatr () {
        GraphInfo gi = new GraphInfo();
        TableModel tabModel = matrInv.getModel(); 
        int n = tabModel.getRowCount() - 1;
        for (int i = 0; i < n; i++) {
            Object o = tabModel.getValueAt(i + 1, 0);
            String val = null;
            if (o != null && o instanceof String) {
                val = (String) o;
            }
            else if (o != null) {
                val = o.toString();
            }
            
            gi.addVertex(val);
        }
        
        int m = tabModel.getColumnCount() - 1;
        for (int j = 0; j < m; j++) {
            int ind = -1;
            int sorce = -1;
            int target = -1;
            boolean c = true;
            for (int i = 0; i < n && c; i++) {
                Object o = tabModel.getValueAt(i + 1, j + 1);
                String val = null;
                if (o != null)
                    val = o.toString();
                if (val != null && val.equals("1")) {
                    if (ind == -1) {
                        ind = i;
                    }
                    else {
                        gi.setEdge(ind, i, "", true);
                        c = false;
                    }
                }
                else if (val != null && val.equals("-1")) {
                    if (sorce == -1) {
                        sorce = i;
                    }
                    if (target != -1) {
                        gi.setEdge(sorce, target, "", false);
                        c = false;
                    }
                }
                else if (val != null && val.equals("+1")) {
                    if (target == -1) {
                        target = i;
                    }
                    if (sorce != -1) {
                        gi.setEdge(sorce, target, "", false);
                        c = false;
                    }
                }
            }
        }
        return createGraphFromeGraphInfo (gi);
    }
    
    static public MyGraph createGraphFromeVesovMatr () {
        GraphInfo gi = new GraphInfo();
        TableModel tabModel = matrVesov.getModel();
        int n = tabModel.getRowCount() - 1;
        for (int i = 0; i < n; i++){
            gi.addVertex( (String) tabModel.getValueAt(0, i + 1));
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Object o = tabModel.getValueAt(i + 1, j + 1);
                String val = null;
                if (o instanceof String)
                    val = (String) o;
                else if (o != null)
                    val = o.toString();
                if (val != null && !val.equals((Object) "")) {
                    String v2 = ((String) tabModel.getValueAt(j + 1, i + 1));
                    if (v2 == null || v2.equals((Object) "")) {
                        if (!gi.hasReversEdge(i, j)){
                            gi.setEdge(i, j, val, false);
                        }
                    }
                    else {
                        if (!gi.hasReversEdge(i, j)){
                            gi.setEdge(i, j, val, true);
                        }
                    }
                }
            }
        }
        //graph.refresh();
        return createGraphFromeGraphInfo (gi);
    }
    
    static public MyGraph createGraphFromeGraphInfo (GraphInfo gi) {
        MyGraph graph = new MyGraph();
        ArrayList <MyCell> vertexs = new ArrayList<MyCell>();
        int n = gi.vertexs.size();
        double fi = Math.PI * 2 / n;
        double r = 100 + 50 * ((int)(n / 4));
        for (int i = 0; i < n; i++){
            vertexs.add(CellFactory.create(Tool.TASK));
            double x = r + 10 + r * Math.cos(fi * (i + 1));
            double y = r + 10 + r * Math.sin(fi * (i + 1));
            vertexs.get(i).setPosition(x, y);
            vertexs.get(i).setFullText( gi.getVertex(i));
            graph.addCell(vertexs.get(i));
        }
        n = gi.edges.size();
        for (int i = 0; i < n; i++) {
            Connector c = null;
            if (gi.getEdge(i).association) {
                c = (Connector) CellFactory.create(Tool.ASSOCIATION);
            }
            else {
                c = (Connector) CellFactory.create(Tool.FLOW);
            }
            String val = gi.getEdge(i).getValue();
            if (val.equals("0") || val.equals("0.0")) {
                c.setFullText("");
                c.setValue("");
            }
            else {
                c.setFullText(val);
                //c.setValue(val);
            }
            graph.addConnection(c, vertexs.get(gi.getEdge(i).getSource()), vertexs.get(gi.getEdge(i).getTarget()));
        }
        return graph;
    }
    
    /**
     * ! Поиск k кратчайших путей в графе gi из вершины s в вершину t
     * @param s - начало кратчайших путей
     * @param t - конец кратчайших путей
     * @param gi - граф
     * @param k - количество кратчайших путей
     * @return - кратчайшие пути
     */
    public static ArrayList <ArrayList <Edge>> findShortestPaths (int s, int t, GraphInfo gi, int k) {
        ArrayList<ArrayList<Edge>> res = new ArrayList<ArrayList<Edge>>(); //Контейнер результатов
        ArrayList <Edge> exceptions = new ArrayList<Edge> (); //Контейнер дуг-исключений

        //Найти кратчайший путь
        res.add(findShortestPath(s, t, gi, exceptions));
        int ind = 0;
        for (int i = 1; i < k && ind > -1; i++) {
            //Возможные кратчайшие пути
            ArrayList<ArrayList<Edge>> paths = new ArrayList<ArrayList<Edge>> ();
            ArrayList<Edge> prev = res.get(res.size() - 1); //Последний найденный кратчайший путь
            int n = prev.size();
            ind = -1;
            double minLength = Double.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                //Добавить j-ую дугу последнего найденного кратчайшего пути в список исключений
                exceptions.add(prev.get(j));
                //Найти кратчайший путь
                ArrayList<Edge> path = findShortestPath(s, t, gi, exceptions);
                //Удалить последнюю добавленную дугу из списка исключений
                exceptions.remove(exceptions.size() - 1);

                //Найти длину полученнго пути
                double d = pathLength(path);
                if (path.size() == 0) {
                    d = Double.MAX_VALUE;
                }
                //Если длина найденнго пути менше минимально найденной
                if (minLength > d) {
                    minLength = d;  //запомнить длину
                    ind = j;        //запомнить индекс пути
                }
                //Добавить найденный путь в список путей
                paths.add(path);
            }
            if (ind > -1) { //Если был найден хоть один путь
                res.add(paths.get(ind)); //Добавить этот путь в результат
                exceptions.add(prev.get(ind)); //Добавить в список исключений дугу, при помощи которой был найден этот путь
            }
        }
        return res;
    }

    /**
     * ! Посчитать длину пути
     * @param path - путь
     * @return - длина пути
     */
    private static double pathLength (ArrayList<Edge> path) {
        int n = path.size();
        double d = 0;
        for (int i = 0; i < n; i++) {
            d += path.get(i).getDoubleValue();
        }
        return d;
    }

    /**
     * Найти кратчайший путь в графе gi из вершины s в вершину t, не переходя по дугам exceptions
     * @param s - начало кратчайших путей
     * @param t - конец кратчайших путей
     * @param gi - граф
     * @param exceptions - дуги, по которым нельзя осуществлять переходы
     * @return - кратчайший путь, который можно получить, не переходя по дугам exceptions
     */
    public static ArrayList <Edge> findShortestPath (int s, int t, GraphInfo gi, ArrayList <Edge> exceptions) {
        ArrayList<Edge> res = new ArrayList<Edge>();
        double [] d = new double [gi.getVertexCount()];
        int [] path = new int [gi.getVertexCount()];
        boolean [] u = new boolean [gi.getVertexCount()];
        for (int i = 0; i < d.length; i++) {
            d[i] = Double.MAX_VALUE;
            u[i] = true;
            path[i] = -1;
        }
        d[s] = 0;
        u[s] = false;

        ArrayList <Integer> vertexs = new ArrayList<Integer> ();
        vertexs.add(s);

        while (contains (u, true) && !vertexs.isEmpty()) {
            int n = vertexs.size();
            ArrayList <Integer> nextVertexs = new ArrayList<Integer> ();
            for (int i = 0; i < n; i++) {
                ArrayList<Edge> outEdges = gi.getOutEdges(vertexs.get(i), exceptions);
                int m = outEdges.size();
                for (int j = 0; j < m; j++) {
                    int targetVertex = outEdges.get(j).getAnothreSide(vertexs.get(i));
                    if (u[targetVertex]) {
                        if (!nextVertexs.contains(targetVertex)) {
                            nextVertexs.add(targetVertex);
                        }
                        double r = d[vertexs.get(i)] + outEdges.get(j).getDoubleValue();

                        if (d[targetVertex] > r) {
                            d[targetVertex] = r;
                            path[targetVertex] = vertexs.get(i);
                        }
                    }
                }
            }
            for (int i = 0; i < n; i++) {
                u[vertexs.get(i)] = false;
            }
            vertexs = nextVertexs;
        }
        if (d[t] != Double.MAX_VALUE) {
            int cur = t;
            while (cur != s) {
                res.add(0, gi.getEdge(path[cur], cur));
                cur = path[cur];
            }
        }
        return res;
    }

    /**
     * ! Содержит ли массив arr значение val
     * @param arr - массив
     * @param val - значение
     * @return - содержит ли массив arr значение val
     */
    static private boolean contains (boolean[] arr, boolean val) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == val) {
                return true;
            }
        }
        return false;
    }

        
    static public void addColumn (JTable table) {
        addColumn(table, false);
    }
    
    static public void addColumn (JTable table, boolean withTitle) {
        DefaultTableModel tabModel = (DefaultTableModel)table.getModel();
        int n = tabModel.getColumnCount();
        tabModel.setColumnCount(n + 1);
        if (withTitle)
            tabModel.setValueAt("A" + (n - 1), 0, n);
    }
    
    static public void addRow (JTable table) {
        DefaultTableModel tabModel = (DefaultTableModel)table.getModel();
        tabModel.setRowCount(tabModel.getRowCount() + 1);
    }
    
    static public void addRow (JTable table, boolean withTitle) {
        DefaultTableModel tabModel = (DefaultTableModel)table.getModel();
        int n = tabModel.getRowCount();
        tabModel.setRowCount(n + 1);
        if (withTitle) {
            tabModel.setValueAt("A" + (n - 1), n, 0);
        }
    }
    
    static public void removeColumn (JTable table) {
        DefaultTableModel tabModel = (DefaultTableModel)table.getModel();
        if (tabModel.getColumnCount() > 2)
            tabModel.setColumnCount(tabModel.getColumnCount() - 1);
    }
    
    static public void removeRow (JTable table) {
        DefaultTableModel tabModel = (DefaultTableModel)table.getModel();
        if (tabModel.getRowCount() > 2)
            tabModel.setRowCount(tabModel.getRowCount() - 1);
    }
    
    public static class GraphInfo {
        ArrayList <String> vertexs = new ArrayList<String> ();
        ArrayList <Edge> edges = new ArrayList<Edge> ();
        
        public GraphInfo () {
            
        }
        
        public GraphInfo (MyGraph graph) {
            Object[] vertices = graph.getChildVertices(graph.getDefaultParent());
            
            for (int i = 0; i < vertices.length; i++){
                vertexs.add (((BasicElement)vertices[i]).getFullText());
            }
            
            for (int i = 0; i < vertices.length - 1; i++){
                for (int j = i + 1; j < vertices.length; j++){
                    Object[] edgesBetween = graph.getEdgesBetween(vertices[i], vertices[j]);
                    if (edgesBetween.length > 0){
                        String ageText = ((Connector) edgesBetween[0]).getFullText();
                        if ( edgesBetween[0] instanceof Association){
                            setEdge(i, j, ageText, true);
                        }
                        else {
                            if (((SequenceFlow) edgesBetween[0]).getSource()== vertices[i])
                                setEdge(i, j, ageText, false);
                            else
                                setEdge(j, i, ageText, false);
                        }
                    }
                }
            }
        }
        
        public ArrayList <Edge> getOutEdges (int vertex, ArrayList <Edge> exceptions) {
            ArrayList <Edge> res = new ArrayList <Edge> ();
            int n = edges.size();
            for (int i = 0; i < n; i++) {
                Edge e = edges.get(i);
                if (!exceptions.contains(e)) {
                    if (e.source == vertex || e.target == vertex && e.association) {
                        res.add(e);
                    }
                }
            }
            return res;
        }
        
        public ArrayList <Edge> getOutEdges (int vertex) {
            return getOutEdges(vertex, new ArrayList<Edge> ());
        }
        
        public int getVertexCount () {
            return vertexs.size();
        }
        
        public int getEdgeCount () {
            return edges.size();
        }
        
        public void addVertex (String val) {
            vertexs.add(val);
        }
        
        public boolean setEdge (int s, int t, String val, boolean a) {
            int n = vertexs.size();
            if (s >= n || t >= n)
                return false;
            
            edges.add(new Edge (s, t, val, a));
            return true;
        }
        
        public String getVertex (int ind) {
            return vertexs.get(ind);
        }
        
        public Edge getEdge (int ind) {
            return edges.get(ind);
        }
        
        public boolean hasReversEdge(int s, int t) {
            int n = edges.size();
            for (int i = 0; i < n; i++) {
                if (edges.get(i).getSource() == t && edges.get(i).getTarget() == s) {
                    return true;
                }
            }
            return false;
        }
        
        public void removeReversEdge (int s, int t) {
            int n = edges.size();
            for (int i = 0; i < n; i++) {
                if (edges.get(i).getSource() == t && edges.get(i).getTarget() == s) {
                    edges.remove(i);
                    return;
                }
            }
        }
        
        public boolean hasEdge(int s, int t) {
            int n = edges.size();
            for (int i = 0; i < n; i++) {
                if (edges.get(i).getSource() == s && edges.get(i).getTarget() == t) {
                    return true;
                }
            }
            return false;
        }
        
        public Edge getEdge(int s, int t) {
            int n = edges.size();
            for (int i = 0; i < n; i++) {
                Edge e = edges.get(i);
                if (e.getSource() == s && e.getTarget() == t || e.association && e.getSource() == t && e.getTarget() == s) {
                    return e;
                }
            }
            return null;
        }
        
        public int getVertexIndex (String val) {
            int n = vertexs.size();
            for (int i = 0; i < n; i++) {
                if (vertexs.get(i).equals(val)) {
                    return i;
                }
            }
            return -1;
        }
        
    }
    
    public static class Edge {
        private int source;
        int target;
        String value;
        boolean association = true;
        
        public Edge (int s, int t, String val, boolean a) {
            source = s;
            target = t;
            value = val;
            association = a;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getSource() {
            return source;
        }

        public int getTarget() {
            return target;
        }

        public String getValue() {
            return value;
        }
        
        public double getDoubleValue() {
            if (value != null && value.matches("[0-9]+\\.?[0-9]*")){
                double data = Double.parseDouble(value);
                return data;
            }
            return 0;
        }
        
        public int getAnothreSide (int side) {
            if (side == source) {
                return target;
            }
            else if (side == target) {
                return source;
            }
            return -1;
        }
    }
}
