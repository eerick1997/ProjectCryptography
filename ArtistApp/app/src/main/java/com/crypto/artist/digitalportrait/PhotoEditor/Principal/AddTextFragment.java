package com.crypto.artist.digitalportrait.PhotoEditor.Principal;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.crypto.artist.digitalportrait.PhotoEditor.Adapter.ColorAdapter;
import com.crypto.artist.digitalportrait.PhotoEditor.Adapter.FontAdapter;
import com.crypto.artist.digitalportrait.PhotoEditor.Interface.AddTextFragmentListener;
import com.crypto.artist.digitalportrait.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.lang.reflect.Type;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTextFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener, FontAdapter.FontAdapterClickListener {

    int colorSelected = Color.parseColor("#000000");
    AddTextFragmentListener listener;

    EditText edtAddText;
    RecyclerView recyclerColor, recyclerFont;
    Button btnAddText;

    Typeface typefaceSelected = Typeface.DEFAULT;

    static AddTextFragment instance;

    static AddTextFragment getInstance(){
        if(instance == null)
            instance = new AddTextFragment();
        return instance;
    }

    public void setListener(AddTextFragmentListener listener){
        this.listener = listener;
    }
    public AddTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_add_text, container, false);
        edtAddText = itemView.findViewById(R.id.edt_add_text);
        btnAddText = itemView.findViewById(R.id.btn_add_text);
        recyclerColor = itemView.findViewById(R.id.recycler_color);
        recyclerColor.setHasFixedSize(true);
        recyclerColor.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        recyclerFont = itemView.findViewById(R.id.recycler_font);
        recyclerFont.setHasFixedSize(true);
        recyclerFont.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        ColorAdapter colorAdapter = new ColorAdapter(getContext(), this);
        recyclerColor.setAdapter(colorAdapter);

        FontAdapter fontAdapter = new FontAdapter(getContext(), this);
        recyclerFont.setAdapter(fontAdapter);
        //Event
        btnAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddTextButtonClick(typefaceSelected, edtAddText.getText().toString(), colorSelected);
            }
        });
        return itemView;
    }

    @Override
    public void onColorSelected(int color) {
        colorSelected = color;
    }

    @Override
    public void onFontSelected(String fontName) {
        typefaceSelected = Typeface.createFromAsset(getContext().getAssets(), new StringBuilder("fonts/")
        .append(fontName).toString());
    }
}
