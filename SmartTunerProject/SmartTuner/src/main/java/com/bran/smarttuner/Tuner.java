package com.bran.smarttuner;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Controls the sampling and processing of microphone input and only exposes
 * the currentPitch to the rest of the application at any given moment
 */
public class Tuner {
    private Pitch currentPitch;
    private short[] buffer;
    // Tuner components
    private Recorder recorder;
    private ACEngine ac;

    public Tuner() {
        ac = new ACEngine();
        recorder = new Recorder();
    }

    public void start() {
        recorder.start();
        currentPitch = new Pitch(30, 10);
    }

    public Pitch getCurrentPitch() { return currentPitch; }
    public short[] getBuffer() { return buffer; }

    /**
     * Represents information about the pitch heard by the Tuner
     */
    public static class Pitch {
        private int note;
        private int centsSharp;

        public Pitch(int note, int centsSharp) {
            this.note = note;
            this.centsSharp = centsSharp;
        }

        public static Pitch fromFrequency(double frequency) {
            double n = 12 * Math.log(frequency/440)/Math.log(2) + 49;
            Log.i("ST", String.format("Pitch.fromFrequency mapped %fhz to n=%f", frequency, n));
            return new Pitch(30, 10); // TODO: calc pitch;
        }

        public int getNote() { return note; }
        public int getCentsSharp() { return centsSharp; }
    }

    /**
     * Controls sampling of the microphone into buffer and makes buffer
     * available for processing by ACEngine
     */
    private class Recorder implements AudioRecord.OnRecordPositionUpdateListener {
        private AudioRecord audioRecord;
        private int bufferSize;
        private int sampleRateInHz;

        private Recorder(){
        }

        private void start() {
            new Thread(new Runnable() {
                public Handler mHandler;
                @Override
                public void run() {
                    Looper.prepare();
                    mHandler = new Handler();
                    // TODO: lower and upper limit buffer size to allow sufficient length for PDA without losing faster notes
                    sampleRateInHz = 44100; // TODO: loop to find successful combination of settings
                    int minBufferSize = 8 * AudioRecord.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                    bufferSize = minBufferSize; // TODO: establish fixed min size
                    buffer = new short[bufferSize];
                    audioRecord = new AudioRecord(
                            MediaRecorder.AudioSource.MIC,
                            sampleRateInHz,
                            AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT,
                            2 * bufferSize // (in bytes) both buffers same length
                    );
                    if(audioRecord.getState()==AudioRecord.STATE_UNINITIALIZED) Log.e("ST", "AudioRecord initialization failed.");
                    audioRecord.setRecordPositionUpdateListener(recorder);
                    audioRecord.setPositionNotificationPeriod(bufferSize/8); // (in bytes) every quarter-buffer
                    audioRecord.startRecording();
                    Looper.loop();
                }
            }, "Recording Thread").start();
        }

        @Override
        public void onMarkerReached(AudioRecord audioRecord) {}

        @Override
        public void onPeriodicNotification(AudioRecord record) {
            int read = record.read(buffer, 0, bufferSize); // (in shorts) request half-buffer
            Log.d("ST", String.format("Recorder read %d shorts of requested %d.", read, bufferSize));
            ac.processBuffer(buffer);
        }
    }

    /** TODO: confirm/correct algorithm. Make static if possible?
     * Performs autocorrelation on the buffer of microphone data supplied
     * by the Recorder
     */
    private class ACEngine {
        // PDA Options
        private static final double PEAK_THRESHOLD_PERCENT = 0.75;
        private final int MAX_PERIOD_IN_SAMPLES = (int) Math.ceil(44100 / 27.50);
        private final int MIN_PERIOD_IN_SAMPLES = (int) Math.floor(44100 / 4186.01);
        private final int AUTOCORRELATION_WINDOW = 2*MAX_PERIOD_IN_SAMPLES;

        private ACEngine() {
        }

        private void processBuffer(short[] buffer) {
            //double[] ac1 = performAutocorrelation(buffer, 0);
            //double[] ac2 = performAutocorrelation(ac1, 1); // Size too small for re-auto-correlation
        }

        private double[] performAutocorrelation(double[] input, int acNumber) {
            //double[] output = new double[input.length];
            ArrayList<Double> output = new ArrayList<Double>(input.length);
            if(MAX_PERIOD_IN_SAMPLES>input.length) throw new RuntimeException("Buffer size (" + input.length + ") insufficient for PDA.");
            for(int lag=MIN_PERIOD_IN_SAMPLES;lag<=MAX_PERIOD_IN_SAMPLES;lag++){
                double sum = 0;
                for(int i=0;i<input.length-lag;i++){
                    sum += input[i]*input[i+lag];
                }
                output.add(sum/(input.length-lag));
            }
            double[] outputArray = new double[output.size()];
            for(int i=0;i<output.size();i++) outputArray[i] = output.get(i);
            onResult(outputArray, acNumber);
            return outputArray;
        }

        public void onResult(double[] output, int acNumber) {
            // Get max value and peakThreshold
            double max = 0;
            for(double d: output) if(d>max) max=d;
            double peakThreshold = PEAK_THRESHOLD_PERCENT * max;
            // Get all peak values.
            ArrayList<Integer> peaks = new ArrayList<Integer>();
            for(int i=1;i+1<output.length;i++) if(output[i]>peakThreshold && output[i]>output[i-1] && output[i]>output[i+1]) peaks.add(i);
            // Get index of first peak in output (after output[0])
            int maxIndex = 0;//MIN_PERIOD_IN_SAMPLES;
            while(output[maxIndex+1]<output[maxIndex]) maxIndex++;
            while(output[maxIndex+1]>output[maxIndex]) maxIndex++;
            Log.i("ace", "MaxIndex: " + maxIndex);

            // Convert period in samples to period in second then get frequency
            double period = ((double) peaks.get(peaks.size()-1)) / peaks.size() / 44100;
            double frequency = 1.0 / period;
            currentPitch = Pitch.fromFrequency(frequency);
            //grapher.drawGraph(output, "Frequency: " + frequency + "; Note Name: " + pitchConverter.getPitchFromFrequency(frequency), true);
            String text="Peak values: ";
            for(int peakIndex : peaks) text+="<br>" + output[peakIndex];
            //grapher.drawText(text);
        }
    }
}
