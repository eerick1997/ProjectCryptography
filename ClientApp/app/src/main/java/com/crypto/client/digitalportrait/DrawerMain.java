package com.crypto.client.digitalportrait;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import com.crypto.client.digitalportrait.Orders.Principal.OrdersMain;

import static com.crypto.artist.digitalportrait.Utilities.Reference.EMAIL;
import static com.crypto.artist.digitalportrait.Utilities.Reference.IMG_PROFILE;
import static com.crypto.artist.digitalportrait.Utilities.Reference.USER_NAME;

public class DrawerMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DrawerMain";

    private int lastId = R.id.nav_ordered;
    private static Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);

        TextView userName = view.findViewById(R.id.txt_user_name);
        userName.setText(getIntent().getStringExtra(USER_NAME));
        TextView email = view.findViewById(R.id.txt_email);
        email.setText(getIntent().getStringExtra(EMAIL));
        CircleImageView userProfileImage = view.findViewById(R.id.img_user_profile_image);
        Glide.with(DrawerMain.this)
                .load(getIntent().getStringExtra(IMG_PROFILE))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.ic_person_black_24dp)
                .override(270, 270)
                .centerCrop()
                .into(userProfileImage);

        getSupportActionBar().setTitle(R.string.title_orders);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new OrdersMain()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_ordered && lastId != R.id.nav_ordered) {
            getSupportActionBar().setTitle(R.string.title_orders);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new OrdersMain()).commit();
            lastId = id;
        } else if (id == R.id.nav_sent && lastId != R.id.nav_sent) {
            getSupportActionBar().setTitle(R.string.title_sent);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new OrdersMain()).commit();
            lastId = id;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
