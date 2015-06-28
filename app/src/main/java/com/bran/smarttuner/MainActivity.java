package com.bran.smarttuner;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends Activity {
    // UI Update Timing constants
    private static final int UPDATE_FREQUENCY = 20;
    private static final int UPDATE_PERIOD = 1000 / UPDATE_FREQUENCY;
    // Main application modules
    private PitchDetector pitchDetector;
    private Tuner tuner;
    private Interpreter interpreter;
    // UI
    private TunerView tunerView;
    private TextView analysis;
    private TextView debugStatus;
    private Button graphButton;
    private GraphView graph;
    // Graph-specific fields
    private LineGraphSeries<DataPoint> rawFrequencies, filteredFrequencies;
    private int x;
    private Boolean paused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
        pitchDetector = new PitchDetector();
        interpreter = new Interpreter(pitchDetector);
        interpreter.start();
        startUiUpdateLoop();
    }

    private void startUiUpdateLoop() {
        prepareGraph();
        // Start loop
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateUi();
                handler.postDelayed(this, UPDATE_PERIOD);
            }
        });
    }

    private void prepareGraph() {
        x = 0;
        paused = false;
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

    private void updateUi() { // TODO: graph analysis (~bar graph)
        if(paused) return;
        analysis.setText(interpreter.getAnalysis().toString());
        Pitch pitch = pitchDetector.getCurrentPitch();
        if(pitch!=null) {
            debugStatus.setText(""+pitch.toString());
            tunerView.displayPitch(pitch);
        }
        else debugStatus.setText("No pitch detected");
        // Update graph
        updateGraph();
    }

    private void updateGraph() {
        filteredFrequencies.appendData(new DataPoint(x, pitchDetector.getFilteredFrequency()), true, 1000);
        rawFrequencies.appendData(new DataPoint(x, pitchDetector.getRawfrequency()), true, 1000);
        x++;
    }

    private void initUi() {
        graph = (GraphView) findViewById(R.id.graph);
        analysis = (TextView) findViewById(R.id.analysis);
        analysis.setMovementMethod(new ScrollingMovementMethod());
        debugStatus = (TextView) findViewById(R.id.debug_status);
        tunerView = (TunerView) findViewById(R.id.tuner_view);
        graphButton = (Button) findViewById(R.id.graph_btn);
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paused = !paused;
                graphButton.setText(paused ? "Resume" : "Pause");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
