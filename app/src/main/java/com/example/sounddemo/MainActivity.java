package com.example.sounddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    AudioManager audioManager;

    private Timer timer;

    private SeekBar timeControl;

    private int seekTo;

    public int mediaDuration;

    private void trackMediaTime (){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int position = mediaPlayer.getCurrentPosition();
                int seekTo = (int) (((double)position/mediaDuration) * 100d);
                timeControl.setProgress(seekTo);
            }
        },0,300);

    }

    public void play(View view){
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            trackMediaTime();
        }
    }

    public void pause(View view){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            timer.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mediaPlayer = MediaPlayer.create(this, R.raw.mistake);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.i("Info", "Finished");
                timer.cancel();
            }
        });

        mediaDuration = mediaPlayer.getDuration();


        timeControl = (SeekBar) findViewById(R.id.timeSeekBar);
        SeekBar volumeControl = (SeekBar) findViewById(R.id.volumeSeekBar);

        timeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int seek = (int) (mediaDuration*(i/100d));
                seekTo = seek;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                timer.cancel();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekTo);
                if(mediaPlayer.isPlaying()) trackMediaTime();
            }
        });

        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(currentVolume);
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}