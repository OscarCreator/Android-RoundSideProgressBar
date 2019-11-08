package com.oscarcreator.roundsideprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

public class DividedRoundSideProgressBar extends RoundSideProgressBar {

    protected static final int DEFAULT_DIVIDER_COLOR_ID = R.color.defaultDividerColor;

    protected int dividers;

    protected Paint dividerPaint;

    protected float dividerWidth;


    public DividedRoundSideProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DividedRoundSideProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public DividedRoundSideProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    /**
     * Set's the quantity of dividers on the progressbar.
     *
     * @param dividers quantity of dividers
     * */
    public void setDividers(int dividers){
        if (dividers != this.dividers){
            this.dividers = dividers;
            this.invalidate();
        }
    }


    /**
     * Set's the width of the dividers on the progressbar.
     *
     * @param width the width of the dividers
     * */
    public void setDividerWidth(float width){
        if (width != dividerWidth){
            this.dividerWidth = width;
            this.invalidate();
        }
    }


    /**
     * Set's the color of the dividers
     *
     * @param color the color for the dividers
     * */
    public void setDividerColor(int color){
        if (color != dividerPaint.getColor()){
            dividerPaint.setColor(color);
            this.invalidate();
        }
    }


    /**
     * Returns the quantity of dividers currently using.
     *
     * @return quantity of dividers
     * */
    public int getDividers() {
        return dividers;
    }


    /**
     * Returns the width of the current dividers.
     *
     * @return width of the dividers
     * */
    public float getDividerWidth() {
        return dividerWidth;
    }


    /**
     * Returns the color of the dividers.
     *
     * @return color of dividers
     * */
    public int getDividerColor() {
        return dividerPaint.getColor();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        if (getOrientation() == CONSTANT_HORIZONTAL) {
            Path p = composeRoundedRect(rectView, CONSTANT_HORIZONTAL);

            float spacing = (viewWidth - (dividerWidth * dividers)) / (dividers + 1);
            for (int i = 1; i <= dividers; i++){
                float x = rectView.left + spacing * i + (dividerWidth * (i - 1));
                canvas.save();
                canvas.clipRect(x, rectView.top,
                        x + dividerWidth, rectView.bottom);
                canvas.drawPath(p, dividerPaint);
                canvas.restore();
            }


        } else {
            Path p = composeRoundedRect(rectView, CONSTANT_VERTICAL);

            float spacing = (viewHeight - (dividerWidth * dividers)) / (dividers + 1);
            for (int i = 1; i <= dividers; i++){
                float y = rectView.top + spacing * i + (dividerWidth * (i - 1));
                canvas.save();
                canvas.clipRect(rectView.left, y,
                        rectView.right, y + dividerWidth);
                canvas.drawPath(p, dividerPaint);
                canvas.restore();
            }
        }

    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.DividedRoundSideProgressBar,
                R.attr.dividedRoundSideProgressBarStyle,
                R.style.DividedRoundSideProgressBar);
        int dividerColor;
        try {


            dividers = typedArray.getInteger(R.styleable.DividedRoundSideProgressBar_dividers,
                    context.getResources().getInteger(R.integer.defDividers));

            dividerWidth = typedArray.getDimension(R.styleable.DividedRoundSideProgressBar_dividerWidth,
                    context.getResources().getDimension(R.dimen.defDividerWidth));

            dividerColor = typedArray.getColor(R.styleable.DividedRoundSideProgressBar_dividerColor,
                    ContextCompat.getColor(context, DEFAULT_DIVIDER_COLOR_ID));
        } finally {
            typedArray.recycle();
        }

        dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dividerPaint.setColor(dividerColor);

    }
}
