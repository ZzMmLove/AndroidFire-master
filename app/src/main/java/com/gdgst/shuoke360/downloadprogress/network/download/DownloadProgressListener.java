package com.gdgst.shuoke360.downloadprogress.network.download;

/**
 * Created by Administrator on 10/25 0025.
 */

public interface DownloadProgressListener {

    /**
     * @param bytesRead     已经下载或上传字节数
     * @param contentLength        总字节数
     * @param done         是否完成
     */
    void update(long bytesRead, long contentLength, boolean done);

}
