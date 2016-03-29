package com.benjaminran.intonationcore;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by benjaminran on 2/17/16.
 */
public class Analysis {

    private static final double MEAN_WEIGHT = 0.34;
    private static final double SPREAD_WEIGHT = 0.33;
    private static final double COUNT_WEIGHT = 0.33;

    private Performance performance;
    private TreeMap<Double, DescriptiveStatistics> priorityMap;

    public Analysis(Performance performance) {
        this.performance = performance;

        // construct pitch list
        List<Pitch> pitches = new ArrayList<Pitch>();
        for(Note note : this.performance.getNotes()) {
            for(Pitch pitch : note.getPitches()) {
                pitches.add(pitch);
            }
        }
        // construct map of noteNumbers to pitch statistics
        TreeMap<Integer, DescriptiveStatistics> pitchMap = new TreeMap<>();
        for(int i=0; i<pitches.size(); i++) {
            Pitch pitch = pitches.get(i);
            int noteNumber = pitch.getNoteNumber();
            if(!pitchMap.containsKey(noteNumber)) {
                pitchMap.put(noteNumber, new DescriptiveStatistics());
                for(int j=i; j<pitches.size(); j++){
                    if(noteNumber==pitches.get(j).getNoteNumber()) {
                        pitchMap.get(noteNumber).addValue(pitches.get(j).getFrequency());
                    }
                }
            }
        }
        // get max size of any pitchStatistics for normalization
        long maxN = 0;
        for(DescriptiveStatistics pitchStatistics : pitchMap.values()) {
            if(pitchStatistics.getN() > maxN) {
                maxN = pitchStatistics.getN();
            }
        }
        // create priority-ordered map to pitch statistics
        priorityMap = new TreeMap<>();
        for(DescriptiveStatistics pitchStatistics : pitchMap.values()) {
            double meanDeviation = Math.abs(meanDeviationOf(pitchStatistics)) / 50.0;
            double spread = pitchStatistics.getStandardDeviation()/50.0;
            double count = ((double) pitchStatistics.getN()) / maxN;
            double priority = MEAN_WEIGHT * meanDeviation + SPREAD_WEIGHT * spread + COUNT_WEIGHT * count;
            priorityMap.put(priority, pitchStatistics);
        }
    }

    /**
     *
     * @param pitchStatistics a DescriptiveStatistics object containing note frequencies all corresponding to the same note number
     * @return The cents difference between the pitch of the average frequency and the true, in-tune pitch it is nearest to
     */
    private double meanDeviationOf(DescriptiveStatistics pitchStatistics) {
        return Pitch.fromFrequency(pitchStatistics.getMean(), 0).getAbsoluteCents() - 100.0 * Math.round(Pitch.fromFrequency(pitchStatistics.getElement(0), 0).getAbsoluteCents() / 100.0);
    }

    public static Analysis of(Performance performance) {
        return new Analysis(performance);
    }
}
