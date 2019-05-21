package com.crypto.client.digitalportrait;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.crypto.client.digitalportrait.Adaptadores.Adaptador_pedido;
import com.crypto.client.digitalportrait.Objetos.Pedido;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Lista_pedidos extends AppCompatActivity {

    //Constantes
    private static final String TAG = "Lista_pedidos.java";
    //Variables
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pedidos);

        recyclerView = findViewById(R.id.lista_pedidos_RV);
        getPedidos();

    }



    private void getPedidos(){
        Log.d(TAG, "getPedidos() called");
        final ArrayList<Pedido> pedidos = new ArrayList<>();
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        CollectionReference datosReference=db.collection("usuarios").document("clientes").collection("cliente1");
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

                    pedidos.add(new Pedido(datos.getDescripcion(),datos.getFecha(),datos.getImagen()));
                }
                Log.d(TAG, "onEvent: pedidos = " + pedidos);
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
