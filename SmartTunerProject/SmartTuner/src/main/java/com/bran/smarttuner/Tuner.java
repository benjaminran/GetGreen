package com.bran.smarttuner;

/**
 * Created by beni on 4/16/15.
 */
public class Tuner {
    private Pitch currentPitch;

    public Tuner() {
        currentPitch = new Pitch(30, 10); // temp
    }

    public Pitch getCurrentPitch() { return currentPitch; }

    public static class Pitch {
        private int note;
        private int centsSharp;

        public Pitch(int note, int centsSharp) {
            this.note = note;
            this.centsSharp = centsSharp;
        }

        public int getNote() { return note; }
        public int getCentsSharp() { return centsSharp; }
    }
}
