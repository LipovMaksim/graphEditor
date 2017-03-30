package diagrammeditorx;

import Elements.Association;
import Elements.BasicElement;
import Elements.MyCell;
import Elements.SequenceFlow;
import Elements.Connector;
import com.mxgraph.io.mxCodec;
import com.mxgraph.io.mxCodecRegistry;
import com.mxgraph.io.mxModelCodec;
import com.mxgraph.io.mxObjectCodec;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Менеджер работы с файлами
 */
public class FileWorker {
    
    static private boolean modified = false;        // Флаг наличия изменений в текущем файле
    static private String fname=null;               // Имя последнего загруженного файла
    static private boolean cryptAsked = false;      // Флаг необходимости шифрования
    static private String cryptKey = null;          // Ключ шифрования при истинности предыдуго флага

    // Получение имени текущего файла
    static public String fName(){
        return fname;
    }
    
    // Установка имени текущего файла
    static public void setFName(String newVal){
        fname = newVal;
    }

    // Получение флага необходимости шифрования
    static public boolean cryptAsked(){
        return cryptAsked;
    }
    
    // Установка флага необходимости шифрования
    static public void setCryptAsked(boolean f){
        cryptAsked = f;
    }

    // Получение ключа шифрования
    static public String cryptKey(){
        return cryptKey;
    }
    
    // Установка ключа гифпрвания
    static public void setCryptKey(String key){
        if (key == null)
            return;
        cryptKey = key;
        cryptAsked = true;
    }
    
    // Установка флага ниличия изменений в файле
    static public void setModified(boolean f){
        modified=f;
    }
    
    // Получение флага наличия изменений в файле
    static public boolean modified(){
        return modified;
    }
    
    // Флаг регистрации кодеков
    static boolean registered = false;
    
    /**
     * Сохранение диаграммы в файл
     * @param graphComponent - рабочая область с диаграммой
     * @param fileName - имя файла для сохранения
     */
    public static void saveGraph( mxGraphComponent graphComponent, String fileName) {   
        // Регистрация кодеков, если это еще не было сделано
        if( !registered)
            registerCodecs();
        try {
            mxGraph graph = graphComponent.getGraph();

            // taken from EditorActions class
            mxCodec codec = new mxCodec();
            Map<String, Object> map = ((mxGraphModel)(graph.getModel())).getCells();
            // Сгенерировать xml код диаграммы
            String xml = mxXmlUtils.getXml(codec.encode(graph.getModel()));
            // Если требуется шифрование - зашифровать файл
            if (cryptAsked())
                xml = "[crypted]" + Encryptor.encrypt(xml, cryptKey());
            // Записать в файл полученное содержимое
            mxUtils.writeFile(xml, fileName);
            
            // Выдать сообщение об успешном сохранении
            JOptionPane.showMessageDialog( graphComponent, "Файл сохранен: " + fileName);
            // Запомнить имя файла
            fname=fileName;
            // Указать, что диаграмма больше не содержит несохраненных изменений
            setModified(false);
        } catch( Exception ex) {
            throw new RuntimeException( ex);
        }
    }
    
