package com.crypto.client.digitalportrait.Orders.Principal;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.crypto.client.digitalportrait.Orders.Utils.BitmapUtils;
import com.crypto.client.digitalportrait.R;


import ja.burhanrashid52.photoeditor.PhotoEditorView;


public class ShowResult extends AppCompatActivity {

    private static final String TAG = "ShowResult";
    public static final String pictureName = "meme2.png";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result_main);
        PhotoEditorView photoEditorView = findViewById(R.id.img_result_edition);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        photoEditorView.getSource().setImageBitmap( BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length) );
    }
}
