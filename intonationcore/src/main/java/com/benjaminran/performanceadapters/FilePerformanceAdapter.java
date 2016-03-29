package com.benjaminran.performanceadapters;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFloatConverter;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchDetector;
import be.tarsos.dsp.pitch.PitchProcessor;
import com.benjaminran.intonationcore.Note;
import com.benjaminran.intonationcore.Performance;
import com.benjaminran.intonationcore.PerformanceAdapter;
import com.benjaminran.intonationcore.Pitch;
import com.benjaminran.intonationcore.Util;

import org.apache.commons.lang3.ArrayUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by benjaminran on 2/17/16.
 */
public class FilePerformanceAdapter implements PerformanceAdapter {

    private static final int MAX_BUFFER_SIZE = (int) Math.pow(2, 13);

    private boolean ready;
    private Performance performance;
    private File file;

    public FilePerformanceAdapter(File file) throws IOException, UnsupportedAudioFileException {
        ready = false;
        this.file = file;
        processFile(file);
    }

    private void processFileRecur(int sampleRate, int frameOffset, float[] buffer, int l, int r, HashMap<Integer, PitchDetector> detectors) throws IOException, UnsupportedAudioFileException {
        int bufferSize = r-l;
        if(detectors.get(bufferSize)==null) {
            detectors.put(bufferSize, PitchProcessor.PitchEstimationAlgorithm.FFT_YIN.getDetector(sampleRate, bufferSize));
        }
        PitchDetector pitchDetector = detectors.get(bufferSize);
        PitchDetectionResult result = pitchDetector.getPitch(ArrayUtils.subarray(buffer, l, r));
        if(result.isPitched()) {
            Pitch resultPitch = Pitch.fromFrequency(result.getPitch(), frameOffset/sampleRate);
            float prob = result.getProbability();
        }
    }

    private void processFileDev(File file) throws IOException, UnsupportedAudioFileException {
        int sampleRate = 44100;
        final AudioInputStream stream = AudioSystem.getAudioInputStream(file);
        TarsosDSPAudioInputStream audioStream = new JVMAudioInputStream(stream);
        TarsosDSPAudioFloatConverter converter = TarsosDSPAudioFloatConverter.getConverter(audioStream.getFormat());
        HashMap<Integer, PitchDetector> detectors = new HashMap<>();
        byte[] byteBuffer = new byte[MAX_BUFFER_SIZE];
        float[] floatBuffer = new float[MAX_BUFFER_SIZE];
        boolean finished = false;
        for(int i=0; !finished; i++) {
            int read = audioStream.read(byteBuffer, 0, MAX_BUFFER_SIZE);
            converter.toFloatArray(byteBuffer, floatBuffer);
            processFileRecur(sampleRate, i*MAX_BUFFER_SIZE, floatBuffer, 0, MAX_BUFFER_SIZE, detectors);
            finished = read!=MAX_BUFFER_SIZE;
        }
    }

    private void processFile(File file) throws IOException, UnsupportedAudioFileException {
        if(file != null) this.file = file;
        int targetSampleRate = 44100;
        int audioBufferSize = 4096; // supports notes as low as C1 and as fast ast sixteenth notes @ 120bpm
        performance = new Performance();
        final AudioDispatcher audioDispatcher = AudioDispatcherFactory.fromFile(this.file, audioBufferSize, audioBufferSize/2);
        ComplexOnsetDetector onset = new ComplexOnsetDetector(audioBufferSize, 0.1);//default .3
        /*onset.setHandler(new OnsetHandler() {
            @Override
            public void handleOnset(double time, double salience) {
                if (currentNote.getPitches().size() > 0) performance.addNote(currentNote);
            }
        });*/
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, targetSampleRate, audioBufferSize, new PitchDetectionHandler() {

            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
                if(pitchDetectionResult.isPitched()) {
                    Note currentNote = new Note();
                    currentNote.addPitch(Pitch.fromFrequency(pitchDetectionResult.getPitch(), audioEvent.getTimeStamp()));
                    performance.addNote(currentNote);
                }
            }
        });
        AudioProcessor finish = new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                return false;
            }

            @Override
            public void processingFinished() {
                ready = true;
            }
        };
        /*audioDispatcher.addAudioProcessor(onset);*/
        audioDispatcher.addAudioProcessor(p);
        audioDispatcher.addAudioProcessor(finish);
        audioDispatcher.run();
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public Performance getPerformance() {
        if(!ready) return null;
        return performance;
    }
}
