package com.oscarcreator.roundsideprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Oscar Creator
 *
 * LinearProgressBar is a view which shows progress in the form of a bar.
 * The bar has round corners and optional outline.
 * */
public class RoundSideProgressBar extends View {

    //Horizontal and Vertical constants
    public static final int CONSTANT_HORIZONTAL = 0;
    public static final int CONSTANT_VERTICAL = 1;

    //Default values
    protected static final float DEFAULT_WIDTH = 250;
    protected static final float DEFAULT_HEIGHT = 50;
    protected static final float DEFAULT_PADDING = 0;

    protected static final int DEFAULT_PROGRESS_COLOR_ID = R.color.defaultProgressColor;
    protected static final int DEFAULT_BACKGROUND_COLOR_ID = R.color.defaultOutlineColor;
    protected static final int DEFAULT_PROGRESS_BACKGROUND_COLOR_ID = R.color.defaultProgressBackgroundColor;

    protected static final int DEFAULT_MAX_PROGRESS = 100;
    protected static final int DEFAULT_PROGRESS = 0;

    protected static final long DEFAULT_ANIMATION_SPEED = 200;

    //Tag
    private static final String TAG = "LinearProgressBar";

    private float maxProgress, progress;

    //Full width and height of the view
    private int fullWidth, fullHeight;

    //The width and height of the progressbar
    private float viewWidth, viewHeight;

    private int orientation;

    private long animationSpeed = DEFAULT_ANIMATION_SPEED;

    //Width of the outline
    private float outlineWidth;

    private int progressColor, outlineColor, progressBackgroundColor;
    private Paint outlinePaint, progressPaint, progressBackgroundPaint;

    private RectF rectView, rectViewPadding;

    private ValueAnimator valueAnimator;

