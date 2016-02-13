package com.bran.getgreen;

import java.util.concurrent.ArrayBlockingQueue;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;

/**
 * Receives raw pitch detection results, applies smoothing filter, and publishes
 */
public class PitchFilter implements PitchDetectionHandler {
    // Current readings
    private float filteredFrequency;
    private float rawFrequency;
    private Pitch filteredPitch;
    // Filter configuration and fields
    private static final int BUFFER_SIZE = 16;
    private ArrayBlockingQueue<Float> buffer;
    private float bufferSum;
    private static final int MAX_CONSEC_NULL = 16;
    private int consecNull;

    public PitchFilter() {
        buffer = new ArrayBlockingQueue<Float>(BUFFER_SIZE);
        for(int i=0; i<BUFFER_SIZE; i++) buffer.add(0f);
        bufferSum = 0f;
    }

    @Override
    public void handlePitch(PitchDetectionResult result, AudioEvent audioEvent) {
        rawFrequency = result.getPitch();
        if(rawFrequency==-1) { // no pitch heard
            consecNull++;
            if(consecNull==MAX_CONSEC_NULL) { // true silence
                filteredPitch = null;
                // clear buffer
                while(buffer.size()!=0) {
                    buffer.remove();
                }
                bufferSum = 0f;
                filteredFrequency = -1;
            }
        }
        else { // pitch heard
            consecNull = 0;
            // calculate filtered frequency
            if(buffer.size()==BUFFER_SIZE) {
                bufferSum -= buffer.remove();
            }
            buffer.add(rawFrequency);
            bufferSum += rawFrequency;
            filteredFrequency = bufferSum/buffer.size();
            filteredPitch = Pitch.fromFrequency(filteredFrequency);
        }
    }

    public Pitch getFilteredPitch() { return filteredPitch; }

    public float getFilteredFrequency() {
        return filteredFrequency;
    }

    public float getRawFrequency() {
        return rawFrequency;
    }
}

