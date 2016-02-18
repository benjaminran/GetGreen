package com.benjaminran.performanceadapters;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import com.benjaminran.intonationcore.Performance;
import com.benjaminran.intonationcore.PerformanceAdapter;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by benjaminran on 2/17/16.
 */
public class FilePerformanceAdapter implements PerformanceAdapter {

    private boolean ready;
    private Performance performance;

    public FilePerformanceAdapter(File file) {
        ready = false;

    }

    private void processFile(File file) throws IOException, UnsupportedAudioFileException {
        int targetSampleRate = 44100;
        int audioBufferSize = 2048;
        final AudioDispatcher audioDispatcher = AudioDispatcherFactory.fromFile(file, audioBufferSize, 0);
        ComplexOnsetDetector onset = new ComplexOnsetDetector(audioBufferSize);
        onset.setHandler(new OnsetHandler() {
            int count = 0;
            @Override
            public void handleOnset(double time, double salience) {
                System.out.println("Onset detected: "+(++count));
            }
        });
        audioDispatcher.addAudioProcessor(onset);
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, targetSampleRate, audioBufferSize, new PitchDetectionHandler() {
            int i = 1;

            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
                System.out.println(String.format("%d  %f", i++, pitchDetectionResult.getPitch()));
            }
        });
        AudioProcessor finish = new AudioProcessor() {
            @Override
            public boolean process(AudioEvent audioEvent) {
                return false;
            }

            @Override
            public void processingFinished() {
                audioDispatcher.stop();
            }
        };
        audioDispatcher.addAudioProcessor(p);
        audioDispatcher.run();
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public Performance getPerformance() {
        if(!ready) return null;
        return performance;
    }
}
