package com.crypto.artist.digitalportrait.PhotoEditor.Principal;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.crypto.artist.digitalportrait.PhotoEditor.Adapter.FrameAdapter;
import com.crypto.artist.digitalportrait.PhotoEditor.Interface.AddFrameListener;

import com.crypto.artist.digitalportrait.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FrameFragment extends BottomSheetDialogFragment implements FrameAdapter.FrameAdapterListener {

    RecyclerView recyclerFrame;
    Button btnAddFrame;
    int frameSelected = -1;
    AddFrameListener listener;

    static FrameFragment instance;

    public static FrameFragment getInstance(){
        if(instance == null)
            instance = new FrameFragment();
        return instance;
    }

    public void setListener(AddFrameListener listener) {
        this.listener = listener;
    }

    public FrameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_frame, container, false);
        recyclerFrame = itemView.findViewById(R.id.recycler_frame);
        btnAddFrame = itemView.findViewById(R.id.btn_add_frame);
        recyclerFrame.setHasFixedSize(true);
        recyclerFrame.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerFrame.setAdapter(new FrameAdapter(getContext(), this));
        btnAddFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddFrame(frameSelected);
            }
        });
        return itemView;
    }

    @Override
    public void onFrameSelected(int frame) {
        frameSelected = frame;
    }
}
