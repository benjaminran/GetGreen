package com.bran.getgreen;

import org.junit.Assert;

/**
 * Created by beni on 7/16/15.
 */
public class PitchTest {

    private static final int ACCURACY_THRESHOLD = 5;


//    @Test
    public void testFromFrequency() throws Exception {
        // A4
        Pitch pitch1 = Pitch.fromFrequency(440f);
        Assert.assertTrue(pitch1.getOctaveNumber()==4);
        Assert.assertTrue(pitch1.getNoteName().equals("A"));
        Assert.assertTrue(Math.abs(pitch1.getCentsSharp())<ACCURACY_THRESHOLD);
        // F6
        Pitch pitch2 = Pitch.fromFrequency(1396.91);
        Assert.assertTrue(pitch2.getOctaveNumber()==6);
        Assert.assertTrue(pitch2.getNoteName().equals("F"));
        Assert.assertTrue(Math.abs(pitch2.getCentsSharp())<ACCURACY_THRESHOLD);
        // C2
        Pitch pitch3 = Pitch.fromFrequency(65.4064);
        Assert.assertTrue(pitch3.getOctaveNumber()==2);
        Assert.assertTrue(pitch3.getNoteName().equals("C"));
        Assert.assertTrue(Math.abs(pitch3.getCentsSharp())<ACCURACY_THRESHOLD);
    }

//    @Test
    public void testFromNoteMidi() throws Exception {
        // A4
        Pitch pitch1 = Pitch.fromNoteMidi(69);
        Assert.assertTrue(pitch1.getOctaveNumber()==4);
        Assert.assertTrue(pitch1.getNoteName().equals("A"));
        Assert.assertTrue(Math.abs(pitch1.getCentsSharp())<ACCURACY_THRESHOLD);
        // F6
        Pitch pitch2 = Pitch.fromNoteMidi(89);
        Assert.assertTrue(pitch2.getOctaveNumber()==6);
        Assert.assertTrue(pitch2.getNoteName().equals("F"));
        Assert.assertTrue(Math.abs(pitch2.getCentsSharp())<ACCURACY_THRESHOLD);
        // C2
        Pitch pitch3 = Pitch.fromNoteMidi(36);
        Assert.assertTrue(pitch3.getOctaveNumber()==2);
        Assert.assertTrue(pitch3.getNoteName().equals("C"));
        Assert.assertTrue(Math.abs(pitch3.getCentsSharp())<ACCURACY_THRESHOLD);
    }

    public void testGetNote() throws Exception {

    }

    public void testGetCentsSharp() throws Exception {

    }

    public void testToString() throws Exception {

    }

    public void testGetNoteName() throws Exception {

    }

    public void testGetOctaveNumber() throws Exception {

    }

    public void testGetNoteName1() throws Exception {

    }

    public void testGetOctaveNumber1() throws Exception {

    }
}