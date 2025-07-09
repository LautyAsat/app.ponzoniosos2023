package edu.unlpam.vet.ponzonosos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class AboutActivity extends AppCompatActivity {

    private static final String TAG = "RMD-AboutActivity";
    TextView tvWebLink;
    ImageView appLogo;
    ImageView vetLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar myToolbar = findViewById(R.id.app_bar);
        //myToolbar.setNavigationIcon(R.drawable.ic_back_white); // si queremos que sea un layout posta entonces descomentenlo
        //setSupportActionBar(myToolbar);
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
                .load(R.drawable.logo_vet_2)
                .apply(options)
                .into(vetLogo);
        tvWebLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBrowserActivity();
            }
        });
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
