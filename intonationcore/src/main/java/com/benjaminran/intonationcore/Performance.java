package com.benjaminran.intonationcore;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjaminran on 2/16/16.
 */
public class Performance {
    private List<Note> notes;

    public Performance() {
        notes = new ArrayList<>();
    }

    public Performance(List<Note> notes) {
        this.notes = notes;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public List getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return StringUtils.join(notes, "\n");
    }
}
