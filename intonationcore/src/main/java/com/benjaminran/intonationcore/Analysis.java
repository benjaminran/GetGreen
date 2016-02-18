package com.benjaminran.intonationcore;

/**
 * Created by benjaminran on 2/17/16.
 */
public class Analysis {

    private Performance performance;

    public Analysis(Performance performance) {
        this.performance = performance;
    }

    public static Analysis of(Performance performance) {
        return new Analysis(performance);
    }
}
