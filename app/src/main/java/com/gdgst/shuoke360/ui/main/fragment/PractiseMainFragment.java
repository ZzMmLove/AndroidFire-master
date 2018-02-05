package com.gdgst.shuoke360.ui.main.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.onepractise.PractiseOriginalActivity;
import com.gdgst.shuoke360.ui.main.view.PractiseActivity;
import com.gdgst.shuoke360.ui.main.view.PractiseWriteView;
import com.gdgst.shuoke360.ui.main.view.RecordUtil;
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
     * 默认是1为6个字按钮,如果等于2则为点击9个字按钮
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
    private NormalTitleBar normalTitleBar;
    private PractiseWriteView writeView;
    private Button practisePreviousBtn;
    private Button practiseClearBtn;
    private Button practiseNextBtn;
    private Button buttonNineWordPractise;
    private Button buttonSixWordPractise;
    private TextView textviewHistoryRecorded,textviewTodayRecorded,textviewChioce, textviewFonts, textviewPainSize;
    /**选择练习文章*/
    private  String chioceText;

    public static String getChioceFonts() {
        return chioceFonts;
    }

    /**选择字体*/
    private static  String chioceFonts;
    /**选择笔画的大小*/
    private static int paintSize = 20;

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
        InputStream in = getActivity().getClass().getClassLoader().getResourceAsStream("assets/" + "txt/" + "songci300.txt");//通过反射来读取唐诗宋词300首
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
        //landscape横屏(风景照) ，显示时宽度大于高度；
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
             rootView = inflater.inflate(R.layout.fra_practise, container, false);
        }
        //portrait竖屏 (肖像照) ， 显示时 高 度大于 宽 度
        else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            rootView = inflater.inflate(R.layout.fra_practise, container, false);
        }

        normalTitleBar = (NormalTitleBar) rootView.findViewById(R.id.fra_practise_NormalTitleBar);
        buttonNineWordPractise = (Button) rootView.findViewById(R.id.fra_practise_button_nineWordPractise);
        buttonSixWordPractise = (Button) rootView.findViewById(R.id.fra_practise_button_sixWordPractise);
        textviewHistoryRecorded = (TextView) rootView.findViewById(R.id.fra_practise_textview_historyrecorded);
        textviewTodayRecorded = (TextView) rootView.findViewById(R.id.fra_practise_textview_todayrecorded);
        textviewChioce = (TextView) rootView.findViewById(R.id.fra_practise_textview_chioce);
        textviewFonts = (TextView) rootView.findViewById(R.id.fra_practise_textview_fonts);
        textviewPainSize = (TextView) rootView.findViewById(R.id.fra_practise_textview_paintsize);

        Button button_landscape = (Button) rootView.findViewById(R.id.fra_practise_Button_original);
        button_landscape.setOnClickListener(this);

        resource = getResources();
        packageName = getActivity().getPackageName();
        resetStaticVariable();
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        init();

        buttonNineWordPractise.setOnClickListener(this);
        buttonSixWordPractise.setOnClickListener(this);
        textviewChioce.setOnClickListener(this);
        textviewFonts.setOnClickListener(this);
        textviewPainSize.setOnClickListener(this);

        normalTitleBar.setTvLeftVisiable(false);//ntb 标题栏
        normalTitleBar.setTitleText(getString(R.string.lianzi));
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
                nineWordPractiseIntent.putExtra("chioceText", chioceText);
               // nineWordPractiseIntent.putExtra("chioceFonts", chioceFonts);
                nineWordPractiseIntent.putExtra("paintSize", paintSize);
                startActivity(nineWordPractiseIntent);
                break;
            /**
             * 六个字练习按钮
             */
            case R.id.fra_practise_button_sixWordPractise:
                Intent sixWordPractiseIntent = new Intent(getActivity(), PractiseActivity.class);
                sixWordPractiseIntent.putExtra("isSixOrNineOnClick", 6);
                sixWordPractiseIntent.putExtra("chioceText", chioceText);
               // sixWordPractiseIntent.putExtra("chioceFonts", chioceFonts);
                sixWordPractiseIntent.putExtra("paintSize", paintSize);
                startActivity(sixWordPractiseIntent);
                break;

            case R.id.fra_practise_Button_original:
                Intent simpleWordPractiseIntent = new Intent(getActivity(), PractiseOriginalActivity.class);
                simpleWordPractiseIntent.putExtra("chioceText", chioceText);
               // simpleWordPractiseIntent.putExtra("chioceFonts", chioceFonts);
                startActivity(simpleWordPractiseIntent);
                break;

            case R.id.fra_practise_textview_chioce:
                showPoppuWindow();
                break;

            case R.id.fra_practise_textview_fonts:
                showPoppuWindowForFonts();
                break;

            case R.id.fra_practise_textview_paintsize:
                showPoppuWindowForPaintSize();
        }
    }

    private void showPoppuWindowForPaintSize() {
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.practiseactivity_inflater_fontsize, null);
        final PopupWindow popupWindow = new PopupWindow(getContext());
        popupWindow.setHeight(400);
        popupWindow.setWidth(400);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        RadioGroup rg = (RadioGroup) convertView.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton20:
                        paintSize = 20;
                        textviewPainSize.setText("最细");
                        break;
                    case R.id.radioButton35:
                        paintSize = 35;
                        textviewPainSize.setText("中等");
                        break;
                    case R.id.radioButton45:
                        paintSize = 45;
                        textviewPainSize.setText("粗");
                        break;
                    case R.id.radioButton50:
                        paintSize = 50;
                        textviewPainSize.setText("较粗");
                        break;
                }
                popupWindow.dismiss();
            }
        });

        popupWindow.setContentView(convertView);
        popupWindow.showAsDropDown(textviewPainSize);
    }

    private void showPoppuWindowForFonts() {

        final String [] data = {"hmmb", "楷书", "隶书", "行楷"};
        ListView listView;
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.poppuwindow_choice, null);
        final PopupWindow poppuWindonw = new PopupWindow(getContext());
        poppuWindonw.setBackgroundDrawable(new BitmapDrawable());
        poppuWindonw.setFocusable(true);
        poppuWindonw.setOutsideTouchable(true);
        poppuWindonw.setContentView(convertView);
        poppuWindonw.setWidth(400);
        poppuWindonw.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        listView = (ListView) convertView.findViewById(R.id.listview_poppu_chioce);
        listView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.poppuwidow_choice_item, R.id.list_item, data));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        chioceFonts = "fonts/hmmb.ttf";
                        textviewFonts.setText(data[0]);
                        break;
                    case 1:
                        chioceFonts = "fonts/kaishu.ttf";
                        textviewFonts.setText(data[1]);
                        break;
                    case 2:
                        chioceFonts = "fonts/lishu.ttf";
                        textviewFonts.setText(data[2]);
                        break;
                    case 3:
                        chioceFonts = "fonts/xingkai.ttf";
                        textviewFonts.setText(data[3]);
                        break;
                }
                poppuWindonw.dismiss();
            }
        });
        poppuWindonw.showAsDropDown(textviewFonts);
    }

    private void showPoppuWindow() {
        final String [] data = {"梦想", "名人名言", "普通500字", "普通2500字", "宋词300首", "唐诗300首" };
        ListView listView;
        View convertView = LayoutInflater.from(getContext()).inflate(R.layout.poppuwindow_choice, null);
        final PopupWindow poppuWindonw = new PopupWindow(getContext());
        poppuWindonw.setBackgroundDrawable(new BitmapDrawable());
        poppuWindonw.setFocusable(true);
        poppuWindonw.setOutsideTouchable(true);
        poppuWindonw.setContentView(convertView);
        poppuWindonw.setWidth(400);
        poppuWindonw.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        listView = (ListView) convertView.findViewById(R.id.listview_poppu_chioce);
        listView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.poppuwidow_choice_item, R.id.list_item, data));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        chioceText = "dream.txt";
                        textviewChioce.setText(data[0]);
                        break;
                    case 1:
                        chioceText = "famous_words.txt";
                        textviewChioce.setText(data[1]);
                        break;
                    case 2:
                        chioceText = "fontlib500.txt";
                        textviewChioce.setText(data[2]);
                        break;
                    case 3:
                        chioceText = "fontlib2500.txt";
                        textviewChioce.setText(data[3]);
                        break;
                    case 4:
                        chioceText = "songci300.txt";
                        textviewChioce.setText(data[4]);
                        break;
                    case 5:
                        chioceText = "tangshi300.txt";
                        textviewChioce.setText(data[5]);
                }
                poppuWindonw.dismiss();
            }
        });
        poppuWindonw.showAsDropDown(textviewChioce);

    }
}
