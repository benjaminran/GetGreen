package com.bran.intune;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kobakei.ratethisapp.RateThisApp;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;


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
    @Bind(R.id.record_button) protected RecordButton recordButton;
    @Bind(R.id.analysis_detail_view) protected AnalysisDetailView analysisDetailView;
    @Bind(R.id.graph) protected Graph graph;

    @BindColor(R.color.clouds) int clouds;
    @BindColor(R.color.emerald) int emerald;
    @BindColor(R.color.wet_asphalt) int wetAsphalt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getActionBar().show();
        ButterKnife.bind(this);
        colorActionBarTitle();
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
        Pitch pitch = pitchDetector.getCurrentPitch();
        tunerView.updatePitch(pitch);
        if(!recordButton.isChecked()) return;
        analysisView.updateAnalysis(interpreter.getAnalysis());
        // Update graph
        graph.updateGraph();
    }

    private void initUi() {
        recordButton.registerOffCallback(analysisDetailView.updateAnalysis);
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

    private void colorActionBarTitle() {
        SpannableString s = new SpannableString("GetGreen");
        s.setSpan(new ForegroundColorSpan(clouds), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(emerald), 3, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);
    }
    
}
