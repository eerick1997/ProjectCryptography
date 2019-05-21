package com.crypto.client.digitalportrait;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crypto.client.digitalportrait.Adaptadores.Adaptador_pedido;
import com.crypto.client.digitalportrait.Objetos.Pedido;
import com.crypto.client.digitalportrait.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class Fragment_pedidos extends BottomSheetDialogFragment {
    private View pedidosView;
    private RecyclerView myPedidosList;
    public Fragment_pedidos()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        pedidosView = inflater.inflate(R.layout.fragment_fragment_pedidos,container,false);
        myPedidosList=(RecyclerView)pedidosView.findViewById(R.id.lista_pedidos_RV);
        myPedidosList.setLayoutManager(new LinearLayoutManager(getContext()));
        return pedidosView;
    }

    private void getPedidos(){
        Log.d("PEDIDOS", "getPedidos() called");
        final ArrayList<Pedido> pedidos = new ArrayList<>();
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        CollectionReference datosReference=db.collection("usuarios").document("clientes").collection("cliente1");
        datosReference.addSnapshotListener((Executor) this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                pedidos.clear();
                if(e!=null){
                    return;
                }
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    Datos datos = documentSnapshot.toObject(Datos.class);
                    datos.setDocumentId(documentSnapshot.getId());

                    pedidos.add(new Pedido(datos.getDescripcion(),datos.getFecha(),datos.getImagen()));
                }
                Log.d("PEDIDOS", "onEvent: pedidos = " + pedidos);
                //Creamos el adaptador
                Adaptador_pedido adaptador_pedido = new Adaptador_pedido(pedidos,getActivity());
                myPedidosList.setAdapter(adaptador_pedido);

            }
        });
    }
}