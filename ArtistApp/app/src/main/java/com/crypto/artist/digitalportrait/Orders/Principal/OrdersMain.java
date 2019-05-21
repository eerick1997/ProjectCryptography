package com.crypto.artist.digitalportrait.Orders.Principal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crypto.artist.digitalportrait.CryptoUtils.Crypto;
import com.crypto.artist.digitalportrait.DrawerMain;
import com.crypto.artist.digitalportrait.Orders.Adapter.OrdersAdapter;
import com.crypto.artist.digitalportrait.Orders.Objects.Datos;
import com.crypto.artist.digitalportrait.Orders.Objects.Order;
import com.crypto.artist.digitalportrait.PhotoEditor.Utils.BitmapUtils;
import com.crypto.artist.digitalportrait.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.crypto.artist.digitalportrait.TestActivity.PERMISSION_PICK_IMAGE;
import static com.crypto.artist.digitalportrait.Utilities.Reference.*;
import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;
import org.spongycastle.util.encoders.Base64;

import java.util.Map;
import java.util.concurrent.Executor;

import javax.crypto.KeyGenerator;

public class OrdersMain extends BottomSheetDialogFragment {

    private static final String TAG = "OrdersMain";
    private static final int SELECT_FILE = 1;
    Bitmap originalBitmap, filteredBitmap, finalBitmap;
    Uri imageSelectedUri;
    private Bitmap bitmap;
    public static final String pictureName = "meme2.png";

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.activity_orders_main, container, false);

        final List<Order> orders = new ArrayList<>();


        final RecyclerView recyclerOrders = itemView.findViewById(R.id.recycler_orders);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Seleccione una imagen"),
                SELECT_FILE);

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        CollectionReference datosReference=db.collection("pedidos");
        datosReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                orders.clear();
                if(e!=null){
                    return;
                }
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    Datos datos = documentSnapshot.toObject(Datos.class);
                    datos.setDocumentId(documentSnapshot.getId());
                    orders.add(new Order(datos.getDescripcion(),datos.getFecha()));
                }

                //Creamos el adaptador
                OrdersAdapter ordersAdapter = new OrdersAdapter(getContext(), orders);
                //Colocamos el adaptador
                recyclerOrders.setHasFixedSize(true);
                recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                recyclerOrders.setAdapter(ordersAdapter);
            }
        });


        return  itemView;
    }
    private void loadImage(Bitmap bmp) {

        Crypto crypto = new Crypto(getContext());

        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");

            keyGen.init(256); //key is 256 bits
            byte[] password = keyGen.generateKey().getEncoded();

            KeyGenerator ivGen = KeyGenerator.getInstance("AES");
            ivGen.init(128); //iv is 128 bits

            final byte[][] byteArray = {null};


            byte[] iv = ivGen.generateKey().getEncoded();


            CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(password), iv);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            byteArray[0] = stream.toByteArray();

            //CAMBIOS
            Log.i("BA",byteArray[0].toString());

            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray[0], 0, byteArray[0].length);
            byte[] encryptedMessage = crypto.encrypt(byteArray[0],ivAndKey);

            Log.i("Encrypt:", String.valueOf(encryptedMessage));





            //FECHA
            Calendar cal = new GregorianCalendar();
            Date date = cal.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formatteDate = df.format(date);



            //FIRESTORE


            Map<String, Object> pedido = new HashMap<>();
            pedido.put("imagen", new String(Base64.encode(encryptedMessage)));
            pedido.put("fecha", formatteDate );
            pedido.put("descripcion","Prueba de correo");
            pedido.put("email",getActivity().getIntent().getStringExtra(EMAIL));
            pedido.put("sin",new String(Base64.encode(byteArray[0])));

            FirebaseFirestore db=FirebaseFirestore.getInstance();

            db.collection("pedidos")
                    .add(pedido)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });





        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {
            if (requestCode == PERMISSION_PICK_IMAGE) {
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(getContext(), data.getData(), 800, 800);
                imageSelectedUri = data.getData();
                Log.i(TAG, "onActivityResult: " + imageSelectedUri);
                this.bitmap = bitmap;
                loadImage(bitmap);
            }
        }



    }




}
