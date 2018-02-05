package com.gdgst.shuoke360.ui.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.gdgst.common.commonutils.TimeUtil;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.api.ApiConstants;
import com.gdgst.shuoke360.bean.History;
import com.gdgst.shuoke360.bean.NoteDownload;
import com.gdgst.shuoke360.ui.main.activity.VideoPlayerActivity;
import com.gdgst.shuoke360.ui.news.activity.CopyBookPhotosListActivity;
import com.gdgst.shuoke360.ui.news.activity.NormalNewsDetailActivity;
import com.gdgst.shuoke360.ui.news.activity.NoteDetailActivity;

import java.util.List;

/**
 *
 * Created by Administrator on 9/21 0021.
 */

public class NoteDownloadAdapter extends CommonRecycleViewAdapter<NoteDownload> {

    public NoteDownloadAdapter(Context context, int layoutId, List<NoteDownload> mDatass) {
        super(context, layoutId, mDatass);
    }

    @Override
    public void convert(final ViewHolderHelper helper, NoteDownload download) {
        String titileName = download.getName();
        final String path = download.getPath();

        helper.setText(R.id.history_name, titileName);

        helper.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(path), "application/vnd.ms-powerpoint");// 打开PPT的Content-type
                mContext.startActivity(intent);
            }
        });

    }
}
