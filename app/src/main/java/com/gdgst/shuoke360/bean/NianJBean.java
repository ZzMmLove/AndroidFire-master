package com.gdgst.shuoke360.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 8/12 0012.
 */

public class NianJBean implements Serializable{

    private boolean isChecked;
    private String str1;
    private String str2;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    @Override
    public String toString() {
        return "NianJBean{" +
                "isChecked=" + isChecked +
                ", str1='" + str1 + '\'' +
                ", str2='" + str2 + '\'' +
                '}';
    }
}
