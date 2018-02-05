package com.gdgst.shuoke360.ui.news.adapter;

import android.content.Context;
import android.view.View;

import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.MultiItemRecycleViewAdapter;
import com.aspsine.irecyclerview.universaladapter.recyclerview.MultiItemTypeSupport;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.api.ApiConstants;
import com.gdgst.shuoke360.bean.CopyBook;
import com.gdgst.shuoke360.ui.news.activity.CopyBookPhotosListActivity;
import com.gdgst.shuoke360.ui.news.activity.NormalNewsDetailActivity;
import com.gdgst.shuoke360.ui.news.activity.NoteDetailActivity;

import java.util.List;

/**
 * des:新闻列表适配器
 * Created by xsf
 * on 2016.09.10:49
 */
public class NewListAdapter  extends MultiItemRecycleViewAdapter<CopyBook> {
    /**代表的是普通的新闻item*/
    public static final int TYPE_ITEM = 0;
    private String pid;
    private String type;

    public void setValue(String pid, String type){
        this.pid = pid;
        this.type = type;
    }

    public NewListAdapter(Context context,final List<CopyBook> datas) {

        super(context, datas, new MultiItemTypeSupport<CopyBook>() {
            @Override
            public int getLayoutId(int type) {
                    return R.layout.item_news;
            }

            @Override
            public int getItemViewType(int position, CopyBook msg) {
                return TYPE_ITEM;
            }
        });
    }

    @Override
    public void convert(ViewHolderHelper holder, CopyBook newsSummary) {
        switch (holder.getLayoutId()) {
            case R.layout.item_news:
                setItemValues(holder,newsSummary,getPosition(holder));
                break;
        }
    }

    /**
     * 普通样式 ，设置每个Item的布局的数据
     * @param holder  item视图的帮助类
     * @param newsSummary 新闻列表的封装数据对象
     * @param position 当前item的位置
     */
    private void setItemValues(final ViewHolderHelper holder, final CopyBook newsSummary, final int position) {

       // LogUtils.logi("===ListBeans==="+newsSummary.toString(), "NewsFrament");  //有数据

        String title = newsSummary.getTitle();
        if (title == null) {
            title = newsSummary.getTitle();
        }
        final String id = newsSummary.getId();
        String imgSrc = newsSummary.getImg_url_s();
        int voidCount = newsSummary.getView_count();

        holder.setText(R.id.news_summary_title_tv, title);
        holder.setText(R.id.news_summary_ptime_tv, "查看次数："+voidCount);
        String imgUrl = ApiConstants.SHUO_KE_HOST + imgSrc;
        holder.setImageUrl(R.id.news_summary_photo_iv, imgUrl);

        holder.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Log.e("TAG", "------------->type="+type);
            //跳转页面到新闻详情页,
                if ("yuedu".equals(type)){
                    NormalNewsDetailActivity.startAction(mContext,holder.getView(R.id.news_summary_photo_iv),ApiConstants.SHUO_KE_HOST+newsSummary.getImg_url_s(), pid, id);
                }else if ("beitie".equals(type)){
                    CopyBookPhotosListActivity.startAction(mContext, holder.getView(R.id.news_summary_photo_iv), id);
                }else if ("note".equals(type)){
                    //ToastUitl.show("Note"+newsSummary.getVideo(), 0);
                    NoteDetailActivity.startAction(mContext, holder.getView(R.id.news_summary_photo_iv), ApiConstants.SHUO_KE_HOST+newsSummary.getVideo(), newsSummary.getTitle());
                }
            }
        });
    }
}
