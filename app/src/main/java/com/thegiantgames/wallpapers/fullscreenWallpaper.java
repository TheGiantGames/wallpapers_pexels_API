package com.thegiantgames.wallpapers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.thegiantgames.wallpapers.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;

public class fullscreenWallpaper extends AppCompatActivity {


    String originalurl = " " ;
    PhotoView photoView;
    //ImageButton button ;


    @SuppressLint({"WrongViewCast", "MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_wallpaper);
        getSupportActionBar().hide();


        //button = findViewById(R.id.btnDownload);
      // VectorDrawable vectorDrawable = (VectorDrawable) getDrawable(R.drawable.ic_baseline_download_24) ;


       // button.setBackground(vectorDrawable);

       // button.setBackgroundColor(Color.RED);




        Intent intent = getIntent();
        originalurl = intent.getStringExtra("originalurl");
        photoView =findViewById(R.id.photoview);



        Glide.with(this).asBitmap().load(originalurl).into(photoView);






        ImageButton button = (ImageButton) findViewById(R.id.btnsetWallpaper);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(fullscreenWallpaper.this);
                Bitmap bitmap = ((BitmapDrawable)photoView.getDrawable()).getBitmap();



                try {
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(fullscreenWallpaper.this, "Wallpaper set" , Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        ImageButton button1 = (ImageButton)  findViewById(R.id.btnDownload);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(originalurl);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                downloadManager.enqueue(request);
                Toast.makeText(fullscreenWallpaper.this , "Downloading Start" , Toast.LENGTH_SHORT).show();
            }
        });






    }





}