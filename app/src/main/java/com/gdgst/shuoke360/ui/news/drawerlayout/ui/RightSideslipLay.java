package com.gdgst.shuoke360.ui.news.drawerlayout.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.ui.news.drawerlayout.adapter.RightSideslipLayAdapter;
import com.gdgst.shuoke360.ui.news.drawerlayout.model.AttrList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 7/26 0026.
 */

public class RightSideslipLay extends RelativeLayout {
    private Context mCtx;
    private ListView selectList;
    private Button resetBrand;
    private Button okBrand;
    private ImageView backBrand;
    private RelativeLayout mRelateLay;
    private RightSideslipLayAdapter slidLayFrameAdapter;
    private String JsonStr = " {\"attr\": [{ \"isoPen\": true,\"single_check\": 0,\"key\": \"作者\", \"vals\": [ { \"val\": \"李斯\"}, {\"val\": \"皇象\" }, {\"val\": \"王羲之\" },{\"val\": \"王献之\" }, {\"val\": \"陆机\" },{\"val\": \"智永\" }, {\"val\": \"褚遂良\" },{\"val\": \"钟绍京\" }, {\"val\": \"冯承素\" }, {\"val\": \"张旭\"},{\"val\": \"怀素\" },{ \"val\": \"颜师古\" }, { \"val\": \"颜勤礼\" }, { \"val\": \"颜真卿\"},{ \"val\": \"柳公权\"},{ \"val\": \"欧阳询\"},{ \"val\": \"苏轼\"},{ \"val\": \"宋徽宗\"},{ \"val\": \"黄庭宗\"},{ \"val\": \"赵佶真\"},{ \"val\": \"赵孟頫\"},{ \"val\": \"文征明\"},{ \"val\": \"王铎\"},{ \"val\": \"康熙\"},{ \"val\": \"王珣伯\"},{ \"val\": \"姚孟起\"},{ \"val\": \"邓石如\"},{ \"val\": \"何兆基\"},{ \"val\": \"吴熙载\"},{ \"val\": \"赵之谦\"},{ \"val\": \"鲁迅\"},{ \"val\": \"郭沫若\"},{ \"val\": \"毛泽东\"},{ \"val\": \"邓散木\"},{ \"val\": \"张猛龙\"},{ \"val\": \"于右任\"},{ \"val\": \"魏钟繇\"},{ \"val\": \"仇靖\"},{ \"val\": \"米芾\"},{ \"val\": \"孙过庭\"},{ \"val\": \"王升撰\"},{ \"val\": \"其他\"}]},\n" +
            "{\"single_check\": 0,\"key\": \"年代\", \"vals\": [{ \"val\": \"秦朝\"},{ \"val\": \"三国\"},{ \"val\": \"东汉\"},{ \"val\": \"东晋\"},{ \"val\": \"西晋\"},{ \"val\": \"北魏\"},{ \"val\": \"南北朝\"},{ \"val\": \"唐代\"},{ \"val\": \"宋代\"},{ \"val\": \"元代\"},{ \"val\": \"明代\"},{ \"val\": \"明末清初\"},{ \"val\": \"清代\"},{ \"val\": \"近现代\"}]},\n" +
            "{\"single_check\": 0,\"key\": \"书体\", \"vals\": [{ \"val\": \"楷书\"},{ \"val\": \"行书\"},{ \"val\": \"草书\"},{ \"val\": \"隶书\"},{ \"val\": \"篆体\"}]}]}";

    public RightSideslipLay(Context context) {
        super(context);
        mCtx = context;
        inflateView();
    }

    private void inflateView() {
        View.inflate(getContext(), R.layout.include_right_sideslip_layout, this);
        selectList = (ListView) findViewById(R.id.selsectFrameLV);
        backBrand = (ImageView) findViewById(R.id.select_brand_back_im);
        resetBrand = (Button) findViewById(R.id.fram_reset_but);
        mRelateLay = (RelativeLayout) findViewById(R.id.select_frame_lay);
        okBrand = (Button) findViewById(R.id.fram_ok_but);
        resetBrand.setOnClickListener(mOnClickListener);
        okBrand.setOnClickListener(mOnClickListener);
        backBrand.setOnClickListener(mOnClickListener);
        mRelateLay.setOnClickListener(mOnClickListener);
        setUpList();
    }

    private List<AttrList.Attr.Vals> ValsData;

    private List<AttrList.Attr> setUpBrandList(List<AttrList.Attr> mAttrList) {
        if ("作者".equals(mAttrList.get(0).getKey())) {
            ValsData = mAttrList.get(0).getVals();
            mAttrList.get(0).setVals(getValsDatas(mAttrList.get(0).getVals()));
        }
        return mAttrList;
    }

