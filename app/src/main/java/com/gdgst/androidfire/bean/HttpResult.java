package com.gdgst.androidfire.bean;

import java.util.List;

/**
 * Created by liukun on 16/3/5.
 */
public class HttpResult {
    private boolean success;

    private int error_code;

    private String message;

    private List<Video> data ;

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
    public void setData(List<Video> data){
        this.data = data;
    }
    public List<Video> getData(){
        return data;
    }
}
