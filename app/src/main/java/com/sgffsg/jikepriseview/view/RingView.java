package com.sgffsg.jikepriseview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sgffsg.jikepriseview.R;


/**
 * 光环view
 * Created by sgffsg on 17/10/16.
 */

public class RingView extends View {
    //光环颜色
    private int RING_COLOR = 0xffe4583e;
    private int radius;
    private int ringWidth;
    private Paint ringPaint;
    private RectF mBounds;//用于获取控件宽高

    //控件总宽度
    private int mWidth;
    //控件高度
    private int mHeight;

    public RingView(Context context) {
        this(context,null);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    //初始化
    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RingView);
        RING_COLOR = typedArray.getColor(R.styleable.RingView_ringColor,0xffe4583e);
        radius = typedArray.getDimensionPixelSize(R.styleable.RingView_ringRadius,10);
        ringWidth = typedArray.getDimensionPixelSize(R.styleable.RingView_ringWidth,3);

        ringPaint=new Paint();
        ringPaint.setAntiAlias(true);
        ringPaint.setColor(RING_COLOR);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(ringWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds=new RectF(getLeft(),getTop(),getRight(),getBottom());
        mWidth= (int) (mBounds.right-mBounds.left);
        mHeight= (int) (mBounds.bottom-mBounds.top);
    }

    public void setRadius(int radius){
        this.radius=radius;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawCircle(mWidth/2,mHeight/2,radius,ringPaint);

//        canvas.drawCircle(mWidth/2, mHeight/2, radius, ringPaint);
//        ringPaint.setAlpha(0);
//        ringPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawCircle(mWidth/2, mHeight/2, radius-ringWidth, ringPaint);

        canvas.drawCircle(mWidth/2,mHeight/2,radius,ringPaint);
    }
}
