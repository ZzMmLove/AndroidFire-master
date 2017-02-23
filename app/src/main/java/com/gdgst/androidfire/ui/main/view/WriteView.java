package com.gdgst.androidfire.ui.main.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.gdgst.androidfire.utils.MyUtils;

/**
 * 字帖练字界面写字板
 *
 * @author kelvin
 */
public class WriteView extends SurfaceView implements
        SurfaceHolder.Callback {

    /**
     * 文字画笔
     */
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 边框画笔
     */
    private Paint framePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 虚线画笔
     */
    private Paint dotLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 虚线效果
     */
    private PathEffect dotLineEffect = new DashPathEffect(new float[]{5, 5,
            5, 5}, 1);

    /**
     * 手绘画笔
     */
    private Paint handwritingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint fontLibPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 字帖自定义字体变量
     */
    private Typeface copyBookFace;

    /**
     * 钢笔自定义字体变量
     */
    private Typeface penFace;

    /**
     * 文字边框左上角X坐标
     */
    private float leftTopX;

    /**
     * 文字边框左上角Y坐标
     */
    private float leftTopY;

    /**
     * 文字边框左下角X坐标
     */
    private float leftBottomX;

    /**
     * 文字边框左下角Y坐标
     */
    private float leftBottomY;

    /**
     * 文字边框右上角X坐标
     */
    private float rightTopX;

    /**
     * 文字边框右上角Y坐标
     */
    private float rightTopY;

    /**
     * 文字边框右下角X坐标
     */
    private float rightBottomX;

    /**
     * 文字边框右下角Y坐标
     */
    private float rightBottomY;

    /**
     * 默认线段粗细
     */
    private static final int DEFAULT_STROKE_WIDTH = 1;

    /**
     * 默认手绘笔迹粗细
     */
    private static final int DEFAULT_HANDWRTING_STROKE_WIDTH = 35;

    private static final int DEFAULT_FONGLIB_TEXT_SIZE = 25;

    /**
     * 默认文字画笔颜色
     */
    private static final int DEFAULT_TEXT_PAINT_COLOR = 0xFFEB8B75;

    /**
     * 默认线段画笔颜色
     */
    private static final int DEFAULT_LINE_PAINT_COLOR = Color.RED;

    /**
     * 默认画布颜色
     */
    private static final int DEFAULT_CANVAS_COLOR = Color.WHITE;

    /**
     * 默认钢笔颜色
     */
    private static final int DEFAULT_PEN_COLOR = Color.BLACK;

    /**
     * 默认文字大小
     */
    private int DEFAULT_TEXT_SIZE; // 屏幕宽度的3/4

    /**
     * 描文字初始X坐标
     */
    private int DEFAULT_START_X; // 应该是屏幕宽度的1/8

    /**
     * 描文字初始Y坐标
     */
    private int DEFAULT_START_Y; // 屏幕高度的1/2

    /**
     * SurfaceHolder
     */
    private SurfaceHolder surfaceHolder;

    /**
     * 文字切换刷新线程
     */
    private WordChangeThread wordChangeThread;

    // private static final String SRC_PEN_FONT = "fonts/gangbi.ttf";

    // private static final String SRC_KAITI_FONT = "fonts/kaiti.ttf";

    private static final String SRC_XINGKAI_FONT = "fonts/xingkai.ttf";

    private static final String DEFAULT_FONT_DEMO = "字";

    private static Path path;

    private PractiseWriteViewListener practiseWriteViewListener;

    private Context context;

    /**
     * 是6个字练习还是9个字练习，此数据用于设置默认字体大小时判断
     * 0是6个字练习
     * 1是9个字练习
     */
    private int isSixWordOrNineWord = 0;

    /**
     * 初始化屏幕参数（宽度和高度）
     */
    private void initScreenParams() {
        int screenHeight = PractiseActivity.getSCREEN_HEIGHT();
        int screenWidth = PractiseActivity.getSCREEN_WIDTH();
        //这是竖屏测试的屏幕高度和宽度
        //screenHeight:1280
        //screenWidth:720

        //这是横屏测试的屏幕高度和宽度
        //screenHeight:1280
        //screenWidth:720
        //screenHeight:720
        //screenWidth:1280

        //这是平板的屏幕宽高参数
        //screenHeight:1964
        //screenWidth:1536
        //screenHeight:1452
        //screenWidth:2048
        /**
         * 当屏幕宽度是超过2000像素高度接近1500时,默认字体的大小是：screenWidth * 3 / 17==355左右,默认X轴开始位置333,默认Y轴开发位置363
         */
        //这是普通手机的屏幕宽高参数
        //screenHeight:1280
        //screenWidth:720
        //screenHeight:720
        //screenWidth:1280

        /**
         * if (PractiseActivity.isSixOrNineOnClick == 6) {
         * DEFAULT_TEXT_SIZE = screenWidth * 3 / 14;
         * DEFAULT_START_X = screenWidth / 9;
         * DEFAULT_START_Y = screenHeight / 3;
         * }else if(PractiseActivity.isSixOrNineOnClick == 9){
         * DEFAULT_TEXT_SIZE = screenWidth * 3 / 17;
         * DEFAULT_START_X = screenWidth / 8;
         * DEFAULT_START_Y = screenHeight / 4;
         * }
         * isPdaOrNormal = 1;
         */

        if (PractiseActivity.isSixOrNineOnClick == 6) {
            /*DEFAULT_TEXT_SIZE = screenWidth * 3 / 16;
            DEFAULT_START_X = screenWidth / 9;
            DEFAULT_START_Y = screenHeight / 3;*/
            DEFAULT_TEXT_SIZE = screenHeight * 3 / 8;

            int a = ((DEFAULT_TEXT_SIZE*3)+140)/2;
            int b = screenWidth / 2;
            int testStartX = b - a;

            DEFAULT_START_X = testStartX;//screenWidth / 8;
            DEFAULT_START_Y = screenHeight / 3;
        }else if(PractiseActivity.isSixOrNineOnClick == 9){
            /*DEFAULT_TEXT_SIZE = screenWidth * 3 / 17;
            DEFAULT_START_X = screenWidth / 8;
            DEFAULT_START_Y = screenHeight / 4;*/
            DEFAULT_TEXT_SIZE = screenHeight * 3 / 12;

            int a = ((DEFAULT_TEXT_SIZE*3)+140)/2;
            int b = screenWidth / 2;
            int testStartX = b - a;

            DEFAULT_START_X = testStartX;//screenWidth / 4;
            DEFAULT_START_Y = screenHeight / 4;
        }

        // screenHeight:1964
        // screenWidth:1536

        Log.i("JenfeeMa", "screenHeight:"+screenHeight);
        Log.i("JenfeeMa", "screenWidth:"+screenWidth);
        Log.i("2DEFAULT_TEXT_SIZE:",DEFAULT_TEXT_SIZE+"");
        Log.i("2DEFAULT_START_X:",DEFAULT_START_X+"");
        Log.i("2DEFAULT_START_Y:",DEFAULT_START_Y+"");
    }


    /**
     * 初始化画笔
     */
    private void initPaint() {
        // 初始化文字画笔
        textPaint.setTextSize(DEFAULT_TEXT_SIZE); // 设置字体大小
        textPaint.setColor(DEFAULT_TEXT_PAINT_COLOR); // 设置画笔颜色
        textPaint.setTypeface(copyBookFace); // 绘制自定义字体
        textPaint.setStrokeWidth(6);//设置画笔的粗细

        // 初始化边框画笔
        framePaint.setStrokeWidth(DEFAULT_STROKE_WIDTH); // 设置画笔粗细
        framePaint.setColor(DEFAULT_LINE_PAINT_COLOR); // 设置画笔颜色

        // 初始化虚线画笔
        dotLinePaint.setStrokeWidth(DEFAULT_STROKE_WIDTH); // 设置画笔粗细
        dotLinePaint.setStyle(Paint.Style.STROKE); // 设置画笔风格
        dotLinePaint.setColor(DEFAULT_LINE_PAINT_COLOR); // 设置画笔颜色
        dotLinePaint.setPathEffect(dotLineEffect); // 设置虚线效果

        // 初始化手绘画笔
        handwritingPaint.setStrokeWidth(DEFAULT_HANDWRTING_STROKE_WIDTH); // 设置画笔粗细
        handwritingPaint.setStyle(Paint.Style.STROKE); // 设置画笔风格

        handwritingPaint.setTypeface(penFace);
        handwritingPaint.setStrokeJoin(Paint.Join.BEVEL);
        handwritingPaint.setColor(DEFAULT_PEN_COLOR); // 设置画笔颜色
        handwritingPaint.setStrokeCap(Paint.Cap.BUTT);

        path = new Path();

        fontLibPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        fontLibPaint.setColor(DEFAULT_PEN_COLOR);
        fontLibPaint.setTextSize(DEFAULT_FONGLIB_TEXT_SIZE);
    }

    /**
     * 初始化View默认方法
     *
     * @param context 上下文
     */
    private void initView(Context context) {

        // 实例化字帖自定义字体 (需读取用户设置文件来读取字体信息)
        copyBookFace = Typeface.createFromAsset(context.getAssets(),
                SRC_XINGKAI_FONT);

        penFace = Typeface.createFromAsset(context.getAssets(),
                SRC_XINGKAI_FONT);

        initPaint();

        initCoordinate();

//        setBackgroundColor(Color.YELLOW);


    }

    //初始化坐标
    private void initCoordinate() {
        Paint.FontMetrics fm = textPaint.getFontMetrics();

        leftTopX = DEFAULT_START_X;
        leftTopY = DEFAULT_START_Y + fm.top;

        rightTopX = DEFAULT_START_X + getStringWidth(DEFAULT_FONT_DEMO);
        rightTopY = leftTopY;

        leftBottomX = leftTopX;
        leftBottomY = DEFAULT_START_Y + fm.bottom;

        rightBottomX = rightTopX;
        rightBottomY = leftBottomY;
    }


    /**
     * 构造方法
     *
     * @param context 上下文
     * @param attr    属性集
     */
    public WriteView(Context context, AttributeSet attr) {
        super(context, attr);

        initScreenParams();
        // 如果没有配置字体
        this.context = context;
        initView(context);

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
    }

    public void addPractiseWriteViewListener(PractiseWriteViewListener mylistener) {
        practiseWriteViewListener = mylistener;
    }

    /**
     * 获取字符串宽度
     *
     * @param str 字符串
     * @return 字符串宽度
     */
    private float getStringWidth(String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            textPaint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }


    /**
     * 将画布设置为白色
     *
     * @param canvas 画布
     */
    private void paintWhite(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
    }

    /**
     * 描字方法
     *
     * @param drawText 要描的字
     * @param canvas   画布
     */
    private void drawText11(String drawText, Canvas canvas) {
        canvas.drawText(drawText, DEFAULT_START_X, DEFAULT_START_Y, textPaint);
    }

    /**
     * 描字方法 (wrote by Jenfee)
     * @param drawText 要描的字
     * @param  canvas  画布
     */
    private void drawText12(String drawText, Canvas canvas) {
        canvas.drawText(drawText,DEFAULT_START_X+getMiddleWordXToRight(),DEFAULT_START_Y,textPaint);
    }

    private void drawText13(String drawText, Canvas canvas) {
        canvas.drawText(drawText,DEFAULT_START_X+getRightWordXToMove(),DEFAULT_START_Y,textPaint);
    }

    /**
     * 描字方法 (wrote by Jenfee)
     * @param drawText
     * @param canvas
     */
    private void drawText21(String drawText, Canvas canvas, float yMoveDown) {
        canvas.drawText(drawText,DEFAULT_START_X,DEFAULT_START_Y+yMoveDown,textPaint);
    }

    /**
     * 描字方法 (wrote by Jenfee)
     * @param drawText
     * @param canvas
     */
    private void drawText22(String drawText, Canvas canvas, float yMoveDown) {
        canvas.drawText(drawText,DEFAULT_START_X+getMiddleWordXToRight(),DEFAULT_START_Y+yMoveDown,textPaint);
    }

    private void drawText23(String drawText, Canvas canvas, float yMoveDown) {
        canvas.drawText(drawText,DEFAULT_START_X+getRightWordXToMove(),DEFAULT_START_Y+yMoveDown,textPaint);
    }


    private void penDraw(Canvas canvas) {
        canvas.drawPath(path, handwritingPaint);
    }

    private void drawLineTest(float stringWidth, Canvas canvas) {
        for (int i = 0;i<3;i++) {
            leftTopX += 400;
            rightTopX += 400;
            leftBottomX += 400;
            rightBottomX += 400;
            canvas.drawLine(leftTopX, leftTopY, rightTopX, rightTopY, framePaint);
            canvas.drawLine(leftTopX, leftTopY, leftBottomX, leftBottomY, framePaint);
            canvas.drawLine(rightTopX, rightTopY, rightBottomX, rightBottomY, framePaint);
            canvas.drawLine(rightBottomX, rightBottomY, leftBottomX, leftBottomY, framePaint);
            Log.v("jenfei's debug", "leftTopX"+String.valueOf(leftTopX));
            Log.v("jenfei's debug", "rightTopX"+String.valueOf(rightTopX));
            Log.v("jenfei's debug", "leftBottomX"+String.valueOf(leftBottomX));
            Log.v("jenfei's debug", "rightBottomX"+String.valueOf(rightBottomX));
            //leftTopX80.0
            // rightTopX656.0
            // leftBottomX80.0
            // rightBottomX656.0
            //加循环之后的数据
            //leftTopX110480.0
            //rightTopX111056.0
            //leftBottomX110480.0
            //rightBottomX111056.0
        }

    }

    /**
     * 描线方法
     *
     * @param stringWidth 要描字的宽度
     * @param canvas      画布
     */
    private void drawLine(float stringWidth, Canvas canvas) {

        // draw frame lines
        canvas.drawLine(leftTopX, leftTopY, rightTopX, rightTopY, framePaint);
        canvas.drawLine(leftTopX, leftTopY, leftBottomX, leftBottomY, framePaint);
        canvas.drawLine(rightTopX, rightTopY, rightBottomX, rightBottomY, framePaint);
        canvas.drawLine(rightBottomX, rightBottomY, leftBottomX, leftBottomY, framePaint);

        // draw frame lines 画框架线(第二个字)
        canvas.drawLine(leftTopX+getMiddleWordXToRight(), leftTopY,rightTopX+getMiddleWordXToRight(),rightTopY,framePaint);//矩形上边
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),leftTopY,leftBottomX+getMiddleWordXToRight(),leftBottomY,framePaint);//矩形左边
        canvas.drawLine(rightTopX+getMiddleWordXToRight(),rightTopY,rightBottomX+getMiddleWordXToRight(),rightBottomY,framePaint);//矩形右边
        canvas.drawLine(rightBottomX+getMiddleWordXToRight(),rightBottomY,leftBottomX+getMiddleWordXToRight(),leftBottomY,framePaint);//矩形下边

        // 第一行的第三个字（三）
        canvas.drawLine(leftTopX+getRightWordXToMove(), leftTopY, rightTopX+getRightWordXToMove(), rightTopY, framePaint);
        canvas.drawLine(leftTopX+getRightWordXToMove(), leftTopY, leftBottomX+getRightWordXToMove(), leftBottomY, framePaint);
        canvas.drawLine(rightTopX+getRightWordXToMove(), rightTopY, rightBottomX+getRightWordXToMove(), rightBottomY, framePaint);
        canvas.drawLine(leftBottomX+getRightWordXToMove(), leftBottomY, rightBottomX+getRightWordXToMove(), rightBottomY ,framePaint);

        // draw frame lines 画框架线(第四个字)
        canvas.drawLine(leftTopX, leftTopY+sixWordYDown(), rightTopX, rightTopY+sixWordYDown(), framePaint);
        canvas.drawLine(leftTopX, leftTopY+sixWordYDown(), leftTopX, leftBottomY+sixWordYDown(), framePaint);
        canvas.drawLine(rightTopX,leftTopY+sixWordYDown(), rightTopX,rightBottomY+sixWordYDown(),framePaint);
        canvas.drawLine(leftTopX, leftBottomY+sixWordYDown(), rightTopX,rightBottomY+sixWordYDown(),framePaint);

        // draw frame lines 画框架线 (第五个字)
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),leftTopY+sixWordYDown(),rightTopX+getMiddleWordXToRight(),leftTopY+sixWordYDown(),framePaint);
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),leftTopY+sixWordYDown(),leftBottomX+getMiddleWordXToRight(),leftBottomY+sixWordYDown(),framePaint);
        canvas.drawLine(rightTopX+getMiddleWordXToRight(),leftTopY+sixWordYDown(),rightTopX+getMiddleWordXToRight(),leftBottomY+sixWordYDown(),framePaint);
        canvas.drawLine(leftBottomX+getMiddleWordXToRight(), leftBottomY+sixWordYDown(),rightTopX+getMiddleWordXToRight(),leftBottomY+sixWordYDown(),framePaint);

        //第二行第三个字的框架线（六）
        canvas.drawLine(leftTopX+getRightWordXToMove(), leftTopY+sixWordYDown(), rightTopX+getRightWordXToMove(), rightTopY+sixWordYDown(), framePaint);
        canvas.drawLine(leftTopX+getRightWordXToMove(), leftTopY+sixWordYDown(), leftBottomX+getRightWordXToMove(), leftBottomY+sixWordYDown(), framePaint);
        canvas.drawLine(rightTopX+getRightWordXToMove(), rightTopY+sixWordYDown(), rightBottomX+getRightWordXToMove(), rightBottomY+sixWordYDown(), framePaint);
        canvas.drawLine(leftBottomX+getRightWordXToMove(), leftBottomY+sixWordYDown(), rightBottomX+getRightWordXToMove(), rightBottomY+sixWordYDown(), framePaint);
