package com.benjaminran.intonationcore;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;

public class PerformanceTest {

    @Test
    public void test() throws IOException, UnsupportedAudioFileException {
        int targetSampleRate = 44100;
        int audioBufferSize = 4096;
        URL url = Thread.currentThread().getContextClassLoader().getResource("balsom-ptrumpet/g.wav");
        final File testFile = new File(url.getFile());
        final AudioDispatcher audioDispatcher = AudioDispatcherFactory.fromFile(testFile, audioBufferSize, 0);
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, targetSampleRate, audioBufferSize, new PitchDetectionHandler() {
            int i = 1;
            PrintWriter writer = new PrintWriter("output.txt");

            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
                writer.print(String.format("%d  %f", i++, pitchDetectionResult.getPitch()));
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
//        assertEquals(0, 1);
    }
}