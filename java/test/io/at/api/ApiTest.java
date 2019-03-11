package io.at.api;

import io.at.api.cache.MarketCache;
import io.at.api.constans.GlobalConstant;
import io.at.api.untils.HttpResponseParser;
import io.at.api.untils.Logger;
import io.at.api.untils.StringUtils;
import io.at.api.untils.WsCustomClient;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: caizhenghao
 * @Date: 2018/8/20 21:57
 * @Description: 这里提供所有接口的测试用例，测试用例就是接口的使用示例
 */
public class ApiTest {
    //TODO:自行设置市场名字
    private String TEST_MARKET_NAME = "ETH_USDT";
    //TODO:自行设置币种名字
    private String TEST_CURRENCY_NAME = "ETH";

    static {
        //TODO:域名请根据文档提供的域名自行设置
        GlobalConstant.ES_HOST = "https://www.BW.com";
        GlobalConstant.KLINE_WS_HOST = "wss://kline.bw.com";
        GlobalConstant.KLINE_HTTP_HOST = GlobalConstant.KLINE_WS_HOST.replaceFirst("wss", "https")
                .replaceFirst("ws", "http");

        //TODO:API_ID和API_SECRET请自行设置
        //测试环境id
        GlobalConstant.API_ID = "7eOiWtBG56e7eOiWtBG56f";
        GlobalConstant.API_SECRET = "91ab5ef791cfe92fdb849869ab011cee";
    }

    @Test
    public void getMarketList() throws Exception {
        String result = Api.getMarketList();
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getCurrencyList() {
        String result = Api.getCurrencyList();
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getUserInfo() {
        String result = Api.getUserInfo();
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void addEntrust() throws Exception {
        String amount = "1.1";
        String price = "5.05";
        //TODO:测试此接口时注意先调好价格和数量，避免失误下单  ，参数type买卖类型：0 卖出 1 购买
//        String result = Api.addEntrust(TEST_MARKET_NAME, amount, price, 0, 0);
//        printfResult(result);
//        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void cancelEntrust() throws Exception {
        String entrustId = "EXXXXXX";//TODO:示例id
        String result = Api.cancelEntrust(TEST_MARKET_NAME, entrustId);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getEntrustById() throws Exception {
        String entrustId = "E6437955999037923328";//TODO:示例id
        String result = Api.getEntrustById(TEST_MARKET_NAME, entrustId);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getUserEntrustRecordFromCacheWithPage() throws Exception {
        String result = Api.getUserEntrustRecordFromCacheWithPage(TEST_MARKET_NAME, 1, 5);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getUserEntrustRecordFromCache() throws Exception {
        String result = Api.getUserEntrustRecordFromCache(TEST_MARKET_NAME);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getUserEntrustList() throws Exception {
        Integer type = null;
        Integer status = null;
        Long startDateTime = null;
        Long endDateTime = null;

        //可选参数自行选择
//        type = 0;         //（可选）委托类型，0 卖出 1 购买  -1 取消
//        status = 1;       //（可选）状态 : -2资金解冻失败 -1用户资金不足 0起始 1取消 2交易成功 3交易一部
//        startDateTime = 1548225569699L;       //（可选）委托下单的起始时间，13位时间戳
//        endDateTime = 1548225569699L;         //（可选）委托下单的结束时间，13位时间戳
        String result = Api.getUserEntrustList(TEST_MARKET_NAME, 1, 10, type, status, startDateTime, endDateTime);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getPayinAddress() {
        String result = Api.getPayinAddress(TEST_CURRENCY_NAME);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getPayinCoinRecord() {
        String result = Api.getPayinCoinRecord(TEST_CURRENCY_NAME, 1, 999);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getpayoutcoinrecord() throws Exception {
        String result = Api.getpayoutcoinrecord(TEST_CURRENCY_NAME, "all", 1, 999);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void fundFindbypage() {
        String result = Api.fundFindbypage(1, 999);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getWithdrawaaddress() throws Exception {
        String result = Api.getWithdrawaddress(TEST_CURRENCY_NAME, 1, 10);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getTickers() {
        String result = Api.getTickers(true);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getTicker() {
        String result = Api.getTicker(null, TEST_MARKET_NAME);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getKlines() {
        String result = Api.getKlines(null, TEST_MARKET_NAME, 1, "1H");
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getTrades() {
        String result = Api.getTrades(null, TEST_MARKET_NAME, 5);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void getEntrusts() {
        String result = Api.getEntrusts(null, TEST_MARKET_NAME, 2);
        printfResult(result);
        Assert.assertTrue(HttpResponseParser.isSucc(result));
    }

    @Test
    public void testWebscoket() {
        String marketId = MarketCache.getMarketIdByName(TEST_MARKET_NAME);
        if (StringUtils.isNullOrEmpty(marketId)) {
            Logger.error("市场不存在");
            return;
        }
        try {
            Map<String, String> header = new HashMap<String, String>();
            header.put("User-Agent", "UserApi Http Client");
            URI uri = new URI(GlobalConstant.KLINE_WS_HOST + GlobalConstant.WEBSOCKET_PATH);
            WsCustomClient wsCustomClient = new WsCustomClient(uri, header, marketId, TEST_MARKET_NAME);
            wsCustomClient.connect();
            Thread.sleep(5000);
//            这里提供最简单的websocket保持连接实现，建议用户根据项目实际需要自行优化
//            while (true){
            int i = 1;
            while (i < 20) {
//                if(i%6 == 0){
//                    wsCustomClient.close();
//                }
                if (wsCustomClient.isClosed()) {
                    Logger.info("连接已断开，重新连接");
                    wsCustomClient.reconnect();
                }
                Thread.sleep(1000);
                i++;
            }
            //结束关闭
            wsCustomClient.close();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void printfResult(String result) {
        System.out.println("返回结果：" + result);
    }

}
