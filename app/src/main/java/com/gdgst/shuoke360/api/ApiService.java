package com.gdgst.shuoke360.api;

import com.gdgst.shuoke360.bean.Avatar;
import com.gdgst.shuoke360.bean.CheckUpdate;
import com.gdgst.shuoke360.bean.CopyBook;
import com.gdgst.shuoke360.bean.CopyBookGlide;
import com.gdgst.shuoke360.bean.History;
import com.gdgst.shuoke360.bean.HttpResult;
import com.gdgst.shuoke360.bean.MessageData;
import com.gdgst.shuoke360.bean.MessageDetail;
import com.gdgst.shuoke360.bean.NewsDetail;
import com.gdgst.shuoke360.bean.Result;
import com.gdgst.shuoke360.bean.SearchBean;
import com.gdgst.shuoke360.bean.UpdateUserinfo;
import com.gdgst.shuoke360.bean.User;
import com.gdgst.shuoke360.bean.Video;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * des:ApiService
 * Created by xsf
 * on 2016.06.15:47
 */
public interface ApiService {


    @GET
    Observable<ResponseBody> getNewsBodyHtmlPhoto(
            @Header("Cache-Control") String cacheControl,
            @Url String photoPath);
    //@Url，它允许我们直接传入一个请求的URL。这样以来我们可以将上一个请求的获得的url直接传入进来，baseUrl将被无视
    // baseUrl 需要符合标准，为空、""、或不合法将会报错

    /**
     *说课360视频的主接口
     * @param params
     * @return
     */
    //http://shuoke360.cn/api/shipin
    //http://shuoke360.cn/api/video?token=gstshuoke360&jiegou=559&bianpang=458&bihua=431&class=619&page=1
    @POST("http://shuoke360.cn/api/video")
    Observable<HttpResult<Video>> getvideo(
            @QueryMap Map<String, String> params);

    /**
     * 新闻列表的主接口
     * @param a
     * @param page
     * @param perpage
     * @param type
     * @param sort
     * @param fid
     * @return
     */
    @POST("http://xssd.phpwind.zhanyouapp.cn/mobile/index.php")
    Observable<MessageData> getMessage(
            @Query("a") String a,
            @Query("args[page]") int page,
            @Query("args[perpage]") String perpage,
            @Query("args[type]") String type,
            @Query("args[sort]") String sort,
            @Query("args[fid]") String fid);

    /**
     * 新闻详情的主接口
     * @param a
     * @param tid
     * @param perpage
     * @return
     */
    @POST("http://xssd.phpwind.zhanyouapp.cn/mobile/index.php")
    Observable<MessageDetail> getMessageDetail(
            @Query("a") String a,
            @Query("args[tid]") String tid,
            @Query("args[perpage]") String perpage);

    /**
     * 获取常用碑帖的列表信息
     * @param token  访问网络的位移标识
     * @param page  分页
     * @return
     */
    @POST("http://shuoke360.cn/api/copybook/")
    Observable<HttpResult<CopyBook>> getCopyBookList(
            @Query("token" )String token,
            @Query("page") int page,
            @Query("type") String type,
            @Query("pid") String pid);

    /**
     * 获取指定碑帖的所有图片
     * @param token
     * @param page
     * @param id
     * @return
     */
    @POST("http://shuoke360.cn/api/copybook/")
    Observable<HttpResult<CopyBookGlide>> getCopyBookListGlide(
            @Header("Cache-Control") String cacheControl,
            @Query("token" )String token,
            @Query("page") int page,
            @Query("id") String id,
            @Query("type") String type);

    //http://shuoke360.cn/api/copybook?token=gstshuoke360&type=yuedu&pid=411&id=1115
    @POST("http://shuoke360.cn/api/copybook")
    Observable<HttpResult<NewsDetail>> getMessageDetail(@Query("token") String token,
                                                        @Query("type") String type,
                                                        @Query("pid") String pid,
                                                        @Query("id") String id);

    /**
     * 账号密码登录
     * @param token
     * @param mob
     * @param password
     * @return
     */
    //http://shuoke360.cn/api/login?token=gstshuoke360&mob=modi@163.com&password=123
    @GET("http://shuoke360.cn/api/login")
    Observable<Result<User>> getUserData(@Query("token") String token,
                                 @Query("mob") String mob,
                                 @Query("password") String password);

