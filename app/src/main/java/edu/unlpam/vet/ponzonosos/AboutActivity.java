package edu.unlpam.vet.ponzonosos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import edu.unlpam.vet.ponzonosos.databinding.ActivityAboutBinding;
import edu.unlpam.vet.ponzonosos.databinding.ActivityContactUsBinding;
import edu.unlpam.vet.ponzonosos.util.Edge;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "RMD-AboutActivity";
    TextView tvWebLink;
    ImageView appLogo;
    ImageView vetLogo;
    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = edu.unlpam.vet.ponzonosos.databinding.ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar myToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);

        //Enable EdgeToEdge
        Window window = getWindow();
        ViewCompat.setOnApplyWindowInsetsListener(window.getDecorView(), (v, insets) -> insets);
        WindowCompat.setDecorFitsSystemWindows(window, false);

        Edge.applyDynamicEdgeAppBar(
                this,
                getWindow(),
                binding.iHeader.getRoot(),
                binding.iHeader.llHeader,
                60f
        );

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        tvWebLink = findViewById(R.id.web_link);
        appLogo = findViewById(R.id.app_logo);
        vetLogo = findViewById(R.id.vet_logo);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_image);
        Glide.with(getApplicationContext())
                .load(R.drawable.ic_ponzonioso)
                .apply(options)
                .into(appLogo);
        Glide.with(getApplicationContext())
                .load(R.drawable.unlpam_logo)
                .apply(options)
                .into(vetLogo);
        tvWebLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBrowserActivity();
            }
        });

        //Listener del boton de more
        
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
    private void goBrowserActivity() {
        try {
            String url = "http://actosresolutivos.unlpam.edu.ar/static_ecs/media/uploads/" +
                    "pdf/8_4_2017_316.pdf";
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
