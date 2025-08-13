package edu.unlpam.vet.ponzonosos;


import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;


import android.view.View;

import android.widget.LinearLayout;


import edu.unlpam.vet.ponzonosos.model.Animal;
import edu.unlpam.vet.ponzonosos.model.Imagen;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Galeria extends AppCompatActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galeria);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        Long idAnimal = (Long) Objects.requireNonNull(getIntent().getExtras()).get("img");
        Animal animal = Animal.getAnimal(idAnimal);
        List<SpacePhoto> photos = new ArrayList<>();

        assert animal != null;
        for (Imagen img : animal.getImages()) {
            photos.add(new SpacePhoto(getFilesDir() + "/" + img.getImg()));
        }

        GaleriaPagerAdapter adapter = new GaleriaPagerAdapter(this, photos);
        viewPager.setAdapter(adapter);

        // Conectar TabLayout con ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setCustomView(getCustomTab(this, position == 0))
        ).attach();

        // Actualizar el estilo del punto seleccionado
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    View customView = Objects.requireNonNull(tabLayout.getTabAt(i)).getCustomView();
                    if (customView != null) {
                        customView.animate()
                                .scaleX(i == position ? 1.5f : 1.0f)
                                .setDuration(200)
                                .start();

                        customView.setBackgroundResource(i == position ? R.drawable.tab_selected : R.drawable.tab_unselected);
                        customView.requestLayout();
                    }
                }
            }
        });

    }

    private View getCustomTab(Context context, boolean selected) {
        View view = new View(context);
        int ancho = selected ? 30 : 14;
        int alto = 14;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);
        params.setMargins(8, 8, 8, 8);
        view.setLayoutParams(params);
        view.setBackgroundResource(selected ? R.drawable.tab_selected : R.drawable.tab_unselected);
        return view;
    }
}
