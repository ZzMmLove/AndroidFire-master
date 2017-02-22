package com.gdgst.androidfire.api;

import com.gdgst.androidfire.bean.GirlData;
import com.gdgst.androidfire.bean.HttpResult;
import com.gdgst.androidfire.bean.MessageData;
import com.gdgst.androidfire.bean.MessageDetail;
import com.gdgst.androidfire.bean.NewsDetail;
import com.gdgst.androidfire.bean.NewsSummary;
import com.gdgst.androidfire.bean.User;
import com.gdgst.androidfire.bean.VideoData;
import com.gdgst.common.basebean.BaseRespose;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * des:ApiService
 * Created by xsf
 * on 2016.06.15:47
 */
public interface ApiService {

    @GET("login")
    Observable<BaseRespose<User>> login(@Query("username") String username, @Query("password") String password);

    @GET("nc/article/{postId}/full.html")
    Observable<Map<String, NewsDetail>> getNewDetail(
            @Header("Cache-Control") String cacheControl,
            @Path("postId") String postId);

    //http://c.m.163.com/nc/article/headline/T1348647909107/0-20.html
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewsSummary>>> getNewsList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type, @Path("id") String id,
            @Path("startPage") int startPage);

    @GET
    Observable<ResponseBody> getNewsBodyHtmlPhoto(
            @Header("Cache-Control") String cacheControl,
            @Url String photoPath);
    //@Url，它允许我们直接传入一个请求的URL。这样以来我们可以将上一个请求的获得的url直接传入进来，baseUrl将被无视
    // baseUrl 需要符合标准，为空、""、或不合法将会报错

    @GET("data/福利/{size}/{page}")
    Observable<GirlData> getPhotoList(
            @Header("Cache-Control") String cacheControl,
            @Path("size") int size,
            @Path("page") int page);

    @GET("nc/video/list/{type}/n/{startPage}-10.html")
    Observable<Map<String, List<VideoData>>> getVideoList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type,
            @Path("startPage") int startPage);


    @POST("http://www.shiyan360.cn/index.php/api/chuangke_list")
    Observable<HttpResult> getvideo(
            @Query("desc_type") String desc_type,
            @Query("category_id") String category_id,
            @Query("page") int page);

    @POST("http://xssd.phpwind.zhanyouapp.cn/mobile/index.php")
    Observable<MessageData> getMessage(
            @Query("a") String a,
            @Query("args[page]") int page,
            @Query("args[perpage]") String perpage,
            @Query("args[type]") String type,
            @Query("args[sort]") String sort,
            @Query("args[fid]") String fid);

    @POST("http://xssd.phpwind.zhanyouapp.cn/mobile/index.php")
    Observable<MessageDetail> getMessageDetail(
            @Query("a") String a,
            @Query("args[tid]") String tid,
            @Query("args[perpage]") String perpage);



}
