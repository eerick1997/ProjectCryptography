package com.crypto.client.digitalportrait.Orders.Principal;

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

import com.crypto.client.digitalportrait.CryptoUtils.Crypto;
import com.crypto.client.digitalportrait.Orders.Adapter.OrdersAdapter;
import com.crypto.client.digitalportrait.Orders.Adapter.PendingOrderAdapter;
import com.crypto.client.digitalportrait.Orders.Objects.Contract;
import com.crypto.client.digitalportrait.Orders.Objects.Datos;
import com.crypto.client.digitalportrait.Orders.Objects.Order;
import com.crypto.client.digitalportrait.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.List;

import static com.crypto.client.digitalportrait.Utilities.Reference.*;

public class PendingOrder extends BottomSheetDialogFragment {

    private static final String TAG = "OrdersMain";
    private String strEmail;
    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View itemView = inflater.inflate(R.layout.activity_pending_orders_main, container, false);
        final List<Contract> contracts = new ArrayList<>();
        final RecyclerView recyclerPending = itemView.findViewById(R.id.recycler_pending_orders);

        strEmail = this.getArguments().getString(EMAIL, getString(R.string.nav_header_subtitle));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference datosReference = db.collection(CONTRACTS);
        datosReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                contracts.clear();
                if(e!=null)
                    return;

                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    Contract contract = documentSnapshot.toObject(Contract.class);
                    contract.setDocumentId(documentSnapshot.getId());
                    contracts.add(new Contract(contract.getPublicKey(), contract.getEmail(), contract.getDate()));
                }

                //Creamos el adaptador
                PendingOrderAdapter pendingOrderAdapter = new PendingOrderAdapter(getContext(), contracts);
                //Colocamos el adaptador
                recyclerPending.setHasFixedSize(true);
                recyclerPending.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                recyclerPending.setAdapter(pendingOrderAdapter);

            }
        });

        return  itemView;
    }

}
