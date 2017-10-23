package com.sgffsg.jikepriseview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sgffsg.jikepriseview.view.NumberTextView;
import com.sgffsg.jikepriseview.view.RingView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout hand_layout;
    private NumberTextView tv;
    private RingView ringView;
    private ImageView iv_shining,iv_hand;
    private boolean checked=false;
    private EditText edit_input_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hand_layout= (RelativeLayout) findViewById(R.id.like_anim_hand_layout);
        tv= (NumberTextView) findViewById(R.id.like_anim_tv);
        iv_hand= (ImageView) findViewById(R.id.like_anim_iv_hand);
        iv_shining= (ImageView) findViewById(R.id.like_anim_iv_shining);
        ringView= (RingView) findViewById(R.id.like_anim_ring);
        tv.setOnClickListener(this);
        hand_layout.setOnClickListener(this);
        findViewById(R.id.like_anim_btn_set).setOnClickListener(this);
        findViewById(R.id.like_anim_btn_another_anim).setOnClickListener(this);
        edit_input_num= (EditText) findViewById(R.id.like_anim_edit_input_num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.like_anim_tv:
            case R.id.like_anim_hand_layout:
                if (checked){
                    checked=false;
                    tv.startPlus(false);
                    iv_shining.setVisibility(View.INVISIBLE);
                    iv_hand.setImageResource(R.mipmap.ic_messages_like_unselected);
                    cancelAnim();
                }else {
                    checked=true;
                    tv.startPlus(true);
                    praiseAnim();
                }
                break;
            case R.id.like_anim_btn_set:
                String input=edit_input_num.getText().toString();
                if(!TextUtils.isEmpty(input)){
                    tv.setNum(Integer.parseInt(input));
                }
                break;
            case R.id.like_anim_btn_another_anim:
                Intent intent=new Intent(this,FlipAnimActivity.class);
                startActivity(intent);
                break;
        }
    }
    /**
     * 取消点赞的动画
     */
    private void cancelAnim(){
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(iv_hand, "scaleX", 1.0f, 0.8f, 1f), ObjectAnimator.ofFloat(iv_hand, "scaleY", 1.0f, 0.8f, 1f));
        set.setDuration(200);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }


    /**
     * 点赞的动画
     */
    private void praiseAnim() {
        ObjectAnimator ringAnimator1=ObjectAnimator.ofInt(ringView,"radius",6,40);
        ObjectAnimator ringAnimator2=ObjectAnimator.ofFloat(ringView,"alpha",1,0.2f);
        final AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator2=ObjectAnimator.ofFloat(iv_shining,"scaleX",0.8f, 1.2f, 1f);
        ObjectAnimator animator3=ObjectAnimator.ofFloat(iv_shining,"scaleY", 0.8f, 1.2f, 1f);
        set.playTogether(ringAnimator1,ringAnimator2,animator2,animator3,ObjectAnimator.ofFloat(iv_hand, "scaleX", 0.8f, 1.2f, 1f), ObjectAnimator.ofFloat(iv_hand, "scaleY", 0.8f, 1.2f, 1f));
        set.setDuration(300);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ringView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ObjectAnimator oldAnimator1=ObjectAnimator.ofFloat(iv_hand,"scaleX",1,0.8f);
        ObjectAnimator oldAnimator2=ObjectAnimator.ofFloat(iv_hand,"scaleY",1,0.8f);
        AnimatorSet set1 = new AnimatorSet();
        set1.playTogether(oldAnimator1,oldAnimator2);
        set1.setDuration(100);
        set1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ringView.setVisibility(View.VISIBLE);
                iv_shining.setVisibility(View.VISIBLE);
                iv_hand.setImageResource(R.mipmap.ic_messages_like_selected);
                set.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set1.start();


    }
}
