package com.crypto.client.digitalportrait;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.crypto.client.digitalportrait.Utilities.Preferences;

import org.spongycastle.util.encoders.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CopyToClipboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_to_clipboard);
        String key = getIntent().getStringExtra("KEY");
        String vector = getIntent().getStringExtra("VECTOR");

        EditText txtKey = findViewById(R.id.txtKey);
        EditText txtVector = findViewById(R.id.txtVector);
        txtKey.setText( key  );
        txtVector.setText(vector.trim());
    }
}