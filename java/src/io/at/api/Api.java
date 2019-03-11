package io.at.api;


import io.at.api.cache.CurrencyCache;
import io.at.api.cache.MarketCache;
import io.at.api.constans.GlobalConstant;
import io.at.api.untils.HttpUtils;
import io.at.api.untils.SignUtils;
import io.at.api.untils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther:
 * @Date: 2018/7/23 13:50
 * @Description:
 */
public class Api {

    /**
     * 获取用户信息
     *
     * @return
     */
    public static String getUserInfo() {

        Map<String, String> header;
        header = SignUtils.getHeaderOfNoParams(GlobalConstant.API_ID, GlobalConstant.API_SECRET);

        try {
            String returnMsg = HttpUtils.doPost(GlobalConstant.ES_HOST, GlobalConstant.API_GET_USER_INFO, null, header, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMarketList() {
        Map<String, String> headers = SignUtils.getHeaderOfNoParams(GlobalConstant.API_ID, GlobalConstant.API_SECRET);
        try {
            String resJson = HttpUtils.doPost(GlobalConstant.ES_HOST, GlobalConstant.API_MARKET_GET_BY_WEBID,
                    null, headers, GlobalConstant.TIMEOUT_SECONDS_HTTP);
            return resJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取资金列表
     *
     * @return
     */
    public static String getCurrencyList() {
        try {
            Map<String, String> headers = SignUtils.getHeaderOfNoParams(GlobalConstant.API_ID, GlobalConstant.API_SECRET);
            String returnMsg = HttpUtils.doPost(GlobalConstant.ES_HOST, GlobalConstant.API_GET_CURRENCY_LIST, null, headers, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 委托下单
     *
     * @param rangeType  委托类型 0 为限价委托 1 区间委托，目前暂时只支持限价委托
     * @param type       买卖类型：0 卖出 1 购买
     * @param marketName 市场名
     * @param amount     数量，比如1个买入或卖出一个btc，传入1
     * @param price      成交价格
     * @return
     */
    public static String addEntrust(String marketName, String amount, String price, Integer rangeType, Integer type) {

        String marketId = MarketCache.getMarketIdByName(marketName);

        Map<String, Object> bodyEntrustMap = new HashMap();
        bodyEntrustMap.put("marketId", marketId);
        bodyEntrustMap.put("amount", amount);
        bodyEntrustMap.put("price", price);
        bodyEntrustMap.put("rangeType", rangeType);//目前只有限价委托，rangType默认为0
        bodyEntrustMap.put("type", type);
        Map<String, String> header;
        header = SignUtils.getHeaderOfBodyJson(GlobalConstant.API_ID, GlobalConstant.API_SECRET, bodyEntrustMap);

        try {
            String returnMsg = HttpUtils.doPost(GlobalConstant.ES_HOST, GlobalConstant.API_ADD_ENTRUST, bodyEntrustMap, header, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 取消委托单
     *
     * @return
     */
    public static String cancelEntrust(String marketName, String entrustId) {

        String marketId = MarketCache.getMarketIdByName(marketName);
        Map<String, Object> entrustMap = new HashMap<String, Object>();
        entrustMap.put("marketId", marketId);
        entrustMap.put("entrustId", entrustId);
        Map<String, String> header;
        header = SignUtils.getHeaderOfBodyJson(GlobalConstant.API_ID, GlobalConstant.API_SECRET, entrustMap);
        try {
            String returnMsg = HttpUtils.doPost(GlobalConstant.ES_HOST, GlobalConstant.API_CANCEL_ENTRUST, entrustMap, header, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 根据委托ID获取委托单信息
     *
     * @return
     */
    public static String getEntrustById(String marketName, String entrustId) {

        String marketId = MarketCache.getMarketIdByName(marketName);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("marketId", marketId);
        paramMap.put("entrustId", entrustId);
        Map<String, String> header;
        header = SignUtils.getHeaderOfKeyValue(GlobalConstant.API_ID, GlobalConstant.API_SECRET, paramMap);

        try {
            String returnMsg = HttpUtils.doGet(GlobalConstant.ES_HOST, GlobalConstant.API_USER_ENTRUST_BY_ID, paramMap, header, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 分页从缓存中获取用户在某市场还未成交的委托记录（新版接口）
     *
     * @param marketName
     * @return
     */
    public static String getUserEntrustRecordFromCacheWithPage(String marketName, int pageIndex, int pageSize) {
        String marketId = MarketCache.getMarketIdByName(marketName);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("marketId", marketId);
        paramMap.put("pageSize", pageSize);
        paramMap.put("pageIndex", pageIndex);//页码，从1开始计算

        Map<String, String> header;
        header = SignUtils.getHeaderOfKeyValue(GlobalConstant.API_ID, GlobalConstant.API_SECRET, paramMap);

        try {
            String returnMsg = HttpUtils.doGet(GlobalConstant.ES_HOST, GlobalConstant.API_USER_FROM_CACHE_RECORD_WITH_PAGE, paramMap, header, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从缓存中获取用户在某市场还未成交的委托记录(旧版接口)，无分页，最多只能获取到20条
     *
     * @param marketName
     * @return
     */
    public static String getUserEntrustRecordFromCache(String marketName) {
        String marketId = MarketCache.getMarketIdByName(marketName);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("marketId", marketId);

        Map<String, String> header;
        header = SignUtils.getHeaderOfKeyValue(GlobalConstant.API_ID, GlobalConstant.API_SECRET, paramMap);

        try {
            String returnMsg = HttpUtils.doGet(GlobalConstant.ES_HOST, GlobalConstant.API_USER_FROM_CACHE_RECORD, paramMap, header, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 分页查询用户已经成交的委托记录
     *
     * @param marketName
     * @param paegSize
     * @param pageIndex
     * @param type      （可选）委托类型，0 卖出 1 购买  -1 取消
     * @param status     （可选）状态 : -2资金解冻失败 -1用户资金不足 0起始 1取消 2交易成功 3交易一部
     * @param startDateTime   （可选）委托下单的起始时间，13位时间戳
     * @param endDateTime     （可选）委托下单的结束时间，13位时间戳
     * @return
     */
    public static String getUserEntrustList(String marketName,Integer pageIndex, Integer paegSize,Integer type,
                                            Integer status,Long startDateTime,Long endDateTime) {

        String marketId = MarketCache.getMarketIdByName(marketName);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("marketId", marketId);
        paramMap.put("pageIndex", pageIndex);
        paramMap.put("pageSize", paegSize);
        if(type != null){
            paramMap.put("type",type);
        }

        if(status != null){
            paramMap.put("status",status);
        }

        if(startDateTime != null){
            paramMap.put("startDateTime",startDateTime);
        }

        if(endDateTime != null){
            paramMap.put("endDateTime",endDateTime);
        }

        Map<String, String> header;
        header = SignUtils.getHeaderOfKeyValue(GlobalConstant.API_ID, GlobalConstant.API_SECRET, paramMap);
        try {
            String returnMsg = HttpUtils.doGet(GlobalConstant.ES_HOST, GlobalConstant.API_GET_USER_ENTRUST_LIST, paramMap, header, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取充币地址
     *
     * @param currencyTypeName
     * @return
     */
    public static String getPayinAddress(String currencyTypeName) {

        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("currencyTypeName", currencyTypeName);

        Map<String, String> header;
        header = SignUtils.getHeaderOfBodyJson(GlobalConstant.API_ID, GlobalConstant.API_SECRET, paramMap);
        try {
            String returnMsg = HttpUtils.doPost(GlobalConstant.ES_HOST, GlobalConstant.API_PAYIN_ADDRESS, paramMap, header, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询充币记录
     *
     * @param currencyTypeName
     * @param paegNum
     * @param pageSize
     * @return
     */
    public static String getPayinCoinRecord(String currencyTypeName, Integer paegNum, Integer pageSize) {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("currencyTypeName", currencyTypeName);
        paramMap.put("paegNum", paegNum);
        paramMap.put("pageSize", pageSize);

        Map<String, String> header;
        header = SignUtils.getHeaderOfBodyJson(GlobalConstant.API_ID, GlobalConstant.API_SECRET, paramMap);
        try {
            String returnMsg = HttpUtils.doPost(GlobalConstant.ES_HOST, GlobalConstant.API_PAYIN_CION_RECORD, paramMap, header, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询提币地址
     *
     * @param currencyName
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static String getWithdrawaddress(String currencyName, Integer pageNum, Integer pageSize) throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String currencyId = CurrencyCache.getCurrencyId(currencyName);
        if (StringUtils.isNullOrEmpty(currencyId)) {
            throw new Exception("传入的币种不存在");
        }

        paramMap.put("currencyId", currencyId);
        paramMap.put("pageNum", pageNum);
        paramMap.put("pageSize", pageSize);

        Map<String, String> header;
        header = SignUtils.getHeaderOfKeyValue(GlobalConstant.API_ID, GlobalConstant.API_SECRET, paramMap);
        System.out.println(header);
        try {
            String returnMsg = HttpUtils.doGet(GlobalConstant.ES_HOST, GlobalConstant.API_FUND_WITHDRAW_ADDRESS, paramMap, header, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询提币记录
     *
     * @param currencyName
     * @param tal          all(所有), wait(已提交，未审核), success(审核通过), fail(审核失败), cancel(用户主动取消)
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static String getpayoutcoinrecord(String currencyName, String tal, Integer pageNum, Integer pageSize) throws Exception {
        String currencyId = CurrencyCache.getCurrencyId(currencyName);
        if (StringUtils.isNullOrEmpty(currencyId)) {
            throw new Exception("传入的币种不存在");
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("currencyId", currencyId);
        paramMap.put("tal", tal);
        paramMap.put("pageNum", pageNum);
        paramMap.put("pageSize", pageSize);

        Map<String, String> header;
        header = SignUtils.getHeaderOfKeyValue(GlobalConstant.API_ID, GlobalConstant.API_SECRET, paramMap);

        try {
            String returnMsg = HttpUtils.doGet(GlobalConstant.ES_HOST, GlobalConstant.API_PAYOUT_CION_RECORD, paramMap, header, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取系统资金列表，分页返回
     *
     * @param pageSize
     * @param pageNum
     * @return
     */
    public static String fundFindbypage(Integer pageNum, Integer pageSize) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pageSize", pageSize);
        paramMap.put("pageNum", pageNum);


        Map<String, String> header;
        header = SignUtils.getHeaderOfBodyJson(GlobalConstant.API_ID, GlobalConstant.API_SECRET, paramMap);

        try {
            String returnMsg = HttpUtils.doPost(GlobalConstant.ES_HOST, GlobalConstant.API_FUND_FIND_BY_PAGE, paramMap, header, 10);
            return returnMsg;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取所有市场行情
     *
     * @param isUseMarketName
     * @return
     */
    public static String getTickers(boolean isUseMarketName) {
        Map<String, Object> params = new HashMap<>();
        params.put("isUseMarketName", true);

        try {
            String result;
            result = HttpUtils.doGet(GlobalConstant.KLINE_HTTP_HOST, GlobalConstant.API_GET_TICKERS, params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取单个市场行情
     *
     * @param marketId   市场id，marketId和marketName传一个即可，传id则返回结果也是使用marketid作为标识，传marketName则返回结果用marketName作为标识
     * @param marketName 市场名
     * @return
     */
    public static String getTicker(String marketId, String marketName) {
        Map<String, Object> params = new HashMap<>();
        if (marketId != null) {
            params.put("marketId", marketId);
        } else {
            params.put("marketName", marketName);
        }
        try {
            String result = HttpUtils.doGet(GlobalConstant.KLINE_HTTP_HOST, GlobalConstant.API_GET_TICKER, params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取k线列表
     *
     * @param marketId   市场id，marketId和marketName传一个即可
     * @param marketName 市场名
     * @param dataSize   数据长度，返回k线列表的长度，最大为100
     * @param type       k线类型，包含1M、5M、15M、30M、1H、1D、1W，分别是1分、5分、15分、30分、1时、1天、1周
     * @return
     */
    public static String getKlines(String marketId, String marketName, int dataSize, String type) {
        Map<String, Object> params = new HashMap<>();
        if (marketId != null) {
            params.put("marketId", marketId);
        } else {
            params.put("marketName", marketName);
        }
        params.put("dataSize", dataSize);
        params.put("type", type);
        try {
            String result = HttpUtils.doGet(GlobalConstant.KLINE_HTTP_HOST, GlobalConstant.API_GET_KILNES, params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取成交列表
     *
     * @param marketId   市场id，marketId和marketName传一个即可
     * @param marketName 市场名
     * @param dataSize   数据长度，最大为20
     * @return
     */
    public static String getTrades(String marketId, String marketName, int dataSize) {
        Map<String, Object> params = new HashMap<>();
        if (marketId != null) {
            params.put("marketId", marketId);
        } else {
            params.put("marketName", marketName);
        }
        params.put("dataSize", dataSize);
        try {
            String result = HttpUtils.doGet(GlobalConstant.KLINE_HTTP_HOST, GlobalConstant.API_GET_TRADES, params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取市场盘口列表
     *
     * @param marketId   市场id，marketId和marketName传一个即可
     * @param marketName 市场名
     * @param dataSize   数据长度，最大为50
     * @return
     */
    public static String getEntrusts(String marketId, String marketName, int dataSize) {
        Map<String, Object> params = new HashMap<>();
        if (marketId != null) {
            params.put("marketId", marketId);
        } else {
            params.put("marketName", marketName);
        }
        params.put("dataSize", dataSize);

        try {
            String result = HttpUtils.doGet(GlobalConstant.KLINE_HTTP_HOST, GlobalConstant.API_GET_ENTRUSTS, params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
