package edu.unlpam.vet.ponzonosos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;

import com.bumptech.glide.request.RequestOptions;

import edu.unlpam.vet.ponzonosos.databinding.ActivityPreventionMeasuresBinding;
import edu.unlpam.vet.ponzonosos.databinding.ActivityWhatToDoBinding;
import edu.unlpam.vet.ponzonosos.util.Edge;

public class PreventionMeasuresActivity extends AppCompatActivity {


    private ActivityPreventionMeasuresBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPreventionMeasuresBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_image);


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
