package edu.unlpam.vet.ponzonosos;

import androidx.appcompat.app.AppCompatActivity;
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

        Edge.applyDynamicEdgeAppBar(
                this,
                getWindow().getDecorView(),
                binding.iHeader.getRoot(),
                binding.iHeader.llHeader,
                60f
        );
    }

}
