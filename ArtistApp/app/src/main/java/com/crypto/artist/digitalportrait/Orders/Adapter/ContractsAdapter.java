package com.crypto.artist.digitalportrait.Orders.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.crypto.artist.digitalportrait.Orders.Objects.Contract;
import com.crypto.artist.digitalportrait.R;

import java.util.List;

public class ContractsAdapter extends RecyclerView.Adapter<ContractsAdapter.OrderViewHolder> {

    static final String TAG = "contractsAdapter";
    String plain = "";
    Context context;
    List<Contract> contracts;

    public ContractsAdapter(Context context, List<Contract> contracts) {
        this.context = context;
        this.contracts = contracts;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.contract_item, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, final int position) {
        holder.txtDate.setText(contracts.get(position).getDate());
        holder.txtStatus.setText(contracts.get(position).getEmail());

        holder.btnAcceptContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptContract();
            }
        });
    }

    private void acceptContract() {
        Toast.makeText(context, "Contract accepted", Toast.LENGTH_LONG).show();
        //Get the pbk
        //Send the pbk to client
        //Generate common key
    }

    @Override
    public int getItemCount() {
        return contracts.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtStatus;
        Button btnAcceptContract;
        public OrderViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtStatus = itemView.findViewById(R.id.txt_status);
            btnAcceptContract = itemView.findViewById(R.id.btn_accept_contract);
        }
    }
}
