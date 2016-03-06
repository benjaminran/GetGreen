package com.bran.getgreen;

import be.tarsos.dsp.util.PitchConverter;

import java.util.HashMap;

/**
 * Represents information about the pitch heard by the Tuner
 *    note: midi note number
 *    centsSharp: number of cents deviation above perfect note pitch
 */
public class Pitch implements Comparable<Pitch> {

    public static final int BOTTOM_NOTE_MIDI = 21;  // Lowest note: A0
    public static final int TOP_NOTE_MIDI = 108;  // Highest note: C8
    public static final int BOTTOM_NOTE_PIANO = midiToPianoNote(BOTTOM_NOTE_MIDI);
    public static final int TOP_NOTE_PIANO = midiToPianoNote(TOP_NOTE_MIDI);
    public static final int A440_NOTE_MIDI = 69;
    public static final int A440_NOTE_PIANO = 49;

    public static final Pitch NO_PITCH = null;

    private static final HashMap<Integer, Range> RANGES = new HashMap<>();
    static {
        for(int n = BOTTOM_NOTE_MIDI; n <= TOP_NOTE_MIDI; n++) {
            double upper = fromNoteMidi(n+0.5).getFrequency();
            double lower = fromNoteMidi(n-0.5).getFrequency();
            RANGES.put(n, new Range(upper, lower));
        }
    }

    private static final String[] NOTE_NAMES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

    private int note;  // midi
    private int centsSharp;
    private double frequency;

    public Pitch(int note, int centsSharp, double frequency) {
        this.note = note;
        this.centsSharp = centsSharp;
        this.frequency = frequency;
    }

    public static Pitch fromFrequency(double frequency) {
        if(frequency<0) return null; // no pitch
        int note = PitchConverter.hertzToMidiKey((double) frequency);
        int centsSharp = ((int) PitchConverter.hertzToAbsoluteCent(frequency)) - 100 * note;
        return new Pitch(note, centsSharp, frequency);
    }

    public static Pitch fromNoteMidi(double n) {
        if(n < BOTTOM_NOTE_MIDI - 0.5 || n > TOP_NOTE_MIDI + 0.5) return null;
        double frequency = Math.pow(2, (n-A440_NOTE_MIDI)/12.0) * 440;
        return fromFrequency(frequency);
    }

    public static int midiToPianoNote(int midi) { return midi - A440_NOTE_MIDI + A440_NOTE_PIANO; }
    public static int pianoToMidiNote(int piano) { return piano + A440_NOTE_MIDI - A440_NOTE_PIANO; }

    public int getNote() { return note; }
    public int getCentsSharp() { return centsSharp; }
    public double getFrequency() { return frequency; }

    public String toString() { return String.format("Note: %d; Cents Sharp: %d; Frequency: %.2f", note, centsSharp, frequency); }
    public String getNoteName() { return (note<0) ? null : Pitch.getNoteName(note); }
    public String getNoteLetter() { return (note<0) ? null : Pitch.getNoteName(note); }
    public int getOctaveNumber() { return Pitch.getOctaveNumber(note); }

    public static String getNoteName(int noteNumber) { return getNoteLetter(noteNumber)+getOctaveNumber(noteNumber); }
    public static String getNoteLetter(int noteNumber) { return NOTE_NAMES[noteNumber%NOTE_NAMES.length]; }
    public static int getOctaveNumber(int noteNumber) { return (noteNumber / NOTE_NAMES.length) - 1; }

    @Override
    public int compareTo(Pitch another) {
        if(frequency<another.frequency) return -1;
        if(frequency>another.frequency) return 1;
        return 0;
    }

    public static class Range {
        public final double upper, lower;

        public Range(double upper, double lower) {
            this.upper = upper;
            this.lower = lower;
        }
    }
}