//--------------------------------------------------------------------------------------------------------------------
        // draw dotted lines 画虚线 (第一个字)
        canvas.drawLine(leftTopX, leftTopY, rightBottomX, rightBottomY, dotLinePaint);
        canvas.drawLine(rightTopX, rightTopY, leftBottomX, leftBottomY, dotLinePaint);
        canvas.drawLine(leftTopX, (leftBottomY - leftTopY) / 2 + leftTopY, rightTopX, (rightBottomY - rightTopY) / 2 + rightTopY, dotLinePaint);
        canvas.drawLine((rightTopX - leftTopX) / 2 + leftTopX, leftTopY, (rightBottomX - leftBottomX) / 2 + leftBottomX, leftBottomY, dotLinePaint);

        // draw dotted lines 画虚线 (第二个字)
        canvas.drawLine(leftTopX+getMiddleWordXToRight(), leftTopY, rightBottomX+getMiddleWordXToRight(), rightBottomY, dotLinePaint);
        canvas.drawLine(rightTopX+getMiddleWordXToRight(), rightTopY, leftBottomX+getMiddleWordXToRight(), leftBottomY, dotLinePaint);
        canvas.drawLine(leftTopX+getMiddleWordXToRight(), (leftBottomY - leftTopY) / 2 + leftTopY, rightTopX+getMiddleWordXToRight(), (rightBottomY - rightTopY) / 2 + rightTopY, dotLinePaint);
        canvas.drawLine((rightTopX+getMiddleWordXToRight() - leftTopX+getMiddleWordXToRight()) / 2 + leftTopX, leftTopY, (rightBottomX+getMiddleWordXToRight() - leftBottomX+getMiddleWordXToRight()) / 2 + leftBottomX, leftBottomY, dotLinePaint);

        //第一行第三个字的虚线(三)
        canvas.drawLine(leftTopX+getRightWordXToMove(), leftTopY, rightBottomX+getRightWordXToMove(), rightBottomY,dotLinePaint);
        canvas.drawLine(leftBottomX+getRightWordXToMove(), leftBottomY,rightTopX+getRightWordXToMove(), rightTopY,dotLinePaint);
        canvas.drawLine(leftTopX+getRightWordXToMove(),(leftBottomY - leftTopY) / 2 + leftTopY,rightTopX+getRightWordXToMove(),(leftBottomY - leftTopY) / 2 + leftTopY,dotLinePaint);
        canvas.drawLine((rightTopX+getRightWordXToMove() - leftTopX+getRightWordXToMove()) / 2 + leftTopX, leftTopY, (rightBottomX+getRightWordXToMove() - leftBottomX+getRightWordXToMove()) / 2 + leftBottomX, leftBottomY, dotLinePaint);

        // draw dotted lines 画虚线 (第四个字)
        canvas.drawLine(leftTopX,leftTopY+sixWordYDown(),rightBottomX,rightBottomY+sixWordYDown(),dotLinePaint);
        canvas.drawLine(rightTopX,rightTopY+sixWordYDown(),leftBottomX,leftBottomY+sixWordYDown(),dotLinePaint);
        canvas.drawLine(leftTopX, (leftBottomY+sixWordYDown() - leftTopY+sixWordYDown()) / 2 + leftTopY, rightTopX, (rightBottomY+sixWordYDown() - rightTopY+sixWordYDown()) / 2 + rightTopY, dotLinePaint);//水平虚线
        canvas.drawLine((rightTopX - leftTopX) / 2 + leftTopX, leftTopY+sixWordYDown(), (rightBottomX - leftBottomX) / 2 + leftBottomX, leftBottomY+sixWordYDown(), dotLinePaint);

        //draw dotted lines 画虚线 (第五个字)
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),leftTopY+sixWordYDown(),rightBottomX+getMiddleWordXToRight(),rightBottomY+sixWordYDown(),dotLinePaint);
        canvas.drawLine(rightTopX+getMiddleWordXToRight(),rightTopY+sixWordYDown(),leftBottomX+getMiddleWordXToRight(),leftBottomY+sixWordYDown(),dotLinePaint);
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),(leftBottomY+sixWordYDown() - leftTopY+sixWordYDown()) / 2 + leftTopY,rightTopX+getMiddleWordXToRight(),(leftBottomY+sixWordYDown() - leftTopY+sixWordYDown()) / 2 + leftTopY,dotLinePaint);
        canvas.drawLine((rightTopX+getMiddleWordXToRight() - leftTopX+getMiddleWordXToRight()) / 2 + leftTopX,leftTopY+sixWordYDown(),(rightTopX+getMiddleWordXToRight() - leftTopX+getMiddleWordXToRight()) / 2 + leftTopX,rightBottomY+sixWordYDown(),dotLinePaint);

        //第二行第三个字的虚线（六）
        canvas.drawLine(leftTopX+getRightWordXToMove(), leftTopY+sixWordYDown(),rightBottomX+getRightWordXToMove(), rightBottomY+sixWordYDown(),dotLinePaint);
        canvas.drawLine(leftBottomX+getRightWordXToMove(), leftBottomY+sixWordYDown(),rightTopX+getRightWordXToMove(), rightTopY+sixWordYDown(),dotLinePaint);
        canvas.drawLine(leftTopX+getRightWordXToMove(), (leftBottomY+sixWordYDown() - leftTopY+sixWordYDown()) / 2 + leftTopY,rightTopX+getRightWordXToMove(),(leftBottomY+sixWordYDown() - leftTopY+sixWordYDown()) / 2 + leftTopY,dotLinePaint);
        canvas.drawLine((rightTopX+getRightWordXToMove() - leftTopX+getRightWordXToMove()) / 2 + leftTopX,leftTopY+sixWordYDown(),(rightTopX+getRightWordXToMove() - leftTopX+getRightWordXToMove()) / 2 + leftTopX,leftBottomY+sixWordYDown(),dotLinePaint);

       /* Log.i("leftTopX",leftTopX+"");
        Log.i("leftTopY",leftTopY+"");
        Log.i("rightTopX",rightTopX+"");
        Log.i("rightTopY",rightTopY+"");

        Log.i("leftBottomY",leftBottomY+"");
        Log.i("rightBottomY",rightBottomY+"");
        Log.i("leftBottomX",leftBottomX+"");
        Log.i("rightBottomX",rightBottomX+"");*/
    }


    /**
     * 六个字练习时Y轴向下移动的距离
     * @return
     */
    private float sixWordYDown() {
        float yMoveToDown;//手机1参数
        float tmp = leftBottomY - leftTopY;
        yMoveToDown = tmp +20;
        //float yMoveToDown = 450;
        return yMoveToDown;
    }

    /**
     * 九个字练习时Y轴向下移动的距离
     * @return
     */
    private float nineWordYDown() {
        //float yMoveToDown = 400;//手机1参数
        float yMoveToDown;
        float tmp = (leftBottomY - leftTopY)*2;
        yMoveToDown = tmp + 40;
        return yMoveToDown;
    }

    /**
     * 当在九个字的情况下中间行Y轴向下移动的距离
     * @return
     */
    private float nineWordMiddleYDown() {
        float yMoveToDown;
        float tmp = leftBottomY - leftTopY;
        yMoveToDown = tmp + 5;
        return yMoveToDown;
    }

    /**
     * 当在九个字的情况下最下面行Y轴向下移动的距离
     * @return
     */
    private float nineWordBottomYDown() {
        float yMoveToDown;
        float tmp = (leftBottomY - leftTopY)*2;
        yMoveToDown = tmp + 10;
        return yMoveToDown;
    }

    /**
     * 中间的字X轴向右移动的距离
     * @return
     */
    private float getMiddleWordXToRight() {
        float f = 0;
        float tmp = rightTopX - leftTopX;
        f = tmp+70;
        return f;
    }

    /**
     * 最右边的第三个字X轴向右移动的距离
     * @return
     */
    private float getRightWordXToMove() {
        float n = 0;
        float tmp = (rightTopX - leftTopX)*2;
        n = tmp + 140;
        return n;
    }

    private void drawLine2(float stringWidth, Canvas canvas) {

        // draw frame lines(框架线第一个字)
        canvas.drawLine(leftTopX, leftTopY, rightTopX, rightTopY, framePaint);
        canvas.drawLine(leftTopX, leftTopY, leftBottomX, leftBottomY, framePaint);
        canvas.drawLine(rightTopX, rightTopY, rightBottomX, rightBottomY, framePaint);
        canvas.drawLine(rightBottomX, rightBottomY, leftBottomX, leftBottomY, framePaint);

        // draw frame lines 画框架线(第二个字)
        canvas.drawLine(leftTopX+getMiddleWordXToRight(), leftTopY,rightTopX+getMiddleWordXToRight(),rightTopY,framePaint);//矩形上边
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),leftTopY,leftBottomX+getMiddleWordXToRight(),leftBottomY,framePaint);//矩形左边
        canvas.drawLine(rightTopX+getMiddleWordXToRight(),rightTopY,rightBottomX+getMiddleWordXToRight(),rightBottomY,framePaint);//矩形右边
        canvas.drawLine(rightBottomX+getMiddleWordXToRight(),rightBottomY,leftBottomX+getMiddleWordXToRight(),leftBottomY,framePaint);//矩形下边

        // 框架线 第一行的第三个字
        canvas.drawLine(leftTopX+getRightWordXToMove(), leftTopY, rightTopX+getRightWordXToMove(), rightTopY, framePaint);
        canvas.drawLine(leftTopX+getRightWordXToMove(), leftTopY, leftBottomX+getRightWordXToMove(), leftBottomY, framePaint);
        canvas.drawLine(rightTopX+getRightWordXToMove(), rightTopY, rightBottomX+getRightWordXToMove(), rightBottomY, framePaint);
        canvas.drawLine(leftBottomX+getRightWordXToMove(), leftBottomY, rightBottomX+getRightWordXToMove(), rightBottomY ,framePaint);

        // draw frame lines 画框架线(第四个字)
        canvas.drawLine(leftTopX, leftTopY+nineWordMiddleYDown(), rightTopX, rightTopY+nineWordMiddleYDown(), framePaint);
        canvas.drawLine(leftTopX, leftTopY+nineWordMiddleYDown(), leftTopX, leftBottomY+nineWordMiddleYDown(), framePaint);
        canvas.drawLine(rightTopX,leftTopY+nineWordMiddleYDown(), rightTopX,rightBottomY+nineWordMiddleYDown(),framePaint);
        canvas.drawLine(leftTopX, leftBottomY+nineWordMiddleYDown(), rightTopX,rightBottomY+nineWordMiddleYDown(),framePaint);

        // draw frame lines 画框架线 (第五个字)
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),leftTopY+nineWordMiddleYDown(),rightTopX+getMiddleWordXToRight(),leftTopY+nineWordMiddleYDown(),framePaint);
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),leftTopY+nineWordMiddleYDown(),leftBottomX+getMiddleWordXToRight(),leftBottomY+nineWordMiddleYDown(),framePaint);
        canvas.drawLine(rightTopX+getMiddleWordXToRight(),leftTopY+nineWordMiddleYDown(),rightTopX+getMiddleWordXToRight(),leftBottomY+nineWordMiddleYDown(),framePaint);
        canvas.drawLine(leftBottomX+getMiddleWordXToRight(), leftBottomY+nineWordMiddleYDown(),rightTopX+getMiddleWordXToRight(),leftBottomY+nineWordMiddleYDown(),framePaint);

        //第二行第三个字的框架线（框架线第六个字）
        canvas.drawLine(leftTopX+getRightWordXToMove(), leftTopY+nineWordMiddleYDown(), rightTopX+getRightWordXToMove(), rightTopY+nineWordMiddleYDown(), framePaint);
        canvas.drawLine(leftTopX+getRightWordXToMove(), leftTopY+nineWordMiddleYDown(), leftBottomX+getRightWordXToMove(), leftBottomY+nineWordMiddleYDown(), framePaint);
        canvas.drawLine(rightTopX+getRightWordXToMove(), rightTopY+nineWordMiddleYDown(), rightBottomX+getRightWordXToMove(), rightBottomY+nineWordMiddleYDown(), framePaint);
        canvas.drawLine(leftBottomX+getRightWordXToMove(), leftBottomY+nineWordMiddleYDown(), rightBottomX+getRightWordXToMove(), rightBottomY+nineWordMiddleYDown(), framePaint);

        //框架线第七个字
        canvas.drawLine(leftTopX,leftTopY+nineWordBottomYDown(),rightTopX,rightTopY+nineWordBottomYDown(),framePaint);
        canvas.drawLine(leftTopX,leftTopY+nineWordBottomYDown(),leftBottomX,leftBottomY+nineWordBottomYDown(),framePaint);
        canvas.drawLine(rightTopX,rightTopY+nineWordBottomYDown(),rightBottomX,rightBottomY+nineWordBottomYDown(),framePaint);
        canvas.drawLine(leftBottomX,leftBottomY+nineWordBottomYDown(),rightBottomX,rightBottomY+nineWordBottomYDown(),framePaint);

        //框架线第八个字
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),leftTopY+nineWordBottomYDown(),rightTopX+getMiddleWordXToRight(),rightTopY+nineWordBottomYDown(),framePaint);
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),leftTopY+nineWordBottomYDown(),leftBottomX+getMiddleWordXToRight(),leftBottomY+nineWordBottomYDown(),framePaint);
        canvas.drawLine(rightTopX+getMiddleWordXToRight(),rightTopY+nineWordBottomYDown(),rightBottomX+getMiddleWordXToRight(),rightBottomY+nineWordBottomYDown(),framePaint);
        canvas.drawLine(leftBottomX+getMiddleWordXToRight(),leftBottomY+nineWordBottomYDown(),rightBottomX+getMiddleWordXToRight(),rightBottomY+nineWordBottomYDown(),framePaint);

        //框架线第九个字
        canvas.drawLine(leftTopX+getRightWordXToMove(),leftTopY+nineWordBottomYDown(),rightTopX+getRightWordXToMove(),rightTopY+nineWordBottomYDown(),framePaint);
        canvas.drawLine(leftTopX+getRightWordXToMove(),leftTopY+nineWordBottomYDown(),leftBottomX+getRightWordXToMove(),leftBottomY+nineWordBottomYDown(),framePaint);
        canvas.drawLine(rightTopX+getRightWordXToMove(),rightTopY+nineWordBottomYDown(),rightBottomX+getRightWordXToMove(),rightBottomY+nineWordBottomYDown(),framePaint);
        canvas.drawLine(leftBottomX+getRightWordXToMove(),leftBottomY+nineWordBottomYDown(),rightBottomX+getRightWordXToMove(),rightBottomY+nineWordBottomYDown(),framePaint);
