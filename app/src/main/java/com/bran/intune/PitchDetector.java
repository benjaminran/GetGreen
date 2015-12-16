package com.bran.intune;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchProcessor;

/** TODO: lifecycle management
 * Handles microphone sampling, pitch detection (using YIN algorithm), and filtering then publishes currentPitch.
 */
public class PitchDetector {

    // Main component field
    private Pitch currentPitch; // TODO: run through high pass filter to eliminate noise (skip momentary note deviations and average several cent measurements?)
    // Filter for raw frequencies detected
    private PitchFilter filter;

    public PitchDetector() {
        filter = new PitchFilter();
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, filter); // TODO: make more robust
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
