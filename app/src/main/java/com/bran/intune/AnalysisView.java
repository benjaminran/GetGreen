package com.bran.intune;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;
import java.util.TreeMap;

/**
 *
 */
public class AnalysisView extends LinearLayout {
    private Context context;
    private Analysis currentAnalysis;

    public AnalysisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setOrientation(HORIZONTAL);
    }

    public void updateAnalysis(Analysis analysis) {
        currentAnalysis = analysis;
        TreeMap<Integer, NoteStatistics> history = currentAnalysis.getHistory();

        if(getChildCount() > 0) removeAllViews();  // remove any children

        for(int i=Pitch.BOTTOM_NOTE_MIDI; i<=Pitch.TOP_NOTE_MIDI; i++) {  // repopulate with children
            addView(new PitchAnalysisView(i, history.get(i)));
        }
    }

    private class PitchAnalysisView extends TextView {
        private int note;
        private DescriptiveStatistics info;

        public PitchAnalysisView(int note, NoteStatistics info) {
            super(context);
            this.note = note;
            this.info = info;
            init();
        }

        private void init() {
            this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            this.setPadding(15,15,15,15);
            //this.setMinimumWidth(300);
            //this.setBackgroundColor(Color.argb(0, 0, 0, 128));

            if(info == null || Double.isNaN(info.getPercentile(50))) {  // no data
                setText(String.format("%n-%n"));
            }
            else {
                double medianFrequency = info.getPercentile(50);
                Pitch medianPitch = Pitch.fromFrequency(medianFrequency);
                assert (medianPitch.getNote() == note);
                boolean isSharp = medianPitch.getCentsSharp() > 0;
                setText(String.format("%s%n%s%n%s",
                        isSharp ? medianPitch.getCentsSharp() : "",
                        medianPitch.getNoteName()+medianPitch.getOctaveNumber(),
                        isSharp ? "" : medianPitch.getCentsSharp()
                ));
                // TODO: prioritize pitches displayed by sorting by amount played (dataset size) and how out of tune they are
            }
        }
    }
}
