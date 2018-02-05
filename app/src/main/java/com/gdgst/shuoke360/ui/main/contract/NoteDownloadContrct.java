package com.gdgst.shuoke360.ui.main.contract;

import com.gdgst.common.base.BaseModel;
import com.gdgst.common.base.BasePresenter;
import com.gdgst.common.base.BaseView;
import com.gdgst.shuoke360.bean.NoteDownload;

import java.util.List;

import rx.Observable;

/**
 *
 * Created by Administrator on 10/27 0027.
 */

public interface NoteDownloadContrct {

    interface Model extends BaseModel{
        Observable<List<NoteDownload>> readDownloadNote(String dirPath, String type);
    }

    interface View extends BaseView {
        void returnReadResult(List<NoteDownload> noteDownloads);
    }

    abstract static class Presenter extends BasePresenter<View, Model> {
        public abstract void readDownloadNote(String dirPath, String type);
    }

}
