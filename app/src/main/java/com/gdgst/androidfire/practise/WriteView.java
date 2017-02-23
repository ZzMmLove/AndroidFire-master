package com.gdgst.androidfire.practise;

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

import com.gdgst.androidfire.ui.main.view.PractiseWriteViewListener;

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

    private int ScreenWidth, ScreenHeight;

    /**
     * 构造方法
     * @param context 上下文
     * @param attr    属性集
     */
    public WriteView(Context context, AttributeSet attr) {
        super(context, attr);

        initScreenParams();
        // 如果没有配置字体
        initView(context);

        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
    }

    /**
     * 初始化屏幕参数（宽度和高度）
     */
    private void initScreenParams() {
        int screenHeight = PractiseOriginalActivity.getSCREEN_HEIGHT();
        int screenWidth = PractiseOriginalActivity.getSCREEN_WIDTH();
        Log.d("原始","打印屏幕的高度"+screenHeight+"屏幕的宽度:"+screenWidth);
        //screenWidth>screenHeight

        DEFAULT_TEXT_SIZE = screenWidth * 3 / 4;//screenHeight * 3 / 8;
        DEFAULT_START_X = screenWidth / 8;//1
        DEFAULT_START_Y = screenHeight / 2;//1/3
        Log.d("原始","打印经过计算之后的屏幕的X坐标"+DEFAULT_START_X+"屏幕的Y坐标:"+DEFAULT_START_Y);

        Log.i("2DEFAULT_TEXT_SIZE:",DEFAULT_TEXT_SIZE+"");
        Log.i("2DEFAULT_START_X:",DEFAULT_START_X+"");
        Log.i("2DEFAULT_START_Y:",DEFAULT_START_Y+"");
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
        //setBackgroundColor(Color.YELLOW);
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

    //初始化坐标
    private void initCoordinate() {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();

        leftTopX = DEFAULT_START_X;
        leftTopY = DEFAULT_START_Y + fontMetrics.top;
        Log.d("原始", "leftTopX:"+leftTopX+"leftTopY:"+leftTopY);

        rightTopX = DEFAULT_START_X + getStringWidth(DEFAULT_FONT_DEMO);
        rightTopY = leftTopY;

        leftBottomX = leftTopX;
        leftBottomY = DEFAULT_START_Y + fontMetrics.bottom;

        rightBottomX = rightTopX;
        rightBottomY = leftBottomY;
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

        // draw frame lines
        canvas.drawLine(leftTopX, leftTopY, rightTopX, rightTopY, framePaint);
        canvas.drawLine(leftTopX, leftTopY, leftBottomX, leftBottomY,
                framePaint);
        canvas.drawLine(rightTopX, rightTopY, rightBottomX, rightBottomY,
                framePaint);
        canvas.drawLine(rightBottomX, rightBottomY, leftBottomX, leftBottomY,
                framePaint);

        // draw dotted lines
        canvas.drawLine(leftTopX, leftTopY, rightBottomX, rightBottomY,
                dotLinePaint);
        canvas.drawLine(rightTopX, rightTopY, leftBottomX, leftBottomY,
                dotLinePaint);

        canvas.drawLine(leftTopX, (leftBottomY - leftTopY) / 2 + leftTopY,
                rightTopX, (rightBottomY - rightTopY) / 2 + rightTopY,
                dotLinePaint);
        canvas.drawLine((rightTopX - leftTopX) / 2 + leftTopX, leftTopY,
                (rightBottomX - leftBottomX) / 2 + leftBottomX, leftBottomY,
                dotLinePaint);

        Log.i("leftTopX",leftTopX+"");
        Log.i("leftTopY",leftTopY+"");
        Log.i("rightTopX",rightTopX+"");
        Log.i("rightTopY",rightTopY+"");

        Log.i("leftBottomY",leftBottomY+"");
        Log.i("rightBottomY",rightBottomY+"");
        Log.i("leftBottomX",leftBottomX+"");
        Log.i("rightBottomX",rightBottomX+"");
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        getHolder().addCallback(this);
        wordChangeThread = new WordChangeThread();
        wordChangeThread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // 更新数据库
//		UserRecordUtil.insertLatestUserRecord(getContext(),
//				PractiseOriginalActivity.getCurFontLib(),
//				PractiseOriginalActivity.getCurWordIndex());
//
        if (WriteView.ifWrite())
            PractiseOriginalActivity.writeSumPlusPlus();
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
        } else if (x > rightTopX) {
//            practiseWriteViewListener.isShare();
            return false;
        }

        if (y < leftTopY) {
//            practiseWriteViewListener.isShare();
            return false;
        } else if (y > leftBottomY) {
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
                String curWord = PractiseOriginalActivity.getCurWord();
                canvas = surfaceHolder.lockCanvas();
                if (canvas == null) {
                    return;
                }
                // 将画布设置为白色
                paintWhite(canvas);
                // 描线
                drawLine(getStringWidth(curWord), canvas);
                // 描字
                drawText(curWord, canvas);
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