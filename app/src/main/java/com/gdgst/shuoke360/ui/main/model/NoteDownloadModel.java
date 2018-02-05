package com.gdgst.shuoke360.ui.main.model;


import com.gdgst.common.baserx.RxSchedulers;
import com.gdgst.shuoke360.bean.NoteDownload;
import com.gdgst.shuoke360.ui.main.contract.NoteDownloadContrct;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 *
 * Created by Administrator on 10/27 0027.
 */

public class NoteDownloadModel implements NoteDownloadContrct.Model {
    @Override
    public Observable<List<NoteDownload>> readDownloadNote(final String dirPath, final String type) {
        return Observable.create(new Observable.OnSubscribe<List<NoteDownload>>() {
            @Override
            public void call(Subscriber<? super List<NoteDownload>> subscriber) {
                List<NoteDownload> noteDownloads = getAllFiles(dirPath, type);
                subscriber.onNext(noteDownloads);
                subscriber.onCompleted();
            }
        }).compose(RxSchedulers.<List<NoteDownload>>io_main());
    }

    /**
     * 获取指定目录内所有文件路径
     * @param dirPath 需要查询的文件目录
     * @param _type 查询类型，比如mp3什么的
     */
    public static List<NoteDownload> getAllFiles(String dirPath, String _type) {
        File f = new File(dirPath);
        if (!f.exists()) {//判断路径是否存在
            return null;
        }

        File[] files = f.listFiles();

        if(files==null){//判断权限
            return null;
        }

        List<NoteDownload> fileList = new ArrayList<>();
        for (File _file : files) {//遍历目录
            if(_file.isFile() && _file.getName().endsWith(_type)){
                String _name=_file.getName();
                String filePath = _file.getAbsolutePath();//获取文件路径
                String fileName = _file.getName().substring(0,_name.length()-4);//获取文件名
//        Log.d("LOGCAT","fileName:"+fileName);
//        Log.d("LOGCAT","filePath:"+filePath);

                NoteDownload _fInfo = new NoteDownload();
                _fInfo.setName(_name);
                _fInfo.setPath(dirPath);
                fileList.add(_fInfo);

            }
        }
        return fileList;
    }
}
