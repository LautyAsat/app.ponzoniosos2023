package edu.unlpam.vet.ponzonosos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import java.util.List;

import edu.unlpam.vet.ponzonosos.adapters.AdaptadorGrid;
import edu.unlpam.vet.ponzonosos.model.Animal;
import edu.unlpam.vet.ponzonosos.model.AnimalDao;

public class MostrarCatalogoActivity extends AppCompatActivity implements View.OnClickListener{
    GridView gridView;
    AnimalDao animalDao;
    List<Animal> animals;
    AdaptadorGrid adaptadorGrid;

    int tipo = 0;

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
        Button cardArania = findViewById(R.id.cv_arania);
        Button cardEscorpion = findViewById(R.id.cv_escorpionn);
        Button cardSerpiente = findViewById(R.id.cv_serpientee);
        //CardView cardTodos = findViewById(R.id.cv_todos);

        cardQueHacer.setOnClickListener(this);
        cardPrevencion.setOnClickListener(this);
        cardArania.setOnClickListener(this);
        cardEscorpion.setOnClickListener(this);
        cardSerpiente.setOnClickListener(this);
        //cardTodos.setOnClickListener(this);
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
            tipo = 1;
        } else if (id == R.id.cv_escorpionn) {
            tipo = 2;
        } else if (id == R.id.cv_serpientee) {
            tipo = 3;
        } else {
            tipo = 0;
        }

        actualizarGrid();
    }

    private void actualizarGrid() {
        if (tipo == 0) {
            animals = animalDao.queryBuilder().list(); // Todos
        } else {
            animals = animalDao.queryBuilder()
                    .where(AnimalDao.Properties.Tipo.eq(tipo))
                    .list();
        }

        adaptadorGrid.setAnimals(animals); // Necesitás este método en tu adaptador
        adaptadorGrid.notifyDataSetChanged();
    }

}