    /**
     * Загрузка диаграммы из файла
     * @param graphComponent - рабочая область диаграммы
     * @param fileName - имя файла сохранения
     * @return 
     */
    public static boolean loadGraph( mxGraphComponent graphComponent, String fileName) {
        // Регистрация кодеков, если это еще не было сделано
        if( !registered)
            registerCodecs();
        try {
            MyGraph graph = new MyGraph();
            graphComponent.setGraph(graph);
            // Считать содержимое файла
            String str = mxUtils.readFile(fileName);
            // Если файл зашифрован - расшифровать
            if (str.indexOf("[crypted]") == 0) {
                MyFrame.getFrame().decryptDialog();
                str = Encryptor.decrypt(str.substring(9), cryptKey());
            }
            // Распарсить содержимое
            Document document = mxXmlUtils.parseXml(str);
            if (document==null){
                JOptionPane.showMessageDialog( graphComponent, "При считывании файла произошла ошибка, возможно файл поврежден", "Ошибка" , JOptionPane.ERROR_MESSAGE);
                return false;
            }
            mxCodec codec = new mxCodec(document);
            Node el = document.getDocumentElement().getFirstChild().getFirstChild();
            Map <String, Object> o = ((mxGraphModel) graph.getModel()).getCells();
            graph.removeCells(new Object[] {o.values()});
            o = ((mxGraphModel) graph.getModel()).getCells();
            Map <String, Object> map = new HashMap<String, Object>();
            while (el!=null){
                if(el.getNodeName()!="mxCell"){
                    if (el.getNodeName()!="Elements.SequenceFlow" && el.getNodeName()!="Elements.Association"){
                        String fullText=null;
                        if (el.getAttributes().getNamedItem("fullText")!=null)
                            fullText = el.getAttributes().getNamedItem("fullText").getNodeValue();
                        Object decode = codec.decode(el);
                        if (fullText!=null)
                            ((BasicElement)decode).setFullText(fullText);
                        map.put(el.getAttributes().getNamedItem("id").getNodeValue(),decode);
                        graph.addCell(decode);
                    }
                    else{
                        //graph.addCell(codec.decode(el));
                        String fullText = null;
                        if (el.getAttributes().getNamedItem("fullText")!=null)
                            fullText = el.getAttributes().getNamedItem("fullText").getNodeValue();
                        Node subel = el.getFirstChild();
                        subel=subel.getNextSibling();
                        while (!subel.getAttributes().getNamedItem("as").getNodeValue().equals("source")){
                            NamedNodeMap attributes = subel.getAttributes();
                            String nodeValue = subel.getAttributes().getNamedItem("as").getNodeValue();
                            subel=subel.getNextSibling();
                        }
                            
                        NamedNodeMap attributes = subel.getAttributes();
                        Node namedItem = attributes.getNamedItem("id");
                        String source = namedItem.getNodeValue();
                        subel=subel.getNextSibling();
                        attributes = subel.getAttributes();
                        namedItem = attributes.getNamedItem("id");
                        String target = namedItem.getNodeValue();
                        Connector cell = null;
                        if (el.getNodeName()=="Elements.SequenceFlow"){
                            cell = new SequenceFlow();
                        }
                        else{
                            cell = new Association();
                        }
                        if (fullText!=null)
                            cell.setFullText(fullText);
                        graph.addConnection(cell, (MyCell)(map.get(source)), (MyCell)(map.get(target)));
                    }
                }
                el=el.getNextSibling();
            }
            
            fname=fileName;
        } catch( Exception ex) {
            throw new RuntimeException( ex);
        }
        
        return true;
    }
    
    /**
     * Экспорт в изображение
     * @param graphComponent - рабочая область диаграммы
     * @param fileName - имя файла сохранения
     * @throws IOException 
     */
    public static void saveAsImag( mxGraphComponent graphComponent, String fileName) throws IOException {
        BufferedImage image = mxCellRenderer.createBufferedImage(graphComponent.getGraph(), null, 1, Color.WHITE, true, null);
        ImageIO.write(image, "JPG", new File(fileName));           
    }
    
    private static void registerCodecs() {

        mxCodecRegistry.addPackage( MyObject.class.getPackage().toString()); 
        mxCodecRegistry.register(new mxObjectCodec( new MyObject()));
        mxCodecRegistry.register(new mxModelCodec(new mxGraphModel()));
/*
        mxCodecRegistry.addPackage("com.mxgraph.model");
        mxCodecRegistry.register(new mxObjectCodec(new com.mxgraph.model.mxCell()));
        mxCodecRegistry.register(new mxObjectCodec(new com.mxgraph.model.mxGeometry()));
        mxCodecRegistry.addPackage("Elements");
        mxCodecRegistry.register(new mxObjectCodec(new Elements.Annotation()));
        mxCodecRegistry.register(new mxObjectCodec(new Elements.Association()));
        mxCodecRegistry.register(new mxObjectCodec(new Elements.BasicElement()));
        mxCodecRegistry.register(new mxObjectCodec(new Elements.Task()));*/


        // enums aren't supported out of the box. with the code below you can support it; posted by gaudenz:
        // http://forum.jgraph.com/questions/2302/how-to-encode-enums?page=1#3433
        
        mxCodecRegistry.register(new mxObjectCodec( new MyObject()) {
            protected boolean isPrimitiveValue(Object value)
            {
              return super.isPrimitiveValue(value) || value.getClass().isEnum();
            }

            protected void setFieldValue(Object obj, String fieldname, Object value)
            {
                Field field = getField(obj, fieldname);

                if (field.getType().isEnum())
                {
                    Object[] c = field.getType().getEnumConstants();

                    for (int i = 0; i < c.length; i++)
                    {
                        if (c[i].toString().equals(value))
                        {
                            value = c[i];
                            break;
                        }
                    }
                }

                super.setFieldValue(obj, fieldname, value);
            }
          });
        }

    private static class MyObject {

        // the name "ID" is reserved for jraph, see UserObject.java example
        public enum NodeType {
            NODE_TYPE_1,
            NODE_TYPE_2,
            NODE_TYPE_3
        }
        private String objectName = null;
        private NodeType objectType = null;

        // empty constructor needed for loading/saving of userobjects in graph
        public MyObject() {
        }

        public String getObjectName() {
          return objectName;
        }

        public void setObjectName(String objectName) {
          this.objectName = objectName;
        }

        public NodeType getObjectType() {
          return objectType;
        }

        public void setObjectType(NodeType objectType) {
          this.objectType = objectType;
        }

        public String toString() {
          return this.objectName;
        }
    }
}
