package com.bran.smarttuner;

import android.widget.TextView;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.util.PitchConverter;

/** TODO: lifecycle management
 * Handles microphone sampling, pitch detection (using YIN algorithm), and filtering then publishes currentPitch.
 */
public class PitchDetector {

    // Filter configuration and fields
    private static final int BUFFER_SIZE = 8;
    private static final int MAX_CONSEC_NULL = 5;
    private int consecNull;
    private ArrayBlockingQueue<Float> buffer;
    private float bufferSum;

    // Main component field
    private Pitch currentPitch; // TODO: run through high pass filter to eliminate noise (skip momentary note deviations and average several cent measurements?)

    public PitchDetector() {
        buffer = new ArrayBlockingQueue<Float>(BUFFER_SIZE);
        for(int i=0; i<BUFFER_SIZE; i++) buffer.add(0f);
        bufferSum = 0;



        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                float frequency = result.getPitch();
                if(frequency==-1) { // no pitch heard
                    consecNull++;
                    if(consecNull==MAX_CONSEC_NULL) { // true silence
                        currentPitch = null;
                        for(int i=0; i<buffer.size(); i++) buffer.remove(); // flush buffer
                    }
                }
                else { // pitch heard
                    consecNull = 0;
                    // calc filtered frequency
                    int bufferSize = buffer.size();
                    if(bufferSize==BUFFER_SIZE) {
                        bufferSum -= buffer.remove();
                        bufferSize --;
                    }
                    bufferSum += frequency;
                    buffer.add(frequency);
                    bufferSize++;
                    float filteredFrequency = bufferSum/bufferSize;
                    currentPitch = Pitch.fromFrequency(filteredFrequency);
                }
            }
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh); // TODO: make more robust
        dispatcher.addAudioProcessor(p);
        new Thread(dispatcher,"Audio Dispatcher").start();
    }

    public Pitch getCurrentPitch() { return currentPitch; }
}
