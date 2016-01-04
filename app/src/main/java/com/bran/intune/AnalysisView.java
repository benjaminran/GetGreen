package com.bran.intune;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;
import java.util.TreeMap;

import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 *
 */
public class AnalysisView extends LinearLayout {

    @BindColor(R.color.clouds) protected int clouds;
    private Context context;
    private Analysis currentAnalysis;

    public AnalysisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setOrientation(HORIZONTAL);
        ButterKnife.bind(this);
        updateAnalysis(new Analysis());  // display empty analysis at start
    }

    public void updateAnalysis(Analysis analysis) {
        currentAnalysis = analysis;
        removeAllViews();
        for(int i=Pitch.BOTTOM_NOTE_MIDI; i<=Pitch.TOP_NOTE_MIDI; i++) {  // repopulate with children
            if(analysis.get(i)!=null)
                addView(new PitchAnalysisView(i, analysis.get(i)));
        }
    }

    private class PitchAnalysisView extends LinearLayout {
        private int note;
        private NoteStatistics info;

        public PitchAnalysisView(int note, NoteStatistics info) {
            super(context);
            this.note = note;
            this.info = info;
            init();
        }

        private void init() {
            setOrientation(VERTICAL);
            setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            setPadding(15, 15, 15, 15);
            //this.setMinimumWidth(300);
            //this.setBackgroundColor(Color.argb(0, 0, 0, 128));

            TextView noteView = new TextView(context);
            TextView centsSharp = new TextView(context);
            TextView spreadView = new TextView(context);

            noteView.setTextColor(clouds);
            centsSharp.setTextColor(clouds);
            spreadView.setTextColor(clouds);

            if(info == null || Double.isNaN(info.getPercentile(50))) {  // no data
                noteView.setText("");
                centsSharp.setText("");
                spreadView.setText("");
            }
            else {
                double medianFrequency = info.getPercentile(50);
                Pitch medianPitch = Pitch.fromFrequency(medianFrequency);
                Util.debugCheck(medianPitch.getNote() == note, new RuntimeException("Inconsistent data in PitchAnalysisView"));
                noteView.setText(medianPitch.getNoteName());
                centsSharp.setText("" + medianPitch.getCentsSharp());
                int spread = info.getCentsIQR();
                spreadView.setText("" + spread + "%");
            }

            addView(noteView);
            addView(centsSharp);
            addView(spreadView);

            TextView count = new TextView(context);
            count.setText(""+info.getN());
            addView(count);
        }
    }
}
