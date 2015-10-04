package com.bran.intune;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.List;


/**
 * Created by beni on 10/4/15.
 */
public class AnalysisView extends LinearLayout {
    private Context context;
    private Interpreter.Analysis currentAnalysis;

    public AnalysisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setOrientation(HORIZONTAL);
    }

    public void updateAnalysis(Interpreter.Analysis analysis) {
        currentAnalysis = analysis;
        DescriptiveStatistics[] history = currentAnalysis.getHistory();

        if(getChildCount() > 0) removeAllViews();  // remove any children

        for(int i=0; i<history.length; i++) {  // repopulate with children
            addView(new PitchAnalysisView(history[i]));
        }
    }

    private class PitchAnalysisView extends View {
        private DescriptiveStatistics info;

        public PitchAnalysisView(DescriptiveStatistics info) {
            super(context);
            this.info = info;
            init();
        }

        private void init() {
            this.setLayoutParams(new LayoutParams(100, LayoutParams.MATCH_PARENT, 1f));
            this.setMinimumWidth(300);

            this.setBackgroundColor(Color.argb(0, 0, 0, 128));
        }
    }
}
