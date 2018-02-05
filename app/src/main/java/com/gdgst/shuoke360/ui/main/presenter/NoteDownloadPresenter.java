package com.gdgst.shuoke360.ui.main.presenter;

import com.gdgst.common.baserx.RxSubscriber;
import com.gdgst.shuoke360.bean.NoteDownload;
import com.gdgst.shuoke360.ui.main.contract.NoteDownloadContrct;

import java.util.List;

/**
 * Created by Administrator on 10/27 0027.
 */

public class NoteDownloadPresenter extends NoteDownloadContrct.Presenter {



    @Override
    public void readDownloadNote(String dirPath, String type) {
        mRxManage.add(mModel.readDownloadNote(dirPath, type)
                .subscribe(new RxSubscriber<List<NoteDownload>>(mContext, false) {

                    @Override
                    public void onStart() {
                        mView.showLoading("正在读取…");
                    }

                    @Override
                    protected void _onNext(List<NoteDownload> noteDownloads) {
                        mView.stopLoading();
                        mView.returnReadResult(noteDownloads);
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                }));
    }
}
