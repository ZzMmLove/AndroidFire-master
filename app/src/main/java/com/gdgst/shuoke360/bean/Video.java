package com.gdgst.shuoke360.bean;

/**
 * Created by Don on 2016/10/15.
 */
public class Video {

    /**
     "id":"4225",
     "name":"测试41",
     "img_url_s":"/Public/Uploads/Video/20170525/thumb_59264c0220a06.jpg",
     "video_url":"/Public/Uploads/Video/20170525/59264c0e8d116.mp4"
     */

    private String id;

    private String name;

    private String img_url_s;

    private String video_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url_s() {
        return img_url_s;
    }

    public void setImg_url_s(String img_url_s) {
        this.img_url_s = img_url_s;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", img_url_s='" + img_url_s + '\'' +
                ", video_url='" + video_url + '\'' +
                '}';
    }
}
