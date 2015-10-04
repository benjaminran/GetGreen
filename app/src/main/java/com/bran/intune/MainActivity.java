package com.bran.intune;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends Activity {
    // UI Update Timing constants
    private static final int UPDATE_FREQUENCY = 20;
    private static final int UPDATE_PERIOD = 1000 / UPDATE_FREQUENCY;
    // Main application modules
    private PitchDetector pitchDetector;
    private Interpreter interpreter;
    // UI
    private TunerView tunerView;
    private AnalysisView analysisView;
    private TextView analysisText;
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
        analysisText.setText(interpreter.getAnalysis().toString());
        analysisView.updateAnalysis(interpreter.getAnalysis());
        Pitch pitch = pitchDetector.getCurrentPitch();
        if(pitch!=null) {
            debugStatus.setText(Html.fromHtml("<i>" + pitch.toString() + "</i>"));
            tunerView.displayPitch(pitch);
        }
        else debugStatus.setText(Html.fromHtml("<i>" + "No pitch detected" + "</i>"));
        // Update graph
        updateGraph();
    }

    private void updateGraph() {
        filteredFrequencies.appendData(new DataPoint(x, pitchDetector.getFilteredFrequency()), true, 1000);
        rawFrequencies.appendData(new DataPoint(x, pitchDetector.getRawFrequency()), true, 1000);
        x++;
    }

    private void initUi() {
        graph = (GraphView) findViewById(R.id.graph);
        analysisText = (TextView) findViewById(R.id.analysis_text);
        analysisText.setMovementMethod(new ScrollingMovementMethod());
        analysisText.scrollTo(0, 3500);
        analysisView = (AnalysisView) findViewById(R.id.analysis);
        debugStatus = (TextView) findViewById(R.id.debug_status);
        tunerView = (TunerView) findViewById(R.id.tuner_view);
        graphButton = (Button) findViewById(R.id.graph_button);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_about:
                String message = String.format("Application ID: %s%nVersion Code: " +
                        "%s%nVersion Name: %s%nBuild Type: %s", BuildConfig.APPLICATION_ID,
                        BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME, BuildConfig.BUILD_TYPE);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public PitchDetector getPitchDetector() { return pitchDetector; }

    public Interpreter getInterpreter() { return interpreter; }
    
}
