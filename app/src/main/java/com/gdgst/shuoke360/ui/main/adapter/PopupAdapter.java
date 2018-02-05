package com.gdgst.shuoke360.ui.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.bean.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 8/12 0012.
 */

public class PopupAdapter extends BaseAdapter {

    private Context context;
    private List<Category.AttrBean.ValsBean> data = new ArrayList<>();

    public PopupAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder.tv1 = (TextView) view.findViewById(R.id.tv1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (data != null && data.size() > 0) {
            viewHolder.tv1.setText(data.get(i).getVal());
            if (data.get(i).getIsCheck()){
                viewHolder.tv1.setBackgroundColor(Color.rgb(2, 136, 206));
                viewHolder.tv1.setTextColor(Color.WHITE);
            } else {
                viewHolder.tv1.setBackgroundColor(Color.rgb(240, 240, 240));
                viewHolder.tv1.setTextColor(Color.GRAY);
            }
        }
        return view;
    }

    static class ViewHolder {
        public TextView tv1;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void notifyDataSetChanged(List<Category.AttrBean.ValsBean> tempData) {
        if (tempData == null || tempData.size() == 0) {
            return;
        }
        data.clear();
        data.addAll(tempData);
        notifyDataSetChanged();
    }

}
