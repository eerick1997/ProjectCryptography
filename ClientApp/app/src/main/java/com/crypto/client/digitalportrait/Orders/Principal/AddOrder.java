package com.crypto.client.digitalportrait.Orders.Principal;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.crypto.client.digitalportrait.CryptoUtils.Crypto;
import com.crypto.client.digitalportrait.Orders.Utils.BitmapUtils;
import com.crypto.client.digitalportrait.Utilities.Preferences;
import com.crypto.client.digitalportrait.Utilities.Reference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.client.digitalportrait.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;
import org.spongycastle.util.encoders.Base64;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.KeyGenerator;

import ja.burhanrashid52.photoeditor.PhotoEditorView;

import static com.crypto.client.digitalportrait.Utilities.Reference.*;

public class AddOrder extends AppCompatActivity {

    public static final int PERMISSION_PICK_IMAGE = 1000;

    private static final String TAG = "AddOrder";
    private PhotoEditorView photoEditorView;
    private Bitmap originalBitmap;
    private String strEmail;
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 128;
    private ProgressDialog progressDialog;

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
                try {
                    sendOrder();
                } catch (Exception e) {
                    Log.e(TAG, "onClick: ", e);
                }
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

    private void sendOrder() throws Exception {
        show();
        final Crypto crypto = new Crypto(AddOrder.this);
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        final byte[] password = keyGenerator.generateKey().getEncoded();
        KeyGenerator IVGenerator = KeyGenerator.getInstance(ALGORITHM);
        IVGenerator.init(IV_SIZE);
        final byte[][] byteArray = {null};
        final byte[] IV = IVGenerator.generateKey().getEncoded();

        CipherParameters IVAndKey = new ParametersWithIV(new KeyParameter(password), IV);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        originalBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byteArray[0] = byteArrayOutputStream.toByteArray();

        byte[] cipherMessage = crypto.encrypt(byteArray[0], IVAndKey);

        Calendar calendar = new GregorianCalendar();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = dateFormat.format(date);

        Map<String, Object> order = new HashMap<>();
        order.put(IMAGE, new String(Base64.encode(cipherMessage)));
        order.put(DATE, format);
        TextView txtDescription = findViewById(R.id.txt_description);
        if (txtDescription.getText().toString().trim().isEmpty()) {
            Toast.makeText(AddOrder.this, getString(R.string.fill_description), Toast.LENGTH_LONG).show();
            return;
        }

        final byte[] decryptedMessage = crypto.decrypt(cipherMessage, IVAndKey);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decryptedMessage, 0, decryptedMessage.length);

        crypto.signGenerator(decodedByte);

        order.put(DESCRIPTION, txtDescription.getText().toString());
        order.put(EMAIL_O, strEmail);
        order.put(PUBLICKEYCLIENT, new String((crypto.getPublicKey())));
        order.put(SIGNATURECLIENT, new String((crypto.getSignature())));
        order.put(STATE, "Enviado");

        final String publicKeyA;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference datosReference = db.collection(ARTISTA);
        datosReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                datos.clear();
                if (e != null)
                    return;

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Datos data = documentSnapshot.toObject(Datos.class);
                    data.setDocumentId(documentSnapshot.getId());
                    if(data.getPublicKeyArtist() != null)
                        publicKeyA = data.getPublicKeyArtist();
                }
            }
        });

        String passencrypt = crypto.RSAEncrypt(crypto.getPrivateKey(),Base64.toBase64String(password));
        String passencrypt2 = crypto.RSAEncrypt(publicKeyA,passencrypt);

        Log.i("SIG", new String(crypto.getSignature()));
        boolean verify = crypto.verifySign(decodedByte, new String(crypto.getPublicKey()).getBytes(), new String(crypto.getSignature()).getBytes());
        Log.i("VERIFY--", String.valueOf(verify));

        FirebaseFirestore DB = FirebaseFirestore.getInstance();
        DB.collection(ORDERS).add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), getString(R.string.order_sent_successfully), Toast.LENGTH_LONG).show();
                        sendKeyByEmail(passencrypt2, IV);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getApplicationContext(), getString(R.string.order_not_sent_successfully), Toast.LENGTH_LONG).show();
                    }
                });
        finish();

    }

    private void openImageFromGallery() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
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
                this.originalBitmap = bitmap;
                photoEditorView.getSource().setImageBitmap(this.originalBitmap);
            }
        }
    }

    private void sendKeyByEmail(String password, byte[] IV) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{strEmail, "vargas.erick030997@gmail.com", "albertoesquivel.97@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Tu llave privada");
        i.putExtra(Intent.EXTRA_TEXT, password + " \n\nvector de inicialización\n\n" + new String(Base64.encode(IV)));
        try {
            startActivity(Intent.createChooser(i, getString(R.string.title_email)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AddOrder.this, getString(R.string.not_services_found), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hide();
    }

    public void show() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    public void hide() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
