package com.crypto.artist.digitalportrait.Orders.Principal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crypto.artist.digitalportrait.Orders.Adapter.*;
import com.crypto.artist.digitalportrait.Orders.Objects.Contract;
import com.crypto.artist.digitalportrait.Orders.Objects.Datos;
import com.crypto.artist.digitalportrait.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import static com.crypto.artist.digitalportrait.Utilities.Reference.*;

public class SentMain extends BottomSheetDialogFragment {

    private static final String TAG = "SentMain";

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.activity_sent_main, container, false);

        final List<Datos> enviados = new ArrayList<>();

        final RecyclerView recyclerSents = itemView.findViewById(R.id.recycler_sents);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference datosReference=db.collection("pedidos");
        datosReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                enviados.clear();
                if(e!=null){
                    return;
                }
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    Datos datos = documentSnapshot.toObject(Datos.class);
                    datos.setDocumentId(documentSnapshot.getId());
                    if(datos.getEstado().equals("Terminado"))
                        enviados.add(new Datos(datos.getDescripcion(),datos.getFecha(),datos.getImagen(),datos.getEmail(),datos.getSin(),datos.getKeyAndIV(),datos.getPassword(),datos.getIv(),datos.getSignature(),datos.getPublicKeyClient(),datos.getDocumentId()));
                }

                //Creamos el adaptador
                SentAdapter sentAdapter = new SentAdapter(getContext(), enviados);
                //Colocamos el adaptador
                recyclerSents.setHasFixedSize(true);
                recyclerSents.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                recyclerSents.setAdapter(sentAdapter);
            }
        });


        return itemView;
    }

}