    /**
     * 自动登录
     * @param token
     * @param accessToken
     * @return
     */
    //http://shuoke360.cn/api/auto_login?token=gstshuoke360&access_token=pp072oi7ooa727dea8012245aedoc72c
    @GET("http://shuoke360.cn/api/auto_login")
    Observable<Result<User>> getAutoLogin(@Query("token") String token,
                                          @Query("access_token") String accessToken);

    /**
     * 上传头像
     * @param token
     * @param accessToken
     * @param base64
     * @return
     */
    //http://shuoke360.cn/api/avatar?token=gstshuoke360&access_token=gj1tjo85h71j09dcjd11fmem797bo0cc&img=base64
    //@Multipart
    @GET("http://shuoke360.cn/api/avatar")
    Observable<Result<Avatar>> getAvatarResult(@Query("token") String token,
                                               @Query("access_token") String accessToken,
                                               @Query("img") String base64);

    /**
     * 修改个人信息
     * @param token
     * @param accessToken
     * @param body
     * @return
     */
    //http://shuoke360.cn/api/update?token=gstshuoke360&access_token=21600wb6c1620146w23s5f1lblwqs02b&data=
    @GET("http://shuoke360.cn/api/update")
    Observable<Result<UpdateUserinfo>> getUpdateUserInfo(@Query("token") String token,
                                                         @Query("access_token") String accessToken,
                                                         @Query("data") String body);

    /**
     * 修改密码
     * @param token
     * @param accessToken
     * @param oldPwd
     * @param newPwd
     * @return
     */
    //http://shuoke360.cn/api/setpwd?token=gstshuoke360&access_token=21600wb6c1620146w23s5f1lblwqs02b&oldpwd=123456&newpwd=1234561
    @GET("http://shuoke360.cn/api/setpwd")
    Observable<Result<UpdateUserinfo>> getUpdatePassword(@Query("token") String token,
                                                         @Query("access_token") String accessToken,
                                                         @Query("oldpwd") String oldPwd,
                                                         @Query("newpwd") String newPwd);

    /**
     * 找回密码
     * @param token
     * @param mob
     * @param password
     * @return
     */
    //http://shuoke360.cn/api/findpwd?token=gstshuoke360&mob=13528605866&pass=123456
    @GET("http://shuoke360.cn/api/findpwd")
    Observable<Result<UpdateUserinfo>> findPassword(@Query("token") String token,
                                                    @Query("mob") String mob,
                                                    @Query("pass") String password);
    /**
     * 注册
     * @param token
     * @param mob
     * @param password
     * @return
     */
    //http://shuoke360.cn/api/register?token=gstshuoke360&mob=13528605866&pass=123456
    @GET("http://shuoke360.cn/api/register")
    Observable<Result<UpdateUserinfo>> register(@Query("token") String token,
                                                @Query("mob") String mob,
                                                @Query("pass") String password);

    /**
     * 获取浏览记录
     * @param token
     * @param accessToken
     * @param page
     * @return
     */
    //http://shuoke360.cn/api/history?token=gstshuoke360&access_token=918lv0flvq9v22288lh7i0qg491y8r89&page=1
    @GET("http://shuoke360.cn/api/history")
    Observable<HttpResult<History>> getHistoryResult(@Query("token") String token,
                                                     @Query("access_token") String accessToken,
                                                     @Query("page") int page);

    /**
     * 检查更新
     * @param versionCode
     * @return
     */
    //http://shuoke360.cn/api/checkVersion?token=gstshuoke360&version=2.1
    @GET("http://shuoke360.cn/api/checkVersion")
    Observable<Result<CheckUpdate>> getCheckUpdateResult(@Query("token") String token,
                                                         @Query("version") String versionCode);

    /**
     * 搜索
     * @param token
     * @param key
     * @return
     */
    //http://shuoke360.cn/api/search?token=gstshuoke360&key=欧阳修&page=1
    @GET("http://shuoke360.cn/api/search")
    Observable<HttpResult<SearchBean>> getSearchResult(@Query("token") String token,
                                                       @Query("key") String key,
                                                       @Query("page") int page);

    /**
     * 下载讲义PPT
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    Observable<Response<ResponseBody>> downloadFileByUrlRx(@Url String fileUrl);

}
