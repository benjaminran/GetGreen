package com.bran.smarttuner;

import android.widget.TextView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.util.PitchConverter;

/**
 * Handles microphone sampling and pitch detection (using YIN algorithm) then publishes currentPitch.
 */
public class PitchDetector {
    private Pitch currentPitch; // TODO: run through high pass filter to eliminate noise (skip momentary note deviations and average several cent measurements?)

    public PitchDetector() {
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                currentPitch = Pitch.fromFrequency(result.getPitch());
            }
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(p);
        new Thread(dispatcher,"Audio Dispatcher").start();
    }

    public Pitch getCurrentPitch() { return currentPitch; }

    /**
     * Represents information about the pitch heard by the Tuner
     *    note: midi note number
     *    centsSharp: number of cents deviation above perfect note pitch
     */
    public static class Pitch {
        private int note;
        private int centsSharp;

        private Pitch(int note, int centsSharp) {
            this.note = note;
            this.centsSharp = centsSharp;
        }

        public static Pitch fromFrequency(float frequency) {
            if(frequency==-1) return null; // no pitch; noise
            int note = PitchConverter.hertzToMidiKey((double) frequency);
            int centsSharp = ((int) PitchConverter.hertzToAbsoluteCent(frequency)) - 100 * note;
            return new Pitch(note, centsSharp);
        }

        public int getNote() { return note; }
        public int getCentsSharp() { return centsSharp; }

        public String toString() { return String.format("Note: %d; Cents Sharp: %d%n", note, centsSharp); }
    }
}
