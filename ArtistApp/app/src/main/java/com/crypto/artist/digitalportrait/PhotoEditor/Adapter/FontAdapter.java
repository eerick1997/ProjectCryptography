package com.crypto.artist.digitalportrait.PhotoEditor.Adapter;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.artist.digitalportrait.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {

    Context context;
    FontAdapterClickListener listener;
    List<String> fontList;
    int rowSelected = -1;
    public FontAdapter(Context context, FontAdapterClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.fontList = loadFontList();
    }

    private List<String> loadFontList(){
        List<String> result = new ArrayList<>();
        result.add("Cheque-Black.otf");
        result.add("Cheque-Regular.otf");
        return result;
    }

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.font_item, parent, false);
        return new FontViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        if(rowSelected == position)
            holder.imgCheck.setVisibility(View.VISIBLE);
        else
            holder.imgCheck.setVisibility(View.INVISIBLE);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), new StringBuilder("fonts/")
        .append(fontList.get(position)).toString());

        holder.txtFontName.setText(fontList.get(position));
        holder.txtFontDemo.setTypeface(typeface);

    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }

    public class FontViewHolder extends RecyclerView.ViewHolder {

        TextView txtFontName, txtFontDemo;
        ImageView imgCheck;

        public FontViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFontDemo = itemView.findViewById(R.id.txt_font_demo);
            txtFontName = itemView.findViewById(R.id.txt_font_name);
            imgCheck = itemView.findViewById(R.id.img_check);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFontSelected(fontList.get(getAdapterPosition()));
                    rowSelected = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }

    }

    public interface FontAdapterClickListener{
        public void onFontSelected(String fontName);
    }
}