//---------------------------------------------------------------------------------------------------------------------------------
        // draw dotted lines 画虚线 (第一个字)
        canvas.drawLine(leftTopX, leftTopY, rightBottomX, rightBottomY, dotLinePaint);
        canvas.drawLine(rightTopX, rightTopY, leftBottomX, leftBottomY, dotLinePaint);
        canvas.drawLine(leftTopX, (leftBottomY - leftTopY) / 2 + leftTopY, rightTopX, (rightBottomY - rightTopY) / 2 + rightTopY, dotLinePaint);
        canvas.drawLine((rightTopX - leftTopX) / 2 + leftTopX, leftTopY, (rightBottomX - leftBottomX) / 2 + leftBottomX, leftBottomY, dotLinePaint);

        // draw dotted lines 画虚线 (第二个字)
        canvas.drawLine(leftTopX+getMiddleWordXToRight(), leftTopY, rightBottomX+getMiddleWordXToRight(), rightBottomY, dotLinePaint);
        canvas.drawLine(rightTopX+getMiddleWordXToRight(), rightTopY, leftBottomX+getMiddleWordXToRight(), leftBottomY, dotLinePaint);
        canvas.drawLine(leftTopX+getMiddleWordXToRight(), (leftBottomY - leftTopY) / 2 + leftTopY, rightTopX+getMiddleWordXToRight(), (rightBottomY - rightTopY) / 2 + rightTopY, dotLinePaint);
        canvas.drawLine((rightTopX+getMiddleWordXToRight() - leftTopX+getMiddleWordXToRight()) / 2 + leftTopX, leftTopY, (rightBottomX+getMiddleWordXToRight() - leftBottomX+getMiddleWordXToRight()) / 2 + leftBottomX, leftBottomY, dotLinePaint);

        //第一行第三个字的虚线(第三个字)
        canvas.drawLine(leftTopX+getRightWordXToMove(), leftTopY, rightBottomX+getRightWordXToMove(), rightBottomY,dotLinePaint);
        canvas.drawLine(leftBottomX+getRightWordXToMove(), leftBottomY,rightTopX+getRightWordXToMove(), rightTopY,dotLinePaint);
        canvas.drawLine(leftTopX+getRightWordXToMove(),(leftBottomY - leftTopY) / 2 + leftTopY,rightTopX+getRightWordXToMove(),(leftBottomY - leftTopY) / 2 + leftTopY,dotLinePaint);
        canvas.drawLine((rightTopX+getRightWordXToMove() - leftTopX+getRightWordXToMove()) / 2 + leftTopX, leftTopY, (rightBottomX+getRightWordXToMove() - leftBottomX+getRightWordXToMove()) / 2 + leftBottomX, leftBottomY, dotLinePaint);

        // draw dotted lines 画虚线 (第四个字)
        canvas.drawLine(leftTopX,leftTopY+nineWordMiddleYDown(),rightBottomX,rightBottomY+nineWordMiddleYDown(),dotLinePaint);
        canvas.drawLine(rightTopX,rightTopY+nineWordMiddleYDown(),leftBottomX,leftBottomY+nineWordMiddleYDown(),dotLinePaint);
        canvas.drawLine(leftTopX, (leftBottomY+nineWordMiddleYDown() - leftTopY+nineWordMiddleYDown()) / 2 + leftTopY, rightTopX, (rightBottomY+nineWordMiddleYDown() - rightTopY+nineWordMiddleYDown()) / 2 + rightTopY, dotLinePaint);//水平虚线
        canvas.drawLine((rightTopX - leftTopX) / 2 + leftTopX, leftTopY+nineWordMiddleYDown(), (rightBottomX - leftBottomX) / 2 + leftBottomX, leftBottomY+nineWordMiddleYDown(), dotLinePaint);

        //draw dotted lines 画虚线 (第五个字)
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),leftTopY+nineWordMiddleYDown(),rightBottomX+getMiddleWordXToRight(),rightBottomY+nineWordMiddleYDown(),dotLinePaint);
        canvas.drawLine(rightTopX+getMiddleWordXToRight(),rightTopY+nineWordMiddleYDown(),leftBottomX+getMiddleWordXToRight(),leftBottomY+nineWordMiddleYDown(),dotLinePaint);
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),(leftBottomY+nineWordMiddleYDown() - leftTopY+nineWordMiddleYDown()) / 2 + leftTopY,rightTopX+getMiddleWordXToRight(),(leftBottomY+nineWordMiddleYDown() - leftTopY+nineWordMiddleYDown()) / 2 + leftTopY,dotLinePaint);
        canvas.drawLine((rightTopX+getMiddleWordXToRight() - leftTopX+getMiddleWordXToRight()) / 2 + leftTopX,leftTopY+nineWordMiddleYDown(),(rightTopX+getMiddleWordXToRight() - leftTopX+getMiddleWordXToRight()) / 2 + leftTopX,rightBottomY+nineWordMiddleYDown(),dotLinePaint);

        //第二行第三个字的虚线（第六个字）
        canvas.drawLine(leftTopX+getRightWordXToMove(), leftTopY+nineWordMiddleYDown(),rightBottomX+getRightWordXToMove(), rightBottomY+nineWordMiddleYDown(),dotLinePaint);
        canvas.drawLine(leftBottomX+getRightWordXToMove(), leftBottomY+nineWordMiddleYDown(),rightTopX+getRightWordXToMove(), rightTopY+nineWordMiddleYDown(),dotLinePaint);
        canvas.drawLine(leftTopX+getRightWordXToMove(), (leftBottomY+nineWordMiddleYDown() - leftTopY+nineWordMiddleYDown()) / 2 + leftTopY,rightTopX+getRightWordXToMove(),(leftBottomY+nineWordMiddleYDown() - leftTopY+nineWordMiddleYDown()) / 2 + leftTopY,dotLinePaint);
        canvas.drawLine((rightTopX+getRightWordXToMove() - leftTopX+getRightWordXToMove()) / 2 + leftTopX,leftTopY+nineWordMiddleYDown(),(rightTopX+getRightWordXToMove() - leftTopX+getRightWordXToMove()) / 2 + leftTopX,leftBottomY+nineWordMiddleYDown(),dotLinePaint);

        canvas.drawLine(leftTopX,leftTopY+nineWordBottomYDown(),rightBottomX,rightBottomY+nineWordBottomYDown(),dotLinePaint);
        canvas.drawLine(leftBottomX,leftBottomY+nineWordBottomYDown(),rightTopX,rightTopY+nineWordBottomYDown(),dotLinePaint);
        canvas.drawLine(leftTopX,((leftBottomY - leftTopY) / 2 + leftTopY)+nineWordBottomYDown(),rightTopX,((leftBottomY - leftTopY) / 2 + leftTopY)+nineWordBottomYDown(),dotLinePaint);
        canvas.drawLine((rightTopX - leftTopX) / 2 + leftTopX,leftTopY+nineWordBottomYDown(),(rightTopX - leftTopX) / 2 + leftTopX,leftBottomY+nineWordBottomYDown(),dotLinePaint);

        canvas.drawLine(leftTopX+getMiddleWordXToRight(),leftTopY+nineWordBottomYDown(),rightBottomX+getMiddleWordXToRight(),rightBottomY+nineWordBottomYDown(),dotLinePaint);
        canvas.drawLine(leftBottomX+getMiddleWordXToRight(),leftBottomY+nineWordBottomYDown(),rightTopX+getMiddleWordXToRight(),rightTopY+nineWordBottomYDown(),dotLinePaint);
        canvas.drawLine(leftTopX+getMiddleWordXToRight(),((leftBottomY - leftTopY) / 2 + leftTopY)+nineWordBottomYDown(),rightTopX+getMiddleWordXToRight(),((leftBottomY - leftTopY) / 2 + leftTopY)+nineWordBottomYDown(),dotLinePaint);
        canvas.drawLine(((rightTopX - leftTopX) / 2 + leftTopX)+getMiddleWordXToRight(),leftTopY+nineWordBottomYDown(),((rightTopX - leftTopX) / 2 + leftTopX)+getMiddleWordXToRight(),leftBottomY+nineWordBottomYDown(),dotLinePaint);

        canvas.drawLine(leftTopX+getRightWordXToMove(),leftTopY+nineWordBottomYDown(),rightBottomX+getRightWordXToMove(),rightBottomY+nineWordBottomYDown(),dotLinePaint);
        canvas.drawLine(leftBottomX+getRightWordXToMove(),leftBottomY+nineWordBottomYDown(),rightTopX+getRightWordXToMove(),rightTopY+nineWordBottomYDown(),dotLinePaint);
        canvas.drawLine(leftTopX+getRightWordXToMove(),((leftBottomY - leftTopY) / 2 + leftTopY)+nineWordBottomYDown(),rightTopX+getRightWordXToMove(),((leftBottomY - leftTopY) / 2 + leftTopY)+nineWordBottomYDown(),dotLinePaint);
        canvas.drawLine(((rightTopX - leftTopX) / 2 + leftTopX)+getRightWordXToMove(),leftTopY+nineWordBottomYDown(),((rightTopX - leftTopX) / 2 + leftTopX)+getRightWordXToMove(),leftBottomY+nineWordBottomYDown(),dotLinePaint);

        /*Log.i("leftTopX",leftTopX+"");
        Log.i("leftTopY",leftTopY+"");
        Log.i("rightTopX",rightTopX+"");
        Log.i("rightTopY",rightTopY+"");

        Log.i("leftBottomY",leftBottomY+"");
        Log.i("rightBottomY",rightBottomY+"");
        Log.i("leftBottomX",leftBottomX+"");
        Log.i("rightBottomX",rightBottomX+"");*/
    }

    /**
     * SurfaceView改变之后执行
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    /**
     * SurfaceView创建之后执行
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        getHolder().addCallback(this);
        wordChangeThread = new WordChangeThread();
        wordChangeThread.start();
    }

    /**
     * SurfaceView销毁之后执行
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 更新数据库
//		UserRecordUtil.insertLatestUserRecord(getContext(),
//				PractiseOriginalActivity.getCurFontLib(),
//				PractiseOriginalActivity.getCurWordIndex());
//
        if (WriteView.ifWrite())
            PractiseActivity.writeSumPlusPlus();
        wordChangeThread.setRunning(false);

    }

    private float cur_x;
    private float cur_y;

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (x < leftTopX) {
//            practiseWriteViewListener.isShare();
            return false;
        } else if (x > rightTopX+getRightWordXToMove()) {
//            practiseWriteViewListener.isShare();
            return false;
        }

        if (y < leftTopY) {
//            practiseWriteViewListener.isShare();
            return false;
        } else if (y > leftBottomY+800) {
//            practiseWriteViewListener.isShare();
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                cur_x = x;
                cur_y = y;
                path.moveTo(cur_x, cur_y);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                path.quadTo(cur_x, cur_y, x, y);
                cur_x = x;
                cur_y = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                // 画path
//                 reDraw();
//                 path.reset();
                break;
            }
        }

        invalidate();
        return true;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int WRAP_WIDTH = 500;
        int WRAP_HEIGHT = 500;

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(WRAP_WIDTH, WRAP_HEIGHT);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(WRAP_WIDTH, height);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, WRAP_HEIGHT);
        }
    }
    public static void clear() {
        path.reset();
    }

    /**
     * 文字切换刷新线程
     *
     * @author kelvin
     */
    class WordChangeThread extends Thread {


        private volatile boolean isRunning = true;

        public synchronized boolean isRunning() {
            return isRunning;
        }

        public synchronized void setRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        public void run() {
            Canvas canvas = null;
            while (isRunning) {

                String curWord = null;
                String curWord9 = null;
                String oneWord = null,twoWord = null,threeWord = null,fourWord = null, fiveWord = null, sixWord = null, sevenWord = null, eightWord = null, nineWord = null;

                int isThreeWordOrSixExercise = PractiseActivity.getIsThreeWordOrSix();
                if (PractiseActivity.isSixOrNineOnClick == 6) {
                    curWord = PractiseActivity.getCurWord();
                    oneWord = curWord.substring(0,1);
                    twoWord = curWord.substring(1,2);
                    threeWord = curWord.substring(2,3);
                    fourWord = curWord.substring(3,4);
                    fiveWord = curWord.substring(4,5);
                    sixWord = curWord.substring(5,6);
                } else if (PractiseActivity.isSixOrNineOnClick == 9) {
                    curWord9 = PractiseActivity.getCurWord9();
                    oneWord = curWord9.substring(0,1);
                    twoWord = curWord9.substring(1,2);
                    threeWord = curWord9.substring(2,3);
                    fourWord = curWord9.substring(3,4);
                    fiveWord = curWord9.substring(4,5);
                    sixWord = curWord9.substring(5,6);
                    sevenWord = curWord9.substring(6,7);
                    eightWord = curWord9.substring(7,8);
                    nineWord = curWord9.substring(8,9);
                }
                canvas = surfaceHolder.lockCanvas();
                if (canvas == null) {
                    return;
                }
                // 将画布设置为白色
                paintWhite(canvas);
                if (PractiseActivity.isSixOrNineOnClick == 6) {
                    drawLine(getStringWidth(curWord), canvas);
                    drawText11(oneWord, canvas);
                    drawText12(twoWord, canvas);
                    drawText13(fiveWord, canvas);
                    drawText21(threeWord, canvas, sixWordYDown());
                    drawText22(fourWord, canvas, sixWordYDown());
                    drawText23(sixWord, canvas, sixWordYDown());
                } else if (PractiseActivity.isSixOrNineOnClick == 9) {
                    drawLine2(getStringWidth(curWord), canvas);
                    drawText11(oneWord, canvas);
                    drawText12(twoWord, canvas);
                    drawText13(fiveWord, canvas);
                    drawText21(threeWord, canvas, nineWordMiddleYDown());
                    drawText22(fourWord, canvas, nineWordMiddleYDown());
                    drawText23(sixWord, canvas, nineWordMiddleYDown());
                    canvas.drawText(sevenWord, DEFAULT_START_X, DEFAULT_START_Y+nineWordBottomYDown(), textPaint);
                    canvas.drawText(eightWord, DEFAULT_START_X+getMiddleWordXToRight(), DEFAULT_START_Y+nineWordBottomYDown(), textPaint);
                    canvas.drawText(nineWord, DEFAULT_START_X+getRightWordXToMove(), DEFAULT_START_Y+nineWordBottomYDown(), textPaint);
                }
                // 描线
                //drawLine2(getStringWidth(curWord), canvas);
                // 描字

                penDraw(canvas);
                surfaceHolder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean ifWrite() {
        return !path.isEmpty();
    }

}