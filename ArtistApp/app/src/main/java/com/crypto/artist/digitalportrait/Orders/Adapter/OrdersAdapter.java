package com.crypto.artist.digitalportrait.Orders.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.artist.digitalportrait.Orders.Objects.Order;
import com.crypto.artist.digitalportrait.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    Context context;
    //OrderAdapterClickListener listerner;
    List<Order> orders;
    int rowSelected = -1;

    public OrdersAdapter(Context context, List<Order> orders/*, OrderAdapterClickListener listerner*/) {
        this.context = context;
        this.orders = orders;
        //this.listerner = listerner;
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

    public class OrderViewHolder extends RecyclerView.ViewHolder{

        TextView txtDate, txtStatus;
        MaterialButton btnSend, btnEdit;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtStatus = itemView.findViewById(R.id.txt_status);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnSend = itemView.findViewById(R.id.btn_send);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public interface OrderAdapterClickListener{
        public void onOrderSelected(Order order);
    }
}
