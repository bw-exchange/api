package io.at.api.untils;

import com.alibaba.fastjson.JSON;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author:
 * @Description:
 * @Date: Created in 2018/6/26  下午3:48
 * @Modified By:
 */
public class SignUtils {
    private static Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    /**
     * 自行根据需要设置,一般认为一个服务里使用的只有一个
     */
    public static String ID_NAME = "Apiid";

    /**
     * 参数是formdata key value，或者get参数 形式的情况下获取签名header
     *
     * @param parameters
     * @return
     */
    public static Map getHeaderOfKeyValue(String id, String secret,Map<String, Object> parameters) {
        long time = System.currentTimeMillis();
        Map header = new HashMap();
        StringBuffer contentSb = new StringBuffer();
        parameters = new TreeMap<>(parameters);
        Iterator it = parameters.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            contentSb.append(entry.getKey()).append(entry.getValue());
        }
//        if (parameters != null) {
//            parameters.entrySet().stream()
//                    .filter(a -> a != null && (!isEmpty(a.getKey()) || !isEmpty((String) a.getValue()))).
//                    sorted(Map.Entry.<String, Object>comparingByKey()).forEachOrdered(e -> contentSb.append(e.getKey() + e.getValue()));
//        }

        header.put(ID_NAME,id);
        header.put("Timestamp", String.valueOf(time));
        header.put("Sign", encryptMD5(id + time + contentSb.toString() + secret));
        return header;

    }

    /**
     * 参数为空情况下获取签名header
     *
     * @return
     */
    public static Map getHeaderOfNoParams(String id, String secret) {
        Map header = new HashMap();
        long time = System.currentTimeMillis();
        header.put(ID_NAME, id);
        header.put("Timestamp", String.valueOf(time));
        header.put("Sign", encryptMD5(id + time + secret));
        return header;

    }

    /**
     * 参数是body json形式的情况下获取签名header
     *
     * @param object
     * @return
     */
    public static Map getHeaderOfBodyJson(String id, String secret, Object object) {
        Map header = new HashMap();
        long time = System.currentTimeMillis();
        String params = JSON.toJSONString(object);
        header.put(ID_NAME, id);
        header.put("Timestamp", String.valueOf(time));
        header.put("Sign", encryptMD5(id + time + params + secret));
        return header;
    }

    public static String encryptMD5(String str) {
        return digest("MD5", str);
    }

    public static String digest(String code, String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(code);
            messageDigest.reset();
            messageDigest.update(str.getBytes(DEFAULT_CHARSET));
            byte[] byteArray = messageDigest.digest();
            StringBuffer md5StrBuff = new StringBuffer();

            for(int i = 0; i < byteArray.length; ++i) {
                if (Integer.toHexString(255 & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
                }
            }

            return md5StrBuff.toString();
        } catch (NoSuchAlgorithmException var6) {
            var6.printStackTrace();
            return null;
        }
    }
}
