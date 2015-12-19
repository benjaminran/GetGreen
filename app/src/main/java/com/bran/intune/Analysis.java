package com.bran.intune;

import java.util.TreeMap;

/**
 *
 */
public class Analysis {
    TreeMap<Integer, NoteStatistics> history;

    public Analysis() {
        history = new TreeMap<Integer, NoteStatistics>();
    }

    public void addPitch(Pitch newPitch) {
        if(newPitch==null) return;  // do not update data set if pitch is not detected
        int note = newPitch.getNote();
        if(history.get(note)==null) history.put(note, new NoteStatistics());
        if(note>=Pitch.BOTTOM_NOTE_MIDI && note<=Pitch.TOP_NOTE_MIDI) {
            // Add pitch to history
            history.get(note).addValue(newPitch.getFrequency());
        }
    }

    @Override
    public String toString() {
        String info = "";
        for(int n=Pitch.BOTTOM_NOTE_MIDI; n<=Pitch.TOP_NOTE_MIDI; n++) {
            NoteStatistics data = history.get(n);
            if(data==null) continue;
            String noteName = Pitch.getNoteName(n);
            int octaveNumber = Pitch.getOctaveNumber(n);
            long size = data.getN();
            double q1 = data.getPercentile(25);
            double q2 = data.getPercentile(50);
            double q3 = data.getPercentile(75);
            info += String.format("Note: %s%d; Count: %d; Q1: %.2f; Q2: %.2f; Q3: %.2f%n", noteName, octaveNumber, size, q1, q2, q3);
        }
        return info;
    }

    public TreeMap<Integer, NoteStatistics> getHistory() { return history; }
}
