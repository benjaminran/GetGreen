package com.bran.getgreen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by benjaminran on 12/14/15.
 */
public class LoudnessView extends View {
    private ShapeDrawable mDrawable;

    private static final int CENTER_X = 500;
    private static final int CENTER_Y = 500;
    private static final int MIN_RADIUS = 200;
    private static final int MAX_RADIUS = 250;

    private double loudness;
    private int radius;

    double x = 0; // temp

    public LoudnessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrawable = new ShapeDrawable(new OvalShape());
        mDrawable.getPaint().setColor(0x80cccccc);
        setLoudness(0);
    }

    /**
     *
     *
     * @param loudness A double in the interval [0,1]
     */
    public void setLoudness(double loudness) {
//        this.loudness = loudness;
        this.loudness = (Math.sin(x) + 1) / 2;
        x += 0.05;
        loudness = this.loudness;
        radius = (int) Math.round(MIN_RADIUS + loudness * (MAX_RADIUS - MIN_RADIUS));
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        mDrawable.setBounds(CENTER_X-radius, CENTER_Y-radius, CENTER_X + radius, CENTER_Y + radius);
        //mDrawable.draw(canvas);
    }
}
