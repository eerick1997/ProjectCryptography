package com.crypto.client.digitalportrait.Orders.Principal;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.crypto.client.digitalportrait.Orders.Utils.BitmapUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crypto.client.digitalportrait.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import ja.burhanrashid52.photoeditor.PhotoEditorView;

import static com.crypto.client.digitalportrait.Utilities.Reference.*;

public class AddOrder extends AppCompatActivity {

    public static final int PERMISSION_PICK_IMAGE = 1000;

    private PhotoEditorView photoEditorView;
    private Uri imageSelectedUri;
    private Bitmap originalBitmap;
    private String strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        strEmail = getIntent().getStringExtra(EMAIL);
        Toast.makeText(getApplicationContext(), strEmail, Toast.LENGTH_LONG).show();
        photoEditorView = findViewById(R.id.img_select);
        FloatingActionButton fabSendOrder = findViewById(R.id.fab_send_order);
        fabSendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOrder();
            }
        });

        Button btnSelectImage = findViewById(R.id.btn_select_image);
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFromGallery();
            }
        });
    }

    private void sendOrder(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference dataReference = db.collection(ORDERS);

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
                            Toast.makeText(AddOrder.this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
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
        if (resultCode == RESULT_OK) {
            if (requestCode == PERMISSION_PICK_IMAGE) {
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);

                this.imageSelectedUri = data.getData();

                this.originalBitmap = bitmap;
                photoEditorView.getSource().setImageBitmap(this.originalBitmap);
            }
        }
    }
}
