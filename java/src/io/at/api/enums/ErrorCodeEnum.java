package io.at.api.enums;


public enum ErrorCodeEnum {

    OK(ExceptionLevelEnum.DEBUG,"1", "success"),
    FAIL(ExceptionLevelEnum.ERROR,"1000", "fail"),
    NOT_LOGIN(ExceptionLevelEnum.INFO,"1001", "not login"),
    PARAMETER_ERROR(ExceptionLevelEnum.ERROR,"1002", "parameter error"),
    INVALID_SIGN(ExceptionLevelEnum.ERROR,"1003", "invalid sign") ;



    private String code;
    private String message ;

    private ExceptionLevelEnum level;

    ErrorCodeEnum(String code, String message) {
        this.message = message;
        this.code = code;
        this.level = ExceptionLevelEnum.WARN;
    }



    ErrorCodeEnum(ExceptionLevelEnum level, String code, String message) {
        this.message = message;
        this.code = code;
        this.level = level;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ExceptionLevelEnum getLevel() {
        return level;
    }

    public void setLevel(ExceptionLevelEnum level) {
        this.level = level;
    }
}