package com.crypto.artist.digitalportrait.PhotoEditor.Principal;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ja.burhanrashid52.photoeditor.PhotoEditor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crypto.artist.digitalportrait.PhotoEditor.Adapter.EmojiAdapter;
import com.crypto.artist.digitalportrait.PhotoEditor.Interface.EmojiFragmentListener;
import com.crypto.artist.digitalportrait.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmojiFragment extends BottomSheetDialogFragment implements EmojiAdapter.EmojiAdapterListener {

    RecyclerView recyclerEmoji;
    static EmojiFragment instance;
    EmojiFragmentListener listener;


    public void setListener(EmojiFragmentListener listener){
        this.listener = listener;
    }

    public static EmojiFragment getInstance(){
        if(instance == null)
            instance = new EmojiFragment();
        return instance;
    }

    public EmojiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemVIew =  inflater.inflate(R.layout.fragment_emoji, container, false);

        recyclerEmoji = itemVIew.findViewById(R.id.recycler_emoji);
        recyclerEmoji.setHasFixedSize(true);
        recyclerEmoji.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        EmojiAdapter adapter = new EmojiAdapter(getContext(), PhotoEditor.getEmojis(getContext()), this);
        recyclerEmoji.setAdapter(adapter);
        return itemVIew;
    }

    @Override
    public void onEmojiItemSelected(String emoji) {
        listener.onEmojiSelected(emoji);
    }
}
