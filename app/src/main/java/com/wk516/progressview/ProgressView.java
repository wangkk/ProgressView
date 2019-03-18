package com.wk516.progressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ke_Wang on 2019/3/18.
 */

public class ProgressView extends View {

    private int TEXT_LINE_SPAEC;
    /**
     * 控件的宽高
     */
    private int mWidth;
    /**
     * 各部分颜色
     */
    private int leftColor;
    private int rightColor;
    private int spaceColor;
    private int angleColor;
    private int textColor;

    private float textSize;
    private float progressHeight;
    private float spaceWidth;
    private float angleSize;//等边直角三角形的高
    private float progress;
    private Paint mPaint;
    private int index;
    private List<String> textList = new ArrayList<>();
    private int textWidth;
    private int paddingLeft;
    private int paddingRight;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeStyle(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initTypeStyle(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ProgressView,
                        defStyleAttr, 0);
        textColor = typedArray.getColor(R.styleable.ProgressView_textColor, Color.BLACK);
        textSize = typedArray.getDimension(R.styleable.ProgressView_textSize, UIUtils.sp2px(context, 12));
        progressHeight = typedArray.getDimension(R.styleable.ProgressView_progressHeight, UIUtils.dip2px(context, 20));
        spaceWidth = typedArray.getDimension(R.styleable.ProgressView_spaceWidth, UIUtils.dip2px(context, 8));
        leftColor = typedArray.getColor(R.styleable.ProgressView_finishColor, Color.RED);
        spaceColor = typedArray.getColor(R.styleable.ProgressView_spaceColor, Color.WHITE);
        rightColor = typedArray.getColor(R.styleable.ProgressView_noFinishColor, Color.GRAY);
        angleSize = typedArray.getDimension(R.styleable.ProgressView_angleHeight, UIUtils.dip2px(context, 10));
        angleColor = typedArray.getColor(R.styleable.ProgressView_angleColor, Color.GRAY);

        TEXT_LINE_SPAEC = UIUtils.dip2px(context, 5);
    }

    private void initPaint() {
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));//设置宽高
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        mWidth = getWidth() - paddingLeft - paddingRight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画文字
        drawText(canvas);
        progress = textWidth * index + textWidth / 2;
        //画左边的线
        drawLeftLine(canvas);

        drawMidLine(canvas);

        drawRightLine(canvas);

        drawTriangle(canvas);
    }

    /**
     * 绘制文字
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        if (textList.size() > 0) {
            textWidth = mWidth / textList.size();
            mPaint.setColor(textColor);
            mPaint.setTextSize(textSize);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                float size = text.length() * textSize;
                //为保证间隙在中间
                canvas.drawText(textList.get(i),
                        paddingLeft + textWidth * i + textWidth / 2 - size / 2, textSize, mPaint);
            }
        }
    }

    /**
     * 绘制三角
     * @param canvas
     */
    private void drawTriangle(Canvas canvas) {
        float startX = progress;
        float startY = textSize + TEXT_LINE_SPAEC + progressHeight;
        float leftX = progress - angleSize;
        float leftY = textSize + TEXT_LINE_SPAEC + progressHeight + angleSize;
        float rightX = progress + angleSize;
        float rightY = textSize + TEXT_LINE_SPAEC + progressHeight + angleSize;
        mPaint.setColor(angleColor);
        //实例化路径
        Path path = new Path();
        path.moveTo(startX, startY);// 此点为多边形的起点
        path.lineTo(leftX, leftY);
        path.lineTo(rightX, rightY);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, mPaint);
    }

    /**
     * 未完成的进度条
     * @param canvas
     */
    private void drawRightLine(Canvas canvas) {
        float left = progress + spaceWidth / 2;
        float top = textSize + TEXT_LINE_SPAEC;
        float right = mWidth;
        float bottom = textSize + TEXT_LINE_SPAEC + progressHeight;
        mPaint.setColor(rightColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    /**
     * 中间间隙
     * @param canvas
     */
    private void drawMidLine(Canvas canvas) {
        float left = progress - spaceWidth / 2;
        float right = progress + spaceWidth / 2;
        float top = textSize + TEXT_LINE_SPAEC;
        float bottom = textSize + TEXT_LINE_SPAEC + progressHeight;
        Log.e("angle:", "----------" + left);
        mPaint.setColor(spaceColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    /**
     * 完成的进度条
     * @param canvas
     */
    private void drawLeftLine(Canvas canvas) {
        float left = paddingLeft;
        float right = progress - spaceWidth / 2;
        float top = textSize + TEXT_LINE_SPAEC;
        float bottom = textSize + TEXT_LINE_SPAEC + progressHeight;
        mPaint.setColor(leftColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    /**
     * 文字集合
     *
     * @param texts
     */
    public void setTexts(List<String> texts, int index) {
        if (texts != null && texts.size() > 0) {
            textList.clear();
            textList.addAll(texts);
            this.index = index;
            invalidate();
        }
    }

    /**
     * 解决7.0以上获取宽为0的情况
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mWidth = getMeasuredWidth();
                    invalidate();
                }
            }
        });
    }
}
