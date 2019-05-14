package com.crypto.artist.digitalportrait.PhotoEditor.Principal;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.crypto.artist.digitalportrait.PhotoEditor.Interface.EditImageFragmentListener;
import com.crypto.artist.digitalportrait.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditImageFragment extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener {

    private EditImageFragmentListener listener;
    SeekBar seekbarBrightness, seekbarConstrant, seekbarSaturation;

    static EditImageFragment instance;

    public static EditImageFragment getInstance(){
        if(instance == null)
            instance = new EditImageFragment();
        return instance;
    }

    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }

    public EditImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_edit_image, container, false);
        seekbarBrightness = itemView.findViewById(R.id.seekbar_brightness);
        seekbarConstrant = itemView.findViewById(R.id.seekbar_constrant);
        seekbarSaturation = itemView.findViewById(R.id.seekbar_saturation);

        seekbarBrightness.setMax(200);
        seekbarBrightness.setProgress(100);

        seekbarConstrant.setMax(20);
        seekbarConstrant.setProgress(0);

        seekbarSaturation.setMax(30);
        seekbarSaturation.setProgress(10);

        seekbarBrightness.setOnSeekBarChangeListener(this);
        seekbarConstrant.setOnSeekBarChangeListener(this);
        seekbarSaturation.setOnSeekBarChangeListener(this);

        return itemView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(listener != null){
            if(seekBar.getId() == R.id.seekbar_brightness){
                listener.onBrightnessChanged(progress - 100);
            } else if(seekBar.getId() == R.id.seekbar_constrant){
                progress += 10;
                float value = 0.10f * progress;
                listener.onConstraintChanged( value );
            } else if(seekBar.getId() == R.id.seekbar_saturation){
                float value = 0.10f * progress;
                listener.onConstraintChanged( value );
            }
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(listener != null)
            listener.onEditStarted();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(listener != null)
            listener.onEditCompleted();
    }

    public void resetControls(){
        seekbarBrightness.setProgress(100);
        seekbarConstrant.setProgress(0);
        seekbarSaturation.setProgress(0);

    }
}
