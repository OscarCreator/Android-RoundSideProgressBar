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

    protected static final int DEFAULT_DIVIDER_QUANTITY = 2;
    protected static final float DEFAULT_DIVIDERS_WIDTH = 10;
    protected static final int DEFAULT_DIVIDER_COLOR_ID = R.color.defaultDividerColor;


    protected int dividers;

    protected int dividerColor;
    protected Paint dividerPaint;

    protected float dividerWidth;

    private List<Integer> dividerPos = new ArrayList<>();

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


        }


    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.DividedRoundSideProgressBar, 0, 0);
        try {


            dividers = typedArray.getInteger(R.styleable.DividedRoundSideProgressBar_dividers,
                    DEFAULT_DIVIDER_QUANTITY);

            dividerWidth = typedArray.getDimension(R.styleable.DividedRoundSideProgressBar_dividerWidth,
                    DEFAULT_DIVIDERS_WIDTH);

            dividerColor = typedArray.getColor(R.styleable.DividedRoundSideProgressBar_dividerColor,
                    ContextCompat.getColor(context, DEFAULT_DIVIDER_COLOR_ID));
        } finally {
            typedArray.recycle();
        }

        dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dividerPaint.setColor(dividerColor);



    }
}
