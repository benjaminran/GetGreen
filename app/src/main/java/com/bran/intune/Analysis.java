package com.bran.intune;

import java.util.TreeMap;

/**
 *
 */
public class Analysis extends TreeMap<Integer, NoteStatistics> {

    private int size;

    public Analysis() {
        super();
        size = 0;
    }

    public void addPitch(Pitch newPitch) {
        size++;
        if(newPitch==null) return;  // do not update data set if pitch is not detected
        int note = newPitch.getNote();
        if(get(note)==null) put(note, new NoteStatistics());
        if(note>=Pitch.BOTTOM_NOTE_MIDI && note<=Pitch.TOP_NOTE_MIDI) {
            // Add pitch to history
            get(note).addValue(newPitch.getFrequency());
        }
    }

    @Override
    public String toString() {
        String info = "";
        for(int n=Pitch.BOTTOM_NOTE_MIDI; n<=Pitch.TOP_NOTE_MIDI; n++) {
            NoteStatistics data = get(n);
            if(data==null) continue;
            String noteName = Pitch.getNoteName(n);
            long size = data.getN();
            int q1 = data.getQ1().getCentsSharp();
            int q2 = data.getQ2().getCentsSharp();
            int q3 = data.getQ3().getCentsSharp();
            info += String.format("Note: %s; Count: %d; Q1: %d; Q2: %d; Q3: %d%n", noteName, size, q1, q2, q3);
        }
        return info;
    }

    public int size() { return size; }
}
