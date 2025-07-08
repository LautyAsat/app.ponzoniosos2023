package edu.unlpam.vet.ponzonosos;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import java.util.List;

import edu.unlpam.vet.ponzonosos.adapters.AdaptadorGrid;
import edu.unlpam.vet.ponzonosos.model.Animal;
import edu.unlpam.vet.ponzonosos.model.AnimalDao;

public class MostrarCatalogoActivity extends AppCompatActivity {
    GridView gridView;
    AnimalDao animalDao;
    List<Animal> animals;
    AdaptadorGrid adaptadorGrid;

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

        animalDao = MainActivity.getInstance().getDaoSession().getAnimalDao();

        animals = animalDao.queryBuilder().list();

        adaptadorGrid = new AdaptadorGrid(this, animals);

        gridView.setAdapter(adaptadorGrid);

    }
}