    private AttrList attr;

    /**
     * 解析Json字符串并适配
     */
    private void setUpList() {
        attr = new Gson().fromJson(JsonStr.toString(), AttrList.class);
        if (slidLayFrameAdapter == null) {
            slidLayFrameAdapter = new RightSideslipLayAdapter(mCtx, setUpBrandList(attr.getAttr()));
            selectList.setAdapter(slidLayFrameAdapter);
        } else {
            slidLayFrameAdapter.replaceAll(attr.getAttr());
        }
        slidLayFrameAdapter.setAttrCallBack(new RightSideslipLayAdapter.SelechDataCallBack() {
            @Override
            public void setupAttr(List<String> mSelectData, String key) {

            }
        });

        slidLayFrameAdapter.setMoreCallBack(new RightSideslipLayAdapter.SelechMoreCallBack() {
            @Override
            public void setupMore(List<AttrList.Attr.Vals> mSelectData) {
                getPopupWindow(mSelectData);
                mDownMenu.setOnMeanCallBack(meanCallBack);
            }
        });
    }

    //在第二个页面改变后，返回时第一个界面随之改变，使用的接口回调
    private RightSideslipChildLay.onMeanCallBack meanCallBack = new RightSideslipChildLay.onMeanCallBack() {
        @Override
        public void isDisMess(boolean isDis, List<AttrList.Attr.Vals> mBrandData, String str) {
            if (mBrandData != null) {
                if (attr.getAttr().size() > 0) {
                    ((AttrList.Attr) attr.getAttr().get(0)).setVals(getValsDatas(mBrandData));
                    ((AttrList.Attr) attr.getAttr().get(0)).setShowStr(str);
                }
                slidLayFrameAdapter.replaceAll(attr.getAttr());
            }
            dismissMenuPop();
        }
    };

    private List<AttrList.Attr.Vals> getValsDatas(List<AttrList.Attr.Vals> mBrandData) {
        List<AttrList.Attr.Vals> mVals = new ArrayList<>();
        if (mBrandData != null && mBrandData.size() > 0) {
            for (int i = 0; i < mBrandData.size(); i++) {
                if (mVals.size() >= 8) {
                    AttrList.Attr.Vals valsAdd = new AttrList.Attr.Vals();
                    valsAdd.setV("查看更多 >");
                    mVals.add(valsAdd);
                    continue;
                } else {
                    mVals.add(mBrandData.get(i));
                }
            }
            mVals = mVals.size() >= 9 ? mVals.subList(0, 9) : mVals;
            return mVals;
        }
        return null;
    }

    private OnClickListenerWrapper mOnClickListener = new OnClickListenerWrapper() {
        @Override
        protected void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.fram_reset_but:
                case R.id.select_brand_back_im:
                case R.id.fram_ok_but:
                    menuCallBack.getShowStr(slidLayFrameAdapter);  //把选择的分类回传到主页面
                    menuCallBack.setupCloseMean();  //关闭菜单
                    break;
            }
        }
    };

    /**
     * 关闭窗口
     */
    private void dismissMenuPop() {
        if (mMenuPop != null) {
            mMenuPop.dismiss();
            mMenuPop = null;
        }
    }

    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindow(List<AttrList.Attr.Vals> mSelectData) {
        if (mMenuPop != null) {
            dismissMenuPop();
            return;
        } else {
            initPopuptWindow(mSelectData);
        }
    }

    /**
     * 创建PopupWindow
     */
    private PopupWindow mMenuPop;
    public RightSideslipChildLay mDownMenu;

    protected void initPopuptWindow(List<AttrList.Attr.Vals> mSelectData) {
        mDownMenu = new RightSideslipChildLay(getContext(), ValsData, mSelectData);
        if (mMenuPop == null) {
            mMenuPop = new PopupWindow(mDownMenu, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        }
        mMenuPop.setBackgroundDrawable(new BitmapDrawable());
        mMenuPop.setAnimationStyle(R.style.popupWindowAnimRight);
        mMenuPop.setFocusable(true);
        mMenuPop.showAtLocation(RightSideslipLay.this, Gravity.TOP, 100, UiUtils.getStatusBarHeight(mCtx));
        mMenuPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dismissMenuPop();
            }
        });
    }


    private CloseMenuCallBack menuCallBack;

    /**
     * 定义关闭接口，在NewsFragment实现此接口并做关闭侧滑的操作
     */
    public interface CloseMenuCallBack {
        void setupCloseMean();
        void getShowStr(RightSideslipLayAdapter rightSideslipLayAdapter);
    }

    /**
     * 设置监听
     * */
    public void setCloseMenuCallBack(CloseMenuCallBack menuCallBack) {
        this.menuCallBack = menuCallBack;
    }
}

