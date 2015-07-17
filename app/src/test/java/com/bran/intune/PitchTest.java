package com.bran.intune;

import org.junit.Test;

/**
 * Created by beni on 7/16/15.
 */
public class PitchTest {

        @Test
        public void testFromFrequency() throws Exception {
            Pitch pitch = Pitch.fromFrequency(440f);
            assert(pitch.getOctaveNumber()==5);
            assert(pitch.getNoteName().equals("C"));
            assert(pitch.getCentsSharp()==0);
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