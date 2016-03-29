package com.benjaminran.intonationcore;

import com.benjaminran.performanceadapters.FilePerformanceAdapter;
import org.junit.Test;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AnalysisTest {

    @Test
    public void test() throws IOException, UnsupportedAudioFileException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("balsom-ptrumpet/a.wav");
        final File testFile = new File(url.getFile());
        FilePerformanceAdapter filePerformanceAdapter = new FilePerformanceAdapter(testFile);
        Performance performance = filePerformanceAdapter.getPerformance();
        Analysis analysis = Analysis.of(performance);
        assertThat(performance, is(notNullValue()));
    }
}