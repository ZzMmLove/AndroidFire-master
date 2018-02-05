package com.gdgst.shuoke360.bean;

import java.util.List;

/**
 * {
 "success":true,
 "error_code":0,
 "message":"数据加载完毕",
 "data":Array[10]
 }
 * Created by liukun on 16/3/5.
 */
public class HttpResult<T> {
    private boolean success;

    private int error_code;

    private String message;

    private List<T> data ;

    public void setSuccess(boolean success){
        this.success = success;
    }
    public boolean getSuccess(){
        return this.success;
    }
    public void setError_code(int error_code){
        this.error_code = error_code;
    }
    public int getError_code(){
        return this.error_code;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setData(List<T> data){
        this.data = data;
    }
    public List<T> getData(){
        return data;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "success=" + success +
                ", error_code=" + error_code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
