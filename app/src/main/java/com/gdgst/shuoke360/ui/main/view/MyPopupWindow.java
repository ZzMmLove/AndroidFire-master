package com.gdgst.shuoke360.ui.main.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.app.AppApplication;
import com.gdgst.shuoke360.app.AppConstant;
import com.gdgst.shuoke360.bean.Category;
import com.gdgst.shuoke360.ui.main.adapter.PopupAdapter;
import com.gdgst.common.commonutils.ToastUitl;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.gdgst.common.baseapp.BaseApplication.getAppContext;

/**
 * Created by Administrator on 8/12 0012.
 */

public class MyPopupWindow extends PopupWindow {

    private View contentView;
    private GridView grid;
    private TextView reset;
    private TextView ok;
    private PopupAdapter adapter;
    private List<Category.AttrBean.ValsBean> data;
    private String itemValue;
    private final String spNameKey = "category";
    private String key;
    private String itemID;
    private String type;

    private SharedPreferences valueSharedPreferences ;

    public MyPopupWindow(final Context context, final List<Category.AttrBean.ValsBean> data) {
        this.data = data;
        key = choiceKey();
        adapter = new PopupAdapter(context);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popup, null);
        grid = (GridView) contentView.findViewById(R.id.grid);
        reset = (TextView) contentView.findViewById(R.id.reset);
        ok = (TextView) contentView.findViewById(R.id.ok);
        grid.setAdapter(adapter);
        valueSharedPreferences = getAppContext().getSharedPreferences(spNameKey, MODE_PRIVATE);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                data.get(position).setIsCheck(!data.get(position).getIsCheck());  //点击后设置为已经点击状态
                for (int i = 0; i < data.size(); i++) {
                    if (i == position) {
                        continue;
                    }
                    data.get(i).setIsCheck(false);
                    valueSharedPreferences.edit().remove(key).commit();
                }
               // Toast.makeText(context, data.get(position).getId(), Toast.LENGTH_SHORT).show();
                valueSharedPreferences.edit().putString(key, data.get(position).getVal()).commit();
                itemValue = data.get(position).getVal();
                if (data.get(position).getIsCheck()){
                    itemID = data.get(position).getId();
                }else itemID = "";
                type = data.get(position).getType();
                adapter.notifyDataSetChanged(data);
            }
        });

        //int h = context.getWindowManager().getDefaultDisplay().getHeight();
        //int w = context.getWindowManager().getDefaultDisplay().getWidth();
        int h = AppApplication.getScreenHeight();
        int w = AppApplication.getScreanWidth();
        this.setContentView(contentView);
        this.setWidth(w);
        this.setHeight(h);
        ColorDrawable dw = new ColorDrawable(00000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(itemValue)){
                    ToastUitl.show("请选择！", 0);
                }else {
                    Log.e("PopupWindow", "====="+key);
                    itemValue = valueSharedPreferences.getString(key, "");
                    itemClickCompleted.onItemClick(itemValue);
                }
                Intent intent = new Intent();
                intent.setAction(AppConstant.BROADCASTRECEIVE_ACTION);
                intent.putExtra("category", itemID);
                intent.putExtra("type", type);
                context.sendBroadcast(intent);
                MyPopupWindow.this.dismiss();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemValue = "";
                valueSharedPreferences.edit().remove(key).commit();
                MyPopupWindow.this.dismiss();
        }
        });
    }

    /**
     * 显示出Popupwindow
     * @param parent
     * @param data
     */
    public void showPricePopup(View parent, final List<Category.AttrBean.ValsBean> data) {
        if (!this.isShowing()) {
//            this.showAsDropDown(parent);
            this.showAsDropDown(parent,0,0);

            adapter.notifyDataSetChanged(data);
        } else {
            this.dismiss();
        }
    }

    /**
     * 根据类别来确定key的值
     * @return
     */
    private String choiceKey(){
        String key;
        if (data.get(0).getVal().equals("三年级上")){
            key = "nianji";
        }else if (data.get(0).getVal().equals("左点")){
            key = "bihua";
        }else if (data.get(0).getVal().equals("人字头")){
            key = "pianpang";
        }else  key = "jiegou";
        return key;
    }

    /**
     * 提供外部设置一个单Item点击完成的事件
     * @param itemClickCompleted
     */
    public void setItemClickCompleted(OnItemClickCompleted itemClickCompleted) {
        this.itemClickCompleted = itemClickCompleted;
    }

    private OnItemClickCompleted itemClickCompleted;

    public interface OnItemClickCompleted{
        void onItemClick(String itemValue);
    }

}
