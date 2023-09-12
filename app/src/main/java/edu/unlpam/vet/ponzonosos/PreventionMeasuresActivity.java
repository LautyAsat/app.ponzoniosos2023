package edu.unlpam.vet.ponzonosos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import edu.unlpam.vet.ponzonosos.R;

public class PreventionMeasuresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevention_measures);
        Toolbar myToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        ImageView imageView = findViewById(R.id.main_image);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_image);
        Glide.with(getApplicationContext())
                .load(R.drawable.prevention_measures)
                .apply(options)
                .into(imageView);
    }
}
