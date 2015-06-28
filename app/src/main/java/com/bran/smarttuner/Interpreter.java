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
    private static final int POLL_PERIOD = 1000 / POLL_FREQUENCY;
    // Main application modules
    private PitchDetector pitchDetector;
    // Data set
    private static final int MIN_MIDI_NUMBER = 21; // Lowest note: A0
    private static final int MAX_MIDI_NUMBER = 108; // Highest note: C8
    private ArrayList<Pitch>[] pitches = new ArrayList[MAX_MIDI_NUMBER+1-MIN_MIDI_NUMBER];


    public Interpreter(PitchDetector pitchDetector) {
        this.pitchDetector = pitchDetector;
        for(int i=0; i<pitches.length; i++) pitches[i] = new ArrayList<Pitch>();
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
                        Pitch currentPitch = pitchDetector.getCurrentPitch();
                        if(currentPitch!=null) {
                            int note = currentPitch.getNote();
                            if(note>=MIN_MIDI_NUMBER && note<=MAX_MIDI_NUMBER) { // TODO: better data collection scheme
                                pitches[note-MIN_MIDI_NUMBER].add(currentPitch); // do not update data set if not pitch is detected
                            }
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
        Analysis analysis = new Analysis();
        for(int n=MIN_MIDI_NUMBER; n<=MAX_MIDI_NUMBER; n++) {
            ArrayList<Pitch> data = pitches[n-MIN_MIDI_NUMBER];
            String noteName = Pitch.getNoteName(n);
            int octaveNumber = Pitch.getOctaveNumber(n);
            int size = data.size();
            analysis.text += String.format("%s%d: %d%n", noteName, octaveNumber, size);
        }
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
