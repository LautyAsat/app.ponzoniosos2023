package edu.unlpam.vet.ponzonosos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rodrimartin.ponzonosos.R;

import edu.unlpam.vet.ponzonosos.model.Imagen;
import edu.unlpam.vet.ponzonosos.model.ImagenDao;
import edu.unlpam.vet.ponzonosos.util.LoadImage;

import java.util.List;

public class MostrarCatalogo extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RMD-MostrarCatalogo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_catalogo);
        Toolbar myToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        CardView cvWhatToDo = findViewById(R.id.cv_what_to_do);
        CardView cvPreventionMeasures = findViewById(R.id.cv_prevention_measures);
        CardView cv_arana = findViewById(R.id.cv_arana);
        CardView cv_serpiente = findViewById(R.id.cv_serpiente);
        CardView cv_escoprion = findViewById(R.id.cv_escorpion);
        CardView cv_todos = findViewById(R.id.cv_todos);
        cv_arana.setOnClickListener(this);
        cv_serpiente.setOnClickListener(this);
        cv_escoprion.setOnClickListener(this);
        cv_todos.setOnClickListener(this);
        cvWhatToDo.setOnClickListener(this);
        cvPreventionMeasures.setOnClickListener(this);
        bindImageViews();
        checkImagesToDownload();
    }

    private void bindImageViews() {
        ImageView whatToDo = findViewById(R.id.img_what_to_do);
        ImageView preventionMeasures = findViewById(R.id.img_prevention_measures);
        ImageView spider = findViewById(R.id.img_spider);
        ImageView snake = findViewById(R.id.img_snake);
        ImageView scorpion = findViewById(R.id.img_scorpion);
        ImageView all = findViewById(R.id.img_all);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_image);
        Glide.with(getApplicationContext())
                .load(R.drawable.ambulance_v3)
                .apply(options)
                .into(whatToDo);
        Glide.with(getApplicationContext())
                .load(R.drawable.prevention)
                .apply(options)
                .into(preventionMeasures);
        Glide.with(getApplicationContext())
                .load(R.drawable.arana)
                .apply(options)
                .into(spider);
        Glide.with(getApplicationContext())
                .load(R.drawable.serpiente)
                .apply(options)
                .into(snake);
        Glide.with(getApplicationContext())
                .load(R.drawable.escorpion)
                .apply(options)
                .into(scorpion);
        Glide.with(getApplicationContext())
                .load(R.drawable.ic_menu_todos)
                .apply(options)
                .into(all);
    }

    private void checkImagesToDownload() {
        Log.d(TAG, "checkImagesToDownload: Checking images to download ...");
        List<Imagen> imagesToDownload = MainActivity.getInstance().getDaoSession()
                .getImagenDao().queryBuilder()
                .where(ImagenDao.Properties.Descargada.eq(false)).list();
        for (final Imagen image : imagesToDownload){
            LoadImage loadImage = new LoadImage(new LoadImage.Listener() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    if (bitmap == null){
                        Log.e(TAG, "onImageLoaded: Image bitmap is null");
                        return;
                    }
                    image.saveImageToInternalStorage(getApplicationContext(),
                            image.getImg(),
                            bitmap);
                }
                @Override
                public void onError() {
                    Log.e(TAG, "onError: Error loading image bitmap");
                }
            });
            loadImage.execute(MainActivity.DIR_IMAGES + image.getImg());
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.cv_what_to_do:
                intent = new Intent(this, WhatToDoActivity.class);
                startActivity(intent);
                break;
            case R.id.cv_prevention_measures:
                intent = new Intent(this, PreventionMeasuresActivity.class);
                startActivity(intent);
                break;
            case R.id.cv_arana:
                intent = new Intent(this,CatalogoGrid.class);
                intent.putExtra("tipo",1);
                this.startActivity(intent);
                break;
            case R.id.cv_escorpion:
                intent = new Intent(this,CatalogoGrid.class);
                intent.putExtra("tipo",2);
                this.startActivity(intent);
                break;
            case R.id.cv_serpiente:
                intent = new Intent(this,CatalogoGrid.class);
                intent.putExtra("tipo",3);
                this.startActivity(intent);
                break;
            case R.id.cv_todos:
                intent = new Intent(this,CatalogoGrid.class);
                intent.putExtra("tipo",0);
                this.startActivity(intent);
                break;
                default:
                    break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_show_catalog, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about){
            goAboutActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void goAboutActivity() {
        Intent aboutActivity = new Intent(this, AboutActivity.class);
        startActivity(aboutActivity);
    }
}
