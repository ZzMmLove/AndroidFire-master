package com.gdgst.shuoke360.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 9/14 0014.
 */

public class Avatar implements Serializable {
    private String img_url;
    private String error_code;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
