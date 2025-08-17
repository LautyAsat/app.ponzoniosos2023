package edu.unlpam.vet.ponzonosos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import edu.unlpam.vet.ponzonosos.R;
import edu.unlpam.vet.ponzonosos.databinding.ActivityWhatToDoBinding;
import edu.unlpam.vet.ponzonosos.util.Edge;
import edu.unlpam.vet.ponzonosos.util.Measures;

import android.view.Window;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class WhatToDoActivity extends AppCompatActivity {

    private ActivityWhatToDoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWhatToDoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);

        //Enable EdgeToEdge
        Window window = getWindow();
        ViewCompat.setOnApplyWindowInsetsListener(window.getDecorView(), (v, insets) -> insets);
        WindowCompat.setDecorFitsSystemWindows(window, false);

        View root = binding.getRoot();
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            int navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(),
                    v.getPaddingRight(), navBarHeight);
            return insets;
        });

        Edge.applyDynamicEdgeAppBar(
                this,
                getWindow(),
                binding.iHeader.getRoot(),
                binding.iHeader.llHeader,
                60f
        );

        //boton de more
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(this, view);
            popup.getMenuInflater().inflate(R.menu.menu_show_catalog, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.about){
                    navigateToAbout();
                    return true;
                } else if (item.getItemId() == R.id.contact) {
                    navigateToContact();
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    private void  navigateToAbout(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void navigateToContact(){
        Intent intent = new Intent(this, ContactUsActivity.class);
        startActivity(intent);
    }

}
