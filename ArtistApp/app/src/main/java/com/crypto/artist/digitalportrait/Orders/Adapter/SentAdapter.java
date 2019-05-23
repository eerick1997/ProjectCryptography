package com.crypto.artist.digitalportrait.Orders.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.artist.digitalportrait.CryptoUtils.Crypto;
import com.crypto.artist.digitalportrait.Orders.Objects.Datos;
import com.crypto.artist.digitalportrait.Orders.Objects.Order;
import com.crypto.artist.digitalportrait.PhotoEditor.Principal.PhotoEditorMain;
import com.crypto.artist.digitalportrait.R;
import com.crypto.artist.digitalportrait.Utilities.Preferences;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;
import org.spongycastle.util.encoders.Base64;

import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.KeyGenerator;

public class SentAdapter extends RecyclerView.Adapter<SentAdapter.SentViewHolder> {


    String plain="";
    Context context;
    List<Datos> sents;

    public SentAdapter(Context context, List<Datos> sents) {
        this.context = context;
        this.sents = sents;
    }

    @NonNull
    @Override
    public SentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.sent_item, parent, false);
        return new SentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SentViewHolder holder, int position) {
            holder.txtDate.setText(sents.get(position).getFecha());
            holder.txtStatus.setText(sents.get(position).getDescripcion());
    }

    private void acceptOrder() {
        Toast.makeText(context, "Order accepted", Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return sents.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtStatus;

        public SentViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtStatus = itemView.findViewById(R.id.txt_status);


        }
    }
}
