/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diagrammeditorx;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author Максим
 */
public class Buttons {

    static JButton addVertex_matrSmej = new JButton("Добавить вершину");
    static JButton removeVertex_matrSmej = new JButton("Удалить вершину");
    static JButton save_matrSmej = new JButton("Сохранить");
    
    static JButton addVertex_matrVesov = new JButton("Добавить вершину");
    static JButton removeVertex_matrVesov = new JButton("Удалить вершину");
    static JButton save_matrVesov = new JButton("Сохранить");
    
    static JButton addEdge_spisokReber = new JButton("Добавить ребро");
    static JButton removeEdge_spisokReber = new JButton("Удалить ребро");
    static JButton save_spisokReber = new JButton("Сохранить");
    
    static JButton addVertex_structSmej = new JButton("Добавить вершину");
    static JButton removeVertex_structSmej = new JButton("Удалить вершину");
    static JButton save_structSmej = new JButton("Сохранить");
    
    Buttons (){
        addVertex_matrSmej.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphWorker.addColumn(GraphWorker.matrSmej, true);
                GraphWorker.addRow(GraphWorker.matrSmej, true);
            }
        });
        
        removeVertex_matrSmej.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphWorker.removeColumn(GraphWorker.matrSmej);
                GraphWorker.removeRow(GraphWorker.matrSmej);
            }
        });
        
        addVertex_matrVesov.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphWorker.addColumn(GraphWorker.matrVesov, true);
                GraphWorker.addRow(GraphWorker.matrVesov, true);
            }
        });
        
        removeVertex_matrVesov.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphWorker.removeColumn(GraphWorker.matrVesov);
                GraphWorker.removeRow(GraphWorker.matrVesov);
            }
        });
        
        addEdge_spisokReber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphWorker.addColumn(GraphWorker.spisokReber);
            }
        });
        
        removeEdge_spisokReber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphWorker.removeColumn(GraphWorker.spisokReber);
            }
        });
        
        addVertex_structSmej.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphWorker.addRow(GraphWorker.structSmej);
            }
        });
        
        removeVertex_structSmej.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphWorker.removeRow(GraphWorker.structSmej);
            }
        });
        

    }

           
}
