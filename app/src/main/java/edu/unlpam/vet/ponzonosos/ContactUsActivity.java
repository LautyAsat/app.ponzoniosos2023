package edu.unlpam.vet.ponzonosos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import edu.unlpam.vet.ponzonosos.databinding.ActivityContactUsBinding;
import edu.unlpam.vet.ponzonosos.databinding.ActivityPreventionMeasuresBinding;
import edu.unlpam.vet.ponzonosos.util.Edge;


public class ContactUsActivity extends AppCompatActivity {
    private ActivityContactUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
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
