package com.gdgst.shuoke360.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

/**
 *
 * Created by Administrator on 4/11 0017.
 */

public class UpdateAppService extends Service {

    /**
     * Android系统下载类
     */
    private DownloadManager manager;
    /**
     * 从服务器下载路径
     */
    private String url;
    /**
     *下载路径，如果不自定义自己的下载路径，Android6.0的系统不自动安装
     */
    private String DOWNLOADPATH = "/downloadapk/";
    private DownLoadCompleteReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //获得从服务器的下载路径
        url = intent.getStringExtra("url");
        Log.e("APPService", "-->initDownManager"+url);
        //定义自己的下载的全路径
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+DOWNLOADPATH+"Shuoke.apk";
        //判断文件是否存在
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        //初始化下载器
        initDownManager();

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if(receiver != null){
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

    private void initDownManager() {

        //获得系统的下载器对象
        manager  = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        //初始化广播接收器
        receiver = new DownLoadCompleteReceiver();
        //设置下载地址
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
        //设置下载的允许的网络，这里是移动和WiFi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        down.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        down.setMimeType(mimeString);
        //下载时，通知栏显示途中
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        //显示下载页面
        down.setVisibleInDownloadsUi(true);
        //down.setDestinationInExternalPublicDir(DOWNLOADPATH, "MicExp.apk");
        down.setTitle("数字书法掌上通");
        down.setDestinationInExternalPublicDir(DOWNLOADPATH, "Shuoke.apk");

        //将下载请求放入队列
        try{
            manager.enqueue(down);
        }catch (Exception e){
            e.printStackTrace();
        }
        //注册下载广播
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    class DownLoadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
                //获得下载文件的id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (manager.getUriForDownloadedFile(downId) != null){
                    //自动安装
                    installAPK(manager.getUriForDownloadedFile(downId), context);
                }else {
                    Toast.makeText(context, "下载失败", Toast.LENGTH_LONG).show();
                }
                //停止服务并关闭广播
                UpdateAppService.this.stopSelf();
            }
        }

        private void installAPK(Uri apk, Context context) {
            //判断系统的版本号是否低于23
            if (Build.VERSION.SDK_INT >= 24){
                Log.e("SDK", "SDK版本："+ Build.VERSION.SDK_INT);
                File file= new File(Environment.getExternalStoragePublicDirectory(DOWNLOADPATH), "Shuoke.apk");
                Uri apkUri = FileProvider.getUriForFile(context, "com.gdgst.fileprovider", file);//在AndroidManifest中的android:authorities值
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
                install.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                context.startActivity(install);
            } else if (Build.VERSION.SDK_INT < 23) {
                Intent intents = new Intent();
                intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intents.setAction(Intent.ACTION_VIEW);
                //intents.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intents.addCategory("android.intent.category.DEFAULT");
                intents.setType("application/vnd.android.package-archive");
                intents.setData(apk);
                intents.setDataAndType(apk, "application/vnd.android.package-archive");
                startActivity(intents);
            }else if (Build.VERSION.SDK_INT == 23){
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH + "Shuoke.apk");
                if (file.exists()){
                    openFile(file, context);
                }
            }
        }

        private void openFile(File file, Context context) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            String type = getMIMEType(file);
            intent.setDataAndType(Uri.fromFile(file), type);
            try{
                context.startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(context, "没有打开此类文件的程序", Toast.LENGTH_LONG).show();
            }
        }

        private String getMIMEType(File file) {
            String var1 = "";
            String var2 = file.getName();
            String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
            var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
            return var1;
        }
    }

}
