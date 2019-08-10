package com.oscarcreator.roundsideprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class DividedRoundSideProgressBar extends RoundSideProgressBar {

    public DividedRoundSideProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DividedRoundSideProgressBar(Context context) {
        super(context);
    }

    public DividedRoundSideProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.RoundSideProgressBar, 0, 0);
    }
}
