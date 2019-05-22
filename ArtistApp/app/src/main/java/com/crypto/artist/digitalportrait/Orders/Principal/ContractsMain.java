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

public class ContractsMain extends BottomSheetDialogFragment {

    private static final String TAG = "ContractsMain";

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.activity_contract_main, container, false);

        final List<Contract> contracts = new ArrayList<>();

        final RecyclerView recyclerOrders = itemView.findViewById(R.id.recycler_contracts);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference datosReference = db.collection(CONTRACTS);
        datosReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                contracts.clear();
                if (e != null) {
                    return;
                }
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Contract contract = documentSnapshot.toObject(Contract.class);
                    contract.setDocumentId(documentSnapshot.getId());
                    contracts.add(new Contract(contract.getPublicKey(), contract.getEmail(), contract.getDate()));
                }

                //Creamos el adaptador
                ContractsAdapter contractsAdapter = new ContractsAdapter(getContext(), contracts);
                //Colocamos el adaptador
                recyclerOrders.setHasFixedSize(true);
                recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                recyclerOrders.setAdapter(contractsAdapter);
            }
        });


        return itemView;
    }

}
