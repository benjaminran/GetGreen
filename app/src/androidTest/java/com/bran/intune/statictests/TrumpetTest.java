package com.bran.intune.statictests;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.bran.intune.MainActivity;
import com.bran.intune.Pitch;

import java.io.IOException;

/**
 * A test class containing the suite of single-pitch tests using trumpet sounds.
 */
public class TrumpetTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;
    private Context context;

    public TrumpetTest() {
        super(MainActivity.class);
    }

    /**
     * Sets up the test fixture for this test case. This method is always called before every test run.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        mainActivity = getActivity();
        context = getInstrumentation().getContext();
    }

    /*
     * Tests begin here
     */

    @MediumTest
    public void testG3() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_g3, new Pitch(35, 0)).execute();
    }

    @MediumTest
    public void testA3() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_a3, new Pitch(37, 0)).execute();
    }

    @MediumTest
    public void testB3() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_b3, new Pitch(39, 0)).execute();
    }

//    @MediumTest
    public void testC4() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_c4, new Pitch(40, 0)).execute();
    }

//    @MediumTest
    public void testD4() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_d4, new Pitch(42, 0)).execute();
    }

//    @MediumTest
    public void testE4() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_e4, new Pitch(44, 0)).execute();
    }

//    @MediumTest
    public void testF4() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_f4, new Pitch(45, 0)).execute();
    }

//    @MediumTest
    public void testG4() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_g4, new Pitch(47, 0)).execute();
    }

//    @MediumTest
    public void testA4() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_a4, new Pitch(49, 0)).execute();
    }

//    @MediumTest
    public void testD5() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_d5, new Pitch(54, 0)).execute();
    }

//    @MediumTest
    public void testE5() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_e5, new Pitch(56, 0)).execute();
    }

//    @MediumTest
    public void testF5() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_f5, new Pitch(57, 0)).execute();
    }

//    @MediumTest
    public void testG5() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_g5, new Pitch(59, 0)).execute();
    }

//    @MediumTest
    public void testB5() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_b5, new Pitch(63, 0)).execute();
    }

//    @MediumTest
    public void testC6() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_c6, new Pitch(64, 0)).execute();
    }

//    @MediumTest
    public void testD6() throws IOException, InterruptedException {
        new SinglePitchTest(context, mainActivity, com.bran.intune.debug.test.R.raw.trumpet_d6, new Pitch(66, 0)).execute();
    }
}