package com.crypto.artist.digitalportrait;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.crypto.artist.digitalportrait.CryptoUtils.Crypto;
import com.crypto.artist.digitalportrait.Login.Login;
import com.crypto.artist.digitalportrait.Orders.Principal.OrdersMain;
import com.crypto.artist.digitalportrait.PhotoEditor.Principal.FiltersListFragment;
import com.crypto.artist.digitalportrait.PhotoEditor.Principal.PhotoEditorMain;
import com.crypto.artist.digitalportrait.PhotoEditor.Utils.BitmapUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.spongycastle.util.test.Test;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private static final String TAG = "TestActivity";
    public static final int PERMISSION_PICK_IMAGE = 1000;
    Uri imageSelectedUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        FloatingActionButton FAB = findViewById(R.id.fab_edit_image);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFromGallery();
            }
        });


    }

    private void openImageFromGallery(){
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, PERMISSION_PICK_IMAGE);
                        } else {
                            Toast.makeText(TestActivity.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {
            if (requestCode == PERMISSION_PICK_IMAGE) {
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);
                imageSelectedUri = data.getData();
                Log.i(TAG, "onActivityResult: " + imageSelectedUri);
                this.bitmap = bitmap;
            }
        }

        Crypto crypto = new Crypto(TestActivity.this);
        try {
            crypto.signGenerator(bitmap);
            boolean verify = crypto.verifySign(bitmap, crypto.getPrivateKey(), crypto.getPublicKey());
            Log.i(TAG, "onActivityResult: verify " + verify);
        } catch (Exception e){
            Log.e(TAG, "onActivityResult: ", e);
        }

    }

}
