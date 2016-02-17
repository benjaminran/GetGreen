package com.benjaminran.intonationcore;

import be.tarsos.dsp.util.PitchConverter;

public class Pitch implements Comparable<Pitch> {

    private double absoluteCents;
    private double frequency;

    public Pitch(double absoluteCents, double frequency) {
        this.absoluteCents = absoluteCents;
        this.frequency = frequency;
    }

    public static Pitch fromFrequency(double frequency) {
        return new Pitch(PitchConverter.hertzToAbsoluteCent(frequency), frequency);
    }

    @Override
    public int compareTo(Pitch another) {
        if(frequency<another.frequency) return -1;
        if(frequency>another.frequency) return 1;
        return 0;
    }
}
