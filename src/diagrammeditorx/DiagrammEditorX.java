package diagrammeditorx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Редактор BPMN-диаграмм
 */
public class DiagrammEditorX {

    /**
     * @param args the command line arguments
     */ 
    public static void main(String[] args) {
        try {
            // Запуск главного окна программы
            MyFrame frame = new MyFrame();
        } catch (IOException ex) {
            Logger.getLogger(DiagrammEditorX.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
