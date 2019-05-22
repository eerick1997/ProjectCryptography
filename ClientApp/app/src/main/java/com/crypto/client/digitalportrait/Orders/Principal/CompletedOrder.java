package com.crypto.client.digitalportrait.Orders.Principal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crypto.client.digitalportrait.Orders.Adapter.OrderCompleted;
import com.crypto.client.digitalportrait.Orders.Objects.Datos;
import com.crypto.client.digitalportrait.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.crypto.client.digitalportrait.Utilities.Reference.*;

public class CompletedOrder extends BottomSheetDialogFragment {

    private static final String TAG = "OrdersMain";
    private String strEmail;
    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View itemView = inflater.inflate(R.layout.activity_completed_orders_main, container, false);
        final List<Datos> datos = new ArrayList<>();
        final RecyclerView recyclerCompleted = itemView.findViewById(R.id.recycler_completed_orders);

        strEmail = this.getArguments().getString(EMAIL, getString(R.string.nav_header_subtitle));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference datosReference = db.collection(ORDERS);
        datosReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                datos.clear();
                if(e!=null)
                    return;

                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    Datos data = documentSnapshot.toObject(Datos.class);
                    data.setDocumentId(documentSnapshot.getId());
                    datos.add(new Datos(data.getDescripcion(), data.getFecha(), data.getImagen(), data.getEmail()));
                }

                //Creamos el adaptador
                OrderCompleted orderCompleted = new OrderCompleted(getContext(), datos);
                //Colocamos el adaptador
                recyclerCompleted.setHasFixedSize(true);
                recyclerCompleted.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                recyclerCompleted.setAdapter(orderCompleted);

            }
        });

        return  itemView;
    }

}
