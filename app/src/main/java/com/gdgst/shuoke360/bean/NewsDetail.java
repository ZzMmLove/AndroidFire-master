/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.gdgst.shuoke360.bean;


public class NewsDetail {
    /**
     *   "id":"1115",
     "title":"广东“女子书法十家”作品展佛山开幕",
     "img_url_s":"/Public/Uploads/Article/20170607/thumb_59375e07c83be.jpg",
     "view_count":"5",
     "content":""
     */

    private String id;
    private String title;
    private String img_url_s;
    private int view_count;
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NewsDetail{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", img_url_s='" + img_url_s + '\'' +
                ", view_count=" + view_count +
                ", content='" + content + '\'' +
                '}';
    }
}