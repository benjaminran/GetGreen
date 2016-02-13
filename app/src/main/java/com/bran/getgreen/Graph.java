package com.bran.getgreen;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


/**
 * Created by benjaminran on 12/16/15.
 */
public class Graph extends GraphView {

    private static final int VIEWPORT_SIZE = 10;

    private MainActivity mainActivity;
    private PitchDetector pitchDetector;
    protected Graph graph;
    private LineGraphSeries<DataPoint> referenceFrequencies, filteredFrequencies;
    private int x;

    public Graph(Context context, AttributeSet attrs) {
        super(context, attrs);
        mainActivity = (context instanceof Activity) ? (MainActivity) context : null;
    }

    public void updateGraph() {
        double frequency = pitchDetector.getFilteredFrequency();
        double referenceFrequency = 0;
        if(frequency==-1) frequency = 0;
        else referenceFrequency = Pitch.fromNoteMidi(Pitch.fromFrequency(frequency).getNote()).getFrequency();
        filteredFrequencies.appendData(new DataPoint(x, frequency), true, 1000);
        referenceFrequencies.appendData(new DataPoint(x, referenceFrequency), true, 1000);
        x++;
    }

    public void prepareGraph() {
        graph = mainActivity.graph;
        pitchDetector = mainActivity.getPitchDetector();
        x = 0;
        filteredFrequencies = new LineGraphSeries<DataPoint>();
        filteredFrequencies.setColor(mainActivity.wetAsphalt);
        filteredFrequencies.setTitle("Pitch Played");
        referenceFrequencies = new LineGraphSeries<DataPoint>();
        referenceFrequencies.setColor(mainActivity.emerald);
        referenceFrequencies.setTitle("True Pitch");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(VIEWPORT_SIZE);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.addSeries(referenceFrequencies);
        graph.addSeries(filteredFrequencies);
    }
}