    public RoundSideProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundSideProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public RoundSideProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * Set's the initial progress newProgress. This is only supposed to be used once on
     * start because it does NOT create the smooth newProgress animation as
     * {@link RoundSideProgressBar#setProgress(float)} does.
     *
     * @param newProgress the initial newProgress of the progressbar
     * */
    public void setInitialProgress(float newProgress){
        if (this.progress != newProgress) {
            this.progress = newProgress;
            this.invalidate();
        }

    }

    /**
     * Set's the progress newProgress with a {@link ValueAnimator}. This will
     * transition the current progress newProgress to passed in newProgress.
     *
     * @param newProgress the progress newProgress to transition to.
     * */
    public void setProgress(float newProgress){
        if (newProgress >= 0 && newProgress <= this.maxProgress){

            if (newProgress != getProgress()){
                long length = (long)(Math.abs(newProgress - getProgress()) / getMaxProgress() * animationSpeed);

                if (valueAnimator != null){
                    if (valueAnimator.isRunning()){
                        valueAnimator.cancel();
                    }
                    valueAnimator.removeAllUpdateListeners();
                    valueAnimator.setFloatValues(getProgress(), newProgress);
                }else{
                    valueAnimator = ValueAnimator.ofFloat(getProgress(), newProgress);
                }
                valueAnimator.addUpdateListener((valueAnimator) -> {
                    this.progress = (float) valueAnimator.getAnimatedValue();
                    this.invalidate();
                    //TODO maybe invalidate only the updated bit
                });
                valueAnimator.setDuration(length)
                        .start();
            }
        }
    }

    /**
     * Returns the current progress of the progressbar.
     *
     * @return current progress
     * */
    public float getProgress(){
        return progress;

    }


    /**
     * Set's the maxProgress newMaxProgress. Note that this will also
     * update the progressBar.
     *
     * @param newMaxProgress the new maxProgress
     * */
    public void setMaxProgress(float newMaxProgress){
        if (getMaxProgress() != newMaxProgress) {
            this.maxProgress = newMaxProgress;
            this.invalidate();
        }
    }

    /**
     * Returns the current maxProgress of the progressbar.
     *
     * @return current maxProgress
     * */
    public float getMaxProgress(){
        return maxProgress;
    }

    /**
     * Set's the width of the outline. Note that this will also
     * update the progressbar.
     *
     * @param newOutlineWidth the new outlineWidth
     * */
    public void setOutlineWidth(float newOutlineWidth) {
        if (getOutlineWidth() != newOutlineWidth) {
            this.outlineWidth = newOutlineWidth;
            this.invalidate();
        }
    }

    /**
     * Returns the current outlineWidth
     *
     * @return current outlineWidth
     * */
    public float getOutlineWidth(){
        return this.outlineWidth;
    }

    /**
     * Set's the color of the outline. Note that this will also
     * update the progressbar.
     *
     * @param newOutlineColor the new outlineColor
     * */
    public void setOutlineColor(int newOutlineColor){
        if (getOutlineColor() != newOutlineColor) {
            this.outlineColor = newOutlineColor;
            outlinePaint.setColor(getOutlineColor());
            this.invalidate();
        }
    }

    /**
     * Returns the current outlineColor
     *
     * @return current outlineColor
     * */
    public int getOutlineColor(){
        return this.outlineColor;
    }

    /**
     * Set's the current progressColor. Note that this will also
     * update the progressbar.
     *
     * @param newProgressColor new progressColor
     * */
    public void setProgressColor(int newProgressColor){
        if (getProgressColor() != newProgressColor){
            this.progressColor = newProgressColor;
            progressPaint.setColor(getProgressColor());
            this.invalidate();
        }
    }

    /**
     * Returns the current progressColor
     *
     * @return current progressColor
     * */
    public int getProgressColor(){
        return this.progressColor;
    }


    public void setProgressBackgroundColor(int newProgressBackgroundColor){

    }

    public int getProgressBackgroundColor(){
        return this.progressBackgroundColor;
    }


    public void setOrientation(int newOrientation){

    }


    public int getOrientation(){
        return this.orientation;
    }


    /**
     * Time for the animation from 0% to 100% progress
     * */
    public void setAnimationSpeed(long millis){
        animationSpeed = millis;
    }

    /**
     * Restore to default
     * */
    public void restoreAnimationSpeed(){
        this.animationSpeed = DEFAULT_ANIMATION_SPEED;
    }

    /**
     *
     * */
    public long getAnimationSpeed(){
        return animationSpeed;
    }


    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.RoundSideProgressBar, 0, 0);

        try {
            maxProgress = typedArray.getFloat(R.styleable.RoundSideProgressBar_maxProgress,
                    DEFAULT_MAX_PROGRESS);
            if (maxProgress <= 0){
                throw new IllegalArgumentException("maxProgress is not allowed to be negative or have a value of zero. value=" + maxProgress);
            }

            progress = typedArray.getFloat(R.styleable.RoundSideProgressBar_progress, DEFAULT_PROGRESS);
            if (maxProgress < 0){
                throw new IllegalArgumentException("progress is not allowed to have a negative value. value=" + progress);
            }

            progressColor = typedArray.getColor(R.styleable.RoundSideProgressBar_progressColor,
                    ContextCompat.getColor(context, DEFAULT_PROGRESS_COLOR_ID));
            outlineColor = typedArray.getColor(R.styleable.RoundSideProgressBar_outlineColor,
                    ContextCompat.getColor(context, DEFAULT_BACKGROUND_COLOR_ID));
            progressBackgroundColor = typedArray.getColor(R.styleable.RoundSideProgressBar_progressBackgroundColor,
                    ContextCompat.getColor(context, DEFAULT_PROGRESS_BACKGROUND_COLOR_ID));

            outlineWidth = typedArray.getDimension(R.styleable.RoundSideProgressBar_outlineWidth, DEFAULT_PADDING);

            orientation = typedArray.getInt(R.styleable.RoundSideProgressBar_orientation, CONSTANT_HORIZONTAL);

        } finally {
            typedArray.recycle();
        }


        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setColor(outlineColor);
        outlinePaint.setStyle(Paint.Style.STROKE);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(progressColor);

        progressBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressBackgroundPaint.setColor(progressBackgroundColor);

