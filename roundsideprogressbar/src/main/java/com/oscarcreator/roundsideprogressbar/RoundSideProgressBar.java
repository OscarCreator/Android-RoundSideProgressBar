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


    protected static final long DEFAULT_ANIMATION_SPEED = 1500;

    //Full width and height of the view
    protected int fullWidth, fullHeight;

    //The width and height of the progressbar
    protected float viewWidth, viewHeight;

    //Paints
    protected Paint outlinePaint, progressPaint, progressBackgroundPaint;

    protected RectF rectView, rectViewPadding;

    //Tag
    private static final String TAG = "LinearProgressBar";

    private float maxProgress, progress;

    /**{@link com.oscarcreator.roundsideprogressbar.R.attr#outlineWidth}*/
    private int orientation;

    private long animationSpeed = DEFAULT_ANIMATION_SPEED;

    //Width of the outline
    private float outlineWidth;

    private int progressColor, outlineColor, progressBackgroundColor;

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
     * start because it does NOT create the smooth animation as
     * {@link RoundSideProgressBar#setProgress(float, boolean)} does. But it could also be
     * used for setting progress instantly based on input
     *
     * @param newProgress the instantly new progress of the progressbar
     * */
    public void setProgress(float newProgress){
        if (this.progress != newProgress) {
            this.progress = newProgress;
            this.invalidate();
        }

    }

    /**
     * Set's the progress newProgress with a {@link ValueAnimator} if animate is true.
     * If animate is false the progress will be set instantly without any animation.
     *
     * @param newProgress the new progress to transition to.
     * @param animate true to animate from current to newProgress
     * */
    public void setProgress(float newProgress, boolean animate){
        if (!animate){
            setProgress(newProgress);
        }else{

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

                    });
                    valueAnimator.setDuration(length)
                            .start();
                }
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
            this.requestLayout();
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
     * Set's the current progress color. Note that this will also
     * update the progressbar with the new colors immediately.
     *
     * @param newProgressColor the new progress color
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

    /**
     * Set's the current progress background color. Note that this will also
     * update the progressbar with the new colors immediately
     *
     * @param newProgressBackgroundColor the new progress background color
     * */
    public void setProgressBackgroundColor(int newProgressBackgroundColor){
        if (getProgressBackgroundColor() != newProgressBackgroundColor){
            this.progressBackgroundColor = newProgressBackgroundColor;
            progressBackgroundPaint.setColor(getProgressBackgroundColor());
            this.invalidate();
        }
    }

    /**
     * Returns the current progress background color
     *
     * @return current progress background color
     * */
    public int getProgressBackgroundColor(){
        return this.progressBackgroundColor;
    }

    /**
     * Set's the orientation of the progressbar.
     * Note use the {@link #CONSTANT_HORIZONTAL} or {@link #CONSTANT_VERTICAL}
     * when setting the orientation.
     *
     * @param newOrientation the new orientation
     * */
    public void setOrientation(int newOrientation){
        if (this.orientation != newOrientation &&
                (newOrientation == CONSTANT_HORIZONTAL | newOrientation == CONSTANT_VERTICAL)){
            this.orientation = newOrientation;
            this.requestLayout();
        }
    }

    /**
     * Returns the current orientation of the progressbar.
     * 0 is horizontal and 1 is vertical as the
     * {@link #CONSTANT_HORIZONTAL} and {@link #CONSTANT_VERTICAL} is.
     *
     * @return current orientation
     * */
    public int getOrientation(){
        return this.orientation;
    }


    /**
     * The time from 0% to 100% of the progressbar animation in milliseconds.
     * Default value is {@link #DEFAULT_ANIMATION_SPEED}
     *
     * @param millis the new animation speed
     * */
    public void setAnimationSpeed(long millis){
        animationSpeed = millis;
    }

    /**
     * Restore to default animation speed which is {@link #DEFAULT_ANIMATION_SPEED}.
     * The time is in milliseconds and is the time from 0% to 100%
     * */
    public void restoreAnimationSpeed(){
        this.animationSpeed = DEFAULT_ANIMATION_SPEED;
    }

    /**
     * Returns the current animation speed. The speed
     * is the time it takes from 0% to 100% in milliseconds.
     *
     * @return the current animation speed
     * */
    public long getAnimationSpeed(){
        return animationSpeed;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        rectView.left = getPaddingLeft() + outlineWidth;
        rectView.top = getPaddingTop() + outlineWidth;
        rectView.bottom = viewHeight + getPaddingTop() + outlineWidth;
        rectView.right = viewWidth + getPaddingLeft() + outlineWidth;

        float lengthProcent = Math.min(progress / maxProgress, 1f);
        float currentBarLength;

        if (orientation == CONSTANT_HORIZONTAL) {

            float paddedViewWidth = Math.max(rectView.right - rectView.left, 0);
            currentBarLength = lengthProcent * paddedViewWidth;

            if (outlineWidth > 0.01) {
                rectViewPadding.left = getPaddingLeft() + outlineWidth * 0.55f;
                rectViewPadding.top = getPaddingTop() + outlineWidth * 0.55f;
                rectViewPadding.bottom = viewHeight + getPaddingTop() + outlineWidth * 1.45f;
                rectViewPadding.right = viewWidth + getPaddingLeft() + outlineWidth * 1.45f;

                if (!(rectViewPadding.right - rectViewPadding.left < 0
                        || rectViewPadding.bottom - rectViewPadding.top < 0)){

                    outlinePaint.setStrokeWidth(outlineWidth * 1.05f);
                    Path p = composeRoundedRect(rectViewPadding, CONSTANT_HORIZONTAL);
                    canvas.drawPath(p, outlinePaint);
                }
            }


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



            //Orientation vertical
        } else {
            float paddedViewHeight = Math.max(rectView.bottom - rectView.top, 0f);
            currentBarLength = lengthProcent * paddedViewHeight;

            //Drawing outline
            if (outlineWidth > 0.01) {
                rectViewPadding.left = getPaddingLeft() + outlineWidth * 0.55f;
                rectViewPadding.top = getPaddingTop() + outlineWidth * 0.55f;
                rectViewPadding.bottom = viewHeight + getPaddingTop() + outlineWidth * 1.45f;
                rectViewPadding.right = viewWidth + getPaddingLeft() + outlineWidth * 1.45f;

                if (!(rectViewPadding.right - rectViewPadding.left < 0
                        || rectViewPadding.bottom - rectViewPadding.top < 0)){
                    outlinePaint.setStrokeWidth(outlineWidth * 1.05f);
                    Path p = composeRoundedRect(rectViewPadding, CONSTANT_VERTICAL);
                    canvas.drawPath(p, outlinePaint);
                }
            }

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


        }
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

    protected void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.RoundSideProgressBar, R.attr.roundSideProgressBarStyle, R.style.RoundSideProgressBar);

        try {

            maxProgress = typedArray.getFloat(R.styleable.RoundSideProgressBar_maxProgress,
                    context.getResources().getInteger(R.integer.defMaxProgress));
            checkValidMaxProgress(maxProgress);

            progress = typedArray.getFloat(R.styleable.RoundSideProgressBar_progress,
                    context.getResources().getInteger(R.integer.defProgress));
            checkValidProgress(progress);


            progressColor = typedArray.getColor(R.styleable.RoundSideProgressBar_progressColor,
                    ContextCompat.getColor(context, DEFAULT_PROGRESS_COLOR_ID));
            outlineColor = typedArray.getColor(R.styleable.RoundSideProgressBar_outlineColor,
                    ContextCompat.getColor(context, DEFAULT_BACKGROUND_COLOR_ID));
            progressBackgroundColor = typedArray.getColor(R.styleable.RoundSideProgressBar_progressBackgroundColor,
                    ContextCompat.getColor(context, DEFAULT_PROGRESS_BACKGROUND_COLOR_ID));


            outlineWidth = typedArray.getDimension(R.styleable.RoundSideProgressBar_outlineWidth,
                    context.getResources().getDimension(R.dimen.defOutlineWidth));
            checkValidOutlineWidth(outlineWidth);

            orientation = typedArray.getInt(R.styleable.RoundSideProgressBar_orientation,
                    context.getResources().getInteger(R.integer.horizontalConst));
            checkValidOrientation(orientation);

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


    protected Path composeRoundedRect(RectF rect, int orientation) {
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

    private boolean checkValidMaxProgress(float value){
        if (value <= 0){
            throw new IllegalArgumentException("maxProgress is not allowed to be negative or have a value of zero. " +
                    "Current value: " + value);
        }
        return true;
    }

    private boolean checkValidProgress(float value){
        if (value < 0){
            throw new IllegalArgumentException("progress is not allowed to have a negative value. " +
                    "Current value: " + value);
        }
        return true;
    }

    private boolean checkValidOutlineWidth(float value){
        if (value < 0){
            throw new IllegalArgumentException("dividerWidth is not allowed to have a negative value. " +
                    "Current value: " + value);
        }
        return true;
    }

    private boolean checkValidOrientation(int value){
        if (value != 0 && value != 1){
            throw new IllegalArgumentException("orientation is only allowed to have a value of \"vertical\"" +
                    " or \"horizontal\". " +
                    "Current value: " + value);
        }
        return true;
    }



}
