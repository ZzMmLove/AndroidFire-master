package com.gdgst.androidfire.ui.main.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.gdgst.androidfire.R;

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
public class PractiseActivity extends Activity {

    @Bind(R.id.practise_write_view)
    WriteView writeView;
    @Bind(R.id.practise_frament_layout_preview)
    Button lastButton;
    @Bind(R.id.practise_frament_layout_clean)
    Button clearButton;
    @Bind(R.id.practise_frament_layout_next)
    Button nextButton;

    private Button mButtonThree,mButtonSix;
    private static int isSixWordExerciseOrNine = 6;
    public static int isSixOrNineOnClick;

    private static int SCREEN_HEIGHT = 0;
    private static int SCREEN_WIDTH = 0;

    private static String words = null;
    private static String curWord = null;
    private static String curWord9 = null;
    private static int curWordIndex = 0;
    private static int curWordIndex9 = 0;
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

    private Intent intent;
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

        writeView.addPractiseWriteViewListener(new PractiseWriteViewListener()
        {
            public void isShare() {
            }

        });

        try {
            readFromAssertFileByFileName();
            if (isSixOrNineOnClick == 6) {
                curWord = words.substring(curWordIndex, curWordIndex + 6);
                wordSum = words.length();
                Log.v("jenfee's", "练习的字的总数:"+String.valueOf(wordSum));
            } else if (isSixOrNineOnClick == 9) {
                curWord9 = words.substring(curWordIndex9, curWordIndex9 + 9);
                wordSum = words.length();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置上一个字
     * @return
     */
    private static boolean setPreviousWord() {
        if (isSixOrNineOnClick == 6) {
            if (curWordIndex == 0) {
                return false;
            }
        } else if (isSixOrNineOnClick == 9) {
            if (curWordIndex9 == 0) {
                return false;
            }
        }

        if (isSixOrNineOnClick == 6) {
            curWordIndex -= 6;
            curWord = words.substring(curWordIndex, curWordIndex + 6);
        } else if (isSixOrNineOnClick == 9) {
            curWordIndex9 -= 9;
            curWord9 = words.substring(curWordIndex9, curWordIndex9 + 9);
        }

        return true;
    }

    /**
     * 设置下一个字
     * @return
     */
    private static boolean setNextWord() {
        if (isSixOrNineOnClick == 6) {
            int remaining = 0;
            remaining = wordSum - curWordIndex;
            Log.v("jenfee's", "6个字练习剩下的字数:"+String.valueOf(remaining));
            if (remaining < 6)
                return false;
        } else if (isSixOrNineOnClick == 9) {
            if (curWordIndex9 == wordSum - 1)
                return false;
        }

        if (isSixOrNineOnClick == 6) {
            curWordIndex += 6;
            curWord = words.substring(curWordIndex, curWordIndex + 6);
        } else if (isSixOrNineOnClick == 9){
            curWordIndex9 += 9;
            curWord9 = words.substring(curWordIndex9, curWordIndex9 + 9);
        }

        return true;
    }
    public static String getCurWord() {
        return curWord;
    }

    public static String getCurWord9() {
        return curWord9;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Display display = getWindowManager().getDefaultDisplay();
        SCREEN_HEIGHT = display.getHeight();// 设置屏幕高度
        SCREEN_WIDTH = display.getWidth();// 设置屏幕宽度

        super.onCreate(savedInstanceState);

        intent = getIntent();
        isSixOrNineOnClick = intent.getIntExtra("isSixOrNineOnClick", 0);

        resource = getResources();
        packageName = getPackageName();
        resetStaticVariable();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.practise_frament_layout);
        ButterKnife.bind(this);

        init();
        //mButtonThree = (Button) findViewById(R.id.practise_frament_layout_3);
        //mButtonThree.setOnClickListener(this);
        //mButtonSix = (Button) findViewById(R.id.practise_frament_layout_6);
        //mButtonSix.setOnClickListener(this);
    }

    private void readFromAssertFileByFileName()
            throws IOException {
//        InputStream in = getResources().getAssets().open( DEFAULT_TRAINING_FONTLIB_FOLDER + filename + DEFAULT_SUFFIX);
        InputStream in = PractiseActivity.this.getClass().getClassLoader().getResourceAsStream("assets/" + "txt/" + "fontlib500.txt");


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
            if (!RecordUtil.addWriteSumToDb(PractiseActivity.this, writeSum))
                Toast.makeText(PractiseActivity.this,
                        "数据库出错", Toast.LENGTH_SHORT).show();
        }

        super.onDestroy();
        if (words != null) {
            words = null;
        }
    }

    @OnClick(R.id.practise_frament_layout_clean)
    public  void setClearButton(){
        WriteView.clear();
    }

    @OnClick(R.id.practise_frament_layout_preview)
    public  void  setLastButton(){
//        PractiseWriteView.clear();
//        curWordIndex--;
//        curWord = words.substring(curWordIndex, curWordIndex + 1);
        if (setPreviousWord())
            if (WriteView.ifWrite()) {
                writeSum++;
                WriteView.clear();
            }

       // Log.i("next word", curWord);
    }

    @OnClick(R.id.practise_frament_layout_next)
    public void nextButton() {
//        PractiseWriteView.clear();
//        curWordIndex++;
//        curWord = words.substring(curWordIndex, curWordIndex + 1);
        if (setNextWord())
            if (WriteView.ifWrite()) {
                writeSum++;
                WriteView.clear();
            }
        /**
         * *********************************************************
         */
        //Log.i("next word", curWord);//此处有空指针异常
        /**
         * *********************************************************
         */
    }


    public static int getSCREEN_HEIGHT() {
        return SCREEN_HEIGHT;
    }

    public static int getSCREEN_WIDTH() {
        return SCREEN_WIDTH;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_practise_menu, menu);
        return super.onCreateOptionsMenu(menu);
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

    @Override
    protected void onResume() {
        // 设置为横屏
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
/**
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.practise_frament_layout_3:
                isSixWordExerciseOrNine = 6;
                break;
            case R.id.practise_frament_layout_6:
                isSixWordExerciseOrNine = 9;
                break;
            default:
                break;
        }
    }
**/
    public static int getIsThreeWordOrSix() {
        return isSixWordExerciseOrNine;
    }

}
