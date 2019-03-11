package io.at.api.untils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

/**
 * @Author: cai.zhenghao
 * @Description:
 * @Date: Created in 2018/8/28  下午5:50
 * @Modified By:
 */
public class WsCustomClient extends WebSocketClient {
    public static final String ENTRUST_SUBSCIBE_STR = "{\"dataType\":\"%s_ENTRUST_ADD_%s\",\"dataSize\":50,\"action\":\"ADD\"}";
    public static final String KLINE_SUBSCIBE_STR = "{\"dataType\":\"%s_KLINE_%s_%s\",\"dataSize\":500,\"action\":\"ADD\"}";
    public static final String TRADE_SUBSCIBE_STR = "{\"dataType\":\"%s_TRADE_%s\",\"dataSize\":20,\"action\":\"ADD\"}";
    public static final String MARKET_STA_SUBSCIBE_STR = "{\"dataType\":\"%s_TRADE_STATISTIC_24H\",\"dataSize\":1,\"action\":\"ADD\"}";
    public static final String ALL_STA_SUBSCIBE_STR = "{\"dataType\":\"ALL_TRADE_STATISTIC_24H\",\"dataSize\":1,\"action\":\"ADD\"}";

    private String marketName;
    private String marketId;


    public WsCustomClient(URI serverUri, Map<String, String> httpHeaders, String marketId, String marketName) {
        super(serverUri, httpHeaders);
        this.marketName = marketName;
        this.marketId = marketId;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Logger.info(getClass().getSimpleName() + "onOpen");
        subscribe();
    }

    public void subscribe() {
        //TODO:一般而言，API用户只需要用到entrust类型的数据，其他三类数据如果需要请自行组合代码
//        订阅盘口数据，其他的数据请参考修改
        this.send(String.format(ENTRUST_SUBSCIBE_STR, marketId, marketName));
////          订阅k线数据,1小时线
//        this.send(String.format(KLINE_SUBSCIBE_STR, marketId, "1H", marketName));
////          订阅交易记录数据
//        this.send(String.format(TRADE_SUBSCIBE_STR, marketId, marketName));
        ////          订阅单个市场的行情数据
//        this.send(String.format(MARKET_STA_SUBSCIBE_STR, marketId));
////          订阅所有市场的行情数据
//        this.send(String.format(ALL_STA_SUBSCIBE_STR));
    }


    @Override
    public void onMessage(String s) {
        Logger.info("接收到websocket消息:\n" + s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        Logger.info(getClass().getSimpleName() + "onClose" + i + s + b);
    }

    @Override
    public void onError(Exception e) {
        Logger.info(getClass().getSimpleName() + "onError" + e.getMessage());
    }
}
