package com.gdgst.androidfire.ui.main.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gdgst.androidfire.R;
import com.gdgst.androidfire.practise.PractiseOriginalActivity;
import com.gdgst.androidfire.practise.PractiseOriginalOneActivity;
import com.gdgst.androidfire.ui.main.view.PractiseActivity;
import com.gdgst.androidfire.ui.main.view.PractiseWriteView;
import com.gdgst.androidfire.ui.main.view.RecordUtil;
import com.gdgst.common.commonwidget.NormalTitleBar;

import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * des:练字首页
 * Created by Don on 2016/10/19.
 * * * * * * * * * * * * * * * * * * * *
 * PractiseMainFragment内部的执行流程  *
 * 1->onCreateView();                  *
 * 2->init();                          *
 * 3->readFromAssertFileByFileName()   *
 * * * * * * * * * * * * * * * * * * * *
 */

public class PractiseMainFragment extends Fragment implements View.OnClickListener {

    /**
     * isSixOrNineOnClick用于判断是点击6个字练习按钮还是点击9个字练习按钮
     * 默认是1为6个字按钮,如何等于2则为点击9个字按钮
     */
    //public int isSixOrNineOnClick = 1;
    private static String words = null;
    private static String curWord = null;
    private static int curWordIndex = 0;
    private static int wordSum = 0;
    private static int writeSum = 0;

    public static final String DEFAULT_SUFFIX = ".txt";
    public static final String ENCODING = "utf-8";
    private static final String DEFAULT_TRAINING_FONTLIB = "fontlib500";
    private static final String DEFAULT_TRAINING_FONTLIB_FOLDER = "txt/";

    private static Resources resource;
    private static String packageName;

    private static final String DEFAULT_STRING_TYPE = "string";

    private static String curLibName = null;
    View view;

    private NormalTitleBar ntb;
    private PractiseWriteView writeView;
    private Button practisePreviousBtn;
    private Button practiseClearBtn;
    private Button practiseNextBtn;
    private Button buttonNineWordPractise;
    private Button buttonSixWordPractise;
    private TextView textviewHistoryRecorded,textviewTodayRecorded;


    public static void writeSumPlusPlus() {
        writeSum++;
    }

    private static void resetStaticVariable() {
        words = null;
        curWord = null;
        curWordIndex = 0;
        wordSum = 0;
        writeSum = 0;
        curLibName = null;
    }


    private static boolean setPreviousWord() {
        if (curWordIndex == 0) {
            return false;
        }

        curWordIndex--;
        curWord = words.substring(curWordIndex, curWordIndex + 4);
        return true;
    }

    private static boolean setNextWord() {
        if (curWordIndex == wordSum - 1)
            return false;

        curWordIndex++;
        curWord = words.substring(curWordIndex, curWordIndex + 4);
        return true;
    }


    public static String getCurWord() {
        return curWord;
    }


    private void readFromAssertFileByFileName()
            throws IOException {
//        InputStream in = getResources().getAssets().open( DEFAULT_TRAINING_FONTLIB_FOLDER + filename + DEFAULT_SUFFIX);
        InputStream in = getActivity().getClass().getClassLoader().getResourceAsStream("assets/" + "txt/" + "songci300.txt");


        // 获取文件的字节数
        int length = in.available();
        // 创建byte数组
        byte[] buffer = new byte[length];
        // 将文件中的数据读到byte数组中
        in.read(buffer);
        words = EncodingUtils.getString(buffer, ENCODING);  //ENCODING为utf-8
    }


    @Override
    public void onDestroy() {
        if (wordSum > 0) {
            // 写入数据库
            if (!RecordUtil.addWriteSumToDb(getActivity(), writeSum))
                Toast.makeText(getActivity(),
                        "数据库出错", Toast.LENGTH_SHORT).show();
        }
        super.onDestroy();
    }

    public void init() {

        /*writeView.addPractiseWriteViewListener(new PractiseWriteViewListener()
        {
            public void isShare() {
            }

        });*/

        try {
            readFromAssertFileByFileName();
            curWord = words.substring(curWordIndex, curWordIndex + 4);
            Log.v("jenfei","设置当前的字体");
            Log.i("read name:", words);
            Log.i("read curWord:", curWord);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
         View rootView = null;
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
             rootView = inflater.inflate(R.layout.fra_practise, container, false);
        }
        else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            rootView = inflater.inflate(R.layout.fra_practise, container, false);
        }

        ntb = (NormalTitleBar) rootView.findViewById(R.id.ntb);
        //writeView = (PractiseWriteView) rootView.findViewById(R.id.practise_write_view);
        //practisePreviousBtn = (Button) rootView.findViewById(R.id.practise_previous_btn);
        //practiseClearBtn = (Button) rootView.findViewById(R.id.practise_clear_btn);
        //practiseNextBtn = (Button) rootView.findViewById(R.id.practise_next_btn);
        buttonNineWordPractise = (Button) rootView.findViewById(R.id.fra_practise_button_nineWordPractise);
        buttonSixWordPractise = (Button) rootView.findViewById(R.id.fra_practise_button_sixWordPractise);
        textviewHistoryRecorded = (TextView) rootView.findViewById(R.id.fra_practise_textview_historyrecorded);
        textviewTodayRecorded = (TextView) rootView.findViewById(R.id.fra_practise_textview_todayrecorded);

        Button button_landscape = (Button) rootView.findViewById(R.id.fra_practise_Button_original);
        button_landscape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PractiseOriginalActivity.class));
            }
        });
        Button button_original_one = (Button) rootView.findViewById(R.id.fra_practise_Button_original_one);
        button_original_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PractiseOriginalOneActivity.class));
            }
        });

        resource = getResources();
        packageName = getActivity().getPackageName();
        resetStaticVariable();
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        init();

//        practisePreviousBtn.setOnClickListener(this);
//        practiseClearBtn.setOnClickListener(this);
//        practiseNextBtn.setOnClickListener(this);
        buttonNineWordPractise.setOnClickListener(this);
        buttonSixWordPractise.setOnClickListener(this);

        ntb.setTvLeftVisiable(false);//ntb 标题栏
        ntb.setTitleText(getString(R.string.lianzi));

        return rootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.practise_previous_btn:
                if (setPreviousWord())
                    if (PractiseWriteView.ifWrite()) {
                        writeSum++;
                        PractiseWriteView.clear();
                    }

                Log.i("next word", curWord);
                break;
            case R.id.practise_clear_btn:
                PractiseWriteView.clear();
                break;
            case R.id.practise_next_btn:
                if (setNextWord())
                    if (PractiseWriteView.ifWrite()) {
                        writeSum++;
                        PractiseWriteView.clear();
                    }
                Log.i("next word", curWord);
                break;
            /**
             * 九个字练习按钮
             */
            case R.id.fra_practise_button_nineWordPractise:
                Intent nineWordPractiseIntent = new Intent(getActivity(), PractiseActivity.class);
                nineWordPractiseIntent.putExtra("isSixOrNineOnClick", 9);
                startActivity(nineWordPractiseIntent);
                break;
            /**
             * 六个字练习按钮
             */
            case R.id.fra_practise_button_sixWordPractise:
                Intent sixWordPractiseIntent = new Intent(getActivity(), PractiseActivity.class);
                sixWordPractiseIntent.putExtra("isSixOrNineOnClick", 6);
                startActivity(sixWordPractiseIntent);
                break;
        }
    }
}
