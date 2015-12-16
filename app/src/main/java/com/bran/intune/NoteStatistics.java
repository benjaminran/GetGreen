package com.bran.intune;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * DescriptiveStatistics subclass that allows for sorting via dataset size.
 */
public class NoteStatistics extends DescriptiveStatistics implements Comparable<NoteStatistics> {
    @Override
    public int compareTo(NoteStatistics another) {
        if(getN() > another.getN()) return 1;
        if(getN() > another.getN()) return -1;
        return 0;
    }
}
