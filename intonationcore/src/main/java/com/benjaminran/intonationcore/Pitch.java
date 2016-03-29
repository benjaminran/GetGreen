package com.benjaminran.intonationcore;

import be.tarsos.dsp.util.PitchConverter;

public class Pitch implements Comparable<Pitch> {

    private double absoluteCents;
    private double frequency;
    private double time;

    public Pitch(double absoluteCents, double frequency, double time) {
        this.absoluteCents = absoluteCents;
        this.frequency = frequency;
        this.time = time;
    }

    public static Pitch fromFrequency(double frequency, double time) {
        return new Pitch(PitchConverter.hertzToAbsoluteCent(frequency), frequency, time);
    }

    public int getNoteNumber() {
        return (int) Math.floor(absoluteCents/100);
    }

    public double getFrequency() {
        return frequency;
    }

    public double getAbsoluteCents() {
        return absoluteCents;
    }

    @Override
    public int compareTo(Pitch another) {
        return Double.compare(frequency,another.frequency);
    }

    @Override
    public String toString() {
        return String.format("%fc", absoluteCents);
    }
}
