package com.crypto.artist.digitalportrait.PhotoEditor.Interface;

public interface EditImageFragmentListener {

    void onBrightnessChanged(int brightness);
    void onConstraintChanged(float constraint);
    void onSaturationChanged(float saturation);
    void onEditStarted();
    void onEditCompleted();
}
