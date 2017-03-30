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
    
    Buttons (){
        addVertex_matrSmej.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphWorker.addColumn(GraphWorker.matrSmej);
                GraphWorker.addRow(GraphWorker.matrSmej);
            }
        });
        
        removeVertex_matrSmej.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphWorker.removeColumn(GraphWorker.matrSmej);
                GraphWorker.removeRow(GraphWorker.matrSmej);
            }
        });

    }

           
}
