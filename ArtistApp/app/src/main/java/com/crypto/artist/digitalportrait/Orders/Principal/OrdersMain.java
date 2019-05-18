package com.crypto.artist.digitalportrait.Orders.Principal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crypto.artist.digitalportrait.Orders.Adapter.OrdersAdapter;
import com.crypto.artist.digitalportrait.Orders.Objects.Datos;
import com.crypto.artist.digitalportrait.Orders.Objects.Order;
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
import java.util.concurrent.Executor;

public class OrdersMain extends BottomSheetDialogFragment {

    private static final String TAG = "OrdersMain";

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.activity_orders_main, container, false);

        final List<Order> orders = new ArrayList<>();
        orders.add( new Order("Accepted", "15\\01\\2019") );
        orders.add( new Order("Waiting", "15\\02\\2019") );
        orders.add( new Order("Accepted", "15\\03\\2019") );
        orders.add( new Order("Waiting", "15\\04\\2019") );
        orders.add( new Order("Accepted", "15\\05\\2019") );
        orders.add( new Order("Waiting", "15\\06\\2019") );
        orders.add( new Order("Accepted", "15\\07\\2019") );
        orders.add( new Order("Waiting", "15\\08\\2019") );
        orders.add( new Order("Accepted", "15\\09\\2019") );
        orders.add( new Order("Waiting", "15\\10\\2019") );

        final RecyclerView recyclerOrders = itemView.findViewById(R.id.recycler_orders);



        FirebaseFirestore db= FirebaseFirestore.getInstance();
        CollectionReference datosReference=db.collection("usuarios").document("clientes").collection("cliente1");
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
                Log.d(TAG, "onEvent: pedidos = " + orders);
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



}
