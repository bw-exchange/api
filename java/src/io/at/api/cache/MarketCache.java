package io.at.api.cache;

import com.alibaba.fastjson.JSONPath;
import io.at.api.Api;
import io.at.api.untils.HttpResponseParser;
import io.at.api.untils.Logger;
import io.at.api.untils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:
 * @Description: 市场相关cache
 * @Date: Created in 2018/6/30 下午7:54.
 */
public class MarketCache {

    private static Map<String,String> MARKET_ID_MAP = new ConcurrentHashMap<String,String>();
    private static Map<String,String> MARKET_ID_TO_NAME_MAP = new ConcurrentHashMap<String,String>();

    //改成懒加载
    private static void init() {

        //1.是否有缓存
        String resJson = Api.getMarketList();
        //3.解析
        parseAndInit(resJson);
    }

    /***设置/获取market  name->id***/
    public static String getMarketIdByName(String name) {
        if(!MARKET_ID_MAP.containsKey(name)){
            init();
        }

        if(MARKET_ID_MAP.containsKey(name)){
            return MARKET_ID_MAP.get(name).toString();
        }
        return null;
    }

    /**设置/获取market  id->name**/
    public static String getMarketNameById(String marketId) throws Exception {
        if(!MARKET_ID_TO_NAME_MAP.containsKey(marketId)){
            init();
        }
        if(MARKET_ID_TO_NAME_MAP.containsKey(marketId)){
            return MARKET_ID_TO_NAME_MAP.get(marketId).toString();
        }
        return null;
    }

    private static void setMarkeId(String name,String id){
        if(!StringUtils.isNullOrEmpty(id)){
            MARKET_ID_MAP.put(name,id);
            MARKET_ID_TO_NAME_MAP.put(id,name);
        }
    }

    private static void parseAndInit(String jsonResult) {
        //1.判断状态码
        if(!HttpResponseParser.isSucc(jsonResult)){
            //错误码处理-该接口不返回错误码  TODO
            Logger.error("初始化市场请求接口失败:"+jsonResult);
        }

        //2.获取datas
        List<Map> datas = (List<Map>) JSONPath.read(jsonResult,"datas");
        if(datas == null || datas.isEmpty()){
            return;
        }

        //3.转换
        for(Map data:datas){
            //3.2 设置map market name->id
            MarketCache.setMarkeId(data.get("name").toString().toUpperCase(), data.get("marketId").toString().toLowerCase());
        }

        return;
    }
}
