package edu.unlpam.vet.ponzonosos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.request.RequestOptions;

public class PreventionMeasuresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevention_measures);
        Toolbar myToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_image);
    }
}
