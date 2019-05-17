package com.crypto.artist.digitalportrait.Orders.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.artist.digitalportrait.Orders.Objects.Order;
import com.crypto.artist.digitalportrait.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    static final String TAG = "OrdersAdapter";
    Context context;
    List<Order> orders;

    public OrdersAdapter(Context context, List<Order> orders) {
        Log.i(TAG, "OrdersAdapter: " + orders.size());
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.txtDate.setText(orders.get(position).getDate());
        holder.txtStatus.setText(orders.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtStatus;
        ToggleButton btnOrderAction;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtStatus = itemView.findViewById(R.id.txt_status);
            btnOrderAction = itemView.findViewById(R.id.btn_order_action);

            btnOrderAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, txtStatus.getText(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
}
