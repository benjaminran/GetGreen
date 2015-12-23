package com.bran.intune;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jjoe64.graphview.series.DataPoint;
import com.kobakei.ratethisapp.RateThisApp;
import com.melnykov.fab.FloatingActionButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {
    // UI Update Timing constants
    private static final int UPDATE_FREQUENCY = 20;
    private static final int UPDATE_PERIOD = 1000 / UPDATE_FREQUENCY;
    // Main application modules
    private PitchDetector pitchDetector;
    private Interpreter interpreter;
    // UI
    @Bind(R.id.tuner_view) protected TunerView tunerView;
    @Bind(R.id.analysis) protected AnalysisView analysisView;
    @Bind(R.id.analysis_text) protected TextView analysisText;
    @Bind(R.id.debug_status) protected TextView debugStatus;
    @Bind(R.id.graph_button) protected ToggleButton graphButton;
    @Bind(R.id.graph) protected Graph graph;
//    @Bind(R.id.loudness_view) protected LoudnessView loudnessView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUi();
        requestRatingIfNeeded();
        pitchDetector = new PitchDetector();
        interpreter = new Interpreter(this);
        interpreter.start();
        startUiUpdateLoop();
    }

    private void startUiUpdateLoop() {
        graph.prepareGraph();
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



    private void updateUi() {
        if(!graphButton.isChecked()) return;
        analysisText.setText(interpreter.getAnalysis().toString());
        analysisView.updateAnalysis(interpreter.getAnalysis());
        Pitch pitch = pitchDetector.getCurrentPitch();
        if(pitch!=null) {
            debugStatus.setText(Html.fromHtml("<i>" + pitch.toString() + "</i>"));
            tunerView.displayPitch(pitch);
        }
        else debugStatus.setText(Html.fromHtml("<i>" + "No pitch detected" + "</i>"));
        // Update graph
        graph.updateGraph();
//        loudnessView.setLoudness(-1);
    }

    private void initUi() {
//        graph = (GraphView) findViewById(R.id.graph);
//        analysisText = (TextView) findViewById(R.id.analysis_text);
        analysisText.setMovementMethod(new ScrollingMovementMethod());
        analysisText.scrollTo(0, 3500);
//        analysisView = (AnalysisView) findViewById(R.id.analysis);
//        debugStatus = (TextView) findViewById(R.id.debug_status);
//        tunerView = (TunerView) findViewById(R.id.tuner_view);
//        graphButton = (Button) findViewById(R.id.graph_button);
        ListView listView = (ListView) findViewById(android.R.id.list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.attachToListView(listView);
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

    private void requestRatingIfNeeded() {
        RateThisApp.onStart(this);  // Request rating when appropriate
        RateThisApp.showRateDialogIfNeeded(this);
    }
    
}
