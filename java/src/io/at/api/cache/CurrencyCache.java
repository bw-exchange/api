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
 * @Description:
 * @Date: Created in 2018/6/30 下午11:35.
 */
public class CurrencyCache {

    /**
     * <currencyName currencyTypeId>
     */
    public static Map<String, String> CURRENCY = new ConcurrentHashMap<>();

    //改成懒加载的
    private static void init() {

        String returnMsg = Api.getCurrencyList();

        if (HttpResponseParser.isSucc(returnMsg)) {
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) JSONPath.read(returnMsg, "/datas");
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> data = dataList.get(i);
                String name = data.get("name").toString().toUpperCase();
                String id = data.get("currencyId").toString();
                CURRENCY.put(name, id);
            }
        } else {  //错误处理
            Logger.error("加载币种信息错误：" + returnMsg);
        }
    }

    public static String getCurrencyId(String name) {
        if (CURRENCY.isEmpty()) {
            init();
            Logger.info("币种列表未初始化或初始化失败，请先初始化");
        }

        String currencyId = CURRENCY.get(name);
        if (!StringUtils.isNullOrEmpty(currencyId)) {
            return currencyId;
        }

        //未找到，尝试重新读取一次最新的货币信息
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        //如果本次还没找到就直接返回-1
        return CURRENCY.get(name) == null ? "-1" : CURRENCY.get(name);
    }
}
