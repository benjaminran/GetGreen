package com.bran.intune;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by beni on 7/17/15.
 */
public class InterpreterTest {

    @Test
    public void testGetAnalysis() throws Exception {

    }

    @Test
    public void testQuartiles() throws Exception {
        for(int i=0; i<25; i++) { // 25 different data sets
            Random random = new Random();
            int n = (int) random.nextDouble()*5000;  // Max dataset length is 5000 elements (~28 minutes of recorded pitches)
            int[] centsData = new int[n];
            for(int j=0; j<n; j++) {
                centsData[j] = ((int)random.nextDouble()*100) - 50;  // centsSharp measurements must be between -50 and 50
            }
            testQuartiles(centsData);
        }
    }


    public void testQuartiles(int[] centsData) {
        Interpreter.Analysis analysis = new Interpreter.Analysis();
        for(int centsSharp : centsData) analysis.addPitch(49, centsSharp);
    }
}