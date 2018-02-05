package com.gdgst.shuoke360.onepractise;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.gdgst.shuoke360.R;
import com.gdgst.shuoke360.ui.main.view.PractiseWriteViewListener;
import com.gdgst.shuoke360.ui.main.view.RecordUtil;
import com.gdgst.common.commonwidget.NormalTitleBar;

import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 字帖练字界面
 *
 * @author kelvin
 */
public class PractiseOriginalActivity extends Activity {

    @Bind(R.id.practise_write_view)
    WriteView writeView;
    @Bind(R.id.practise_previous_btn)
    Button lastButton;
    @Bind(R.id.practise_clear_btn)
    Button clearButton;
    @Bind(R.id.practise_next_btn)
    Button nextButton;

    private NormalTitleBar normalTitleBar;


    private static int SCREEN_HEIGHT = 0;
    private static int SCREEN_WIDTH = 0;

    private static String words = null;
    private static String curWord = null;
    private static int curWordIndex = 0;
    private static int wordSum = 0;
    private static int writeSum = 0;

    //public static final String DEFAULT_SUFFIX = ".txt";
    public static final String ENCODING = "utf-8";
    //private static final String DEFAULT_TRAINING_FONTLIB = "fontlib500";
    //private static final String DEFAULT_TRAINING_FONTLIB_FOLDER = "txt/";
    /**练字的文章*/
    private static String chioceTextResouce = null;
    /**访问系统的资源文件的类*/
    private static Resources resource;

    private static String packageName;

    private static final String DEFAULT_STRING_TYPE = "string";

    private static String curLibName = null;

//    private PractiseWriteView writeView;

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

    public void init() {
        normalTitleBar = (NormalTitleBar) findViewById(R.id.practise_frament_layout_origin_NormalTitleBar);
        normalTitleBar.setBackVisibility(true);
        normalTitleBar.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PractiseOriginalActivity.this.finish();
            }
        });

        writeView.addPractiseWriteViewListener(new PractiseWriteViewListener()
        {
            public void isShare() {
            }

        });

        try {
            readFromAssertFileByFileName();
            curWord = words.substring(curWordIndex, curWordIndex + 1);
            Log.i("read name:", words);
            Log.i("read curWord:", curWord);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置上一个字
     * @return
     */
    private static boolean setPreviousWord() {
        if (curWordIndex == 0) {
            return false;
        }

        curWordIndex--;
        curWord = words.substring(curWordIndex, curWordIndex + 1);
        return true;
    }

    /**
     * 设置下一个字
     * @return
     */
    private static boolean setNextWord() {
        if (curWordIndex == wordSum - 1)
            return false;

        curWordIndex++;
        curWord = words.substring(curWordIndex, curWordIndex + 1);
        return true;
    }
    public static String getCurWord() {
        return curWord;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay();
        SCREEN_HEIGHT = display.getHeight();// 设置屏幕高度
        SCREEN_WIDTH = display.getWidth();// 设置屏幕宽度
        super.onCreate(savedInstanceState);
        resource = getResources();
        packageName = getPackageName();
        resetStaticVariable();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.practise_frament_layout_original);
        ButterKnife.bind(this);
        chioceTextResouce = getIntent().getStringExtra("chioceText");
        if (chioceTextResouce == null){
            chioceTextResouce = "dream.txt";
        }
        init();

    }

    private void readFromAssertFileByFileName()
            throws IOException {
//        InputStream in = getResources().getAssets().open( DEFAULT_TRAINING_FONTLIB_FOLDER + filename + DEFAULT_SUFFIX);
        //读取assets文件夹中的TXT文章文件
        InputStream in = PractiseOriginalActivity.this.getClass().getClassLoader().getResourceAsStream("assets/" + "txt/" + chioceTextResouce);


        // 获取文件的字节数
        int length = in.available();
        // 创建byte数组
        byte[] buffer = new byte[length];
        // 将文件中的数据读到byte数组中
        in.read(buffer);
        words = EncodingUtils.getString(buffer, ENCODING);
    }


    @Override
    protected void onDestroy() {
        if (wordSum > 0) {
            // 写入数据库
            if (!RecordUtil.addWriteSumToDb(PractiseOriginalActivity.this, writeSum))
                Toast.makeText(PractiseOriginalActivity.this,
                        "数据库出错", Toast.LENGTH_SHORT).show();
        }

        super.onDestroy();

    }

    @OnClick(R.id.practise_clear_btn)
    public  void setClearButton(){
        WriteView.clear();
    }
    @OnClick(R.id.practise_previous_btn)
    public  void  setLastButton(){
//        PractiseWriteView.clear();
//        curWordIndex--;
//        curWord = words.substring(curWordIndex, curWordIndex + 1);
        if (setPreviousWord())
            if (WriteView.ifWrite()) {
                writeSum++;
                WriteView.clear();
            }

        Log.i("next word", curWord);
    }
    @OnClick(R.id.practise_next_btn)

    public void nextButton() {
//        PractiseWriteView.clear();
//        curWordIndex++;
//        curWord = words.substring(curWordIndex, curWordIndex + 1);
        if (setNextWord())
            if (WriteView.ifWrite()) {
                writeSum++;
                WriteView.clear();
            }
        Log.i("next word", curWord);

    }


    public static int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }

    public static int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    //    public static String getFontLibName(Context context) {
//        String fontLibName = "";
//        fontLibName += context.getString(resource.getIdentifier(curLibName,
//                DEFAULT_STRING_TYPE, packageName));
//
////		Log.i("curLibName", curLibName);
//        fontLibName += ": ";
//        fontLibName += ((curWordIndex + 1) + "/" + wordSum);
//
//        return fontLibName;
//    }

}
