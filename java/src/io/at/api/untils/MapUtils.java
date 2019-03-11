package io.at.api.untils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapUtils {


    public static void clearMap(Map<String,Object> map){
        clearMap(map,false);
    }

    /**
     *
     * @param map map对象
     * @param isClearEmpty 值为空字符串(如"")是否清理
     */
    public static void clearMap(Map<String,Object> map,boolean isClearEmpty){

        Iterator<Map.Entry<String,Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String,Object> entry = entries.next();
            if(entry.getValue() == null ||
                    (isClearEmpty && entry.getValue() instanceof String && entry.getValue().toString().trim().isEmpty()) ){
                entries.remove();
            } else if(entry.getValue().getClass() == HashMap.class){//处理嵌套问题
                clearMap((Map)entry.getValue(),isClearEmpty);
            } else if(entry.getValue() instanceof Collection || entry.getValue().getClass().isArray()){
                clearMapItem(entry.getValue(),isClearEmpty);
            }
        }
    }

    public static void clearMapItem(Object object,boolean isClearEmpty){
        if(object instanceof Collection ){
            Object[] values = ((Collection)object).toArray(new Object[0]);
            for(Object value:values){
                Class clazz = value.getClass();
                if(value instanceof Collection || clazz.isArray()){
                    clearMapItem(value,isClearEmpty);
                }else if(value instanceof Map){
                    clearMap((Map)value,isClearEmpty);
                }
            }

        } else if (object.getClass().isArray()){
            for(int i = 0; i < Array.getLength(object); ++i) {
                Object value = Array.get(object, i);
                if(value instanceof Collection || value.getClass().isArray()){
                    clearMapItem(value,isClearEmpty);
                }else if(value instanceof Map){
                    clearMap((Map)value,isClearEmpty);
                }
            }
        }
    }




    public static boolean isNullOrEmpty(Map map){
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map map){
        return !isNullOrEmpty(map);
    }

    public static void main(String[] args){
        Map<String,Object> map1 = new HashMap<>();
        map1.put("1","1");
        map1.put("2",null);
        map1.put("3","");

        clearMap(map1,true);

        System.out.println(map1);

        map1 = new HashMap<>();
        map1.put("1","1");
        map1.put("2",null);
        map1.put("3","");
        clearMap(map1);
        System.out.println(map1);
    }
}
