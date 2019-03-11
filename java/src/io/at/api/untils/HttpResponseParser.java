package io.at.api.untils;


import com.alibaba.fastjson.JSON;
import io.at.api.enums.ErrorCodeEnum;

public class HttpResponseParser {

    /**
     * 判断是否成功
     * @param jsonStr
     * @return
     */
    public static boolean isSucc(String jsonStr){
        if(StringUtils.isNullOrEmpty(jsonStr)){
            return false;
        }
        ResponseEntity responseEntity = JSON.parseObject(jsonStr, ResponseEntity.class);
        return responseEntity.getResMsg().getCode().equals(ErrorCodeEnum.OK.getCode());
    }

//    /**
//     * 获取 json中的返回值  返回类型为对象
//     * @param jsonStr
//     * @param c
//     * @param <T>
//     * @return
//     */
//    public static <T> T getData(String jsonStr,Class c){
//        try {
//            return (T) JSONPath.newInstance(jsonStr).value("datas",c);
//        } catch (ParseException e) {
//            Logger.error("getList error1:",e);
//        } catch (Exception e) {
//            Logger.error("getList error2:",e);
//        }
//        return null;
//    }
//
//    /**
//     * 获取 json中的返回值  返回类型为数组
//      * @param jsonStr
//     * @param c
//     * @param <T>
//     * @return
//     */
//    public static <T> List<T> getList(String jsonStr,Class c) {
//        try {
//            return JSONPath.newInstance(jsonStr).listObject("datas",c);
//        } catch (ParseException e) {
//            Logger.error("getList error1:",e);
//
//        } catch (Exception e) {
//            Logger.error("getList error1:",e);
//        }
//        return null;
//    }
}
