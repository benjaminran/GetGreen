package com.bran.intune;


import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * DescriptiveStatistics subclass that allows for sorting via dataset size.
 */
public class NoteStatistics extends DescriptiveStatistics implements Comparable<NoteStatistics> {

    private static final double MEDIAN_WEIGHT = 0.4;  // deviation of median pitch
    private static final double SPREAD_WEIGHT = 0.4;  // variability of pitches
    private static final double SIZE_WEIGHT = 0.2;  // number of times the pitch was played

    private static final int MAX_MEDIAN = 50;


    @Override
    public int compareTo(NoteStatistics another) {
        if(getN() > another.getN()) return 1;
        if(getN() < another.getN()) return -1;
        return 0;
    }

    /**
     * Calculates severity of intonation problems on this note
     * @return a double on the interval [0,1] with 1 being the most severe and 0 being most in tune
     */
    public double getSeverity(int analysisSize) {
        double medianComponent = MEDIAN_WEIGHT * Math.abs(getQ2().getCentsSharp()) / MAX_MEDIAN;
        double spreadComponent = SPREAD_WEIGHT * getSpread();
        double analysisComponent = SIZE_WEIGHT * analysisSize;
        return medianComponent + spreadComponent + analysisComponent;
    }

    /**
     * Get a measure of how variable the data is
     * @return a double on the interval [0,1] with 1 being the most variable and 0 the least
     */
    public double getSpread() {
        return ((double) getCentsIQR()) / 100;
    }

    public int getCentsIQR() {
        return getQ3().getCentsSharp() - getQ1().getCentsSharp();
    }

    public Pitch getQ1() {
        return Pitch.fromFrequency(getPercentile(0.25));
    }

    public Pitch getQ2() {
        return Pitch.fromFrequency(getPercentile(0.50));
    }

    public Pitch getQ3() {
        return Pitch.fromFrequency(getPercentile(0.75));
    }
}
