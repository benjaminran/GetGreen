package com.bran.smarttuner;

import be.tarsos.dsp.util.PitchConverter;

/**
 * Represents information about the pitch heard by the Tuner
 *    note: midi note number
 *    centsSharp: number of cents deviation above perfect note pitch
 */
public class Pitch {
    private static final String[] NOTE_NAMES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

    private int note;
    private int centsSharp;

    private Pitch(int note, int centsSharp) {
        this.note = note;
        this.centsSharp = centsSharp;
    }

    public static Pitch fromFrequency(float frequency) {
        if(frequency==-1) return null; // no pitch; noise
        int note = PitchConverter.hertzToMidiKey((double) frequency);
        int centsSharp = ((int) PitchConverter.hertzToAbsoluteCent(frequency)) - 100 * note;
        return new Pitch(note, centsSharp);
    }

    public int getNote() { return note; }
    public int getCentsSharp() { return centsSharp; }

    public String toString() { return String.format("Note: %d; Cents Sharp: %d", note, centsSharp); }
    public String getNoteName() { return Pitch.getNoteName(note); }
    public int getOctaveNumber() { return Pitch.getOctaveNumber(note); }

    public static String getNoteName(int noteNumber) { return NOTE_NAMES[noteNumber%12]; }
    public static int getOctaveNumber(int noteNumber) { return ((int) noteNumber / 12) - 1; }
}
