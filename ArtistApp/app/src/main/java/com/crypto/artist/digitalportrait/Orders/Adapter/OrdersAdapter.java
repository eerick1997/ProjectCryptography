package com.crypto.artist.digitalportrait.Orders.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.artist.digitalportrait.Orders.Objects.Order;
import com.crypto.artist.digitalportrait.PhotoEditor.Principal.PhotoEditorMain;
import com.crypto.artist.digitalportrait.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    static final String TAG = "OrdersAdapter";
    Context context;
    List<Order> orders;

    public OrdersAdapter(Context context, List<Order> orders) {
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
        holder.txtStatus.setText(orders.get(position).getStatus());

        if(holder.txtStatus.getText().equals("Accepted"))
            holder.btnOrderAction.setChecked(true);
        else
            acceptOrder();
    }

    private void acceptOrder() {
        Toast.makeText(context, "Order accepted", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtStatus;
        ToggleButton btnOrderAction;
        public OrderViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtStatus = itemView.findViewById(R.id.txt_status);
            btnOrderAction = itemView.findViewById(R.id.btn_order_action);

            btnOrderAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!btnOrderAction.isChecked()) {
                        btnOrderAction.setChecked(true);
                        Intent intent = new Intent(context, PhotoEditorMain.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
