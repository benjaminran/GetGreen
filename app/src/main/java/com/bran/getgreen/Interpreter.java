package com.bran.getgreen;

import android.os.Handler;
import android.os.Looper;


/**
 * This class performs the analysis of pitches returned by PitchDetector. It
 * establishes a thread on  which to regularly poll the PitchDetector for the current pitch and
 * update its analysis accordingly.
 */
public class Interpreter {
    // Timing constants
    private static final int POLL_FREQUENCY = 20;
    private static final int POLL_PERIOD = 1000 / POLL_FREQUENCY;
    // Main application modules
    private MainActivity mainActivity;
    private PitchDetector pitchDetector;
    // Analysis
    private Analysis analysis;


    public Interpreter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        pitchDetector = mainActivity.getPitchDetector();
        analysis = new Analysis();
    }

    /**
     *  Start interpreter loop
     */
    public void start() {
        new Thread(new Runnable() {
            private Handler handler;
            @Override
            public void run() {
                Looper.prepare();
                handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mainActivity.recordButton.isChecked())
                            analysis.addPitch(pitchDetector.getCurrentPitch());
                        handler.postDelayed(this, POLL_PERIOD);
                    }
                });
                Looper.loop();
            }
        }, "Interpreter Thread").start();
    }
    public Analysis getAnalysis() {
        return analysis;
    }


}
