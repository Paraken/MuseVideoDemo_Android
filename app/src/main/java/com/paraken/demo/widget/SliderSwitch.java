package com.paraken.demo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.paraken.demo.R;


/**
 * Created by lifs on 16-4-22.
 */
public class SliderSwitch extends View {
    //private static final String TAG = "SliderSwitch";
    public static final int SWITCH_OFF = 0;//关闭状态
    public static final int SWITCH_ON = 1;//打开状态
    public static final int SWITCH_SCROLING = 2;//滚动状态
    //开关状态图
    private Bitmap mSwitch_off, mSwitch_on, mSwitch_thumb;
    //用于显示的文本
    private String mOnText = "打开";
    private String mOffText = "关闭";
    private int mSwitchStatus = SWITCH_OFF;
    private boolean mHasScrolled = false;//表示是否发生过滚动
    private int mSrcX = 0, mDstX = 0;
    private int mBmpWidth = 0;
    private int mBmpHeight = 0;
    private int mThumbWidth = 0;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private OnSwitchChangedListener mOnSwitchChangedListener = null;

    public SliderSwitch(Context context) {
        this(context, null);
    }

    public SliderSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SliderSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    //初始化三幅图片
    private void init(AttributeSet attrs) {
        Resources res = getResources();
        mSwitch_off = BitmapFactory.decodeResource(res, R.drawable.switch_off);
        mSwitch_on = BitmapFactory.decodeResource(res, R.drawable.switch_on);
        mSwitch_thumb = BitmapFactory.decodeResource(res, R.drawable.switch_thumb);
        mBmpWidth = mSwitch_on.getWidth();
        mBmpHeight = mSwitch_on.getHeight();
        mThumbWidth = mSwitch_thumb.getWidth();

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SliderSwitch, 0, 0);
        boolean checked = a.getBoolean(R.styleable.SliderSwitch_checked, false);
        if (checked) {
            mSwitchStatus = SWITCH_ON;
        }
        a.recycle();
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        params.width = mBmpWidth;
        params.height = mBmpHeight;
        super.setLayoutParams(params);
    }

    /**
     * 为开关控件设置状态改变监听函数
     *
     * @param onSwitchChangedListener 参见 {@link OnSwitchChangedListener}
     */
    public void setOnSwitchChangedListener(OnSwitchChangedListener onSwitchChangedListener) {
        mOnSwitchChangedListener = onSwitchChangedListener;
    }

    /**
     * 设置开关上面的文本
     *
     * @param onText  控件打开时要显示的文本
     * @param offText 控件关闭时要显示的文本
     */
    public void setText(final String onText, final String offText) {
        mOnText = onText;
        mOffText = offText;
        invalidate();
    }

    /**
     * 设置开关的状态
     *
     * @param on 是否打开开关 打开为true 关闭为false
     */
    public void setStatus(boolean on) {
        mSwitchStatus = (on ? SWITCH_ON : SWITCH_OFF);
    }

    public int getStatus() {
        return mSwitchStatus;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        int action = event.getAction();
        //Log.d(TAG, "onTouchEvent  x=" + event.getX());
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mSrcX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mDstX = Math.max((int) event.getX(), 10);
                mDstX = Math.min(mDstX, 62);
                if (mSrcX == mDstX)
                    return true;
                mHasScrolled = true;
                mSrcX = mDstX;
                break;
            case MotionEvent.ACTION_UP:
                mHasScrolled = false;
                if (mSwitchStatus == SWITCH_ON) {
                    mSwitchStatus = SWITCH_OFF;
                } else {
                    mSwitchStatus = SWITCH_ON;
                }
                setStatus(mSwitchStatus == SWITCH_ON);
                invalidate();
                //状态改变的时候 回调事件函数
                if (mOnSwitchChangedListener != null) {
                    mOnSwitchChangedListener.onSwitchChanged(this, mSwitchStatus);
                }
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘图的时候 内部用到了一些数值的硬编码，其实不太好，
        //主要是考虑到图片的原因，图片周围有透明边界，所以要有一定的偏移
        //硬编码的数值只要看懂了代码，其实可以理解其含义，可以做相应改进。
        mPaint.setTextSize(14);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);

        if (mSwitchStatus == SWITCH_OFF) {
            drawBitmap(canvas, null, null, mSwitch_off);
            drawBitmap(canvas, null, null, mSwitch_thumb);
            mPaint.setColor(Color.rgb(105, 105, 105));
            canvas.translate(mSwitch_thumb.getWidth(), 0);
            //canvas.drawText(mOffText, 0, 20, mPaint);
        } else if (mSwitchStatus == SWITCH_ON) {
            drawBitmap(canvas, null, null, isEnabled() ? mSwitch_on : mSwitch_off);
            int count = canvas.save();
            canvas.translate(mSwitch_on.getWidth() - mSwitch_thumb.getWidth(), 0);
            drawBitmap(canvas, null, null, mSwitch_thumb);
            mPaint.setColor(Color.WHITE);
            canvas.restoreToCount(count);
            //canvas.drawText(mOnText, 17, 20, mPaint);
        }
    }

    public void drawBitmap(Canvas canvas, Rect src, Rect dst, Bitmap bitmap) {
        dst = (dst == null ? new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()) : dst);
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, src, dst, paint);
    }

    public interface OnSwitchChangedListener {
        /**
         * 状态改变 回调函数
         *
         * @param status SWITCH_ON表示打开 SWITCH_OFF表示关闭
         */
        void onSwitchChanged(SliderSwitch obj, int status);
    }
}