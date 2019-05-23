package com.crypto.client.digitalportrait.Orders.Adapter;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.client.digitalportrait.CryptoUtils.Crypto;
import com.crypto.client.digitalportrait.Orders.Objects.Datos;
import com.crypto.client.digitalportrait.Orders.Principal.ShowResult;
import com.crypto.client.digitalportrait.R;

import org.spongycastle.crypto.CipherParameters;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;
import org.spongycastle.util.encoders.Base64;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.KeyGenerator;

public class OrderCompleted extends RecyclerView.Adapter<OrderCompleted.OrderViewHolder> {

    static final String TAG = "OrderCompleted";
    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 128;
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
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {
        holder.txtDate.setText(datos.get(position).getFecha());
        holder.txtDescription.setText(datos.get(position).getEmail());

        holder.btnShowResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
                    keyGenerator.init(KEY_SIZE);
                    final byte[] passAux = Base64.decode(holder.etPassword.getText().toString().getBytes());
                    final KeyGenerator IVGenerator = KeyGenerator.getInstance(ALGORITHM);
                    final byte[] ivAux = Base64.decode(holder.etIV.getText().toString().getBytes());
                    comenzar(position, passAux, ivAux, IVGenerator);
                } catch (NoSuchAlgorithmException e) {
                    Log.e(TAG, "onClick: ", e);
                }
            }
        });
    }

    private void comenzar(int position, byte[] passUse, byte[] ivUse, KeyGenerator IVGenerator) {

        byte[] plainText = Base64.decode(datos.get(position).getImagen().getBytes());
        Crypto crypto = new Crypto(context);

        IVGenerator.init(IV_SIZE);

        CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(passUse), ivUse);

        try {
            final byte[] decryptedMessage = crypto.decrypt(plainText, ivAndKey);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decryptedMessage, 0, decryptedMessage.length);

            byte[] signa = datos.get(position).getSignatureArtist().getBytes();
            byte[] pubK = datos.get(position).getPublicKeyClient().getBytes();

            Log.i("SIG", new String(signa));
            Log.i("PUBK", new String(pubK));

            boolean verify = crypto.verifySign(decodedByte, pubK, signa);
            Log.i("VERIFY", String.valueOf(verify));
            if (verify) {
                Toast.makeText(context, "Firma válida", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, ShowResult.class);
                intent.putExtra("image", decryptedMessage);

                intent.putExtra("documentName", datos.get(position).getDocumentId());
                context.startActivity(intent);
            } else
                Toast.makeText(context, "Firma NO válida", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate, txtDescription;
        EditText etPassword, etIV;
        Button btnShowResult;

        public OrderViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtDescription = itemView.findViewById(R.id.txt_completed_description);
            btnShowResult = itemView.findViewById(R.id.btn_show_result);
            etPassword = itemView.findViewById(R.id.et_password);
            etIV = itemView.findViewById(R.id.et_IV);
        }
    }
}
