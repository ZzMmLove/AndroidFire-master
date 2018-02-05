package com.gdgst.shuoke360.bean;

/**
 * 常用碑帖的实体类
 * Created by Administrator on 6/13 0013.
 */

public class CopyBook {
    /**
     "id":"1154",
     "title":"赵孟頫道德经",
     "img_url_s":"/Public/Uploads/Beitie/20170605/thumb_5934f0fc40df0.png",
     "view_count":"0"
     */


    private String id;
    private String title;
    private String img_url_s;
    private int view_count;
    private String video;

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

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

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    @Override
    public String toString() {
        return "CopyBook{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", img_url_s='" + img_url_s + '\'' +
                ", view_count=" + view_count +
                '}';
    }
}
