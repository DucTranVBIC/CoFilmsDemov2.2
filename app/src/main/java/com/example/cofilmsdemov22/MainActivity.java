package com.example.cofilmsdemov22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.example.cofilmsdemov22.Adapter.VideoAdapter;
import com.example.cofilmsdemov22.Model.VideoModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static int CAMERA_PERMISSION_CODE = 100;
    private static int VIDEO_RECORD_CODE = 101;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerviewLayoutManager;

    ArrayList<VideoModel> arrayListVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isCameraPresentInPhone()){
            Log.i("VIDEO_RECORD_TAG","Camera is detected");
            getCameraPermission();
        }
        else{
            Log.i("VIDEO_RECORD_TAG","No camera is detected");
        }

        init();
    }

    public void recordVideoButtonPressed(View view) {
        recordVideo();
    }

    private boolean isCameraPresentInPhone()
    {
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        }
        else{
            return false;
        }
    }

    private void recordVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_RECORD_CODE);
    }

    private void getCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewVideo);
        recyclerviewLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(recyclerviewLayoutManager);
        arrayListVideos = new ArrayList<>();
        fetchView();
    }

    private void fetchView() {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name, column_id, thumb;


        String absolutePathImage = null;

        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Thumbnails.DATA};

        String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        //column_id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
        thumb = cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA);

        while (cursor.moveToNext()) {
            absolutePathImage = cursor.getString(column_index_data);

            VideoModel videoModel = new VideoModel();

            videoModel.setBoolean_selected(false);
            videoModel.setStr_path(absolutePathImage);
            videoModel.setStr_thumb(cursor.getString(thumb));

            arrayListVideos.add(videoModel);
        }

        VideoAdapter videoAdapter = new VideoAdapter(getApplicationContext(), arrayListVideos, MainActivity.this );
        recyclerView.setAdapter(videoAdapter);
    }


}