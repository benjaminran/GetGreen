package com.bran.smarttuner;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

/**
 * Created by beni on 4/16/15.
 */
public class Interpreter {
    // Timing constants
    private static final int POLL_FREQUENCY = 20;
    private static final int MS_PER_SEC = 1000;
    private static final int POLL_PERIOD = MS_PER_SEC / POLL_FREQUENCY;
    // Main application modules
    private Tuner tuner;
    // Data set
    private ArrayList<Tuner.Pitch>[] pitches = new ArrayList[12];

    public Interpreter(final Tuner tuner) {
        this.tuner = tuner;
        for(int i=0; i<pitches.length; i++) pitches[i] = new ArrayList<Tuner.Pitch>();
        // Start interpreter loop
        start();
    }

    private void start() {
        new Thread(new Runnable() {
            private Handler handler;
            @Override
            public void run() {
                Looper.prepare();
                handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Tuner.Pitch currentPitch = tuner.getCurrentPitch();
                        pitches[currentPitch.getNote()%12].add(currentPitch);
                        handler.postDelayed(this, POLL_PERIOD);
                    }
                });
                Looper.loop();
            }
        }, "Interpreter Thread").start();
    }

    public Analysis getAnalysis() {
        // analyze data and return something
        Analysis analysis = new Analysis();
        analysis.text = String.format("[0]: %d%n[1]: %d%n[2]: %d%n[3]: %d%n[4]: %d%n[5]: %d%n[6]: %d%n[7]: %d%n[8]: %d%n[9]: %d%n[10]: %d%n[11]: %d%n",
                pitches[0].size(),
                pitches[1].size(),
                pitches[2].size(),
                pitches[3].size(),
                pitches[4].size(),
                pitches[5].size(),
                pitches[6].size(),
                pitches[7].size(),
                pitches[8].size(),
                pitches[9].size(),
                pitches[10].size(),
                pitches[11].size());
        return analysis;
    }

    public static class Analysis {
        String text;

        @Override
        public String toString() {
            return text;
        }
    }
}
