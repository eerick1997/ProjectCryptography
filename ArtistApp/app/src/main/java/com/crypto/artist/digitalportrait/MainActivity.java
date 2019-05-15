package com.crypto.artist.digitalportrait;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.crypto.artist.digitalportrait.Orders.Adapter.OrdersAdapter;
import com.crypto.artist.digitalportrait.Orders.Principal.OrdersMain;
import com.crypto.artist.digitalportrait.PhotoEditor.Principal.PhotoEditorMain;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton FAB = findViewById(R.id.fab_edit_image);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrdersMain.class);
                startActivity(intent);
            }
        });
    }
}