        rectView = new RectF(0, 0, viewWidth, viewHeight);
        rectViewPadding = new RectF(0, 0, 0, 0);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        rectView.left = getPaddingLeft() + outlineWidth - 1;
        rectView.top = getPaddingTop() + outlineWidth - 1;
        rectView.bottom = viewHeight + getPaddingTop() + outlineWidth + 1;
        rectView.right = viewWidth + getPaddingLeft() + outlineWidth + 1;

        float lengthProcent = Math.min(progress / maxProgress, 1f);
        float currentBarLength;

        if (orientation == CONSTANT_HORIZONTAL) {

            float paddedViewWidth = Math.max(rectView.right - rectView.left, 0);
            currentBarLength = lengthProcent * paddedViewWidth;

            //If padding fills the entire viewheight
            if (!(paddedViewWidth < 0 || rectView.bottom - rectView.top < 0)) {

                Path p = composeRoundedRect(rectView, CONSTANT_HORIZONTAL);

                if (1 - lengthProcent > 0) {
                    canvas.save();
                    canvas.clipRect(rectView.left + currentBarLength, rectView.top, rectView.right, rectView.bottom);
                    canvas.drawPath(p, progressBackgroundPaint);
                    canvas.restore();
                }

                if (lengthProcent > 0) {
                    canvas.save();
                    canvas.clipRect(rectView.left, rectView.top, rectView.left + currentBarLength, rectView.bottom);
                    canvas.drawPath(p, progressPaint);
                    canvas.restore();
                }
            }

            if (outlineWidth >= 0) {
                rectViewPadding.left = getPaddingLeft() + outlineWidth * 0.5f;
                rectViewPadding.top = getPaddingTop() + outlineWidth * 0.5f;
                rectViewPadding.bottom = viewHeight + getPaddingTop() + outlineWidth * 1.5f;
                rectViewPadding.right = viewWidth + getPaddingLeft() + outlineWidth * 1.5f;

                if (!(rectViewPadding.right - rectViewPadding.left < 0
                        || rectViewPadding.bottom - rectViewPadding.top < 0)){

                    outlinePaint.setStrokeWidth(outlineWidth);
                    Path p = composeRoundedRect(rectViewPadding, CONSTANT_HORIZONTAL);
                    canvas.drawPath(p, outlinePaint);
                }
            }

        //Orientation vertical
        } else {
            float paddedViewHeight = Math.max(rectView.bottom - rectView.top, 0f);
            currentBarLength = lengthProcent * paddedViewHeight;

            //If padding fills the entire viewHeight
            if (!(paddedViewHeight < 0 || rectView.right - rectView.left < 0)) {

                Path p = composeRoundedRect(rectView, CONSTANT_VERTICAL);

                if (1 - lengthProcent > 0) {
                    canvas.save();
                    canvas.clipRect(rectView.left, rectView.top, rectView.right, rectView.bottom - currentBarLength);
                    canvas.drawPath(p, progressBackgroundPaint);
                    canvas.restore();
                }

                if (lengthProcent > 0) {
                    canvas.save();
                    canvas.clipRect(rectView.left, rectView.bottom - currentBarLength, rectView.right, rectView.bottom);
                    canvas.drawPath(p, progressPaint);
                    canvas.restore();
                }
            }

            //Drawing outline
            if (outlineWidth >= 0) {
                rectViewPadding.left = getPaddingLeft() + outlineWidth * 0.505f;
                rectViewPadding.top = getPaddingTop() + outlineWidth * 0.505f;
                rectViewPadding.bottom = viewHeight + getPaddingTop() + outlineWidth * 1.495f;
                rectViewPadding.right = viewWidth + getPaddingLeft() + outlineWidth * 1.495f;

                if (!(rectViewPadding.right - rectViewPadding.left < 0
                        || rectViewPadding.bottom - rectViewPadding.top < 0)){
                    outlinePaint.setStrokeWidth(outlineWidth);
                    Path p = composeRoundedRect(rectViewPadding, CONSTANT_VERTICAL);
                    canvas.drawPath(p, outlinePaint);
                }
            }
        }
    }

    private Path composeRoundedRect(RectF rect, int orientation) {
        Path p = new Path();
        float cornerRadius;
        if (orientation == CONSTANT_HORIZONTAL) {
            cornerRadius = Math.abs((rect.bottom - rect.top) / 2);
            p.moveTo(rect.left + cornerRadius, rect.top);
            p.lineTo(rect.right - cornerRadius, rect.top);
            p.arcTo(new RectF(rect.right - cornerRadius * 2, rect.top, rect.right, rect.bottom), -90, 180);
            p.lineTo(rect.left + cornerRadius, rect.bottom);
            p.arcTo(new RectF(rect.left, rect.top, rect.left + cornerRadius * 2, rect.bottom), 90, 180);
        } else {
            cornerRadius = Math.abs((rect.right - rect.left) / 2);
            p.moveTo(rect.left, rect.top + cornerRadius);
            p.arcTo(new RectF(rect.left, rect.top, rect.right, rect.top + cornerRadius * 2), 180, 180);
            p.lineTo(rect.right, rect.bottom - cornerRadius);
            p.arcTo(new RectF(rect.left, rect.bottom - cornerRadius * 2, rect.right, rect.bottom), 0, 180);
            p.lineTo(rect.left, rect.top + cornerRadius);
        }

        p.close();
        return p;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //Must be this size
        if (widthMode == MeasureSpec.EXACTLY) {
            fullWidth = widthSize;
            viewWidth = (fullWidth - getPaddingLeft() - getPaddingRight() - outlineWidth * 2);

        //Can't be bigger than...
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Just used good display for the user when using default values
            if (orientation == CONSTANT_HORIZONTAL){
                fullWidth = (int) (Math.min(DEFAULT_WIDTH + getPaddingLeft() + getPaddingRight() + outlineWidth * 2, widthSize));
            }else{
                fullWidth = (int) (Math.min(DEFAULT_HEIGHT + getPaddingLeft() + getPaddingRight() + outlineWidth * 2, widthSize));
            }

            viewWidth = (fullWidth - getPaddingLeft() - getPaddingRight() - outlineWidth * 2);

        //Be whatever you want
        } else {
            fullWidth = (int) (DEFAULT_WIDTH + getPaddingLeft() + getPaddingRight() + outlineWidth * 2);
            viewWidth = DEFAULT_WIDTH;
        }

        //Must be this size
        if (heightMode == MeasureSpec.EXACTLY) {
            fullHeight = heightSize;
            viewHeight = (fullHeight - getPaddingTop() - getPaddingBottom() - outlineWidth * 2);

        //Can't be bigger than... ~wrap_content
        } else if (heightMode == MeasureSpec.AT_MOST) {

            //Just used good display for the user when using default values
            if (orientation == CONSTANT_HORIZONTAL){
                fullHeight = (int) Math.min(DEFAULT_HEIGHT + outlineWidth * 2 + getPaddingTop() + getPaddingBottom(), heightSize);
            }else{
                fullHeight = (int) Math.min(DEFAULT_WIDTH + outlineWidth * 2 + getPaddingTop() + getPaddingBottom(), heightSize);
            }
            viewHeight = (fullHeight - outlineWidth * 2 - getPaddingTop() - getPaddingBottom());

        //Be whatever you want
        } else {
            fullHeight = (int) (DEFAULT_HEIGHT + getPaddingTop() + getPaddingBottom() + outlineWidth * 2);
            viewHeight = DEFAULT_HEIGHT;
        }

//        Log.i(TAG, "heightSize:" + heightSize + " widthSize:" + widthSize);
//        Log.i(TAG, "heightmode:" + heightMode + " widthmode:" + widthMode);
//
//        Log.i("LinearProgressBar", "fullwidth: " + fullWidth + ", fullheight: " + fullHeight +
//                " viewWidth:" + viewWidth + " viewHeight:" + viewHeight + " my padding:" + outlineWidth);

        //Must call this
        setMeasuredDimension(fullWidth, fullHeight);
    }


}
