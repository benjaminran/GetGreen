package com.bran.intune;

import com.bran.intune.Pitch;

import org.junit.Test;

/**
 * Created by beni on 7/16/15.
 */
public class PitchTest {

    private static final int ACCURACY_THRESHOLD = 5;


        @Test
        public void testFromFrequency() throws Exception {
            Pitch pitch = Pitch.fromFrequency(440f);
            assert(pitch.getOctaveNumber()==4);
            assert(pitch.getNoteName().equals("A"));
            assert(Math.abs(pitch.getCentsSharp())<ACCURACY_THRESHOLD);
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