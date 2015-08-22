package com.bran.intune.statictests;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;

public class SinglePitchTest implements MediaPlayer.OnCompletionListener {
    private Context context;
    private MediaPlayer mediaPlayer;
    private boolean playingAudio;

    public SinglePitchTest(Context context, int resId) throws IOException {
        this.context = context;
        playingAudio = false;
        AssetFileDescriptor afd = context.getResources().openRawResourceFd(resId);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        afd.close();
        mediaPlayer.prepare();

    }

    public void execute() throws InterruptedException {
        start();
        while(playingAudio) {
            Thread.sleep(100);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Toast.makeText(context,"Playback Complete", Toast.LENGTH_LONG).show();
        stop();
    }

    private void start() {
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(this);
        playingAudio = true;
    }

    private void stop() {
        mediaPlayer.stop();
        mediaPlayer.release();
        playingAudio = false;
    }
}