package edu.unlpam.vet.ponzonosos

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import edu.unlpam.vet.ponzonosos.databinding.ActivityMostrarCatalogoPruebaBinding
import edu.unlpam.vet.ponzonosos.model.Animal
import edu.unlpam.vet.ponzonosos.model.AnimalDao

class MostrarCatalogoPruebaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMostrarCatalogoPruebaBinding
    private lateinit var animalDao: AnimalDao
    private lateinit var animals : List<Animal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostrarCatalogoPruebaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        //traemos los animalitos
        animalDao = MainActivity.getInstance().getDaoSession().getAnimalDao()
        animals = animalDao.queryBuilder().list()

        //binding.cvArania.setOnClickListener {
        //    Log.i("lau", "Aaa me clickearon")
        //}
    }
}