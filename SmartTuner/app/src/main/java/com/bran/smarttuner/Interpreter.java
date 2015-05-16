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
    private PitchDetector pitchDetector;
    // Data set
    private ArrayList<PitchDetector.Pitch>[] pitches = new ArrayList[12];

    public Interpreter(PitchDetector pitchDetector) {
        this.pitchDetector = pitchDetector;
        for(int i=0; i<pitches.length; i++) pitches[i] = new ArrayList<PitchDetector.Pitch>();
    }

    /* start interpreter loop */
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
                        PitchDetector.Pitch currentPitch = pitchDetector.getCurrentPitch();
                        if(currentPitch!=null) {
                            pitches[currentPitch.getNote()%12].add(currentPitch); // do not update data set if not pitch is detected
                            // TODO: update analysis in loop then poll from main thread
                        }
                        handler.postDelayed(this, POLL_PERIOD);
                    }
                });
                Looper.loop();
            }
        }, "Interpreter Thread").start();
    }

    public Analysis getAnalysis() { // TODO: find average and variation in centsSharp of all same-note measurements
        // analyze data and return something
        Analysis analysis = new Analysis();
        analysis.text = String.format("C[0]: %d%nC#[1]: %d%nD[2]: %d%nEb[3]: %d%nE[4]: %d%nF[5]: %d%nF#[6]: %d%nG[7]: %d%nG#[8]: %d%nA[9]: %d%nBb[10]: %d%nB[11]: %d%n",
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
