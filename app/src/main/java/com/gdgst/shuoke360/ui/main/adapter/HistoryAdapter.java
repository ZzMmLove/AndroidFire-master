package com.gdgst.shuoke360.ui.main.adapter;

import android.content.Context;
import android.view.View;

import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.api.ApiConstants;
import com.gdgst.shuoke360.bean.History;
import com.gdgst.shuoke360.ui.main.activity.VideoPlayerActivity;
import com.gdgst.shuoke360.ui.news.activity.CopyBookPhotosListActivity;
import com.gdgst.shuoke360.ui.news.activity.NormalNewsDetailActivity;
import com.gdgst.shuoke360.ui.news.activity.NoteDetailActivity;
import com.gdgst.common.commonutils.TimeUtil;

import java.util.List;

/**
 *
 * Created by Administrator on 9/21 0021.
 */

public class HistoryAdapter extends CommonRecycleViewAdapter<History> {

    public HistoryAdapter(Context context, int layoutId, List<History> mDatass) {
        super(context, layoutId, mDatass);
    }

    @Override
    public void convert(final ViewHolderHelper helper, History history) {
        final String titileName = history.getName();
        final String type = history.getModel();
        final int id = history.getId();
        final String url = history.getUrl();
        Long tileLine = history.getDateline();
        String time = TimeUtil.formatData(TimeUtil.dateFormatYMDHM, tileLine);
        String model = "";
        switch (type){
            case "read":
                model = "阅读";
                break;
            case "beitie":
                model = "碑帖";
                break;
            case "peixun":
                model = "培训学院";
                break;
            case "video":
                model = "视频";
                break;
            case "note":
                model = "讲义";
                break;
        }

        helper.setText(R.id.history_name, titileName);
        helper.setText(R.id.history_type, model);
        helper.setText(R.id.history_time, time);
        //设置每个item的点击事件
        helper.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case "read":
                    case "peixun":
                        NormalNewsDetailActivity.startAction(mContext, helper.getView(R.id.rl_root), ApiConstants.SHUO_KE_HOST + url, null, String.valueOf(id));
                        break;

                    case "beitie":
                        CopyBookPhotosListActivity.startAction(mContext, helper.getView(R.id.rl_root), String.valueOf(id));
                        break;
                    case "video":
                        VideoPlayerActivity.startAction(mContext, helper.getView(R.id.rl_root), ApiConstants.SHUO_KE_HOST + url, titileName);
                        break;
                    case "note":
                        NoteDetailActivity.startAction(mContext, helper.getView(R.id.rl_root), ApiConstants.SHUO_KE_HOST + url, titileName);
                        break;
                }
            }
        });

    }
}
