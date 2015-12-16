package com.bran.intune;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * Created by benjaminran on 12/16/15.
 */
public class Graph extends GraphView {

    private MainActivity mainActivity;
    private PitchDetector pitchDetector;
    protected Graph graph;
    private LineGraphSeries<DataPoint> rawFrequencies, filteredFrequencies;
    private int x;

    public Graph(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainActivity = (context instanceof Activity) ? (MainActivity) context : null;
    }

    public void updateGraph() {
        filteredFrequencies.appendData(new DataPoint(x, pitchDetector.getFilteredFrequency()), true, 1000);
        rawFrequencies.appendData(new DataPoint(x, pitchDetector.getRawFrequency()), true, 1000);
        x++;
    }

    public void prepareGraph() {
//        graph = mainActivity.graph;
        pitchDetector = mainActivity.getPitchDetector();

        x = 0;
        filteredFrequencies = new LineGraphSeries<DataPoint>();
        filteredFrequencies.setColor(Color.BLACK);
        filteredFrequencies.setTitle("Filtered Frequencies");
        rawFrequencies = new LineGraphSeries<DataPoint>();
        rawFrequencies.setColor(Color.LTGRAY);
        rawFrequencies.setTitle("Raw Frequencies");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        graph.addSeries(rawFrequencies);
        graph.addSeries(filteredFrequencies);
    }
}
