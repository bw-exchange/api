package io.at.api.untils;

import com.alibaba.fastjson.JSON;
import io.at.api.exception.GlobalException;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;


public class HttpUtils {
    //http  超时时间 单位秒
    public static Integer HTTP_TIME_OUT_SECONDS = 20;

    /**
     * @param hostUrl
     * @param location
     * @param params
     * @param headers
     * @param timeOut
     * @param retryTimes
     * @return
     * @throws Exception
     */
    public static String doGet(String hostUrl, String location, Map<String, Object> params, Map<String, String> headers,
                               Integer timeOut, int retryTimes) throws Exception {
        for (int i = 1; i <= retryTimes; i++) {
            try {
                return doGet(hostUrl, location, params, headers, timeOut);
            } catch (Exception e) {
                Logger.error("==>请求异常，hostUrl: " + hostUrl + " => location: " + location + "=>重试! i: " + i, e);
                if (i == retryTimes) {
                    throw e;
                }
            }
        }
        throw new RuntimeException("请求超时");
    }

    /**
     * get方式请求
     *
     * @return
     */
    public static String doGet(String hostUrl, String location, Map<String, Object> params, Map<String, String> headers,
                               Integer timeOut) throws IOException {
        if (StringUtils.isNullOrEmpty(hostUrl)) {
            GlobalException.error("hostUrl cannot be null!");
        }
        if (timeOut == null) {
            timeOut = HTTP_TIME_OUT_SECONDS;
        }
        CloseableHttpClient httpClient = getHttpClient();

        try {
            //创建HttpGet
            String url = buildGetUrl(hostUrl, location, params);
            HttpGet httpGet = new HttpGet(url);

            //设置超时
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeOut*1000/4).setConnectionRequestTimeout(timeOut*1000/4)
                    .setSocketTimeout(timeOut*1000/2).build();
            httpGet.setConfig(requestConfig);

            addHeaders(httpGet, headers);
            //执行get请求
            Logger.info("请求连接：" + url +",请求头:" + JSON.toJSONString(headers));
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String jsonStr = EntityUtils.toString(entity);
            response.close();
            return jsonStr;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
        }
    }

    /**
     * get方式请求
     *
     * @return
     */
    public static String doGet(String hostUrl, String location, Map<String, Object> params, Map<String, String> headers) throws IOException {
        return doGet(hostUrl, location, params, headers, HTTP_TIME_OUT_SECONDS);
    }


    /**
     * @param host
     * @param port
     * @param relativeUrl
     * @param params
     * @return
     */
    public static String doGet(String host, Integer port, String relativeUrl, Map<String, Object> params) throws IOException {
        String absUrl = buildUrl(host, port);
        return doGet(absUrl, relativeUrl, params, null, null);
    }


    /**
     * get方式请求
     *
     * @return
     */
    public static String doGet(String hostUrl, String location, Map<String, Object> params) throws IOException {
        return doGet(hostUrl, location, params, null, HTTP_TIME_OUT_SECONDS);
    }

    /**
     * @param hostUrl
     * @param location
     * @param data
     * @param headers
     * @param timeOut
     * @param retryTimes
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> String doPost(String hostUrl, String location, T data, Map<String, String> headers, Integer
            timeOut, int retryTimes) throws Exception {

        for (int i = 1; i <= retryTimes; i++) {
            try {
                return doPost(hostUrl, location, data, headers, timeOut);
            } catch (Exception e) {
                Logger.error("==>请求异常，hostUrl: " + hostUrl + " => location: " + location + "=>重试! i: " + i, e);
                if (i == retryTimes) {
                    throw e;
                }
            }
        }

        throw new RuntimeException("请求超时");
    }

    /**
     * @param hostUrl
     * @param location
     * @param data
     * @param headers
     * @param timeOut
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> String doPost(String hostUrl, String location, T data, Map<String, String> headers, Integer
            timeOut) throws Exception {

        if (StringUtils.isNullOrEmpty(hostUrl)) {
            GlobalException.error("hostUrl cannot be null!");
        }

        if (timeOut == null) {
            timeOut = HTTP_TIME_OUT_SECONDS;
        }
        CloseableHttpClient httpClient = getHttpClient();

        try {
            //创建HttpPost
            String url = buildUrl(hostUrl, location);
            HttpPost httpPost = new HttpPost(url);
            //设置超时
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeOut*1000/4).setConnectionRequestTimeout(timeOut*1000/4)
                    .setSocketTimeout(timeOut*1000/2).build();
            httpPost.setConfig(requestConfig);

            addHeaders(httpPost, headers);

            // 解决中文乱码问题
            StringEntity stringEntity = null;
            if (data != null) {
                String postData;
                if (isBasicType(data.getClass())) {
                    postData = data.toString();
                } else {
                    postData = JSON.toJSONString(data);
                }
                stringEntity = new StringEntity(postData, "UTF-8");
                stringEntity.setContentType("application/json;charset=UTF-8");
                httpPost.setEntity(stringEntity);
            }

            Logger.info("请求连接：" + url +",请求参数:"+JSON.toJSONString(data)+",请求头:"
                    + JSON.toJSONString(headers));
            //执行get请求
            CloseableHttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            String jsonStr = EntityUtils.toString(entity);
            response.close();
            httpClient.close();
            return jsonStr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
        }
    }

    /**
     * @param hostUrl
     * @param location
     * @param data
     * @param headers
     * @param <T>
     * @return
     * @throws Exception
     */
    @Deprecated
    public static <T> String doPost(String hostUrl, String location, T data, Map<String, String> headers) throws Exception {
        return doPost(hostUrl, location, data, headers, HTTP_TIME_OUT_SECONDS);
    }

    /**
     * @param host
     * @param port
     * @param relativeUrl
     * @param params
     * @return
     * @throws Exception
     */
    public static String doPost(String host, Integer port, String relativeUrl, Map<String, Object> params)
            throws Exception {
        String absUrl = buildUrl(host, port);
        return doPost(absUrl, relativeUrl, params);
    }

    /**
     * @param hostUrl
     * @param location
     * @param entity
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> String doPost(String hostUrl, String location, T entity)
            throws Exception {
        return doPost(hostUrl, location, entity, null, null);
    }

    /**
     * @param hostUrl
     * @param location
     * @return
     */
    public static String buildUrl(String hostUrl, String location) {
        if (StringUtils.isNullOrEmpty(location)) {
            return hostUrl;
        }
        return hostUrl + location;
    }

    /**
     * @param hostUrl
     * @param location
     * @return
     */
    public static String buildGetUrl(String hostUrl, String location, Map<String, Object> params) {
        String url = buildUrl(hostUrl, location);
        if (params != null && !params.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(url);
            stringBuilder.append("?");
            Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                if(stringBuilder.lastIndexOf("?")!=stringBuilder.length()-1){
                    stringBuilder.append("&");
                }
                Map.Entry<String, Object> data = iterator.next();
                stringBuilder.append(data.getKey());
                stringBuilder.append("=");
                stringBuilder.append(data.getValue().toString());
            }
            return stringBuilder.toString();
        }
        return url;
    }

    /**
     * @return
     */
    public static void addHeaders(HttpRequestBase httpRequest, Map<String, String> header) {
        httpRequest.addHeader("Accept-Encoding", "gzip");
        httpRequest.addHeader("Pragma", "no-cache");
        httpRequest.addHeader("User-Agent", "UserApi Http Client");
        httpRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//        httpRequest.addHeaders("Connection", "keep-alive");
        if (header != null && !header.isEmpty()) {
            Iterator<Map.Entry<String, String>> iterator = header.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> data = iterator.next();
                httpRequest.addHeader(data.getKey(), data.getValue());
            }
        }
    }

    /**
     * @param host
     * @param port
     * @return
     */
    public static String buildUrl(String host, Integer port) {
        return "http://" + host + ":" + port;
    }

    public static void main(String[] args) {
        try {
            //失败
            String result = doGet("https://www.zbg.com/exchange/config/controller/website/marketcontroller/getByWebId", null, null);
            Logger.error(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isBasicType(Class clazz) {
        return clazz == null || clazz.isPrimitive() || clazz.getName().startsWith("java.lang");
    }

    private static CloseableHttpClient getHttpClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        //指定信任密钥存储对象和连接套接字工厂
        try {
            SSLContext sslContext = SSLContexts.custom().useTLS().build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        }
        catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        //构建客户端
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }


    public static boolean wsTest(Class clazz) {
        CloseableHttpClient httpClient = getHttpClient();

        return true;
    }

}
