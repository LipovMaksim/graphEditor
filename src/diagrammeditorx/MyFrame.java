package diagrammeditorx;

import Elements.*;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.handler.mxGraphTransferHandler;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraphSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.RenderedImage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TimerTask;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * Главное окно программы
 */
public class MyFrame extends JFrame{
    
    private MyScrollPane sp = null;
    private mxGraphTransferHandler transferHandler;     // Менджер копирования/вставки
    private MyGraphComponent graphComponent = null;     // Рабочее поле
    /**
     * Получение рабочей области
     * @return - рабочая область
     */
    public MyGraphComponent getGraphComponent(){
        return graphComponent;
    }
    private MyGraph graph = null;                       // Граф
    private static MyFrame frame = null;                // Экземпляр текущего окна
    
    private HashMap<Tool,MyButton> buttonList = new HashMap<Tool,MyButton>();   // Кнопки панели элементов
    private Tool currentTool = Tool.NONE;               // Текущий выделенный элемент
    private MyCell sourceCell = null, targetCell = null;// Начальная и конечная вершины для связи
    private Connector ActiveEdge = null;                // Выделенная связь
    
    /**
     * Поулчение экземпляра окна
     * @return - экземпляр текущего окна
     */
    public static MyFrame getFrame() {
        return frame;
    }
    
    
    public MyFrame() throws IOException
    {
        super();
        new Buttons();
        new GraphWorker ();
        setExtendedState(JFrame.MAXIMIZED_BOTH);   // Запуск в полноэкранном режиме
        setMinimumSize(new Dimension(500,720));    // Ограничение минимального размера окна
        frame = this;                              // Сохранение экземпляра текущего окна
        SystemRussifier.Russify();                 // Переопределение англоязычных констант диалогов с пользователем
        
        graph = new MyGraph();                              // Создание графа
        graphComponent = new MyGraphComponent(graph);       // Создание рабочей области
        transferHandler = (mxGraphTransferHandler) graphComponent.getTransferHandler(); // Создание менеджера копирования/вставки
        JPanel graphPanel = new JPanel();
        JPanel smejMatr = new JPanel();
        JPanel invMatr = new JPanel();
        JPanel vesovMatr = new JPanel();
        JPanel spisokReber = new JPanel();
        JPanel smejStruct = new JPanel();
        graphPanel.setLayout(new BorderLayout());
        smejMatr.setLayout(new BorderLayout());
        invMatr.setLayout(new BorderLayout());
        vesovMatr.setLayout(new BorderLayout());
        spisokReber.setLayout(new BorderLayout());
        smejStruct.setLayout(new BorderLayout());
        sp = new MyScrollPane(graphComponent);              
        //this.getContentPane().add(sp,BorderLayout.CENTER);
        graphPanel.add(sp,BorderLayout.CENTER);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Граф", graphPanel);
        tabbedPane.add("Матрица смежности", smejMatr);
        tabbedPane.add("Матрица инвариантности", invMatr);
        tabbedPane.add("Матрица весов", vesovMatr);
        tabbedPane.add("Список ребер", spisokReber);
        tabbedPane.add("Структура смежности", smejStruct);
        
        JPanel matrSmejButtonsPanel = new JPanel(new GridLayout(1,2,1,1));
        matrSmejButtonsPanel.add(Buttons.addVertex_matrSmej,BorderLayout.NORTH);
        matrSmejButtonsPanel.add(Buttons.removeVertex_matrSmej,BorderLayout.CENTER);
        //matrSmejButtonsPanel.add(Buttons.addRow_matrSmej,BorderLayout.CENTER);
        //matrSmejButtonsPanel.add(Buttons.removeRow_matrSmej,BorderLayout.CENTER);
        smejMatr.add(matrSmejButtonsPanel,BorderLayout.NORTH);
        smejMatr.add(GraphWorker.matrSmej,BorderLayout.CENTER);
        invMatr.add(GraphWorker.matrInv,BorderLayout.CENTER);
        vesovMatr.add(GraphWorker.matrVesov,BorderLayout.CENTER);
        //spisokReber.add();
        smejStruct.add(GraphWorker.structSmej,BorderLayout.CENTER);
           
        this.getContentPane().add(tabbedPane);

    
        // Создание панели меню
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Файл");
        
        //Сохранение в картинку
        JMenuItem saveAsImage = new JMenuItem("Сохранить как изображение");
        saveAsImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("JPG & JPEG", "jpg", "jpeg"));
                if ( fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) 
                {
                    String filename = fc.getSelectedFile().getAbsolutePath();
                    if (!filename.toLowerCase().endsWith(".jpg") && !filename.toLowerCase().endsWith(".jpeg"))
                        filename += ".jpg";
                    try {
                        ImageIO.write((RenderedImage) graph.getImage(), "jpg", new File(filename));
                    } catch (IOException ex) {
                        Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(frame,"Ошибка при экспорте в изображение", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        // Сохранить как...
        JMenuItem saveAs = new JMenuItem("Сохранить как..");
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveDialog();
            }
        });
        
