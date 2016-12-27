package com.zkk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;


public class RulerView extends View {

    private int mMinVelocity;
    private Scroller mScroller;  //Scroller是一个专门用于处理滚动效果的工具类   用mScroller记录/计算View滚动的位置，再重写View的computeScroll()，完成实际的滚动
    private VelocityTracker mVelocityTracker; //主要用跟踪触摸屏事件（flinging事件和其他gestures手势事件）的速率。
    private int mWidth;
    private int mHeight;

    private float mSelectorValue = 50.0f; // 未选择时 默认的值 滑动后表示当前中间指针正在指着的值
    private float mMaxValue = 200;        // 最大数值
    private float mMinValue = 100.0f;     //最小的数值
    private float mPerValue = 1;          //最小单位  如 1:表示 每2条刻度差为1.   0.1:表示 每2条刻度差为0.1
                                          // 在demo中 身高mPerValue为1  体重mPerValue 为0.1

    private float mLineSpaceWidth = 5;    //  尺子刻度2条线之间的距离
    private float mLineWidth = 4;         //  尺子刻度的宽度
    private float mLineMaxHeight = 420;   //  尺子刻度分为3中不同的高度。 mLineMaxHeight表示最长的那根(也就是 10的倍数时的高度)
    private float mLineMidHeight = 30;    //  mLineMidHeight  表示中间的高度(也就是 5  15 25 等时的高度)
    private float mLineMinHeight = 17;    //  mLineMinHeight  表示最短的那个高度(也就是 1 2 3 4 等时的高度)

    private float mTextMarginTop = 10;    //o
    private float mTextSize =30;         //尺子刻度下方数字 textsize

    private boolean mAlphaEnable = false;  // 尺子 最左边 最后边是否需要透明 (透明效果更好点)

    private float mTextHeight;            //尺子刻度下方数字  的高度

    private Paint mTextPaint;             // 尺子刻度下方数字( 也就是每隔10个出现的数值) paint
    private Paint mLinePaint;             //  尺子刻度  paint

    private int mTotalLine;               //共有多少条 刻度
    private int mMaxOffset;               //所有刻度 共有多长
    private float mOffset;                // 默认状态下，mSelectorValue所在的位置  位于尺子总刻度的位置
    private int mLastX, mMove;
    private OnValueChangeListener mListener;  // 滑动后数值回调


    public RulerView(Context context) {
        this(context, null);

    }

    public RulerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        Log.d("zkk---","init");
        mScroller = new Scroller(context);


        mMinVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.BLACK);
        mTextHeight = getFontHeight(mTextPaint);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(mLineWidth);
        mLinePaint.setColor(Color.GRAY);

        this.mLineSpaceWidth = myfloat(25.0F);
        this.mLineWidth = myfloat(2.0F);
        this.mLineMaxHeight = myfloat(100.0F);
        this.mLineMidHeight = myfloat(60.0F);
        this.mLineMinHeight = myfloat(40.0F);
        this.mTextHeight = myfloat(40.0F);

       // setValue(1990, 1940, 2016, 1);

    }
    public static int  myfloat(float paramFloat){
        return (int)(0.5F + paramFloat * 1.0f);
    }

    private float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }


    public void setValue(float selectorValue, float minValue, float maxValue, float per) {
        this.mSelectorValue = selectorValue;
        this.mMaxValue = maxValue;
        this.mMinValue = minValue;
        this.mPerValue = (int) (per * 10.0f);
        this.mTotalLine = ((int) ((mMaxValue * 10 - mMinValue * 10) / mPerValue)) + 1;


        mMaxOffset = (int) (-(mTotalLine - 1) * mLineSpaceWidth);
        mOffset = (mMinValue - mSelectorValue) / mPerValue * mLineSpaceWidth * 10;
        Log.d("zkk===","mOffset--           "+mOffset  +"         =====mMaxOffset    "+mMaxOffset
        +"  mTotalLine  " +mTotalLine);
        invalidate();
        setVisibility(VISIBLE);
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        mListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mWidth = w;
            mHeight = h;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left, height;
        String value;
        int alpha = 0;
        float scale;
        int srcPointX = mWidth / 2;
        for (int i = 0; i < mTotalLine; i++) {
            left = srcPointX + mOffset + i * mLineSpaceWidth;

            if (left < 0 || left > mWidth) {
                continue;  //  先画默认值在正中间，左右各一半的view。  多余部分暂时不画(也就是从默认值在中间，画旁边左右的刻度线)
            }

            if (i % 10 == 0) {
                height = mLineMaxHeight;
            } else if (i % 5 == 0) {
                height = mLineMidHeight;
            } else {
                height = mLineMinHeight;
            }
            if (mAlphaEnable) {
                scale = 1 - Math.abs(left - srcPointX) / srcPointX;
                alpha = (int) (255 * scale * scale);

                mLinePaint.setAlpha(alpha);
            }
            canvas.drawLine(left, 0, left, height, mLinePaint);

            if (i % 10 == 0) {
                value = String.valueOf((int) (mMinValue + i * mPerValue / 10));
                if (mAlphaEnable) {
                    mTextPaint.setAlpha(alpha);
                }
                canvas.drawText(value, left - mTextPaint.measureText(value) / 2,
                        height + mTextMarginTop + mTextHeight, mTextPaint);    // 在为整数时,画 数值
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("zkk---","onTouchEvent-");

        int action = event.getAction();
        int xPosition = (int) event.getX();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mLastX = xPosition;
                mMove = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mMove = (mLastX - xPosition);
                changeMoveAndValue();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                countMoveEnd();
                countVelocityTracker();
                return false;
            default:
                break;
        }

        mLastX = xPosition;
        return true;
    }

    private void countVelocityTracker() {
        Log.d("zkk---","countVelocityTracker-");
        mVelocityTracker.computeCurrentVelocity(1000);  //初始化速率的单位
        float xVelocity = mVelocityTracker.getXVelocity(); //当前的速度
        if (Math.abs(xVelocity) > mMinVelocity) {
            mScroller.fling(0, 0, (int) xVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        }
    }


    /**
     * 滑动结束后，若是指针在2条刻度之间时，改变mOffset 让指针正好在刻度上。
     */
    private void countMoveEnd() {

        mOffset -= mMove;
        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset;
        } else if (mOffset >= 0) {
            mOffset = 0;
        }

        mLastX = 0;
        mMove = 0;

        mSelectorValue = mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mLineSpaceWidth) * mPerValue / 10.0f;
        mOffset = (mMinValue - mSelectorValue) * 10.0f / mPerValue * mLineSpaceWidth;



        notifyValueChange();
        postInvalidate();
    }


    /**
     * 滑动后的操作
     */
    private void changeMoveAndValue() {
        mOffset -= mMove;

        if (mOffset <= mMaxOffset) {
            mOffset = mMaxOffset;
            mMove = 0;
            mScroller.forceFinished(true);
        } else if (mOffset >= 0) {
            mOffset = 0;
            mMove = 0;
            mScroller.forceFinished(true);
        }
        mSelectorValue = mMinValue + Math.round(Math.abs(mOffset) * 1.0f / mLineSpaceWidth) * mPerValue / 10.0f;


        notifyValueChange();
        postInvalidate();
    }

    private void notifyValueChange() {
        if (null != mListener) {
            mListener.onValueChange(mSelectorValue);
        }
    }


    /**
     * 滑动后的回调
     */
    public interface OnValueChangeListener {
        void onValueChange(float value);
    }

    @Override
    public void computeScroll() {
        Log.d("zkk---","computeScroll-");
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {     //mScroller.computeScrollOffset()返回 true表示滑动还没有结束
            if (mScroller.getCurrX() == mScroller.getFinalX()) {
                countMoveEnd();
            } else {
                int xPosition = mScroller.getCurrX();
                mMove = (mLastX - xPosition);
                changeMoveAndValue();
                mLastX = xPosition;
            }
        }
    }
}
