package com.bran.getgreen;

import junit.framework.TestCase;

/**
 * Created by benjaminran on 1/3/16.
 */
public class NoteStatisticsTest extends TestCase {

    public void testGetSeverity() throws Exception {

    }

    public void testGetSpread() throws Exception {

    }

    public void testGetCentsIQR() throws Exception {
        NoteStatistics n = new NoteStatistics();
        for(double d = 0; d <= 1; d += 0.001) {
            n.addValue(d);
        }
//        assertEquals(n.getQ2().getFrequency(), 0.5);
        System.out.println("IQR: "+n.getCentsIQR());
    }

    public void testGetQ1() throws Exception {

    }

    public void testGetQ2() throws Exception {

    }

    public void testGetQ3() throws Exception {

    }
}