        // Печать
        JMenuItem printMI = new JMenuItem("Печать");
        printMI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphComponent.print();
            }
        });

        // Предварительный просмотр
        JMenuItem view = new JMenuItem("Предварительный просмотр");
        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double scale = 3.8; //Параметр от которого завист размер окна для предварительного просмотра
                int lh=(int) (210*scale),lw=(int)(297*scale); //Конкретные размеры окна (в пикселях)
                Dimension d = new Dimension((int)(lw*1.05), (int)(lh*1.05));
                JFrame fr = new JFrame(); //Создаем новый фрейм
                JMenuBar menuBar= new JMenuBar();
                JMenu menu = new JMenu("Меню"); //Создаем меню
                JMenuItem printMI = new JMenuItem("Печать"); //Создаем печать
                printMI.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        graphComponent.print();
                    }
                });
                menu.add(printMI); //Добавляем в меню печать
                menuBar.add(menu); //Добавляем меню на панель
                fr.setJMenuBar(menuBar); //Добавляем панель на фрейм
                
                int xs=(int)(lw*0.02), ys = (int)(lh*0.02); //Стартовые координаты картинки
                Image image = graph.getImage(); //Получаем скрин графа
                if (image!=null){
                    //Размеры изображения
                    int h=image.getHeight(null);
                    int w=image.getWidth(null);
                    //Если один из размеров больше окна, масштабируем изображение на необходимую величину
                    if (h>lh || w>lw){ 
                        if (h-lh>w-lw){
                            image = image.getScaledInstance((int)(w*((double)lh/h)), lh, 0);
                        }
                        else{
                            image = image.getScaledInstance(lw, (int)(h*((double)lw/w)), 0);
                        }
                        //Обновляем размеры изображения
                        h=image.getHeight(null);
                        w=image.getWidth(null);
                    }
                    Image forPrint = image; //Копируем изображение в новую переменную, т.к. для метода отрисовки нужна final переменная
                    int wForPrint = w, hForPrint = h;
                    //Отрисовываем изображение
                    JPanel pan = new JPanel(){
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            g.drawImage(forPrint, xs+(lw-wForPrint)/2, ys+(lh-hForPrint)/2, null);
                        }
                    };
                    //Ограничения по изменению размеров окна (чтобы окно было пропорционально листу A4)
                    fr.add(pan);
                    fr.setSize(d);
                    fr.setMinimumSize(d);
                    fr.setMaximumSize(d);
                    fr.setResizable(false);

                    pan.setBackground(Color.WHITE);

                    pan.show();
                    fr.show();
                }
                else{
                    JOptionPane.showMessageDialog(graphComponent,"Нечего демонстрировать","Ошибка",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Открыть...
        JMenuItem open = new JMenuItem("Открыть..");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(FileWorker.modified()){
                    if(saveQuestionDialog()){
                        openDialog();
                    }
                }
                else
                    openDialog();
            }
        });

        // Создать
        JMenuItem _new = new JMenuItem("Создать");
        _new.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // При наличии изменений в текущем файле - спросить пользователя о необходимости сохранить
                if(FileWorker.modified()){
                    if(saveQuestionDialog()){
                        graph = new MyGraph();
                        graphComponent.setGraph(graph);
                        graph.getSelectionModel().addListener(mxEvent.CHANGE, mxIEventListener);
                        graphComponent.repaint();
                        FileWorker.setModified(false);
                    }
                }
                else{
                    graph = new MyGraph();
                    graphComponent.setGraph(graph);
                    graph.getSelectionModel().addListener(mxEvent.CHANGE, mxIEventListener);
                    graphComponent.repaint();
                    FileWorker.setModified(false);
                }
            }
        });
        
        // Сохранить
        JMenuItem save = new JMenuItem("Сохранить");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        
        // Добавление кнопок к панели меню
        menu.add(_new);
        menu.add(open);
        menu.add(save);
        menu.add(saveAs);
        menu.add(saveAsImage);
        menu.add(view);
        menu.add(printMI);
        
        // Добавление меню к окну
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        
        // Добавление меню по правой кнопке мыши
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                mouseReleased(e);
            }
            public void mouseReleased(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    showGraphPopupMenu(e);
                }
            }
        });
        
        //---------------------------выбор связи элементов---------------------------
        graph.getSelectionModel().addListener(mxEvent.CHANGE, mxIEventListener);
        //---------------------------ограничение на изменение размера элемента-------------------------
        graph.addListener(mxEvent.RESIZE_CELLS, new  mxIEventListener() { 
           @Override 
           public void invoke(Object sender, mxEventObject eo) {
               System.out.println("RESIZE_CELLS");
               Object []cells = (Object[]) eo.getProperty("cells");
               MyCell cell = null;
               if(cells.length == 0)
                   return;
               // Проверить размеры для масштабированного элемента
               cell = (MyCell)cells[0];
               if(cell == null || !(cell instanceof BasicElement)) return;
               ((BasicElement)cell).validateCellSize();
           }
        });
        //-----------------------------ограничение на вводимый текст---------------------------
        graphComponent.addListener(mxEvent.LABEL_CHANGED, new mxIEventListener() { 
            @Override 
            public void invoke(Object sender, mxEventObject eo) {
                System.out.println("LABEL_CHANGED");
                // Получение подписи элемента
                MyCell cell =(MyCell) eo.getProperty("cell");
                if (cell == null) return;
                String text = (String) eo.getProperty("value");
                // Проверить размеры подписи
                cell.ValidateText(text);
                // При превышении максимально допустимой длины подписи выдать сообщенние об ошибке
                if(text.length()>cell.getMaxTextLenght())
                    JOptionPane.showMessageDialog(new JFrame(), "В элементе может содержаться не более "+ cell.getMaxTextLenght() + " символов.", "Предупреждение",JOptionPane.ERROR_MESSAGE);
            }
        });
        // Вощвращение полной подписи при её редактировании
        graphComponent.addListener(mxEvent.START_EDITING, new mxIEventListener() { 
            @Override 
            public void invoke(Object sender, mxEventObject eo) {
                System.out.println("START_EDITING");
                MyCell cell =(MyCell) eo.getProperty("cell");
                if (cell == null) return;
                    cell.setFullText();
            }
        });
        //-----------------------------переустановка связи на другой объект-----------------------------
        graph.addListener(mxEvent.CONNECT_CELL, new  mxIEventListener() { 
            @Override 
            public void invoke(Object sender, mxEventObject eo) {
                System.out.println("CONNECT_CELL");
                MyCell edge =  (MyCell) eo.getProperty("edge");
                MyCell terminal =  (MyCell) eo.getProperty("terminal");
                MyCell previous =  (MyCell) eo.getProperty("previous");
                MyCell source =  (MyCell) edge.getSource();
                MyCell target =  (MyCell) edge.getTarget();
                boolean isSource = (boolean) eo.getProperty("source");//перемещено начало связи
                boolean possibleToConnect = true;
                boolean connectionAlreadyExist = false;
                try {
                    if(isSource) //перемещено начало связи
                    {
                        connectionAlreadyExist = graph.hasEdgesBetween(terminal, target);
                        possibleToConnect = ((Connector)edge).checkConnection(terminal,target);
                        if(!connectionAlreadyExist && possibleToConnect)
                            graph.addConnection(((Connector)edge), terminal, target); 
                        else//возвращаем связь на место
                            graph.addConnection(((Connector)edge), previous, target);
                    }
                    else //перемещен конец связи
                    {
                        connectionAlreadyExist = graph.hasEdgesBetween(source, terminal);
                        possibleToConnect = ((Connector)edge).checkConnection(source,terminal);
                        if (!connectionAlreadyExist && possibleToConnect)
                            graph.addConnection(((Connector)edge), source, terminal); 
                        else //возвращаем связь на место
                            graph.addConnection(((Connector)edge), source, previous);
                    }
                    //вывод сообщения об ошибке в случае, если невозможно установить связь
                    if(!possibleToConnect)
                    {
                        String errorMessage = "";
                        if(edge instanceof Association)
                            errorMessage = "Невозможно установить ассоциативную связь между элементами.";
                        else if(edge instanceof SequenceFlow)
                            errorMessage = "Невозможно установить поток управления между элементами в этом направлении.";
                        JOptionPane.showMessageDialog(graphComponent,errorMessage,"Ошибка",JOptionPane.ERROR_MESSAGE);
                    }
                    else if(connectionAlreadyExist)
                        JOptionPane.showMessageDialog(graphComponent,"Cвязь между элементами уже установлена.","Ошибка",JOptionPane.ERROR_MESSAGE);
                    
                    
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        //--------------------------обработка события - добавления элемента на диаграмму-------------------
        //----------------------------- создание объектов -----------------------------
        MouseListener listner = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Если текущий инструмент выбран и это не ассоциация и не поток управления
                if(currentTool!=Tool.NONE && currentTool!=Tool.ASSOCIATION && currentTool!=Tool.FLOW){
                    MyCell ActiveCell = CellFactory.create(currentTool); //Создать объект согласно выбранному инструменту
                    //Устанавливаем позицию согласно текущей координате мыши на графе
                    ActiveCell.setPosition(graphComponent.getTrueMousePosition().getX() - ActiveCell.getGeometry().getWidth()/2, graphComponent.getTrueMousePosition().getY() - ActiveCell.getGeometry().getHeight()/2);
                    graph.addCell(ActiveCell); //Добавление нового объекта на граф
                    FileWorker.setModified(true); //Граф модифицирован
                    resetCurrentTool(); //Снять текущий инструмент
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        };
        graphComponent.getGraphControl().addMouseListener(listner); //Добавление слушетеля

        // Подсчёт времени, которое курсор мыши остаётся неподвижным поверх элемента
        MouseMotionListener mml = new MouseMotionListener() {
            MyCell currentElement=null; // Текущий элемент
            Timer timer=null;           // Таймер времени остановки курсора
            Tooltip tooltip = null;     // Подсказка полной подписи элемента
            boolean create=true;
            @Override
            public void mouseDragged(MouseEvent e) {
                currentElement=null;
                if (timer!=null){
                    timer.cancel();
                    timer=null;
                    create=false;
                }

                if(tooltip!=null){
                    graphComponent.getGraph().removeCells(new Object[] {tooltip});
                    tooltip=null;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                
                // Получение объекта, на который наведена мышь
                double x=graphComponent.getTrueMousePosition().getX();
                double y=graphComponent.getTrueMousePosition().getY();
                Object ob=graphComponent.getCellAt((int)x, (int)y);

                // Если мышь наведена на объект с непустой подписи
                if (currentElement==null && ob != null && ob instanceof MyCell && 
                        ((MyCell)ob).getFullText() != null && !((String)((MyCell)ob).getFullText()).isEmpty()){
                    create=true;
                    currentElement=(MyCell)ob;
                    TimerTask task;
                    // Вывод подсказки при срабатывании таймера
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            Point trueMousePosition = graphComponent.getTrueMousePosition();
                            double xx=trueMousePosition.getX();
                            double yy=trueMousePosition.getY();
                            Object[] selectionCells = graphComponent.getGraph().getSelectionCells();
                            if (create && tooltip==null && (selectionCells.length==0 || selectionCells[0]!=ob) && !((String)((MyCell)ob).getFullText()).isEmpty()){
                                tooltip = new Tooltip((String)((MyCell)ob).getFullText(), getGraphics());
                                tooltip.setPosition(xx, yy);
                                graphComponent.getGraph().addCell(tooltip);
                            }
                        }
                    };
                    // Запуск таймера
                    timer=new Timer();
                    timer.schedule(task, 1500);
                }
                
                // Удаление подсказки после отображения
                if (currentElement!=ob && (tooltip==null ||  ob!=tooltip)){
                    currentElement=null;
                    if (timer!=null){
                        timer.cancel();
                        timer=null;
                        create=false;
                    }
                    
                    if(tooltip!=null){
                        graphComponent.getGraph().removeCells(new Object[] {tooltip});
                        tooltip=null;
                    }
                }
            }
        };
        graphComponent.getGraphControl().addMouseMotionListener(mml);
        
        //----------------------------ПАНЕЛЬ ИНСТРУМЕНТОВ---------------------------------
        JPanel ButtonsPanel = new JPanel();
        JPanel panelForLayout = new JPanel();
        ButtonsPanel.setLayout(new BorderLayout());
        panelForLayout.setLayout(new BoxLayout(panelForLayout, BoxLayout.Y_AXIS));
        ButtonsPanel.add(panelForLayout, BorderLayout.NORTH);
        //this.getContentPane().add(ButtonsPanel,BorderLayout.WEST);
        graphPanel.add(ButtonsPanel, BorderLayout.WEST);
        //панель процессов
        JPanel ButtonsPanelProc = new JPanel(new GridLayout(1,2,1,1));
        TitledBorder tb1 = new TitledBorder(new LineBorder(Color.BLACK),"Вершина");
        tb1.setTitleJustification(TitledBorder.ABOVE_TOP);
        ButtonsPanelProc.setBorder(tb1);
        panelForLayout.add(ButtonsPanelProc);
        
        //панель событий
        JPanel ButtonsPanelEvents = new JPanel(new GridLayout(2,2,1,1));
        TitledBorder tb2 = new TitledBorder(new LineBorder(Color.BLACK),"События");
        tb2.setTitleJustification(TitledBorder.CENTER);
        ButtonsPanelEvents.setBorder(tb2);
        //ButtonsPanel.add(ButtonsPanelEvents);
        //панель шлюзов
        JPanel ButtonsPanelGate = new JPanel(new GridLayout(2,2,1,1));
        TitledBorder tb3 = new TitledBorder(new LineBorder(Color.BLACK),"Шлюзы");
        tb3.setTitleJustification(TitledBorder.CENTER);
        ButtonsPanelGate.setBorder(tb3);
        //ButtonsPanel.add(ButtonsPanelGate);
        //панель пулов
        JPanel ButtonsPanelPool = new JPanel(new GridLayout(1,2,1,1));
        TitledBorder tb4 = new TitledBorder(new LineBorder(Color.BLACK),"Пулы");
        tb4.setTitleJustification(TitledBorder.CENTER);
        ButtonsPanelPool.setBorder(tb4);
        //ButtonsPanel.add(ButtonsPanelPool);
        //панель вспомогательных элементлв
        JPanel ButtonsPanelExtra = new JPanel(new GridLayout(2,2,1,1));
        TitledBorder tb5 = new TitledBorder(new LineBorder(Color.BLACK),"Вспомогательные");
        tb5.setTitleJustification(TitledBorder.CENTER);
        ButtonsPanelExtra.setBorder(tb5);
        //ButtonsPanel.add(ButtonsPanelExtra);
        //панель связей
        JPanel ButtonsPanelConnect = new JPanel(new GridLayout(1,2,1,1));
        TitledBorder tb6 = new TitledBorder(new LineBorder(Color.BLACK),"Дуги");
        tb6.setTitleJustification(TitledBorder.CENTER);
        ButtonsPanelConnect.setBorder(tb6);
        panelForLayout.add(ButtonsPanelConnect);
        //Сохранить граф
        JPanel ButtonsPanelSave = new JPanel(new GridLayout(1,2,1,1));
        TitledBorder tb7 = new TitledBorder(new LineBorder(Color.BLACK),"Сохранение");
        tb7.setTitleJustification(TitledBorder.CENTER);
        ButtonsPanelSave.setBorder(tb7);
        panelForLayout.add(ButtonsPanelSave);

        
        //Элемент Задание
        MyButton addTask = new MyButton("/icons/vertex.png","Вершина");
        buttonList.put(Tool.TASK,addTask);
        addTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { 
                if (currentTool == Tool.TASK){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.TASK);
                }
            }
        });
        ButtonsPanelProc.add(addTask);
        
        //Элемент Начальное событие
        MyButton addStartEvent = new MyButton("/icons/start.png","Начальное событие");
        buttonList.put(Tool.START_EVENT,addStartEvent);
        addStartEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                if (currentTool==Tool.START_EVENT){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.START_EVENT);
                }
            }
        });
        ButtonsPanelEvents.add(addStartEvent);
        
        //Элемент Завершающее событие
        MyButton addEndEvent = new MyButton("/icons/end.png","Завершающее событие");
        buttonList.put(Tool.END_EVENT,addEndEvent);
        addEndEvent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                if (currentTool==Tool.END_EVENT){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.END_EVENT);
                }
            }
        });
        ButtonsPanelEvents.add(addEndEvent);
        
        // Элемент Таймер
        MyButton addTimer = new MyButton("/icons/timer2.png","Таймер");
        buttonList.put(Tool.TIMER,addTimer);
        addTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                if (currentTool==Tool.TIMER){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.TIMER);
                }
            }
        });
        ButtonsPanelEvents.add(addTimer);
        
        // Элемент Свернутый пул
        MyButton addPool = new MyButton("/icons/poolrus.png","Свернутый пул");
        buttonList.put(Tool.POOL,addPool);
        addPool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                if (currentTool==Tool.POOL){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.POOL);
                }
            }
        });
        ButtonsPanelPool.add(addPool);
        
        // Элемент Шлюз исключающее или
        MyButton addExclusive = new MyButton("/icons/exclusive.png","Шлюз исключающее или");
        buttonList.put(Tool.EXCLUSIVE,addExclusive);
        addExclusive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                if (currentTool==Tool.EXCLUSIVE){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.EXCLUSIVE);
                }
            }
        });
        ButtonsPanelGate.add(addExclusive);
        
        // Элемент Шлюз включающее или
        MyButton addInclusive = new MyButton("/icons/inclusive.png","Шлюз включающее или");
        buttonList.put(Tool.INCLUSIVE,addInclusive);
        addInclusive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                if (currentTool == Tool.INCLUSIVE){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.INCLUSIVE);
                }
            }
        });
        ButtonsPanelGate.add(addInclusive);
        
        // Элемент Шлюз и
        MyButton addParallel = new MyButton("/icons/parallel.png","Шлюз и");
        buttonList.put(Tool.PARALLEL,addParallel);
        addParallel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                if (currentTool==Tool.PARALLEL){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.PARALLEL);
                }
            }
        });
        ButtonsPanelGate.add(addParallel);
        
        // Элемент Начальный соединитель страниц
        MyButton addStartPageConnector = new MyButton("/icons/startPageConnector.png","Начальный соединитель страниц");
        buttonList.put(Tool.START_PAGE_CONNECTOR,addStartPageConnector);
        addStartPageConnector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                if (currentTool==Tool.START_PAGE_CONNECTOR){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.START_PAGE_CONNECTOR);
                }
            }
        });
        ButtonsPanelExtra.add(addStartPageConnector);
        
        // Элемент Конечный соединитель страниц
        MyButton addEndPageConnector = new MyButton("/icons/endPageConnector.png","Конечный соединитель страниц");
        buttonList.put(Tool.END_PAGE_CONNECTOR,addEndPageConnector);
        addEndPageConnector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                if (currentTool==Tool.END_PAGE_CONNECTOR){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.END_PAGE_CONNECTOR);
                }
            }
        });
        ButtonsPanelExtra.add(addEndPageConnector);
        
        // Элемент Аннотация
        MyButton addAnnotation = new MyButton("/icons/annotationtrue.png","Аннотация");
        buttonList.put(Tool.ANNOTATION,addAnnotation);
        addAnnotation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                if (currentTool==Tool.ANNOTATION){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.ANNOTATION);
                }
            }
        });
        ButtonsPanelExtra.add(addAnnotation);
        //--------------------------------связи--------------------------------
        // Элемент Поток управления
        MyButton addSeqFlow = new MyButton("/icons/arrow.png","Поток управления");
        buttonList.put(Tool.FLOW,addSeqFlow);
        addSeqFlow.addActionListener(new ActionListener() { 
            @Override 
            public void actionPerformed(ActionEvent e) { 
                if (currentTool==Tool.FLOW){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.FLOW);
                }
            } 
        }); 
        ButtonsPanelConnect.add(addSeqFlow); 

        // Элемент Ненаправленная ассоциация
        MyButton addAssociation = new MyButton("/icons/Association2.png","Ненаправленная ассоциация");
        buttonList.put(Tool.ASSOCIATION,addAssociation);
        addAssociation.addActionListener(new ActionListener() { 
            @Override 
            public void actionPerformed(ActionEvent e) { 
                if (currentTool==Tool.ASSOCIATION){
                    resetCurrentTool();
                }
                else{
                    resetCurrentTool();
                    setCurrentTool(Tool.ASSOCIATION);
                }
            } 
        }); 
        ButtonsPanelConnect.add(addAssociation);
        
        // Сохранить граф
        MyButton saveGraph = new MyButton("/icons/save.png","Сохранить");
        //buttonList.put(Tool.ASSOCIATION,saveGraph);
        saveGraph.addActionListener(new ActionListener() { 
            @Override 
            public void actionPerformed(ActionEvent e) { 
                GraphWorker.fillSmejMatr(graph);
            } 
        }); 
        ButtonsPanelSave.add(saveGraph);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 600));
        frame.pack();
        frame.setVisible(true);
    }
    
    // Установка текущего инструмента
    public void setCurrentTool (Tool tool)
    {
        // Установка текущего элемента
        currentTool = tool;
        sourceCell = null;
        targetCell = null;
        
        // Если устанавливается не пустой элемент - выделить его на панели
        if (currentTool!=Tool.NONE)
            buttonList.get(tool).active(true);
    }
    // Смена текущего инструмента
    public void resetCurrentTool()
    {
        if (currentTool != Tool.NONE)
            buttonList.get(currentTool).active(false);
        currentTool = Tool.NONE;
        sourceCell= targetCell = null;
        ActiveEdge=null;
    }
    
    // Демонстрация меню по правой кнопке мыши
    public void showGraphPopupMenu(MouseEvent e)
    {
        // Определение точки, на которой нажата правая кнопка
        Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),
                        graphComponent);
        transferHandler.setLocation(pt);
        JPopupMenu menu = new JPopupMenu();
        boolean selected = !getGraphComponent().getGraph().isSelectionEmpty();

        // Вырезать
        JMenuItem cut = new JMenuItem("Вырезать");
        cut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                transferHandler.setLocation(null);
                transferHandler.getCutAction().actionPerformed(new ActionEvent(graphComponent, e.getID(), e.getActionCommand()));
            }
        });
        // Копировать
        JMenuItem copy = new JMenuItem("Копировать");
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                transferHandler.getCopyAction().actionPerformed(new ActionEvent(graphComponent, e.getID(), e.getActionCommand()));
            }
        });
        // Вставить
        JMenuItem paste = new JMenuItem("Вставить");
        paste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                graph.setPasting(true);
                transferHandler.getPasteAction().actionPerformed(new ActionEvent(graphComponent, e.getID(), e.getActionCommand()));
            }
        });

        // Добавление пунктов к меню
        menu.add(cut).setEnabled(selected);
        menu.add(copy).setEnabled(selected);
        menu.add(paste);
        menu.show(graphComponent, pt.x, pt.y);
        e.consume();
    }
    
    // Диалог сохранения фалйа 
    private void saveDialog(){
        encryptDialog();
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Diagramm editir X (.dex)", "dex"));
        if ( fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) {
            String filename = fc.getName(fc.getSelectedFile());
            // Если имя выбранного файла не оканчивается на требуемое расширение - дописать его
            if (!filename.toLowerCase().endsWith(".dex"))
                filename += ".dex";
            try
            {
                // Попытаться сохранить диаграмму в файл
                FileWriter fw = new FileWriter(new File(filename));
                FileWorker.saveGraph(graphComponent, fc.getCurrentDirectory() + "/" + filename);
            }
            catch (IOException e1 ) {
                JOptionPane.showMessageDialog(this,"Ошибка при сохранении в файл", "Ошибка",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Диалог шифрования файла
    private void encryptDialog() {    
        Object[] options = {"Да",
                            "Нет"};
        int answer = JOptionPane.showOptionDialog(this,
            "Зашифровать сохраняемый файл?",
            "Шифрование",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]);
        if (answer == JOptionPane.YES_OPTION) {
            // При желании сохранить - запросить и установить ключ шифрования
            FileWorker.setCryptKey(JOptionPane.showInputDialog(this, "Введите ключ шифрования:", "Шифрование", JOptionPane.PLAIN_MESSAGE));
        }
        else {
            FileWorker.setCryptAsked(false);
        }
    }
    
    // Диалог расшифрования файла
    public void decryptDialog() {
        // Запросить и установить ключ шифрования
        FileWorker.setCryptKey(JOptionPane.showInputDialog(this, "Файл зашифрован. Введите ключ шифрования:", "Шифрование", JOptionPane.PLAIN_MESSAGE));
    }
    
    // Диалог открытия файла
    private void openDialog(){
        JFileChooser fc = new JFileChooser();
        //fc.setApproveButtonText("Открыть");
        fc.setFileFilter(new FileNameExtensionFilter("Diagramm editir X (.dex)", "dex"));
        if ( fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
            try ( FileReader fr = new FileReader(fc.getSelectedFile()) ) 
            {
                // При выборе файла попытаться считать и отобразить его содержимое
                if(FileWorker.loadGraph(graphComponent, fc.getCurrentDirectory() + "/" + fc.getName(fc.getSelectedFile()))){
                    graph=(MyGraph) graphComponent.getGraph();
                    graph.getSelectionModel().addListener(mxEvent.CHANGE, mxIEventListener);
                    graphComponent.repaint();
                    FileWorker.setModified(false);
                }
            }
            catch ( IOException e1 ) {
                JOptionPane.showMessageDialog(this,"При считывании файла произошла ошибка. Возможно, файл поврежден","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Диалог при наличии изменений в текущем файле
    private boolean saveQuestionDialog(){
        Object[] options = {"Да",
                            "Нет",
                            "Отмена"};
        int unswer = JOptionPane.showOptionDialog(this,
            "Диаграмма не сохранена. Сохранить?",
            "Предупреждение",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[2]);
        
        // Сохранить текущий файл при положительном ответе на предложение
        if(unswer==JOptionPane.YES_OPTION){
            save();
        }
        return unswer==JOptionPane.YES_OPTION || unswer==JOptionPane.NO_OPTION;
    }
    
    // Сохранение файла
    private void save(){
        // Если имя файла неизвестно - запросить
        if (FileWorker.fName()==null){
            saveDialog();
        }
        // иначе сохранить в него
        else{
            FileWorker.saveGraph(graphComponent, FileWorker.fName());
        }
    }
    //установка связи между объектами
    mxIEventListener mxIEventListener = new mxIEventListener() { 
        @Override
        public void invoke(Object sender, mxEventObject eo) {
            System.out.println("CHANGE1");
            FileWorker.setModified(true);
            if(currentTool!=Tool.ASSOCIATION && currentTool!=Tool.FLOW) return;

            if (ActiveEdge==null){
                ActiveEdge = (Connector)CellFactory.create(currentTool);
            }

            mxGraphSelectionModel sm = (mxGraphSelectionModel) sender;
            MyCell cell = (MyCell) sm.getCell();
            boolean wrongConnection = false;
            if (cell != null && cell.isVertex()) 
            {
                if (sourceCell==null)
                    sourceCell= (MyCell) graph.getSelectionCell();
                else if (targetCell==null)
                { 
                    targetCell = (MyCell) graph.getSelectionCell();
                    
                    {
                        //проверяем, что между этими элементами еще нет связи
                        boolean connectionAlreadyExist = graph.hasEdgesBetween(sourceCell,targetCell);
                        //проверяем - между элементами можно установить данный тип связи связи
                        //boolean possibleToConnect = ActiveEdge.checkConnection(sourceCell,targetCell);
                        if (!connectionAlreadyExist){

                            graph.addConnection(ActiveEdge, sourceCell, targetCell);

                            //----------------------------поворот текстовой аннотации--------------------------------
                            mxPoint p = null;
                            Annotation annotation = null;
                            if(sourceCell instanceof Annotation)
                            {
                                p = ActiveEdge.fromPoint();
                                annotation = (Annotation)sourceCell;
                            }
                            else if(targetCell instanceof Annotation)
                            {
                                p = ActiveEdge.toPoint();
                                annotation = (Annotation)targetCell;
                            }
                            if (p != null)
                            {
                                if(p.getX() < 0.5)
                                    annotation.flipToRight();
                                else
                                    annotation.flipToLeft();
                                graphComponent.refresh();
                            }
                            //--------------------------------------------------------------------------------------------
                            graphComponent.getGraph().setSelectionCell(null);
                            
                        }
                        else
                        {
                            if(ActiveEdge instanceof SequenceFlow)
                            {
                                //if (graph.ha)
                                String errorMessage = "";
                                if(ActiveEdge instanceof Association)
                                    errorMessage = "Невозможно установить ассоциативную связь между элементами.";
                                else if(ActiveEdge instanceof SequenceFlow)
                                    errorMessage = "Связь между элементами уже установлена.";
                                JOptionPane.showMessageDialog(graphComponent,errorMessage,"Ошибка",JOptionPane.ERROR_MESSAGE);
                            }
                            else if (connectionAlreadyExist)
                                JOptionPane.showMessageDialog(graphComponent,"Связь между элементами уже установлена.","Ошибка",JOptionPane.ERROR_MESSAGE);
                        }
                    } 
                    sourceCell = targetCell = null;
                    ActiveEdge = null;
                    graph.setSelectionCell(null);
                }
            }
            else if (cell == null)
            {
                graph.setSelectionCell(null);
                sourceCell = targetCell = null;
            }
        }
    };
}