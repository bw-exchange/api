package io.at.api.untils;


import io.at.api.enums.ErrorCodeEnum;

public class ResponseEntity<T> {

    //返回数据
    private T datas;

    //返回状态
    private ResMsg resMsg;

    public ResponseEntity() {
    }

    public ResponseEntity(T datas) {
        this.datas = datas;
        this.resMsg = new ResMsg("success !", null, ErrorCodeEnum.OK.getCode());
    }

    public ResponseEntity(T datas, ResMsg resMsg) {
        this.datas = datas;
        this.resMsg = resMsg;
    }

    public static ResponseEntity succ(){
        return new ResponseEntity(null);
    }

    public static<S> ResponseEntity succ(S datas){
        return new ResponseEntity<S>(datas);
    }

    public static ResponseEntity fail(String msg){
        ResponseEntity result = new ResponseEntity();
        result.setResMsg(new ResMsg(msg, null, ErrorCodeEnum.FAIL.getCode()));
        return result;
    }

    public static ResponseEntity fail(String code, String msg){
        ResponseEntity result = new ResponseEntity();
        result.setResMsg(new ResMsg(msg,null,code));
        return result;
    }


    public Object getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }

    public ResMsg getResMsg() {
        return resMsg;
    }

    public void setResMsg(ResMsg resMsg) {
        this.resMsg = resMsg;
    }

}
