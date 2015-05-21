package com.bran.smarttuner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;

import com.github.lzyzsd.circleprogress.ArcProgress;

/**
 * Created by beni on 5/20/15.
 */
public class TunerView extends ArcProgress {
    private static final float ARC_ANGLE = 180;

    private String noteNameDisplayed;
    private int centsDisplayed;
    private Paint paint;

    public TunerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        centsDisplayed = 0;
        //configureSize(context); TODO: find solution
        initPaint();
        setArcAngle(ARC_ANGLE);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.rgb(72, 106, 176));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(getStrokeWidth());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void displayPitch(Pitch pitch) {
        // update note name
        if(pitch==null) return; // TODO: hold last reading for ~5 sec without new pitch
        String newNoteName = pitch.getNoteName();
        if(!newNoteName.equals(noteNameDisplayed)) {
            setBottomText(newNoteName);
            noteNameDisplayed = newNoteName;
        }
        // update cents
        int newCents = pitch.getCentsSharp();
        if(newCents!=centsDisplayed) {
            setProgress(newCents + 50);
            centsDisplayed = newCents;
        }
    }

    /*@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = 270 - getArcAngle() / 2f;
        float finishedSweepAngle = getProgress() / (float) getMax() * getArcAngle();
        float finishedStartAngle = startAngle;
        paint.setColor(getUnfinishedStrokeColor());
        canvas.drawArc(rectF, startAngle, getArcAngle(), false, paint);
        paint.setColor(getFinishedStrokeColor());
        canvas.drawArc(rectF, finishedStartAngle, finishedSweepAngle, false, paint);

        String text = String.valueOf(getProgress());
        if (!TextUtils.isEmpty(text)) {
            textPaint.setColor(getTextColor());
            textPaint.setTextSize(getTextSize());
            float textHeight = textPaint.descent() + textPaint.ascent();
            float textBaseline = (getHeight() - textHeight) / 2.0f;
            canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, textBaseline, textPaint);
            textPaint.setTextSize(getSuffixTextSize());
            float suffixHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(getSuffixText(), getWidth() / 2.0f  + textPaint.measureText(text) + getSuffixTextPadding(), textBaseline + textHeight - suffixHeight, textPaint);
        }

        if (!TextUtils.isEmpty(getBottomText())) {
            textPaint.setTextSize(getBottomTextSize());
            float bottomTextBaseline = getHeight() - arcBottomHeight - (textPaint.descent() + textPaint.ascent()) / 2;
            canvas.drawText(getBottomText(), (getWidth() - textPaint.measureText(getBottomText())) / 2.0f, bottomTextBaseline, textPaint);
        }
    }*/

    private void configureSize(Context context) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = calculateWidth(context);
        params.height = params.width; // height and width should match
        setLayoutParams(params);
    }

    private int calculateWidth(Context context) {
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;
        return (int) dpWidth;
    }
}
