package com.crypto.client.digitalportrait.Orders.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.crypto.client.digitalportrait.Orders.Objects.Datos;
import com.crypto.client.digitalportrait.R;

import java.util.List;

public class OrderCompleted extends RecyclerView.Adapter<OrderCompleted.OrderViewHolder> {

    static final String TAG = "OrderCompleted";
    Context context;
    List<Datos> datos;

    public OrderCompleted(Context context, List<Datos> datos) {
        this.context = context;
        this.datos = datos;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.completed_order_item, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, final int position) {
        holder.txtDate.setText(datos.get(position).getFecha());
        holder.txtDescription.setText(datos.get(position).getEmail());

        holder.btnShowResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show image
            }
        });
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtDescription;
        Button btnShowResult;

        public OrderViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtDescription = itemView.findViewById(R.id.txt_completed_description);
            btnShowResult = itemView.findViewById(R.id.btn_show_result);
        }
    }
}
