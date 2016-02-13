package com.bran.getgreen;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * Created by benjaminran on 12/30/15.
 */
public class AnalysisLabelView extends LinearLayout {

    private Context context;
    @BindColor(R.color.clouds) protected int clouds;

    public AnalysisLabelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        ButterKnife.bind(this);
        initLabels();
    }

    private void initLabels() {
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        setPadding(15, 15, 15, 15);
        setOrientation(VERTICAL);
        TextView noteLabel = new TextView(context);
        noteLabel.setText("Note");
        noteLabel.setTypeface(null, Typeface.BOLD);
        noteLabel.setTextColor(clouds);
        TextView centsSharpLabel = new TextView(context);
        centsSharpLabel.setText("Cents Sharp");
        centsSharpLabel.setTypeface(null, Typeface.BOLD);
        centsSharpLabel.setTextColor(clouds);
        TextView spreadLabel = new TextView(context);
        spreadLabel.setText("Spread");
        spreadLabel.setTypeface(null, Typeface.BOLD);
        spreadLabel.setTextColor(clouds);
        addView(noteLabel);
        addView(centsSharpLabel);
        addView(spreadLabel);
    }
}
