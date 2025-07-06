package edu.unlpam.vet.ponzonosos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import edu.unlpam.vet.ponzonosos.R;

import edu.unlpam.vet.ponzonosos.adapters.AdaptadorGrid;
import edu.unlpam.vet.ponzonosos.adapters.AggressivenessSpinnerAdapter;
import edu.unlpam.vet.ponzonosos.model.Agresividad;
import edu.unlpam.vet.ponzonosos.model.Animal;
import edu.unlpam.vet.ponzonosos.model.AnimalDao;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

public class CatalogoGrid extends AppCompatActivity {

    private AlertDialog dialog;
    private GridView gridView;
    private int tipo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("rmdebug", "CatalogoGrid - onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_grid);
        Toolbar myToolbar = findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        gridView = findViewById(R.id.gv_catalogo);
        List<Animal> animales;
        AnimalDao animalDao = MainActivity.getInstance().getDaoSession().getAnimalDao();
        Bundle bundle = getIntent().getExtras();
        tipo = 0;
        if (bundle != null) {
            tipo = bundle.getInt("tipo");
        }
        if (tipo == 0) {
            animales = animalDao.queryBuilder().list();
        }else {
            animales = animalDao.queryBuilder()
                    .where(AnimalDao.Properties.Tipo.eq(String.valueOf(tipo))).list();
        }
        gridView.setAdapter(new AdaptadorGrid(this, animales));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.catalogogridactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter){
            showFilterDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        if (dialog != null && !dialog.isShowing()){
            dialog.show();
            return;
        }
        List<Agresividad> aggressiveness = Agresividad.getHardcodedElements();
        AlertDialog.Builder builder = new AlertDialog.Builder(CatalogoGrid.this);
        getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.dialog_filteranimals,null);
        final EditText etName = view.findViewById(R.id.filter_name);
        final Spinner sAggressiveness = view.findViewById(R.id.filter_aggressiveness);
        Button bApplyFilter = view.findViewById(R.id.apply_filter);
        Button bCancelFilter = view.findViewById(R.id.cancel_filter);
        AggressivenessSpinnerAdapter adapter = new AggressivenessSpinnerAdapter(this,
                R.layout.aggressiveness_spinner_item, aggressiveness);
        sAggressiveness.setAdapter(adapter);
        sAggressiveness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        bApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                Long aggressiveness = ((Agresividad)sAggressiveness.getSelectedItem()).getId();
                applyFilter(name, aggressiveness);
            }
        });
        bCancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null){
                    dialog.dismiss();
                    dialog = null;
                }
                applyFilter("",0L);
            }
        });
        builder.setView(view)
                .setCancelable(true)
                .setTitle("Filtrar elementos");
        dialog = builder.create();
        dialog.show();
    }

    private void applyFilter(String name, Long aggressiveness) {
        removeFilterDialog();
        QueryBuilder<Animal> qb = MainActivity.getInstance().getDaoSession().getAnimalDao().queryBuilder();
        if (tipo != 0){
            WhereCondition wc = AnimalDao.Properties.Tipo.eq(String.valueOf(tipo));
            qb.where(wc);
        }
        if (!name.equals("")){
            Log.d("rmdebug", "name " + name);
            WhereCondition wc = AnimalDao.Properties.Nombre.like("%" + name + "%");
            qb.where(wc);
        }
        if (aggressiveness != 0L){
            WhereCondition wc = AnimalDao.Properties.Agresividad.eq(aggressiveness);
            qb.where(wc);
        }
        List<Animal> animals = qb.list();
        gridView.setAdapter(new AdaptadorGrid(this, animals));
    }

    private void removeFilterDialog() {
        if (dialog != null){
            dialog.dismiss();
        }
    }

}
