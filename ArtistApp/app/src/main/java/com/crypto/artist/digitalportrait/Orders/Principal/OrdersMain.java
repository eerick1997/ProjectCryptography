package com.crypto.artist.digitalportrait.Orders.Principal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crypto.artist.digitalportrait.Orders.Adapter.OrdersAdapter;
import com.crypto.artist.digitalportrait.Orders.Objects.Order;
import com.crypto.artist.digitalportrait.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class OrdersMain extends BottomSheetDialogFragment {

    private static final String TAG = "OrdersMain";

    @SuppressLint("WrongConstant")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.activity_orders_main, container, false);

        List<Order> orders = new ArrayList<>();
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

        RecyclerView recyclerOrders = itemView.findViewById(R.id.recycler_orders);
        OrdersAdapter ordersAdapter = new OrdersAdapter(getContext(), orders);

        recyclerOrders.setHasFixedSize(true);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerOrders.setAdapter(ordersAdapter);

        return  itemView;
    }



}
