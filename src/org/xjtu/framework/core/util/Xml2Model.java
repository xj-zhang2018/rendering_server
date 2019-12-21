package org.xjtu.framework.core.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Xml2Model<T> {
 
	@SuppressWarnings("unchecked")   
    public List<T> readXML(String XMLPathAndName, T t) {   
        long lasting = System.currentTimeMillis();//鏁堢巼妫�娴�   
        List<T> list = new ArrayList<T>();//鍒涘缓list闆嗗悎   
        try {   
            File f = new File(XMLPathAndName);//璇诲彇鏂囦欢   
            SAXReader reader = new SAXReader();   
            Document doc = reader.read(f);//dom4j璇诲彇   
            Element root = doc.getRootElement();//鑾峰緱鏍硅妭鐐�   
            Element foo;//浜岀骇鑺傜偣   
            Field[] properties = t.getClass().getDeclaredFields();//鑾峰緱瀹炰緥鐨勫睘鎬�   
            //瀹炰緥鐨刧et鏂规硶   
            Method getmeth;   
            //瀹炰緥鐨剆et鏂规硶   
            Method setmeth;   
               
            for (Iterator i = root.elementIterator(t.getClass().getSimpleName()); i.hasNext();) {//閬嶅巻t.getClass().getSimpleName()鑺傜偣   
                foo = (Element) i.next();//涓嬩竴涓簩绾ц妭鐐�   
               t=(T)t.getClass().newInstance();//鑾峰緱瀵硅薄鐨勬柊鐨勫疄渚�   
               for (int j = 0; j < properties.length; j++) {//閬嶅巻鎵�鏈夊瓩瀛愯妭鐐�   
                    //瀹炰緥鐨剆et鏂规硶   
                      setmeth = t.getClass().getMethod(   
                            "set"  
                                    + properties[j].getName().substring(0, 1)   
                                            .toUpperCase()   
                                    + properties[j].getName().substring(1),properties[j].getType());   
                  //properties[j].getType()涓簊et鏂规硶鍏ュ彛鍙傛暟鐨勫弬鏁扮被鍨�(Class绫诲瀷)   
                    setmeth.invoke(t, foo.elementText(properties[j].getName()));//灏嗗搴旇妭鐐圭殑鍊煎瓨鍏�   
                    
                    System.out.println("t is "+properties[j].getName()); 
           
                }   
       
                list.add(t);   
            }   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
        long lasting2 = System.currentTimeMillis();   
        System.out.println("读取xml结束，用时"+(lasting2 - lasting)+"ms");   
        return list;   
    }
}
