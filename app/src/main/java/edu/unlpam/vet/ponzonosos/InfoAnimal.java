package edu.unlpam.vet.ponzonosos;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import java.util.Objects;

import edu.unlpam.vet.ponzonosos.model.Imagen;
import edu.unlpam.vet.ponzonosos.model.Animal;

public class InfoAnimal extends AppCompatActivity {

//    private TextView tvDescripcion;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_animal);

//        context = this;

        Long idAnimal = (Long) Objects.requireNonNull(getIntent().getExtras()).get("obj");
        final Animal animal = Animal.getAnimal(idAnimal);

        if (animal == null) {
            Toast.makeText(getApplicationContext(), "No existe un animal con el id " + idAnimal, Toast.LENGTH_SHORT).show();
            return;
        }

        // Vincular vistas
        ImageView ivPrincipal = findViewById(R.id.iv_img_principal);
        TextView tvNombreComun = findViewById(R.id.tv_nombre_comun);
        TextView tvNombreCientifico = findViewById(R.id.tv_nombre_cientifico);
        View tvAgresividad = findViewById(R.id.tv_nivel_riesgo);
        TextView tvTamano = findViewById(R.id.tv_tamano);
        TextView tvSintomas = findViewById(R.id.tv_sintomas_picadura);
        TextView tvAntidoto = findViewById(R.id.tv_antidoto);
        TextView tvLugarEncuentro = findViewById(R.id.tv_habitat);
        TextView tvAcciones = findViewById(R.id.tv_acciones_picadura);
        TextView tvConfusion = findViewById(R.id.tv_confusion);
//        tvDescripcion = findViewById(R.id.tv_descripcion); // Solo si lo agregás
//// Solo si lo incluís en el XML

        // Imagen principal
        Imagen img = animal.getPrincipalImage();
        if (img != null) {
            Glide.with(getApplicationContext())
                    .load(getFilesDir() + "/" + img.getImg())
                    .into(ivPrincipal);
        }

        // Setear textos
        tvNombreComun.setText(animal.getNombre());
        tvNombreCientifico.setText(animal.getNombreC());
        tvTamano.setText(animal.getTamano());
        tvSintomas.setText(animal.getEfectoPicadura());
        tvAntidoto.setText(animal.getAntidoto());
        tvLugarEncuentro.setText(animal.getLugar_encuentro());
        tvAcciones.setText(animal.getAccion_picadura());
        tvConfusion.setText(animal.getConfusion());
//        if (tvDescripcion != null)
//            tvDescripcion.setText(animal.getDescripcion());

        Drawable fondo;
        // Nivel de riesgo
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

        // Botón de entrar al carousel
        if (ivPrincipal != null) {
            ivPrincipal.setOnClickListener(view -> {
                if (!animal.getImages().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), Galeria.class);
                    intent.putExtra("img", animal.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "No hay imágenes cargadas", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
