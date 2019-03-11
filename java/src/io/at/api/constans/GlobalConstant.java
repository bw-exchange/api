package io.at.api.constans;

/**
 * @Auther:
 * @Date: 2018/7/23 13:39
 * @Description:
 */
public class GlobalConstant {

    //http 超时时间
    public static int TIMEOUT_SECONDS_HTTP = 60;


    //TODO:域名地址请自行配置
    public static String ES_HOST = "";
    public static String KLINE_WS_HOST = "";
    public static String KLINE_HTTP_HOST = KLINE_WS_HOST.replaceFirst("wss","https")
            .replaceFirst("ws","http");

    //TODO:API_ID和API_SECRET请自行设置
    public static String API_ID = "";
    public static String API_SECRET= "";

    public static String WEBSOCKET_PATH = "/websocket";

    /**
     * url 获取用户信息
     */
    public static final String API_GET_USER_INFO ="/exchange/user/controller/website/usercontroller/getuserinfo";

    /**
     * url 查询market
     */
    public static final String API_MARKET_GET_BY_WEBID = "/exchange/config/controller/website/marketcontroller/getByWebId";

    /**
     * url 获取资金列表
     */
    public static final String API_GET_CURRENCY_LIST="/exchange/config/controller/website/currencycontroller/getCurrencyList";

    /**
     * url 新增委托
     */
    public static final String API_ADD_ENTRUST = "/exchange/entrust/controller/website/EntrustController/addEntrust";

    /**
     * url 取消委托
     */
    public static final String API_CANCEL_ENTRUST = "/exchange/entrust/controller/website/EntrustController/cancelEntrust";
    /**
     * url 根据委托单ID 查询委托单信息
     */
    public static final String API_USER_ENTRUST_BY_ID ="/exchange/entrust/controller/website/EntrustController/getEntrustById";
    /**
     * url 从缓存中获取用户还未成交的委托记录，新版，支持分页
     */
    public static final String API_USER_FROM_CACHE_RECORD="/exchange/entrust/controller/website/EntrustController/getUserEntrustRecordFromCache";
    /**
     * url 分页从缓存中获取用户还未成交的委托记录(旧版)，无分页，最多获取20条
     */
    public static final String API_USER_FROM_CACHE_RECORD_WITH_PAGE="/exchange/entrust/controller/website/EntrustController/getUserEntrustRecordFromCacheWithPage";
    /**
     * url 分页获取用户的委托记录
     */
    public static final String API_GET_USER_ENTRUST_LIST="/exchange/entrust/controller/website/EntrustController/getUserEntrustList";

    /**
     * url 获取充币地址
     */
    public static final String API_PAYIN_ADDRESS="/exchange/fund/controller/website/fundcontroller/getPayinAddress";
    /**
     * url 查询充币记录
     */
    public static final String API_PAYIN_CION_RECORD="/exchange/fund/controller/website/fundcontroller/getPayinCoinRecord";
    /**
     * url 查询提币记录
     */
    public static final String API_PAYOUT_CION_RECORD="/exchange/fund/controller/website/fundwebsitecontroller/getpayoutcoinrecord";
    /**
     * url 获取用户所有资金信息
     */
    public static final String API_FUND_FIND_BY_PAGE="/exchange/fund/controller/website/fundcontroller/findbypage";
    /**
     * url 查询提币地址
     */
    public static final String API_FUND_WITHDRAW_ADDRESS="/exchange/fund/controller/website/fundwebsitecontroller/getwithdrawaddress";


    /**
     * url 查询所有市场行情
     */
    public static final String API_GET_TICKERS="/api/data/v1/tickers";

    /**
     * url 查询单个市场行情
     */
    public static final String API_GET_TICKER="/api/data/v1/ticker";

    /**
     * url 查询k线列表
     */
    public static final String API_GET_KILNES="/api/data/v1/klines";

    /**
     * url 查询交易记录
     */
    public static final String API_GET_TRADES="/api/data/v1/trades";

    /**
     * url 获取盘口（市场深度）
     */
    public static final String API_GET_ENTRUSTS="/api/data/v1/entrusts";
}
