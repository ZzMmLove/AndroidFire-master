package com.gdgst.shuoke360.bean;

/**
 *
 * Created by Administrator on 9/21 0021.
 */

public class History {

    /**
     "url":"/read/innerread.html?qq=1109",
     "member_id":"1",
     "name":"孩子怎样才能写一手好字？",
     "model":"read",
     "dateline":"1498264831",
     "id":"1109"
     */
    private String url;
    private int member_id;
    private String name;
    private String model;
    private long dateline;
    private int id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
