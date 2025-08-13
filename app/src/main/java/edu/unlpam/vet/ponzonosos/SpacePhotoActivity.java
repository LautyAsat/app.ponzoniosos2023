package edu.unlpam.vet.ponzonosos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class SpacePhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_photo);

        String url = getIntent().getStringExtra("photo");
        ImageView image = findViewById(R.id.image);

        Glide.with(this)
                .load(url)
                .into(image);
    }



    }
