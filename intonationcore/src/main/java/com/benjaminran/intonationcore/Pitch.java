package com.benjaminran.intonationcore;

import be.tarsos.dsp.util.PitchConverter;

public class Pitch implements Comparable<Pitch> {

    private double absoluteCents;
    private double frequency;
    private long time;

    public Pitch(double absoluteCents, double frequency, long time) {
        this.absoluteCents = absoluteCents;
        this.frequency = frequency;
        this.time = time;
    }

    public static Pitch fromFrequency(double frequency, long time) {
        return new Pitch(PitchConverter.hertzToAbsoluteCent(frequency), frequency, time);
    }

    @Override
    public int compareTo(Pitch another) {
        return Double.compare(frequency,another.frequency);
    }

    @Override
    public String toString() {
        return String.format("%fHz", frequency);
    }
}
