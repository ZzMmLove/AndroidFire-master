package com.gdgst.shuoke360.downloadprogress.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.api.ApiConstants;
import com.gdgst.shuoke360.downloadprogress.network.DownloadAPI;
import com.gdgst.shuoke360.downloadprogress.network.download.DownloadProgressListener;
import com.gdgst.shuoke360.downloadprogress.bean.Download;
import com.gdgst.shuoke360.downloadprogress.util.StringUtils;
import com.gdgst.shuoke360.ui.news.activity.NoteDetailActivity;

import java.io.File;

import rx.Subscriber;

/**
 * Created by Administrator on 10/27 0027.
 */

public class DownloadService extends IntentService {

    private static final String TAG = "DownloadService";

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private String url;
    private String name;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        url = intent.getStringExtra("url");
        name = intent.getStringExtra("name");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_action_download)
                .setContentTitle("Download")
                .setContentText("讲义下载")
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());

        download();
    }

    private void download() {
        DownloadProgressListener listener = new DownloadProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                Download download = new Download();
                download.setTotalFileSize(contentLength);
                download.setCurrentFileSize(bytesRead);
                int progress = (int) ((bytesRead * 100) / contentLength);
                download.setProgress(progress);

                sendNotification(download);
            }
        };
        File outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS), name+".ppt");
//        Log.e("PATH", "创建的："+outputFile.getAbsolutePath()+"--"+outputFile.getPath());

        String baseUrl = StringUtils.getHostName(ApiConstants.SHUO_KE_HOST);

        new DownloadAPI(baseUrl, listener).downloadAPK(url, outputFile, new Subscriber() {
            @Override
            public void onCompleted() {
                downloadCompleted();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                downloadCompleted();
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    /**
     * 下载完成
     */
    private void downloadCompleted() {
        Download download = new Download();
        download.setProgress(100);
        sendIntent(download);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("下载完成");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNotification(Download download) {

        sendIntent(download);
        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText(
                StringUtils.getDataSize(download.getCurrentFileSize()) + "/" +
                        StringUtils.getDataSize(download.getTotalFileSize()));
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(Download download) {

        Intent intent = new Intent(NoteDetailActivity.MESSAGE_PROGRESS);
        intent.putExtra("download", download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

}
