package com.bran.smarttuner;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by beni on 4/16/15.
 */
public class Tuner {
    private Pitch currentPitch;

    public Tuner() {
        currentPitch = new Pitch(30, 10); // temp
    }

    public Pitch getCurrentPitch() { return currentPitch; }

    public static class Pitch {
        private int note;
        private int centsSharp;

        public Pitch(int note, int centsSharp) {
            this.note = note;
            this.centsSharp = centsSharp;
        }

        public int getNote() { return note; }
        public int getCentsSharp() { return centsSharp; }
    }

    private class MicBuffer implements AudioRecord.OnRecordPositionUpdateListener {
        private MicBuffer micBuffer;
        private AudioRecord audioRecord;
        private short[] buffer;
        private int minBufferSize;
        private int sampleRateInHz;

        private MicBuffer(){
            micBuffer = this;
        }

        private void startListening() {
            new Thread(new Runnable() {
                public Handler mHandler;
                @Override
                public void run() {
                    Looper.prepare();
                    mHandler = new Handler();

                    sampleRateInHz = 44100;
                    minBufferSize = AudioRecord.getMinBufferSize(sampleRateInHz, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                    buffer = new short[minBufferSize*4];
                    audioRecord = new AudioRecord(
                            MediaRecorder.AudioSource.DEFAULT,
                            sampleRateInHz,
                            AudioFormat.CHANNEL_IN_MONO,
                            AudioFormat.ENCODING_PCM_16BIT,
                            8 * minBufferSize
                    );
                    audioRecord.setRecordPositionUpdateListener(micBuffer);
                    audioRecord.setPositionNotificationPeriod(2 * minBufferSize); // size in bytes
                    audioRecord.startRecording();
                    Looper.loop();
                }
            }, "Recording Thread").start();
        }

        @Override
        public void onMarkerReached(AudioRecord audioRecord) {}

        @Override
        public void onPeriodicNotification(AudioRecord record) {
            int read = record.read(buffer, 0, 2 * minBufferSize); // size in shorts
        }
    }
}
