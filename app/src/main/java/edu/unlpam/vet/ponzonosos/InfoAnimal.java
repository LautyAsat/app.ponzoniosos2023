package edu.unlpam.vet.ponzonosos;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import edu.unlpam.vet.ponzonosos.R;



import edu.unlpam.vet.ponzonosos.model.Imagen;
import edu.unlpam.vet.ponzonosos.model.Animal;

public class InfoAnimal extends AppCompatActivity{

    private ViewPager galeria;
    private TextView nombre;
    private TextView nombreC;
    private TextView agresividad;
    private Context context;
    private TextView tamaño;
    private Button verFotos;
    private ImageView principalImg;
    private TextView efectoPicadura;
    private TextView antidoto;
    private TextView lugar_encuentro;
    private TextView accion_picadura;
    private TextView confusion;
    private TextView descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_animal);
        context = this;
        //final Animal animal = (Animal) getIntent().getSerializableExtra("obj");
        Long idAnimal = (Long) getIntent().getExtras().get("obj");
        Log.d("rmdebug", "id animal: " + idAnimal);
        final Animal animal = Animal.getAnimal(idAnimal);
        if (animal != null) {
            Log.d("rmdebug", animal.getNombre());
            LinearLayout ll_confusion = findViewById(R.id.ll_confusion);
            View viewConfusion = getLayoutInflater().inflate(R.layout.info_animal_confusion, null);
            LinearLayout ll_lugarEncuentro = findViewById(R.id.ll_lugar_encuentro);
            View viewLugarEncuentro = getLayoutInflater().inflate(R.layout.info_animal_lugar_encuentro, null);
            LinearLayout ll_tamaño = findViewById(R.id.ll_tamaño);
            View viewTamaño = getLayoutInflater().inflate(R.layout.info_animal_tamanio, null);
            LinearLayout ll_picadura = findViewById(R.id.ll_picadura);
            View viewPicadura = getLayoutInflater().inflate(R.layout.info_animal_picadura, null);
            LinearLayout ll_antidoto = findViewById(R.id.ll_antidoto);
            View viewAntidoto = getLayoutInflater().inflate(R.layout.info_animal_antidoto, null);
            LinearLayout ll_accion_picadura = findViewById(R.id.ll_acciones_picadura);
            View viewAccionPicadura = getLayoutInflater().inflate(R.layout.info_animal_accion_picadura,null);
            LinearLayout ll_descripcion = findViewById(R.id.ll_descripcion);
            View viewDescripcion = getLayoutInflater().inflate(R.layout.info_animal_descripcion,null);

            principalImg = findViewById(R.id.iv_img_principal);
            Imagen img = animal.getPrincipalImage();
            if (img != null){
                Glide.with(getApplicationContext())
                        .load(getFilesDir()+"/"+img.getImg())
                        .into(principalImg);
            }
            nombre = findViewById(R.id.tv_nombre);
            nombreC = findViewById(R.id.tv_nombreC);
            agresividad = findViewById(R.id.tv_nivelRiesgo);
            tamaño = viewTamaño.findViewById(R.id.tv_tamaño);
            efectoPicadura = viewPicadura.findViewById(R.id.tv_picadura);
            antidoto = viewAntidoto.findViewById(R.id.tv_antidoto);
            accion_picadura = viewAccionPicadura.findViewById(R.id.tv_accionesPicadura);
            confusion = viewConfusion.findViewById(R.id.tv_confusion);
            lugar_encuentro = viewLugarEncuentro.findViewById(R.id.tv_lugarEncuentro);
            descripcion = viewDescripcion.findViewById(R.id.tv_descripcion);
            verFotos = findViewById(R.id.btn_ver_fotos);
            verFotos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!animal.getImages().isEmpty()) {
                        Intent intent = new Intent(getApplicationContext(), Galeria.class);
                        intent.putExtra("img", animal.getId());
                        startActivity(intent);
                    }else {
                        Toast.makeText(context, "No hay imágenes cargadas",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            nombre.setText(animal.getNombre());
            nombreC.setText(animal.getNombreC());
            switch (animal.getAgresividad()) {
                case 1:
                    agresividad.setText("Alto");
                    break;
                case 2:
                    agresividad.setText("Medio");
                    break;
                case 3:
                    agresividad.setText("Bajo");
                    break;
                default:
                    agresividad.setText("No especificado");
                    break;
            }
            tamaño.setText(animal.getTamaño());
            if (!animal.getTamaño().equals("")) {
                ll_tamaño.addView(viewTamaño);
            }
            efectoPicadura.setText(animal.getEfectoPicadura());
            if (!animal.getEfectoPicadura().equals("")){
                ll_picadura.addView(viewPicadura);
            }
            antidoto.setText(animal.getAntidoto());
            if (!animal.getAntidoto().equals("")){
                ll_antidoto.addView(viewAntidoto);
            }
            lugar_encuentro.setText(animal.getLugar_encuentro());
            if (!animal.getLugar_encuentro().equals("")) {
                ll_lugarEncuentro.addView(viewLugarEncuentro);
            }
            accion_picadura.setText(animal.getAccion_picadura());
            if (!animal.getAccion_picadura().equals("")){
                ll_accion_picadura.addView(viewAccionPicadura);
            }
            confusion.setText(animal.getConfusion());
            if (!animal.getConfusion().equals("")) {
                ll_confusion.addView(viewConfusion);
            }
            descripcion.setText(animal.getDescripcion());
            if (!animal.getDescripcion().equals("")) {
                ll_descripcion.addView(viewDescripcion);
            }
        }else {
            Toast.makeText(getApplicationContext(),
                    "No existe un animal con el id " + idAnimal,
                    Toast.LENGTH_SHORT).show();
        }

    }
}
