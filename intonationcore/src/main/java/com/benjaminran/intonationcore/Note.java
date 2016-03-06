package com.benjaminran.intonationcore;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjaminran on 2/16/16.
 */
public class Note {
    private List<Pitch> pitches;

    public Note() {
        this.pitches = new ArrayList<>();
    }

    public Note(List<Pitch> pitches) {
        this.pitches = pitches;
    }

    public void addPitch(Pitch pitch) {
        pitches.add(pitch);
    }

    public List<Pitch> getPitches() {
        return pitches;
    }

    @Override
    public String toString() {
        return "Note: " + StringUtils.join(pitches, ", ");
    }
}
