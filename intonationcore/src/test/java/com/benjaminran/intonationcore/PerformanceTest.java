package com.benjaminran.intonationcore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.benjaminran.performanceadapters.FilePerformanceAdapter;
import org.junit.Test;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class PerformanceTest {

    @Test
    public void test() throws IOException, UnsupportedAudioFileException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("balsom-ptrumpet/a.wav");
        final File testFile = new File(url.getFile());
        FilePerformanceAdapter filePerformanceAdapter = new FilePerformanceAdapter(testFile);
        Performance performance = filePerformanceAdapter.getPerformance();
        assertThat(performance, is(notNullValue()));
        System.out.println(performance);
        System.out.println(performance.getNotes().size());
    }
}