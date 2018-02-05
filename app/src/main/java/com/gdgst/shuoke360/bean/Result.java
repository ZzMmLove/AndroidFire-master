package com.gdgst.shuoke360.bean;

import java.io.Serializable;


/**
 * des:接口返回数据基类
 "success":true,
 "error_code":0,
 "message":"数据加载完毕",
 "data":
 */
public class Result<T> implements Serializable {


    private boolean success;
    private int error_code;
    private String message;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
