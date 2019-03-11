package io.at.api.untils;

/**
 * @Author:
 * @Description:
 * @Date: Created in 2018/8/20  下午7:27
 * @Modified By:
 */
public class ResMsg {
    //响应消息
    private String message;

    //请求方法
    private String method;

    //错误码
    private String code;



    public ResMsg() {
    }

    public ResMsg(String message, String method, String code) {
        this.message = message;
        this.method = method;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
