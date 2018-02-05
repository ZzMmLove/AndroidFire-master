package com.gdgst.shuoke360.ui.main.view;

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

import com.gdgst.shuoke360.ui.main.activity.MainActivity;
import com.gdgst.shuoke360.ui.main.fragment.PractiseMainFragment;

/**
 * 字帖练字界面写字板
 *
 * @author kelvin
 * * * * * * * * * *
 * 1->PractiseWriteView(),创建PractiseWriteView对象，执行PractiseWriteView的构造函数
 * 2->surfaceCreated()当SurfaceView被实例化之后马上执行
 * 3->new WordChangeThread.start()，执行文字切换刷新线程
 * 4->onTouchEvent(),当屏幕有触摸事件发生时调用，重新计算x,y坐标
 * 5->surfaceChanged(),当SurfaceView被改变之后执行
 *
 */
public class PractiseWriteView extends SurfaceView implements
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
    private static final int DEFAULT_HANDWRTING_STROKE_WIDTH = 40;

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

    private int mFistHeight, mSecondHeight;

    /**
     * 初始化屏幕参数（宽度和高度）
     */
    private void initScreenParams() {
        int screenHeight = MainActivity.getSCREEN_HEIGHT();
        int screenWidth = MainActivity.getSCREEN_WIDTH();

        DEFAULT_TEXT_SIZE = screenWidth * 3 / 8;//初始:screenWidth * 3/4
        DEFAULT_START_X = screenWidth / 24;//初始:screenWdth / 8
        DEFAULT_START_Y = screenHeight / 4;//初始:screenHeight / 2


        Log.i("2DEFAULT_TEXT_SIZE:",DEFAULT_TEXT_SIZE+"");
        Log.i("2DEFAULT_START_X:",DEFAULT_START_X+"");
        Log.i("2DEFAULT_START_Y:",DEFAULT_START_Y+"");
    }

