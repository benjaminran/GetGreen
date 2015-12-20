package com.bran.intune.statictests;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.bran.intune.Analysis;
import com.bran.intune.MainActivity;
import com.bran.intune.Interpreter;
import com.bran.intune.Pitch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.fail;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SinglePitchTest implements MediaPlayer.OnCompletionListener {
    private static final int MEASUREMENT_PERIOD = 100;

    private Context context;
    private MainActivity mainActivity;
    private Interpreter interpreter;

    private MediaPlayer mediaPlayer;
    private boolean playingAudio;

    private Pitch expectedPitch;

    public SinglePitchTest(Context context, MainActivity mainActivity, int resId, Pitch expectedPitch) throws IOException {
        this.context = context;
        this.mainActivity = mainActivity;
        interpreter = mainActivity.getInterpreter();

        playingAudio = false;
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(resId);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        afd.close();
        mediaPlayer.prepare();

        this.expectedPitch = expectedPitch;
    }

    public void execute() throws InterruptedException {
        start();
        while(playingAudio) {
            Thread.sleep(MEASUREMENT_PERIOD);
            if (!mediaPlayer.isPlaying()) playingAudio = false;
        }
        onCompletion(mediaPlayer); // onCompletion not being called otherwise
    }

    /* Called manually for now */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stop();
        checkResults();
    }

    private void checkResults() {
        Analysis analysis = interpreter.getAnalysis();

    }

    private void start() {
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(this);
        playingAudio = true;
    }

    private void stop() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}