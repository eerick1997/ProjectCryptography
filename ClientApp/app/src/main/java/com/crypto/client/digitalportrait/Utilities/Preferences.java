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


    private String bytesToString(byte[] bytes){
        if (bytes == null){
            return null;
        }
        if (bytes.length == 0)
            return "";

        StringBuffer sb = new StringBuffer(bytes.length);
        String str;
        for (int i = 0; i < bytes.length; i++) {
            str = Integer.toHexString(0xFF & bytes[i]);
            if (str.length() < 2)
                sb.append(0);
            sb.append(str.toUpperCase());
        }
        return sb.toString();
    }

    public byte[] stringToBytes(String data){
        String hexString=data.toUpperCase().trim();
        if (hexString.length()%2!=0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for(int i=0;i<hexString.length();i++)
        {
            int int_ch;
            char hex_char1 = hexString.charAt(i);
            int int_ch1;
            if(hex_char1 >= '0' && hex_char1 <='9')
                int_ch1 = (hex_char1-48)*16;
            else if(hex_char1 >= 'A' && hex_char1 <='F')
                int_ch1 = (hex_char1-55)*16;
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i);
            int int_ch2;
            if(hex_char2 >= '0' && hex_char2 <='9')
                int_ch2 = (hex_char2-48);
            else if(hex_char2 >= 'A' && hex_char2 <='F')
                int_ch2 = hex_char2-55;
            else
                return null;
            int_ch = int_ch1+int_ch2;
            retData[i/2]=(byte) int_ch;//The converted number into Byte
        }
        return retData;
    }

    public void store(@NonNull String key, @NonNull byte[] data){
        try {
            sharedPreferences = context.getSharedPreferences(NAME_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, bytesToString(data));
            editor.apply();
        } catch (Exception e){
            Log.e(TAG, "save: ", e);
        }
    }

    public byte[] get(@NonNull String key){
        try {
            sharedPreferences = context.getSharedPreferences(NAME_PREFERENCES, Context.MODE_PRIVATE);
            String str = sharedPreferences.getString(key, null);
            return stringToBytes(str);
        } catch (Exception e){
            Log.e(TAG, "get: ", e);
        }
        return null;
    }
}
