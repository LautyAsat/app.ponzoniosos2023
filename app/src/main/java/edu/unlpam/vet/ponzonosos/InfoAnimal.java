package edu.unlpam.vet.ponzonosos;


import android.animation.ValueAnimator;
import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.unlpam.vet.ponzonosos.databinding.ActivityMostrarCatalogoPruebaBinding;
import edu.unlpam.vet.ponzonosos.model.Imagen;
import edu.unlpam.vet.ponzonosos.model.Animal;
import edu.unlpam.vet.ponzonosos.util.Measures;

import android.view.Window;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;

public class InfoAnimal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_animal);


        // Enable EdgeToEdge
        Window window = getWindow();
        ViewCompat.setOnApplyWindowInsetsListener(window.getDecorView(), (v, insets) -> insets);
        WindowCompat.setDecorFitsSystemWindows(window, false);



        Long idAnimal = (Long) Objects.requireNonNull(getIntent().getExtras()).get("obj");
        final Animal animal = Animal.getAnimal(idAnimal);

        if (animal == null) {
            Toast.makeText(getApplicationContext(), "No existe un animal con el id " + idAnimal, Toast.LENGTH_SHORT).show();
            return;
        }

        ViewPager2 viewPager = findViewById(R.id.viewPager_info);
        TabLayout tabLayout = findViewById(R.id.tabLayout_info);

        List<String> rutas = new ArrayList<>();
        for (Imagen img : animal.getImages()) {
            rutas.add(getFilesDir() + "/" + img.getImg());
        }

        InfoAnimalImageAdapter adapter = new InfoAnimalImageAdapter(this, rutas);
        viewPager.setAdapter(adapter);

        // Conectar TabLayout con ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setCustomView(getCustomTab(this, position == 0))
        ).attach();


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    View customView = Objects.requireNonNull(tabLayout.getTabAt(i)).getCustomView();
                    if (customView != null) {
                        int width = (i == position) ? 30 : 14;
                        final ViewGroup.LayoutParams layoutParams = customView.getLayoutParams();
                        ValueAnimator animator = ValueAnimator.ofInt(layoutParams.width, width);
                        animator.setDuration(200);
                        animator.addUpdateListener(animation -> {
                            layoutParams.width = (int) animation.getAnimatedValue();
                            customView.setLayoutParams(layoutParams);
                        });
                        animator.start();


                        customView.setBackgroundResource(i == position ? R.drawable.tab_selected : R.drawable.tab_unselected);
                        customView.requestLayout();
                    }
                }
            }
        });

        LinearLayout containerBlockInformation = findViewById(R.id.info_container);

        // Rellenamos información básica del animal
        TextView tvNombreComun = findViewById(R.id.tv_nombre_comun);
        TextView tvNombreCientifico = findViewById(R.id.tv_nombre_cientifico);
        View tvAgresividad = findViewById(R.id.tv_nivel_riesgo);

        tvNombreComun.setText(animal.getNombre());
        tvNombreCientifico.setText(animal.getNombreC());
        Drawable fondo;
        switch (animal.getAgresividad()) {
            case 2:
                fondo = ContextCompat.getDrawable(this, R.drawable.yellow_texture);
                tvAgresividad.setBackground(fondo);
                break;
            case 3:
                fondo = ContextCompat.getDrawable(this, R.drawable.green_texture);
                tvAgresividad.setBackground(fondo);
                break;
            default:
                fondo = ContextCompat.getDrawable(this, R.drawable.red_texture);
                tvAgresividad.setBackground(fondo);
                break;
        }

        // Rellenamos información dinamica según la info del animal
        addContentBlock(this, containerBlockInformation, getText(R.string.s_ntomas_picadura).toString(), animal.getEfectoPicadura());
        addContentBlock(this, containerBlockInformation, getText(R.string.ant_doto).toString(), animal.getAntidoto());
        addContentBlock(this, containerBlockInformation, getText(R.string.h_bitat).toString(), animal.getLugar_encuentro());
        addContentBlock(this, containerBlockInformation, getText(R.string.medidas_frente_a_una_picadura).toString(), animal.getAccion_picadura());
        addContentBlock(this, containerBlockInformation, getText(R.string.tama_o).toString(), animal.getTamano());
        addContentBlock(this, containerBlockInformation, getText(R.string.se_puede_confundir).toString(), animal.getConfusion());
        addContentBlock(this, containerBlockInformation, getText(R.string.observaciones).toString(), animal.getDescripcion());

    }
    private View getCustomTab(Context context, boolean selected) {
        View view = new View(context);
        int width = selected ? 30 : 14;
        int height = 14;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(8, 8, 8, 8);
        view.setLayoutParams(params);
        view.setBackgroundResource(selected ? R.drawable.tab_selected : R.drawable.tab_unselected);
        return view;
    }
    private void addContentBlock(
            Context context,
            LinearLayout container,
            String title,
            String content
    ) {
        if (!content.isEmpty()) {
            // Título
            TextView tvTitle = new TextView(context);
            tvTitle.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            );
            tvTitle.setText(title);
            tvTitle.setTextColor(Color.BLACK);
            tvTitle.setTypeface(tvTitle.getTypeface(), Typeface.BOLD);

            // Contenido
            TextView tvContent = new TextView(context);
            tvContent.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            tvContent.setText(content);
            tvContent.setPadding(0, 0, 0, (int) Measures.dpToPx(context, 8));

            container.addView(tvTitle);
            container.addView(tvContent);
        }
    }

}