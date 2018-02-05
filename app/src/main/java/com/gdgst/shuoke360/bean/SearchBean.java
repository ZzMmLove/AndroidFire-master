package com.gdgst.shuoke360.bean;

/**
 *
 * Created by Administrator on 8/31 0031.
 */

public class SearchBean {

    /**
     *  "id":"1223",
     "title":"欧阳询书法",
     "img_url_s":"/Public/Uploads/Beitie/20170705/thumb_595ccd7fbecc0.png",
     "path":"",
     "dateline":"1499254338",
     "model":"beitie",
     "view_count":"14"
     */

    private String id;
    private String title;
    private String img_url_s;
    private String path;
    private String model;
    private long dateline;
    private int view_count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_url_s() {
        return img_url_s;
    }

    public void setImg_url_s(String img_url_s) {
        this.img_url_s = img_url_s;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }
}
