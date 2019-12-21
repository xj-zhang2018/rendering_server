package org.xjtu.framework.core.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.jetty.util.log.Log;

/**
 * 鎿嶄綔XML鏂囦欢鐨勫伐鍏风被
 * 
 * @author glw
 */
public class XMLUtil {
    /**
     * 寰楀埌XML鏂囨。
     * 
     * @param xmlFile
     *            鏂囦欢鍚嶏紙璺緞锛�
     * @return XML鏂囨。瀵硅薄
     * @throws DocumentException
     */
    public static Document getDocument(String xmlFile) {
        SAXReader reader = new SAXReader();
        reader.setEncoding("UTF-8");
        File file = new File(xmlFile);
        try {
            if (!file.exists()) {
                return null;
            } else {
                return reader.read(file);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e + "指定文件【" + xmlFile + "】读取错误");
        }
    }

    /**
     * 寰楀埌XML鏂囨。(缂栫爜鏍煎紡-gb2312)
     * 
     * @param xmlFile
     *            鏂囦欢鍚嶏紙璺緞锛�
     * @return XML鏂囨。瀵硅薄
     * @throws DocumentException
     */
    public static Document getDocument_gb2312(String xmlFile) {
        SAXReader reader = new SAXReader();
        reader.setEncoding("gb2312");
        File file = new File(xmlFile);
        try {
            if (!file.exists()) {
                return null;
            } else {
                return reader.read(file);
            }
        } catch (DocumentException e) {
        	 throw new RuntimeException(e + "指定文件【" + xmlFile + "】读取错误");
        }
    }

    public static String getText(Element element) {
        try {
            return element.getTextTrim();
        } catch (Exception e) {
            throw new RuntimeException(e + "指定【" + element.getName() + "】节点读取错误");
        }

    }

    /**
     * 澧炲姞xml鏂囦欢鑺傜偣
     * 
     * @param document
     *            xml鏂囨。
     * @param elementName
     *            瑕佸鍔犵殑鍏冪礌鍚嶇О
     * @param attributeNames
     *            瑕佸鍔犵殑鍏冪礌灞炴��
     * @param attributeValues
     *            瑕佸鍔犵殑鍏冪礌灞炴�у��
     */
    public static Document addElementByName(Document document, String elementName, Map<String, String> attrs, String cdata) {
        try {
            Element root = document.getRootElement();
            Element subElement = root.addElement(elementName);
            for (Map.Entry<String, String> attr : attrs.entrySet()) {
                subElement.addAttribute(attr.getKey(), attr.getValue());
            }
            subElement.addCDATA(cdata);
        } catch (Exception e) {
            throw new RuntimeException(e + "->指定【" + elementName + "】节点增加错误");
        }
        return document;
    }

