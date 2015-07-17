package com.bran.intune;

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

    public void displayPitch(Pitch pitch) {
        if(pitch==null) return; // TODO: hold last reading for ~5 sec without new pitch
        Boolean changed = false;
        String newNoteName = pitch.getNoteName()+pitch.getOctaveNumber();
        int newCents = pitch.getCentsSharp();
        if(!newNoteName.equals(noteNameDisplayed) || newCents!=centsDisplayed) changed = true;
        if(changed==true) {
            noteNameDisplayed = newNoteName;
            centsDisplayed = newCents;
            setText(Html.fromHtml(String.format("<b>%s</b> <i>%s%d</i>", noteNameDisplayed, (centsDisplayed<0) ? "-" : "+", Math.abs(centsDisplayed))));
        }
    }
}
