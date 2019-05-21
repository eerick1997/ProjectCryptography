package com.crypto.artist.digitalportrait.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static com.crypto.artist.digitalportrait.Utilities.Reference.NAME_PREFERENCES;

import androidx.annotation.NonNull;

public class Preferences {

    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String TAG = "Preferences";

    public Preferences(Context context) {
        this.context = context;
    }


    public void store(@NonNull String key, @NonNull String data){
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, data);
            editor.apply();
        } catch (Exception e){
            Log.e(TAG, "save: ", e);
        }
    }

    public byte[] get(@NonNull String key){
        try {
            sharedPreferences = context.getSharedPreferences(NAME_PREFERENCES, Context.MODE_PRIVATE);
            String str = sharedPreferences.getString(key, null);
            if(str != null){
                String[] split = str.substring(1, str.length() - 1).split(", ");
                byte[] array = new byte[split.length];
                for (int i = 0; i < split.length; i++){
                    array[i] = Byte.parseByte(split[i]);
                }
                return array;
            }
        } catch (Exception e){
            Log.e(TAG, "get: ", e);
        }
        return null;
    }
}
