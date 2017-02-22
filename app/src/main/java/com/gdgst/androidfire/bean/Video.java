package com.gdgst.androidfire.bean;

/**
 * Created by Don on 2016/10/15.
 */
public class Video {
    private String id;

    private String cateid;

    private String name;

    private String img_url;

    private String img_url_s;

    private String video_url;

    private String view_count;

    private String remark;

    private String time;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setCateid(String cateid) {
        this.cateid = cateid;
    }

    public String getCateid() {
        return this.cateid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_url() {
        return this.img_url;
    }

    public void setImg_url_s(String img_url_s) {
        this.img_url_s = img_url_s;
    }

    public String getImg_url_s() {
        return this.img_url_s;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_url() {
        return this.video_url;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public String getView_count() {
        return this.view_count;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }

}
