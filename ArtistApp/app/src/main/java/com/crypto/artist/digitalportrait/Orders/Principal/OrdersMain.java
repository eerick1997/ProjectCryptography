package com.crypto.artist.digitalportrait.Orders.Principal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.crypto.artist.digitalportrait.Orders.Adapter.OrdersAdapter;
import com.crypto.artist.digitalportrait.Orders.Objects.Order;
import com.crypto.artist.digitalportrait.R;

import java.util.ArrayList;
import java.util.List;

public class OrdersMain extends AppCompatActivity {

    private static final String TAG = "OrdersMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_main);
        List<Order> orders = new ArrayList<>();
        orders.add( new Order("Editing00", "15\\01\\2019") );
        orders.add( new Order("Editing01", "15\\02\\2019") );
        orders.add( new Order("Editing02", "15\\03\\2019") );
        orders.add( new Order("Editing03", "15\\04\\2019") );
        orders.add( new Order("Editing04", "15\\05\\2019") );
        orders.add( new Order("Editing05", "15\\06\\2019") );
        orders.add( new Order("Editing06", "15\\07\\2019") );
        orders.add( new Order("Editing07", "15\\08\\2019") );
        orders.add( new Order("Editing08", "15\\09\\2019") );
        orders.add( new Order("Editing09", "15\\10\\2019") );

        RecyclerView recyclerOrders = findViewById(R.id.recycler_orders);
        OrdersAdapter ordersAdapter = new OrdersAdapter(OrdersMain.this, orders);
        recyclerOrders.setAdapter(ordersAdapter);
    }


}