    /**
     * 鍒犻櫎xml鏂囦欢鑺傜偣
     */
    @SuppressWarnings("unchecked")
    public static Document deleteElementByName(Document document, String elementName) {
        Element root = document.getRootElement();
        Iterator<Object> iterator = root.elementIterator("file");
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            // 鏍规嵁灞炴�у悕鑾峰彇灞炴�у��
            Attribute attribute = element.attribute("name");
            if (attribute.getValue().equals(elementName)) {
                root.remove(element);
                document.setRootElement(root);
                break;
            }
        }
        return document;
    }

    /**
     * 杈撳嚭xml鏂囦欢
     * 
     * @param document
     * @param filePath
     * @throws IOException
     */
    public static void writeXml(Document document, String filePath) throws IOException {
        File xmlFile = new File(filePath);
        XMLWriter writer = null;
        try {
            if (xmlFile.exists())
                xmlFile.delete();
            writer = new XMLWriter(new FileOutputStream(xmlFile), OutputFormat.createPrettyPrint());
            writer.write(document);
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    /**
     * 鍒涘缓Document鍙婃牴鑺傜偣
     * 
     * @param rootName
     * @param attributeName
     * @param attributeVaule
     * @return
     */
    public static Document createDocument(String rootName, String attributeName, String attributeVaule) {
        Document document = null;
        try {
            document = DocumentHelper.createDocument();
            Element root = document.addElement(rootName);
            root.addAttribute(attributeName, attributeVaule);
        } catch (Exception e) {
            throw new RuntimeException(e + "->创建的【" + rootName + "】根节点出现错误");
        }
        return document;
    }

    /**
     * 鍒犻櫎xml鏂囦欢鑺傜偣
     */
    @SuppressWarnings("unchecked")
    public static Document deleteElementAddressByName(Document document, String elementName) {
        Element root = document.getRootElement();
        Iterator<Object> iterator = root.elementIterator("address");
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            // 鏍规嵁灞炴�у悕鑾峰彇灞炴�у��
            Attribute attribute = element.attribute("name");
            if (attribute.getValue().equals(elementName)) {
                root.remove(element);
                document.setRootElement(root);
                break;
            }
        }
        return document;
    }
    
    /**
     *    鍒犻櫎灞炴�х瓑浜庢煇涓�肩殑鍏冪礌
     *    @param document  XML鏂囨。
     *    @param xpath xpath璺緞琛ㄨ揪寮�
     *    @param attrName 灞炴�у悕
     *    @param attrValue 灞炴�у��
     *    @return      
     */
    @SuppressWarnings("unchecked")
    public static Document deleteElementByAttribute(Document document, String xpath, String attrName, String attrValue) {
        Iterator<Object> iterator = document.selectNodes(xpath).iterator();
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            Element parentElement = element.getParent();
            // 鏍规嵁灞炴�у悕鑾峰彇灞炴�у��
            Attribute attribute = element.attribute(attrName);
            if (attribute.getValue().equals(attrValue)) {
                parentElement.remove(element);
            }
        }
        return document;
    }
    
    
    
    
    
    
    
    
	/* @author:zxj 2018-7-14
	 * @param filePath
	 * @param ElementPath
	 * @param name
	 * @param value
	 * @throws Exception
	 */
    //eg锛�"name:sampleCount","value:100",鍥哄畾鏍囩璺緞涓嬬殑鍏冪礌鐨刱ey鍊肩瓑浜巒ame=sampleCount锛屼慨鏀箆alue=1000
    //eg淇敼鍍忕礌
	//handal("C:/Users/xjtu4/Desktop/1.xml","scene/sensor/film/integer","name:height","value:10000");
	public static void changeAttribute(String filePath,String ElementPath,String name,String value) throws Exception{
		
	        SAXReader reader = new SAXReader();  
	        // 璁剧疆璇诲彇鏂囦欢鍐呭鐨勭紪鐮�  
	        reader.setEncoding("GBK");  
	        Document doc = reader.read(filePath);  
	  
	  
	        // 淇敼鍐呭涔嬩竴: 濡傛灉book鑺傜偣涓璼how灞炴�х殑鍐呭涓簓es,鍒欎慨鏀规垚no  
	        // 鍏堢敤xpath鏌ユ壘瀵硅薄  
	        // 鏍规嵁璇曠敤锛屾牴鑺傜偣books鐨剎path璺緞瑕佸姞/鎴栦笉鍔犻兘鍙互銆�  
	        List<Attribute> attrList = doc.selectNodes(ElementPath);  
	        Iterator<Attribute> i = attrList.iterator();  
	        while (i.hasNext())  
	        {  
	           Element attribute = (Element) i.next(); 
	           String eleName=attribute.attributeValue(name.split(":")[0]);
	           if(eleName!=null&&eleName.equals(name.split(":")[1])){
	        	   System.out.println("value is"+attribute.attributeValue(value.split(":")[0]));
	        	   attribute.setAttributeValue(value.split(":")[0], value.split(":")[1]);
	           }
	        }
	        OutputFormat format = OutputFormat.createPrettyPrint();  
	        // 鍒╃敤鏍煎紡鍖栫被瀵圭紪鐮佽繘琛岃缃�  
	        format.setEncoding("GBK");  
	        String newFilePath=filePath.substring(0,filePath.lastIndexOf("/"));
	        String FileName=filePath.substring(filePath.lastIndexOf("/")+1,filePath.length());
	        String[]tmp=FileName.split("\\.");
	        FileOutputStream output=null;
	        if(tmp[0].contains("_pre"))
	        	 output = new FileOutputStream(new File(filePath)); 
	        else{
	        String newFileName=tmp[0]+"_pre."+tmp[1];
	      Log.info("new fileName is"+newFileName);
	       output = new FileOutputStream(new File(newFilePath+"/"+newFileName)); 
	        }
	        XMLWriter writer = new XMLWriter(output, format);  
	        writer.write(doc);  
	        writer.flush();  
	        writer.close();  
	    }  
		
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}