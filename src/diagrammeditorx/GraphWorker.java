/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagrammeditorx;

import Elements.Association;
import Elements.BasicElement;
import Elements.Connector;
import Elements.SequenceFlow;
import Elements.Vertex;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Максим
 */
public class GraphWorker {
    
    static JTable matrSmej = new JTable();
    static JTable matrInv = new JTable();
    static JTable matrVesov = new JTable();
    static JTable structSmej = new JTable();

    GraphWorker () {
        DefaultTableModel tabModel = (DefaultTableModel)matrSmej.getModel();
        tabModel.setColumnCount(2);
        tabModel.setRowCount(2);
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

    static void addColumn (JTable table) {
        DefaultTableModel tabModel = (DefaultTableModel)table.getModel();
        tabModel.setColumnCount(tabModel.getColumnCount() + 1);
    }
    
    static void addRow (JTable table) {
        DefaultTableModel tabModel = (DefaultTableModel)table.getModel();
        tabModel.setRowCount(tabModel.getRowCount() + 1);
    }
    
    static void removeColumn (JTable table) {
        DefaultTableModel tabModel = (DefaultTableModel)table.getModel();
        tabModel.setColumnCount(tabModel.getColumnCount() - 1);
    }
    
    static void removeRow (JTable table) {
        DefaultTableModel tabModel = (DefaultTableModel)table.getModel();
        tabModel.setRowCount(tabModel.getRowCount() - 1);
    }
}
