package com.bran.intune;

import java.util.concurrent.ArrayBlockingQueue;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

/** TODO: lifecycle management
 * Handles microphone sampling, pitch detection (using YIN algorithm), and filtering then publishes currentPitch.
 */
public class PitchDetector {

    // Main component field
    private Pitch currentPitch; // TODO: run through high pass filter to eliminate noise (skip momentary note deviations and average several cent measurements?)
    // Filter for raw frequencies detected
    PitchFilter filter;

    public PitchDetector() {
        filter = new PitchFilter();
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, filter); // TODO: make more robust
        dispatcher.addAudioProcessor(p);
        new Thread(dispatcher,"Audio Dispatcher").start();
    }

    public Pitch getCurrentPitch() {
        return currentPitch;
    }

    public float getFilteredFrequency() {
        return filter.getFilteredFrequency();
    }

    public float getRawFrequency() {
        return filter.getRawFrequency();
    }


}
