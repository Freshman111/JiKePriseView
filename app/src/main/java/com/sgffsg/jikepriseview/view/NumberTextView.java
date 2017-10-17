package com.sgffsg.jikepriseview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.sgffsg.jikepriseview.R;
import com.sgffsg.jikepriseview.utils.DisplayUtils;

/**
 * 可滑轮显示数字的Textview
 * Created by sgffsg on 17/10/16.
 */

public class NumberTextView extends View {
    private static final String TAG="NumberTextView";

    private int MAIN_TEXT_COLOR = 0xff999999;

    private Paint textPaint;//文本画笔
    private Paint tranpTextPaint;//透明度文本画笔
    private RectF mBounds;//用于获取控件宽高
    private Rect textBound;//用于计算文本的宽高
    private int textSize;

    //控件总宽度
    private int mWidth;
    //控件高度
    private int mHeight;
    private int num;//数字
    private String text;
    private boolean isPlus=true;
    private boolean isAniming=false;//是否在动画过程中
    private int time=0;//当前执行的动画绘制次数
    private int paintAlpha=0;//画笔透明度
    private float offsetY;//y方向动画每次上移或下移的偏移
    private float afterX;//后面文字开始的x
    private int index=0;//开始动画的地方
    private String startBeforeStr,startAfterStr;//上面的文本上半部分和后半部分
    private String endAfterStr;// 后半部分
    private float moreWidth=0;//多预留出一个数字的位置


    public NumberTextView(Context context) {
        this(context,null);
    }

    public NumberTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NumberTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 初始化
     */
    private void init(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.NumberTextView);
        MAIN_TEXT_COLOR = typedArray.getColor(R.styleable.NumberTextView_textColor,0xff999999);
        textSize = typedArray.getDimensionPixelSize(R.styleable.NumberTextView_textSize,30);
        num=typedArray.getInteger(R.styleable.NumberTextView_number,0);
        text=num+"";
        typedArray.recycle();//注意要回收

        textPaint=new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(MAIN_TEXT_COLOR);
        textPaint.setTextSize(textSize);

        tranpTextPaint=new Paint();
        tranpTextPaint.setAntiAlias(true);
        tranpTextPaint.setColor(MAIN_TEXT_COLOR);
        tranpTextPaint.setTextSize(textSize);
        textBound=new Rect();
        moreWidth=textPaint.measureText("2.");

        textPaint.getTextBounds(text,0,text.length(),textBound);


    }

    /**
     * 设置数字
     * @param num 数字
     */
    public void setNum(int num) {
        this.num = num;
        textPaint.getTextBounds(num+"",0,text.length(),textBound);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds=new RectF(getLeft(),getTop(),getRight(),getBottom());
        mWidth= (int) (mBounds.right-mBounds.left);
        mHeight= (int) (mBounds.bottom-mBounds.top);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);// 获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);   //获取宽的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec); //获取高的模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);   //获取宽的尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec); //获取高的尺寸
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            //如果match_parent或者具体的值，直接赋值
            width = widthSize;
        } else {
            //如果是wrap_content，我们要得到控件需要多大的尺寸
            float textWidth = textBound.width()+moreWidth;   //文本的宽度
            //控件的宽度就是文本的宽度加上两边的内边距。内边距就是padding值，在构造方法执行完就被赋值
            width = (int) (getPaddingLeft() + textWidth + getPaddingRight());
        }
        //高度跟宽度处理方式一样
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            float textHeight = textBound.height()+ DisplayUtils.dpToPx(getContext(),20);
            height = (int) (getPaddingTop() + textHeight + getPaddingBottom());
        }
        //保存测量宽度和测量高度
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        float startX= (getWidth()-moreWidth)/2-textBound.width()/2;
        offsetY=getHeight()/60f;
        tranpTextPaint.setAlpha(255-paintAlpha*8);
        if (isAniming){
            if (!TextUtils.isEmpty(startBeforeStr)){
                canvas.drawText(startBeforeStr,startX,getHeight()/2+textBound.height()/2,textPaint);
            }
            if (isPlus){
                canvas.drawText(startAfterStr,startX+afterX,getHeight()/2+textBound.height()/2-offsetY*time,tranpTextPaint);
                canvas.drawText(endAfterStr,startX+afterX,getHeight()+textBound.height()/2-offsetY*time,textPaint);
            }else {
                canvas.drawText(startAfterStr,startX+afterX,getHeight()/2+textBound.height()/2+offsetY*time,tranpTextPaint);
                canvas.drawText(endAfterStr,startX+afterX,textBound.height()/2+offsetY*time,textPaint);
            }
        }else {
            canvas.drawText(text,startX,getHeight()/2+textBound.height()/2,textPaint);
        }


    }

    /**
     * 开始加减数字动画
     */
    public void startPlus(boolean isPlus){
        this.isPlus=isPlus;
        isAniming=true;
        if (isPlus){
            num++;
        }else {
            num--;
        }
        String resStr=num+"";
        if (text.length()!=resStr.length()){
            startBeforeStr="";
            startAfterStr=text;
            endAfterStr=resStr;
            index=0;
        }else {
            for (int i=0;i<resStr.length();i++){
                if (resStr.charAt(i)!=text.charAt(i)){
                    index=i;
                    break;
                }
            }
            startBeforeStr=text.substring(0,index);
            startAfterStr=text.substring(index);
            endAfterStr=resStr.substring(index);
        }
        afterX=textPaint.measureText(resStr,0,index);
        text=resStr;
        new Thread(new AnimThread()).start();

    }

    private class AnimThread implements Runnable {

        @Override
        public void run() {
            for (int i=1;i<=30;i++){
                paintAlpha++;
                try {
                    Thread.sleep(10);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
                time=i;
                postInvalidate();
            }
            paintAlpha=0;
            time=0;
            isAniming=false;
        }
    }
}
