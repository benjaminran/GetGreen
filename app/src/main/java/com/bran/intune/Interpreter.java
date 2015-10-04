package com.bran.intune;

import android.os.Handler;
import android.os.Looper;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
    private PitchDetector pitchDetector;
    // Analysis
    private Analysis analysis;


    public Interpreter(PitchDetector pitchDetector) {
        this.pitchDetector = pitchDetector;
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
                        analysis.addPitch(pitchDetector.getCurrentPitch());
                        handler.postDelayed(this, POLL_PERIOD);
                    }
                });
                Looper.loop();
            }
        }, "Interpreter Thread").start();
    }

    public Analysis getAnalysis() { // TODO: find average and variation in centsSharp of all same-note measurements
        return analysis;
    }

    public class Analysis {
        // Data set options
        private static final int MIN_MIDI_NUMBER = 21; // Lowest note: A0
        private static final int MAX_MIDI_NUMBER = 108; // Highest note: C8
        // Fields
        DescriptiveStatistics[] history;
        TreeMap<Pitch,?>[] history2;  // A record of all the pitches processed, organized by note number
//        int[] q1, q2, q3;  // The 1st, 2nd, and 3rd quartiles of the centsSharp measurements for individual sets from history separated by note number

        public Analysis() {
            history = new DescriptiveStatistics[MAX_MIDI_NUMBER+1-MIN_MIDI_NUMBER];
            for(int i=0; i<history.length; i++) history[i] = new DescriptiveStatistics();
//            history = new TreeMap[MAX_MIDI_NUMBER+1-MIN_MIDI_NUMBER];
//            for(int i=0; i<history.length; i++) history[i] = new TreeMap<>();
            /*q1 = new int[MAX_MIDI_NUMBER+1-MIN_MIDI_NUMBER];
            q2 = new int[MAX_MIDI_NUMBER+1-MIN_MIDI_NUMBER];
            q3 = new int[MAX_MIDI_NUMBER+1-MIN_MIDI_NUMBER];*/
        }

        public void addPitch(Pitch newPitch) {
            if(newPitch==null) return;  // do not update data set if pitch is not detected
            int note = newPitch.getNote();
            if(note>=MIN_MIDI_NUMBER && note<=MAX_MIDI_NUMBER) {
                // Add pitch to history
                history[note-MIN_MIDI_NUMBER].addValue(newPitch.getFrequency());
                // Update quartiles
//                updateQuartiles(newPitch);
            }
        }

        /**
         * Update the quartiles of the data for the specified note
         * @param newPitch The note whose corresponding data set should be updated
         */
        /*private void updateQuartiles(Pitch newPitch) {
            int i = newPitch.getNote()-MIN_MIDI_NUMBER; // The index corresponding to this note number in the analysis arrays
            Pitch[] data = (Pitch[]) history[i].keySet().toArray();  // This is O(n)... TODO
            int n = data.length;
            int m; // The number of elements in the median (either 1 or 2)
            // Find Q2
            if(n%2==0) {
                m = 2;
                q2[i] = (data[n/2-1].getCentsSharp() + data[n/2].getCentsSharp()) / 2;
            }
            else {
                m = 1;
                q2[i] = data[n/2].getCentsSharp();
            }
            // Find Q1 and Q3
            if(((n-m)/2)%2==0) {
                q1[i] = 0;
            }
            else {
                q1[i] = data[((n-m)/2)/2].getCentsSharp();
                q3[i] = data[(n-m)/2+m+((n-m)/2)/2].getCentsSharp();
            }
        }*/

        @Override
        public String toString() {
            String info = "";
            for(int n=MIN_MIDI_NUMBER; n<=MAX_MIDI_NUMBER; n++) {
                DescriptiveStatistics data = history[n-MIN_MIDI_NUMBER];
                String noteName = Pitch.getNoteName(n);
                int octaveNumber = Pitch.getOctaveNumber(n);
                long size = data.getN();
                double q1 = data.getPercentile(25);
                double q2 = data.getPercentile(50);
                double q3 = data.getPercentile(75);
                info += String.format("Note: %s%d; Count: %d; Q1: %.2f; Q2: %.2f; Q3: %.2f%n", noteName, octaveNumber, size, q1, q2, q3);
            }
            return info;
        }

        public DescriptiveStatistics[] getHistory() { return history; }
    }
}
