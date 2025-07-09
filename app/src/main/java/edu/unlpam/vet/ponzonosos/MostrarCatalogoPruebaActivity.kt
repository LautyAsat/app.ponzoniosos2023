package edu.unlpam.vet.ponzonosos

import android.os.Bundle
import android.util.TypedValue
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import edu.unlpam.vet.ponzonosos.adapters.MostrarCatalogoAdapter
import edu.unlpam.vet.ponzonosos.databinding.ActivityMostrarCatalogoPruebaBinding
import edu.unlpam.vet.ponzonosos.model.Animal
import edu.unlpam.vet.ponzonosos.model.AnimalDao

class MostrarCatalogoPruebaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMostrarCatalogoPruebaBinding
    private lateinit var animalDao: AnimalDao
    private lateinit var animals : List<Animal>

    private lateinit var adapter: MostrarCatalogoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostrarCatalogoPruebaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        /* ---- */
        dinamicPadding() // Se centra dinamicamente según el tamaño de pantalla el padding del recyclingView
        /* ---- */

        //traemos los animalitos
        animalDao = MainActivity.getInstance().getDaoSession().getAnimalDao()

        /* Tabla de tipos */
        // 0 - Default (nada)
        // 1 - Arañas
        // 2 - Escorpiones/Alacranes
        // 3 - Serpientes

        val tipo = 1
        animals = animalDao.queryBuilder().where(AnimalDao.Properties.Tipo.eq(tipo.toString())).list();

        initUI()
    }

    private fun initUI(){
        adapter = MostrarCatalogoAdapter(animals){ navigateToDetail() }

        binding.rvCatalogo.layoutManager = GridLayoutManager(this, 2)
        binding.rvCatalogo.adapter = adapter
    }

    private fun navigateToDetail(){

    }

    private fun dinamicPadding(){

        // 1. Obtener las dimensiones de la pantalla
        val displayMetrics = resources.displayMetrics
        val screenWidthPx = displayMetrics.widthPixels - dpToPx(50f).toInt() // Ancho de la pantalla en píxeles

        // 2. Definir el ancho de tus ítems y el espaciado deseado (en dp)
        val itemWidthDp = 170f // Ancho de tu item_animal.xml
        val itemSpacingDp = 8f // Espacio que deseas entre los ítems (el marginEnd del item)

        // 3. Convertir DP a Píxeles
        val itemWidthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemWidthDp, displayMetrics).toInt()
        val itemSpacingPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemSpacingDp, displayMetrics).toInt()

        // 4. Calcular el ancho total que ocuparán los dos ítems y el espaciado entre ellos.
        // Esto es (ancho_item1 + margen_derecho_item1) + (ancho_item2)
        val contentWidthPx = (itemWidthPx + itemSpacingPx) + itemWidthPx

        // 5. Calcular el padding horizontal necesario para centrar
        val horizontalPaddingPx = (screenWidthPx - contentWidthPx) / 2

        // 6. Aplicar el padding al RecyclerView
        if (horizontalPaddingPx > 0) {
            // rvCatalogo es tu instancia de RecyclerView. Asumo que usas binding o findViewById.
            // Si usas binding, sería binding.rvCatalogo.
            // Si es findViewById, sería rvCatalogo.
            binding.rvCatalogo.setPadding(horizontalPaddingPx, binding.rvCatalogo.paddingTop, horizontalPaddingPx, binding.rvCatalogo.paddingBottom)
            binding.rvCatalogo.clipToPadding = false
        }

    }

    fun pxToDp(px: Float): Float {
        return px / this.resources.displayMetrics.density
    }

    fun dpToPx(dp: Float): Float {
        return dp * resources.displayMetrics.density
    }
}