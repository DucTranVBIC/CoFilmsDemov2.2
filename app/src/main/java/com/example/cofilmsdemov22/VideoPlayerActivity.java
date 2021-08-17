package com.example.cofilmsdemov22;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    VideoView videoView;
    ImageView imageView;
    Spinner spinner;
    SeekBar seekbar;
    String str_video_url;
    boolean isPlay = false;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
    }

    private void init(){
        videoView = (VideoView)findViewById(R.id.videoView);
        imageView = (ImageView)findViewById(R.id.toggleButton);
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemClickListener(this::onItemSelected);
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        str_video_url = getIntent().getStringExtra("video");
        videoView.setVideoPath(str_video_url);
        videoView.start();
        isPlay = true;
        imageView.setImageResource(R.drawable.pausebutton);
        updateSeekBar();
    }

    private void updateSeekBar(){
        handler.postDelayed(updateTimeTask,100);
    }

    public Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            seekbar.setProgress(videoView.getCurrentPosition());
            seekbar.setMax(videoView.getDuration());
            handler.postDelayed(this,100);

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    handler.removeCallbacks(updateTimeTask);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    handler.removeCallbacks(updateTimeTask);
                    videoView.seekTo(seekBar.getProgress());
                    updateSeekBar();
                }
            });
        }
    };

    public void toggle_method(View v){
       if(isPlay){
           videoView.pause();
           isPlay = false;
           imageView.setImageResource(R.drawable.playbutton);
       }
       else {
           videoView.start();
           updateSeekBar();
           isPlay = true;
           imageView.setImageResource(R.drawable.pausebutton);
       }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // VideoRecording();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}