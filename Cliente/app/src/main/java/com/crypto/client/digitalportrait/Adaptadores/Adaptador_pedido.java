package com.crypto.client.digitalportrait.Adaptadores;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crypto.client.digitalportrait.Objetos.Pedido;
import com.crypto.client.digitalportrait.R;

import java.util.ArrayList;

public class Adaptador_pedido extends RecyclerView.Adapter<Adaptador_pedido.PedidoViewHolder>{

    //Constantes
    private static final String TAG = "Adaptador_pedidos.java";
    //Variables
    private ArrayList<Pedido> pedidos;
    private Activity actividad;

    //Constructor de la superclase
    @SuppressLint("LongLogTag")
    public Adaptador_pedido(ArrayList<Pedido> pedidos, Activity actividad){
        this.pedidos = pedidos;
        this.actividad = actividad;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.elemento_lista_pedido, parent, false);
        PedidoViewHolder pedidoViewHolder = new PedidoViewHolder(itemView);
        return pedidoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, final int position) {
        final Pedido pedido = pedidos.get(position);
        holder.asignarInterfaz(pedido);

        //Asignamos el evento onClick a cada elemento del List View
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, pedido.getDescripcion() + " seleccionado ", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class PedidoViewHolder extends RecyclerView.ViewHolder{
        //Constantes
        private static final String TAG = "PedidosViewHolder.java";
        //Variables
        private TextView TXT_nombre;


        public PedidoViewHolder(View view){
            super(view);
            //Obtenemos los elementos de la interfaz
            TXT_nombre = view.findViewById(R.id.pedido_Descripcion);

        }

        public void asignarInterfaz(Pedido pedido){
            TXT_nombre.setText(pedido.getDescripcion());

        }
    }
}