//
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
        handwritingPaint.setTypeface(penFace);
        handwritingPaint.setColor(DEFAULT_PEN_COLOR); // 设置画笔颜色

        handwritingPaint.setStyle(Paint.Style.STROKE); // 设置画笔风格
        handwritingPaint.setStrokeCap(Paint.Cap.SQUARE);
        handwritingPaint.setStrokeJoin(Paint.Join.BEVEL);

        handwritingPaint.setAntiAlias(true);


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
    public PractiseWriteView(Context context, AttributeSet attr) {
        super(context, attr);

        initScreenParams();
        // 如果没有配置字体
        initView(context);

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        Log.v("jenfei","执行PractiseWriteView类的构造函数,初始化屏幕参数,初始化视图,初始化画笔,初始化坐标");
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
    private void drawText(String drawText, Canvas canvas) {
        canvas.drawText(drawText, DEFAULT_START_X, DEFAULT_START_Y, textPaint);
    }

    /**
     * 描字方法 (wrote by jenfei)
     * @param drawText 要描的字
     * @param  canvas  画布
     */
    private void drawText1(String drawText, Canvas canvas) {
        canvas.drawText(drawText,DEFAULT_START_X+400,DEFAULT_START_Y,textPaint);
    }

    /**
     * 描字方法 (wrote by jenfei)
     * @param drawText
     * @param canvas
     */
    private void drawText2(String drawText, Canvas canvas) {
        canvas.drawText(drawText,DEFAULT_START_X,DEFAULT_START_Y+400,textPaint);
    }

    /**
     * 描字方法 (wrote by jenfei)
     * @param drawText
     * @param canvas
     */
    private void drawText3(String drawText, Canvas canvas) {
        canvas.drawText(drawText,DEFAULT_START_X+400,DEFAULT_START_Y+400,textPaint);
    }

    private void penDraw(Canvas canvas) {
        canvas.drawPath(path, handwritingPaint);
    }

    /**
     * 描线方法
     *
     * @param stringWidth 要描字的宽度
     * @param canvas      画布
     */
    private void drawLine(float stringWidth, Canvas canvas) {

        // draw frame lines 画框架线(第一个字)
        canvas.drawLine(leftTopX, leftTopY, rightTopX, rightTopY, framePaint);
        canvas.drawLine(leftTopX, leftTopY, leftBottomX, leftBottomY, framePaint);
        canvas.drawLine(rightTopX, rightTopY, rightBottomX, rightBottomY, framePaint);
        canvas.drawLine(rightBottomX, rightBottomY, leftBottomX, leftBottomY, framePaint);

        // draw frame lines 画框架线(第二个字)
        canvas.drawLine(leftTopX+400, leftTopY,rightTopX+400,rightTopY,framePaint);//矩形上边
        canvas.drawLine(leftTopX+400,leftTopY,leftBottomX+400,leftBottomY,framePaint);//矩形左边
        canvas.drawLine(rightTopX+400,rightTopY,rightBottomX+400,rightBottomY,framePaint);//矩形右边
        canvas.drawLine(rightBottomX+400,rightBottomY,leftBottomX+400,leftBottomY,framePaint);//矩形下边

        // draw frame lines 画框架线(第三个字)
        canvas.drawLine(leftTopX, leftTopY+400, rightTopX, rightTopY+400, framePaint);
        canvas.drawLine(leftTopX, leftTopY+400, leftTopX, leftBottomY+400, framePaint);
        canvas.drawLine(rightTopX,leftTopY+400, rightTopX,rightBottomY+400,framePaint);
        canvas.drawLine(leftTopX, leftBottomY+400, rightTopX,rightBottomY+400,framePaint);

        // draw frame lines 画框架线 (第四个字)
        canvas.drawLine(leftTopX+400,leftTopY+400,rightTopX+400,leftTopY+400,framePaint);
        canvas.drawLine(leftTopX+400,leftTopY+400,leftBottomX+400,leftBottomY+400,framePaint);
        canvas.drawLine(rightTopX+400,leftTopY+400,rightTopX+400,leftBottomY+400,framePaint);
        canvas.drawLine(leftBottomX+400, leftBottomY+400,rightTopX+400,leftBottomY+400,framePaint);

        // draw dotted lines 画虚线 (第一个字)
        canvas.drawLine(leftTopX, leftTopY, rightBottomX, rightBottomY, dotLinePaint);//斜杠->\
        canvas.drawLine(rightTopX, rightTopY, leftBottomX, leftBottomY, dotLinePaint);//反斜杠->/
        canvas.drawLine(leftTopX, (leftBottomY - leftTopY) / 2 + leftTopY, rightTopX, (rightBottomY - rightTopY) / 2 + rightTopY, dotLinePaint);//水平虚线
        canvas.drawLine((rightTopX - leftTopX) / 2 + leftTopX, leftTopY, (rightBottomX - leftBottomX) / 2 + leftBottomX, leftBottomY, dotLinePaint);//竖直虚线

        // draw dotted lines 画虚线 (第二个字)
        canvas.drawLine(leftTopX+400, leftTopY, rightBottomX+400, rightBottomY, dotLinePaint);
        canvas.drawLine(rightTopX+400, rightTopY, leftBottomX+400, leftBottomY, dotLinePaint);
        canvas.drawLine(leftTopX+400, (leftBottomY - leftTopY) / 2 + leftTopY, rightTopX+600, (rightBottomY - rightTopY) / 2 + rightTopY, dotLinePaint);
        canvas.drawLine((rightTopX+400 - leftTopX+400) / 2 + leftTopX, leftTopY, (rightBottomX+400 - leftBottomX+400) / 2 + leftBottomX, leftBottomY, dotLinePaint);

        // draw dotted lines 画虚线 (第三个字)
        canvas.drawLine(leftTopX,leftTopY+400,rightBottomX,rightBottomY+400,dotLinePaint);
        canvas.drawLine(rightTopX,rightTopY+400,leftBottomX,leftBottomY+400,dotLinePaint);
        canvas.drawLine(leftTopX, (leftBottomY+400 - leftTopY+400) / 2 + leftTopY, rightTopX, (rightBottomY+400 - rightTopY+400) / 2 + rightTopY, dotLinePaint);//水平虚线
        canvas.drawLine((rightTopX - leftTopX) / 2 + leftTopX, leftTopY+400, (rightBottomX - leftBottomX) / 2 + leftBottomX, leftBottomY+400, dotLinePaint);

        //draw dotted lines 画虚线 (第四个字)
        canvas.drawLine(leftTopX+400,leftTopY+400,rightBottomX+400,rightBottomY+400,dotLinePaint);
        canvas.drawLine(rightTopX+400,rightTopY+400,leftBottomX+400,leftBottomY+400,dotLinePaint);


        Log.i("jenfei leftTopX",leftTopX+"");
        Log.i("jenfei leftTopY",leftTopY+"");
        Log.i("jenfei rightTopX",rightTopX+"");
        Log.i("jenfei rightTopY",rightTopY+"");

        Log.i("jenfei leftBottomY",leftBottomY+"");
        Log.i("jenfei rightBottomY",rightBottomY+"");
        Log.i("jenfei leftBottomX",leftBottomX+"");
        Log.i("jenfei rightBottomX",rightBottomX+"");
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        getHolder().addCallback(this);
        wordChangeThread = new WordChangeThread();
        wordChangeThread.start();
        Log.v("jenfei", "在SurfaceView创建之后执行方法:surfaceCreated");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 更新数据库
//		UserRecordUtil.insertLatestUserRecord(getContext(),
//				PractiseOriginalActivity.getCurFontLib(),
//				PractiseOriginalActivity.getCurWordIndex());
//
        if (PractiseWriteView.ifWrite())
            PractiseMainFragment.writeSumPlusPlus();
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
        } else if (x > rightTopX+400) {
//            practiseWriteViewListener.isShare();
            return false;
        }

        if (y < leftTopY) {
//            practiseWriteViewListener.isShare();
            return false;
        } else if (y > leftBottomY+400) {
//            practiseWriteViewListener.isShare();
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                cur_x = x;
                cur_y = y;
                /**
                 * moveTo表示将绘制点移动到某一个坐标处，该方法并不会进行绘制，主要是用来移动画笔。默认情况下起始坐标位于（0，0）点，我们可以手动调整默认位置(wrote by jenfei)
                 */
                path.moveTo(cur_x, cur_y);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                /**
                 * quadTo可以用来绘制一个带控制点的曲线，说白了，其实就是贝塞尔曲线,前两个参数表示控制点的坐标，后两个参数表示结束点的坐标(wrote by jenfei)
                 */
                path.quadTo(cur_x, cur_y, x, y);
                cur_x = x;
                cur_y = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                // 画path
//                 reDraw();
//                 path.reset();
                event.getSize();
                Log.i("event size:",event.getSize()+"");
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
        Log.v("jenfei's debug","PractiseWriteView类中的重写SurfaceView父类的方法onMeasure()");
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
            /**
             * 此次的isRunning是一个死循环
             */
            while (isRunning) {
                Log.v("jenfei","执行线程把要画的字画出来");
                String doubleString = PractiseMainFragment.getCurWord();
                String one = doubleString.substring(0,1);
                String two = doubleString.substring(1,2);
                String three = doubleString.substring(2,3);
                String four = doubleString.substring(3,4);
                String curWord = PractiseMainFragment.getCurWord();
                canvas = surfaceHolder.lockCanvas();
                // 将画布设置为白色
                paintWhite(canvas);
                // 描线
                drawLine(getStringWidth("宋"), canvas);
                // 描字
                drawText(one, canvas);
                drawText1(two, canvas);
                drawText2(three, canvas);
                drawText3(four, canvas);
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