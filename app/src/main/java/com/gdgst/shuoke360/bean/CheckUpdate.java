package com.gdgst.shuoke360.bean;

/**
 * Created by Administrator on 10/11 0011.
 */

public class CheckUpdate {

    /**
     * "app_ver":2.2,
     "app_link":"http://www.shiyan360.cn/Public/Uploads/image/20160909/57d25f5e4e677.apk",
     "app_readme":"版本：2.2 大小：19.5MB
     */

    private String app_ver;
    private String app_link;
    private String app_readme;

    public String getApp_ver() {
        return app_ver;
    }

    public void setApp_ver(String app_ver) {
        this.app_ver = app_ver;
    }

    public String getApp_link() {
        return app_link;
    }

    public void setApp_link(String app_link) {
        this.app_link = app_link;
    }

    public String getApp_readme() {
        return app_readme;
    }

    public void setApp_readme(String app_readme) {
        this.app_readme = app_readme;
    }
}
