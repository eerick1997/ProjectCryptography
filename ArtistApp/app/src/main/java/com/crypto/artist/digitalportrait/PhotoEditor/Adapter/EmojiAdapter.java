package com.crypto.artist.digitalportrait.PhotoEditor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crypto.artist.digitalportrait.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.github.rockerhieu.emojicon.EmojiconTextView;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {

    Context context;
    List<String> emojisList;
    EmojiAdapterListener listener;

    public EmojiAdapter(Context context, List<String> emojisList, EmojiAdapterListener listener) {
        this.context = context;
        this.emojisList = emojisList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.emoji_item, parent, false);
        return new EmojiViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        holder.emojiTextView.setText(emojisList.get(position));
    }

    @Override
    public int getItemCount() {
        return emojisList.size();
    }

    public class EmojiViewHolder extends RecyclerView.ViewHolder{

        EmojiconTextView emojiTextView;
        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            emojiTextView = itemView.findViewById(R.id.emoji_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEmojiItemSelected(emojisList.get(getAdapterPosition()));
                }
            });
        }
    }



    public interface EmojiAdapterListener{
        void onEmojiItemSelected(String emoji);
    }
}
