/*package com.crypto.client.digitalportrait;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crypto.client.digitalportrait.Adaptadores.Adaptador_pedido;
import com.crypto.client.digitalportrait.Objetos.Pedido;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Pedidos extends Fragment {
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      getPedidos();
        return inflater.inflate(R.layout.fragment_pedidos, container, false);
    }

    private void getPedidos(){

        final ArrayList<Pedido> pedidos = new ArrayList<>();
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        CollectionReference datosReference=db.collection("peditriage").document("usuarios").collection("pacientes");
        datosReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                pedidos.clear();
                if(e!=null){
                    return;
                }
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    Datos datos = documentSnapshot.toObject(Datos.class);
                    datos.setDocumentId(documentSnapshot.getId());

                    pedidos.add(new Pedido(datos.getDescripcion(),datos.getFecha(),datos.getImagen());
                }

                //Creamos el adaptador
                Adaptador_pedido adaptador_pedido = new Adaptador_pedido(pedidos,
                        Lista_pedidos.this);
                //Colocamos el adaptador
                recyclerView.setAdapter(adaptador_pedido);
                recyclerView.setLayoutManager(new LinearLayoutManager(Lista_pedidos.this, LinearLayoutManager.VERTICAL, false));
            }
        });
    }


}
*/