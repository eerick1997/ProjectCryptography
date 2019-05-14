package com.crypto.artist.digitalportrait.PhotoEditor.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.artist.digitalportrait.R;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    Context context;
    List<Integer> colorList;
    ColorAdapterListener listener;

    public ColorAdapter(Context context, ColorAdapterListener listener) {
        this.context = context;
        this.colorList = genColorList();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.color_item, parent, false);
        return new ColorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        holder.colorSection.setCardBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder{

        public CardView colorSection;


        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            colorSection = itemView.findViewById(R.id.color_section);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onColorSelected(colorList.get(getAdapterPosition()));
                }
            });
        }
    }

    private List<Integer> genColorList(){
        List<Integer> colorList = new ArrayList<>();
        colorList.add(Color.parseColor("#5359af"));
        colorList.add(Color.parseColor("#ffccd5"));
        colorList.add(Color.parseColor("#896d66"));
        colorList.add(Color.parseColor("#b819f0"));
        colorList.add(Color.parseColor("#a10000"));
        colorList.add(Color.parseColor("#a15000"));
        colorList.add(Color.parseColor("#1f3f3c"));
        colorList.add(Color.parseColor("#416600"));
        colorList.add(Color.parseColor("#121c25"));
        colorList.add(Color.parseColor("#e6f6ff"));
        return  colorList;
    }

    public interface ColorAdapterListener{
        void onColorSelected(int color);

    }
}
