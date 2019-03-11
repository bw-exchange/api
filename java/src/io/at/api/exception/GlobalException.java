package io.at.api.exception;


import io.at.api.enums.ErrorCodeEnum;
import io.at.api.enums.ExceptionLevelEnum;

public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String             code;
    private String             msg;
    private String             details;
    private ExceptionLevelEnum level;

    private void init(ErrorCodeEnum errorCode) {
        this.code = errorCode.getCode();
        this.msg = errorCode.getMessage();
        this.level = errorCode.getLevel();
    }

    @Override
    public String getMessage() {
        String errMsg = super.getMessage();
        StringBuffer message = new StringBuffer();
        if (this.level != null)
            message.append("  global exception level:").append(this.level);
        message.append("  global exception code:").append(this.code);
        if (this.getMsg() != null)
            message.append("  global exception desc:").append(this.getMsg());
        if (this.details != null)
            message.append("  global exception detail:").append(this.details);
        if (errMsg != null)
            message.append("  global exception original msg:").append(errMsg);
        return message.toString();
    }

    @Override
    public String toString() {
        return this.getMessage();
    }

    public GlobalException(final ErrorCodeEnum errorCode) {
        this.init(errorCode);
    }

    public final static void throwException(final ErrorCodeEnum errorCode) {
        throw new GlobalException(errorCode);
    }

    public GlobalException(final ErrorCodeEnum errorCode, final Throwable throwable) {
        super(throwable);
        this.init(errorCode);
    }

    public final static void throwException(final ErrorCodeEnum errorCode, final Throwable throwable) {
        throw new GlobalException(errorCode, throwable);
    }

    public GlobalException(final ErrorCodeEnum errorCode, final Throwable throwable, final String details) {
        this(errorCode, throwable);
        this.details = details;
    }

    public final static void throwException(final ErrorCodeEnum errorCode, final Throwable throwable,
                                            final String details) {
        throw new GlobalException(errorCode, throwable, details);
    }

    public GlobalException(final ErrorCodeEnum errorCode, final String details) {
        this(errorCode);
        this.details = details;
    }

    public final static void throwException(final ErrorCodeEnum errorCode, final String details) {
        throw new GlobalException(errorCode, details);
    }

    public final static void parameterError( final String details) {
        throw new GlobalException(ErrorCodeEnum.PARAMETER_ERROR, details);
    }

    public final static void error( final String details) {
        throw new GlobalException(ErrorCodeEnum.FAIL, details);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ExceptionLevelEnum getLevel() {
        return level;
    }

    public void setLevel(ExceptionLevelEnum level) {
        this.level = level;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}