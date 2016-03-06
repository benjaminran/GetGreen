package com.bran.getgreen;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * View to display the current pitch as a physical tuner might
 */
public class TunerView extends TextView {
    // Configuration
    private static final int HOLD_AFTER_PITCH_FINISH = 3000; // continue displaying tuner reading until 3 seconds after pitch stops

    // Pitch info fields
    private String noteNameDisplayed;
    private int centsDisplayed;

    public TunerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        centsDisplayed = 0;
    }

    public void updatePitch(Pitch pitch) {
        if(pitch==null || pitch.getFrequency()<0) {
            setText(Html.fromHtml("<i>" + "No pitch detected" + "</i>"));
            return;
        }
        String newNoteName = pitch.getNoteName();
        int newCents = pitch.getCentsSharp();
        noteNameDisplayed = newNoteName;
        centsDisplayed = newCents;
        setText(Html.fromHtml(String.format("<b>%s</b> <i>%s%d</i>", noteNameDisplayed, (centsDisplayed<0) ? "-" : "+", Math.abs(centsDisplayed))));
    }
}
