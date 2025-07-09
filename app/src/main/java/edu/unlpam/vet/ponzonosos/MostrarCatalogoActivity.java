package edu.unlpam.vet.ponzonosos;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.unlpam.vet.ponzonosos.adapters.AdaptadorGrid;
import edu.unlpam.vet.ponzonosos.model.Animal;
import edu.unlpam.vet.ponzonosos.model.AnimalDao;

public class MostrarCatalogoActivity extends AppCompatActivity implements View.OnClickListener{
    GridView gridView;
    AnimalDao animalDao;
    List<Animal> animals;
    AdaptadorGrid adaptadorGrid;

    int tipo = 0;

    private Button cardArania, cardEscorpion, cardSerpiente;
    private  boolean spiderOn=true,scorpionOn=false,snakeOn=false;

    Set<Integer> tiposSeleccionados = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.menucatalogo);

        Toolbar myToolbar = findViewById(R.id.app_bar);

        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        gridView = findViewById(R.id.gv_catalogo);

        //traemos los animalitos
        animalDao = MainActivity.getInstance().getDaoSession().getAnimalDao();

        animals = animalDao.queryBuilder().list();

        adaptadorGrid = new AdaptadorGrid(this, animals);

        gridView.setAdapter(adaptadorGrid);

        Button cardQueHacer = findViewById(R.id.cv_what_to_doo);
        Button cardPrevencion = findViewById(R.id.cv_prevention_measuress);
        cardArania = findViewById(R.id.cv_arania);
        cardEscorpion = findViewById(R.id.cv_escorpionn);
        cardSerpiente = findViewById(R.id.cv_serpientee);
        //CardView cardTodos = findViewById(R.id.cv_todos);

        cardQueHacer.setOnClickListener(this);
        cardPrevencion.setOnClickListener(this);
        cardArania.setOnClickListener(this);
        cardEscorpion.setOnClickListener(this);
        cardSerpiente.setOnClickListener(this);
        //cardTodos.setOnClickListener(this);

        tiposSeleccionados.add(1);
        tiposSeleccionados.add(2);
        tiposSeleccionados.add(3);

        spiderOn = true;
        scorpionOn = true;
        snakeOn = true;

    }

    @Override

    public void onClick(View view) {
        int id = view.getId();


        if (id == R.id.cv_what_to_doo) {
            startActivity(new Intent(this, WhatToDoActivity.class));
            return;
        }

        if (id == R.id.cv_prevention_measuress) {
            startActivity(new Intent(this, PreventionMeasuresActivity.class));
            return;
        }

        if (id == R.id.cv_arania) {
            toggleTipo(1);
            spiderOn=!spiderOn;
            actualizarFondo(1);
        } else if (id == R.id.cv_escorpionn) {
            toggleTipo(2);
            scorpionOn=!scorpionOn;
            actualizarFondo(2);

        } else if (id == R.id.cv_serpientee) {
            toggleTipo(3);
            snakeOn=!snakeOn;
            actualizarFondo(3);

        } else if (spiderOn && scorpionOn && snakeOn) {
            toggleTipo(0);
        }

        actualizarGrid();
    }

    private void toggleTipo(int tipo) {
        if (tiposSeleccionados.contains(tipo)) {
            tiposSeleccionados.remove(tipo);
        } else {
            tiposSeleccionados.add(tipo);
        }
    }

    private void actualizarFondo(int option){
        switch (option){
            case 1:
                if(spiderOn){
                    cardArania.setBackground(ContextCompat.getDrawable(this, R.drawable.colour_spider));

                }
                else{
                    cardArania.setBackground(ContextCompat.getDrawable(this, R.drawable.whiteblack_spider));
                }
                break;
            case 2:
                if(scorpionOn){
                    cardEscorpion.setBackground(ContextCompat.getDrawable(this, R.drawable.colour_scorpion));
                }
                else{
                    cardEscorpion.setBackground(ContextCompat.getDrawable(this, R.drawable.whiteblack_scorpion));
                }
                break;
            case 3:
                if(snakeOn){
                    cardSerpiente.setBackground(ContextCompat.getDrawable(this, R.drawable.colour_snake));

                }
                else{
                    cardSerpiente.setBackground(ContextCompat.getDrawable(this, R.drawable.whiteblack_snake));
                }
                break;

            default:
                actualizarFondo(1);
                actualizarFondo(2);
                actualizarFondo(3);


                break;
        }
    }
    private void actualizarGrid() {
        if (tiposSeleccionados.isEmpty()) {
            animals = animalDao.queryBuilder().list(); // mostrar todos
        } else {
            // construir el IN (...)
            animals = animalDao.queryBuilder()
                    .where(AnimalDao.Properties.Tipo.in(tiposSeleccionados.toArray()))
                    .list();
        }
//        Necesitás este metodo en tu adaptador
        adaptadorGrid.setAnimals(animals);
        adaptadorGrid.notifyDataSetChanged();
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
        if (item.getItemId() == R.id.contact){
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void goAboutActivity() {
        Intent aboutActivity = new Intent(this, AboutActivity.class);
        startActivity(aboutActivity);
    }

}
