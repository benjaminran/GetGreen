package com.bran.intune;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchProcessor;

/**
 * Handles microphone sampling, pitch detection (using YIN algorithm), and filtering then publishes currentPitch.
 */
public class PitchDetector {

    // Main component field
    private Pitch currentPitch;
    // Filter for raw frequencies detected
    private PitchFilter filter;

    public PitchDetector() {
        filter = new PitchFilter();
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, filter);
        dispatcher.addAudioProcessor(p);
        new Thread(dispatcher,"Audio Dispatcher").start();
    }

    public Pitch getCurrentPitch() {
        return filter.getFilteredPitch();
    }

    public float getFilteredFrequency() {
        return filter.getFilteredFrequency();
    }

    public float getRawFrequency() {
        return filter.getRawFrequency();
    }


}
