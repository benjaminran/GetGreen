package com.benjaminran.intonationcore;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Frequency");
        AudioDispatcher audioDispatcher = AudioDispatcherFactory.fromPipe(args[0], 44100, 4096, 0);
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, 4096, new PitchDetectionHandler() {
            int i = 1;
            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
                System.out.println(String.format("%d  %f", i++, pitchDetectionResult.getPitch()));
            }
        });
        audioDispatcher.addAudioProcessor(p);
        audioDispatcher.run();
    }

}
