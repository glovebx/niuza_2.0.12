package com.xunlei.library.pulltorefresh.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.niuza.android.R;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Mode;

@SuppressLint({"ViewConstructor"})
public class IndicatorLayout extends FrameLayout implements AnimationListener {
    static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;
    private ImageView mArrowImageView;
    private Animation mInAnim;
    private Animation mOutAnim;
    private final Animation mResetRotateAnimation;
    private final Animation mRotateAnimation;

    public IndicatorLayout(Context context, Mode mode) {
        super(context);
        int inAnimResId;
        int outAnimResId;

        this.mArrowImageView = new ImageView(context);
        Drawable arrowD = getResources().getDrawable(R.drawable.pulltorefresh_indicator_arrow);
        this.mArrowImageView.setImageDrawable(arrowD);
        int padding = getResources().getDimensionPixelSize(R.dimen.indicator_internal_padding);
        this.mArrowImageView.setPadding(padding, padding, padding, padding);
        addView(this.mArrowImageView);
        switch (mode) {
            case PULL_FROM_END:
                inAnimResId = R.anim.slide_in_from_bottom;
                outAnimResId = R.anim.slide_out_to_bottom;
                setBackgroundResource(R.drawable.pulltorefresh_indicator_bg_bottom);
                this.mArrowImageView.setScaleType(ScaleType.MATRIX);
                Matrix matrix = new Matrix();
                matrix.setRotate(180.0f, ((float) arrowD.getIntrinsicWidth()) / 2.0f, ((float) arrowD.getIntrinsicHeight()) / 2.0f);
                this.mArrowImageView.setImageMatrix(matrix);
                break;
            default:
                inAnimResId = R.anim.slide_in_from_top;
                outAnimResId = R.anim.slide_out_to_top;
                setBackgroundResource(R.drawable.pulltorefresh_indicator_bg_top);
                break;
        }
        this.mInAnim = AnimationUtils.loadAnimation(context, inAnimResId);
        this.mInAnim.setAnimationListener(this);
        this.mOutAnim = AnimationUtils.loadAnimation(context, outAnimResId);
        this.mOutAnim.setAnimationListener(this);
        Interpolator interpolator = new LinearInterpolator();
        this.mRotateAnimation = new RotateAnimation(0.0f, -180.0f, 1, 0.5f, 1, 0.5f);
        this.mRotateAnimation.setInterpolator(interpolator);
        this.mRotateAnimation.setDuration(150);
        this.mRotateAnimation.setFillAfter(true);
        this.mResetRotateAnimation = new RotateAnimation(-180.0f, 0.0f, 1, 0.5f, 1, 0.5f);
        this.mResetRotateAnimation.setInterpolator(interpolator);
        this.mResetRotateAnimation.setDuration(150);
        this.mResetRotateAnimation.setFillAfter(true);
    }

    public final boolean isVisible() {
        Animation currentAnim = getAnimation();
        if (currentAnim != null) {
            if (this.mInAnim == currentAnim) {
                return true;
            }
            return false;
        } else if (getVisibility() != 0) {
            return false;
        } else {
            return true;
        }
    }

    public void hide() {
        startAnimation(this.mOutAnim);
    }

    public void show() {
        this.mArrowImageView.clearAnimation();
        startAnimation(this.mInAnim);
    }

    public void onAnimationEnd(Animation animation) {
        if (animation == this.mOutAnim) {
            this.mArrowImageView.clearAnimation();
            setVisibility(View.GONE);
        } else if (animation == this.mInAnim) {
            setVisibility(View.VISIBLE);
        }
        clearAnimation();
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
        setVisibility(View.VISIBLE);
    }

    public void releaseToRefresh() {
        this.mArrowImageView.startAnimation(this.mRotateAnimation);
    }

    public void pullToRefresh() {
        this.mArrowImageView.startAnimation(this.mResetRotateAnimation);
    }
}
