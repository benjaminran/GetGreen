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
import com.benjaminran.intonationcore.Note;
import com.benjaminran.intonationcore.Performance;
import com.benjaminran.intonationcore.PerformanceAdapter;
import com.benjaminran.intonationcore.Pitch;

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
    private File file;

    public FilePerformanceAdapter(File file) throws IOException, UnsupportedAudioFileException {
        ready = false;
        this.file = file;
        processFile(file);
    }

    private void processFile(File file) throws IOException, UnsupportedAudioFileException {
        if(file != null) this.file = file;
        int targetSampleRate = 44100;
        int audioBufferSize = 2048;
        performance = new Performance();
        final Note currentNote = new Note();
        final AudioDispatcher audioDispatcher = AudioDispatcherFactory.fromFile(this.file, audioBufferSize, 0);
        ComplexOnsetDetector onset = new ComplexOnsetDetector(audioBufferSize, 0.1);//default .3
        onset.setHandler(new OnsetHandler() {
            @Override
            public void handleOnset(double time, double salience) {
                if(currentNote.getPitches().size()>0) performance.addNote(currentNote);
            }
        });
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, targetSampleRate, audioBufferSize, new PitchDetectionHandler() {
            int i = 1;

            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
                if(pitchDetectionResult.isPitched()) {
                    currentNote.addPitch(Pitch.fromFrequency(pitchDetectionResult.getPitch(), 0));
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
        audioDispatcher.addAudioProcessor(onset);
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
