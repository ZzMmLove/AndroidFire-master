package com.gdgst.shuoke360.ui.main.activity;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.bean.SearchBean;
import com.gdgst.shuoke360.db.RecordSQLiteOpenHelper;
import com.gdgst.shuoke360.ui.main.adapter.SearchAdapter;
import com.gdgst.shuoke360.ui.main.contract.SearchContract;
import com.gdgst.shuoke360.ui.main.model.SearchModel;
import com.gdgst.shuoke360.ui.main.presenter.SearchPresenter;
import com.gdgst.common.base.BaseActivity;
import com.gdgst.common.commonutils.ToastUitl;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 搜索页面
 */
public class SearchActivity extends BaseActivity<SearchPresenter, SearchModel> implements SearchContract.View, OnRefreshListener, OnLoadMoreListener {
    /**热门搜索*/
    @Bind(R.id.search_flowlayout)
    TagFlowLayout mflowLayout;
    /**搜索词输入框*/
    @Bind(R.id.et_search)
    EditText etSearch;
    /**搜索按钮*/
    @Bind(R.id.tv_search)
    TextView tvSearch;
    /**显示搜索结果的RecycleView*/
    @Bind(R.id.irc_content)
    IRecyclerView iRecyclerView;
    /**历史搜索*/
    @Bind(R.id.lv_history)
    ListView lvHistory;
    /**清除搜索关键词*/
    @Bind(R.id.iv_clear)
    ImageView ivClear;
    /**返回按钮*/
    @Bind(R.id.iv_back)
    ImageView ivBack;
    /**清空搜索历史*/
    @Bind(R.id.iv_delete)
    ImageView ivDelete;

    private String[] mVals = new String[]{"碑帖", "王羲之", "书法", "讲义", "欧阳询", "写法", "王献之"};
    private RecordSQLiteOpenHelper helper;
    private SimpleCursorAdapter adapter;
    private SQLiteDatabase db;
    private List<SearchBean> datas = new ArrayList<>();
    private SearchAdapter searchAdapter;
    private int mStartPage;
    private String searchKey;


    @Override
    public int getLayoutId() {
       return R.layout.activity_search;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initView() {

        iRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchAdapter = new SearchAdapter(this, R.layout.item_news, datas);
        //为每一个item设置一个动画
        searchAdapter.openLoadAnimation(new ScaleInAnimation());
        iRecyclerView.setAdapter(searchAdapter);
        iRecyclerView.setOnRefreshListener(this);
        iRecyclerView.setOnLoadMoreListener(this);

        helper = new RecordSQLiteOpenHelper(this);
        queryHistory("");
        /**流式布局设置数据*/
        mflowLayout.setAdapter(new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView itemText = new TextView(SearchActivity.this);
                itemText.setBackgroundResource(R.drawable.search_seletor);
                itemText.setText(s);
                return itemText;
            }
        });

        /**流式布局设置item标签的点击事件*/
        mflowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
//                ToastUitl.show(mVals[position], 0);
                etSearch.setText(mVals[position]);
                return true;
            }
        });

        /**输入框的触摸事件*/
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                etSearch.setFocusable(true);
                etSearch.setFocusableInTouchMode(true);
                tvSearch.setVisibility(View.VISIBLE);
                return false;
            }
        });

        /**搜索按钮的点击事件*/
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchKey = etSearch.getText().toString();
                iRecyclerView.setVisibility(View.VISIBLE);
                lvHistory.setVisibility(View.GONE);
                mStartPage = 1;
                mPresenter.loadSearchResult("gstshuoke360", searchKey, mStartPage);
                if (searchKey != null || "".equals(searchKey)){
                    if (!hasData(searchKey)){
                        insertHistory(searchKey);
                        queryHistory("");
                    }
                }
//                ToastUitl.show(searchKey, 0);
            }
        });

        /**输入框内的删除图标*/
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                iRecyclerView.setVisibility(View.GONE);
                lvHistory.setVisibility(View.VISIBLE);
            }
        });

        /**返回按钮的点击事件*/
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**清空搜索历史*/
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteHistory();
                queryHistory("");
            }
        });
    }

    /**将搜索关键词放到数据库中*/
    private void insertHistory(String name){
        db = helper.getWritableDatabase();
        db.execSQL("insert into search_history(name) values('"+name+"')");
        db.close();
    }

    /**判断是否存在某条历史搜索*/
    private boolean hasData(String name){
        Cursor cursor = helper.getReadableDatabase().rawQuery("select id as _id , name from search_history where name = ?", new String[]{name});
        return cursor.moveToNext();
    }

    /**从数据库查出历史搜索*/
    private void queryHistory(String name){
        //模糊搜索
        Cursor cursor = helper.getReadableDatabase().rawQuery("select id as _id , name from search_history where name like '%"+name+"%' order by id desc", null);
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"name"}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lvHistory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String str = textView.getText().toString();
                etSearch.setText(str);
            }
        });
    }
    /**删除历史搜索*/
    private void deleteHistory(){
        db = helper.getWritableDatabase();
        db.execSQL("delete from search_history");
        db.close();
    }

    @Override
    public void onRefresh() {
        searchAdapter.getPageBean().setRefresh(true);
        mStartPage = 1;
        //发起请求
        iRecyclerView.setRefreshing(true);
        mPresenter.loadSearchResult("gstshuoke360", searchKey, mStartPage);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        searchAdapter.getPageBean().setRefresh(false);
        //发起请求
        iRecyclerView.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.loadSearchResult("gstshuoke360", searchKey, mStartPage);
    }

    @Override
    public void returnSearchResult(List<SearchBean> searchBean) {

        if (searchBean != null){
            mStartPage += 1;
            if (searchAdapter.getPageBean().isRefresh()){
                iRecyclerView.setRefreshing(false);
                searchAdapter.replaceAll(searchBean);
            }else {
                if (searchBean.size() > 0){
                    iRecyclerView.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    searchAdapter.addAll(searchBean);
                }else {
                    iRecyclerView.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
        }
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }
}
