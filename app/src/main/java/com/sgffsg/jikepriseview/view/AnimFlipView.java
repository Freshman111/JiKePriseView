package com.sgffsg.jikepriseview.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.sgffsg.jikepriseview.R;


/**
 * 圆圈连续半折叠动画view
 * Created by sgffsg on 17/10/19.
 */

public class AnimFlipView extends View{

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Camera camera = new Camera();

    int degreeX;
    int circleDegree=0;
    AnimatorSet animationSet=new AnimatorSet();
    ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "degreeX", 0, -45);
    ObjectAnimator animator2 = ObjectAnimator.ofInt(this, "degreeX", 0, -45);
    ObjectAnimator animator = ObjectAnimator.ofInt(this, "circleDegree", -45, 0, 90);

    public AnimFlipView(Context context) {
        super(context);
    }

    public AnimFlipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimFlipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
        animationSet.playSequentially(animator2,animator);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(1000);
        animationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                circleDegree=0;
                animator1.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator1.setDuration(1000);
        animator1.setInterpolator(new LinearInterpolator());
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                degreeX=0;
                invalidate();
                circleDegree=-45;
                animationSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator1.start();
//        animationSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator1.end();
//        animationSet.end();
    }

    @SuppressWarnings("unused")
    public void setDegreeX(int degree) {
        this.degreeX = degree;
        invalidate();
    }

    @SuppressWarnings("unused")
    public void setCircleDegree(int circleDegree){
        this.circleDegree=circleDegree;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        float x = centerX - bitmapWidth / 2;
        float y = centerY - bitmapHeight / 2;

        // 下半部分
        canvas.save();
        canvas.rotate(-circleDegree*2,centerX,centerY);
        canvas.clipRect(0, centerY, getWidth(), getHeight());
        canvas.rotate(circleDegree*2,centerX,centerY);
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

        // 上半部分
        canvas.save();

        canvas.rotate(-circleDegree*2,centerX,centerY);
        canvas.clipRect(0, 0, getWidth(), centerY);

        camera.save();
        camera.rotateX(degreeX);
        canvas.translate(centerX, centerY);
        camera.applyToCanvas(canvas);
        canvas.translate(-centerX, -centerY);
        camera.restore();
        canvas.rotate(circleDegree*2,centerX,centerY);
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

    }
}
