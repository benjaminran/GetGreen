package com.benjaminran.charting;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import be.tarsos.dsp.io.TarsosDSPAudioFloatConverter;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;

public class Chart {

    private float[] getRawAudioDataOld(File file) throws IOException, UnsupportedAudioFileException {
        AudioInputStream preStream = AudioSystem.getAudioInputStream(file);
        int preB = preStream.read();
        int max = preB;
        int count = 0;
        for(; preB!=-1; count++) {
            preB = preStream.read();
            if(max<preB) max = preB;
        }
        AudioInputStream stream = AudioSystem.getAudioInputStream(file);
        float[] data = new float[count];
        int b = stream.read();
        for(int i=0; b!=-1; i++) {
            data[i] = ((float) b)/max;
        }
        return data;
    }

    private float[] getRawAudioData(File file) throws IOException, UnsupportedAudioFileException {
        JVMAudioInputStream stream = new JVMAudioInputStream(AudioSystem.getAudioInputStream(file));
        TarsosDSPAudioFloatConverter converter = TarsosDSPAudioFloatConverter.getConverter(stream.getFormat());
        byte[] buffer = new byte[(int) file.length()];
        int len = stream.read(buffer, 0, buffer.length);
        float[] data = new float[len/2];
        converter.toFloatArray(buffer,data);
        return data;
    }

    @Test
    public void chartPitchDetectionPTrumpet() throws IOException, UnsupportedAudioFileException {
        chartPitchDetection(new File(Thread.currentThread().getContextClassLoader().getResource("balsom-ptrumpet/a.wav").getFile()), "ptrumpet.png");
    }

    //@Test
    public void chartPitchDetectionSarabande() throws IOException, UnsupportedAudioFileException {
        chartPitchDetection(new File(Thread.currentThread().getContextClassLoader().getResource("balsom-sarabande/sarabande.wav").getFile()), "sarabande.png");
    }

    public void chartPitchDetection(File inFile, String outFile) throws IOException, UnsupportedAudioFileException {
        int sampleRate = 44100;
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries rawAudioSeries = new XYSeries("Raw Input");
        float[] rawAudioData = getRawAudioData(inFile);
        for(int i=0; i<rawAudioData.length; i++) {
            rawAudioSeries.add(((float) i)/sampleRate*1000, rawAudioData[i]);
        }
        dataset.addSeries(rawAudioSeries);
        JFreeChart chart = ChartFactory.createXYLineChart("", "Time", "", dataset);

        File chartFile = new File(System.getProperty("chartsDir", "nochartsdir") + "/" + outFile);
        ChartUtilities.saveChartAsPNG(chartFile, chart, rawAudioData.length/10, 360);
    }
}
