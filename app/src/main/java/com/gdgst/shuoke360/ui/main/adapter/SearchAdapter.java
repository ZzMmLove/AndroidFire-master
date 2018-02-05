package com.gdgst.shuoke360.ui.main.adapter;

import android.content.Context;
import android.view.View;

import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.api.ApiConstants;
import com.gdgst.shuoke360.bean.SearchBean;
import com.gdgst.shuoke360.ui.main.activity.VideoPlayerActivity;
import com.gdgst.shuoke360.ui.news.activity.CopyBookPhotosListActivity;
import com.gdgst.shuoke360.ui.news.activity.NormalNewsDetailActivity;
import com.gdgst.shuoke360.ui.news.activity.NoteDetailActivity;
import com.gdgst.common.commonutils.TimeUtil;

import java.util.List;

/**
 * Created by Administrator on 10/12 0012.
 */

public class SearchAdapter extends CommonRecycleViewAdapter<SearchBean> {

    public SearchAdapter(Context context, int layoutId, List<SearchBean> mDatass) {
        super(context, layoutId, mDatass);
    }

    @Override
    public void convert(final ViewHolderHelper helper, SearchBean searchBean) {
        final String titileName = searchBean.getTitle();
        final String type = searchBean.getModel();
        final String id = searchBean.getId();
        final String url = searchBean.getPath();
        Long tileLine = searchBean.getDateline();
        String imageUrl = searchBean.getImg_url_s();
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
            case "video_play":
                model = "视频";
                break;
            case "note":
                model = "讲义";
                break;
        }

        helper.setText(R.id.news_summary_title_tv, titileName);
        helper.setText(R.id.news_summary_ptime_tv, time);
        String imgUrl = ApiConstants.SHUO_KE_HOST + imageUrl;
        helper.setImageUrl(R.id.news_summary_photo_iv, imgUrl);
        helper.setText(R.id.news_summary_digest_tv, model);

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
                    case "video_play":
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
