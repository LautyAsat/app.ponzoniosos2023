package edu.unlpam.vet.ponzonosos

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import edu.unlpam.vet.ponzonosos.adapters.MostrarCatalogoAdapter
import edu.unlpam.vet.ponzonosos.databinding.ActivityMostrarCatalogoPruebaBinding
import edu.unlpam.vet.ponzonosos.model.Animal
import edu.unlpam.vet.ponzonosos.model.AnimalDao

class MostrarCatalogoPruebaActivity : AppCompatActivity() {

    class ToggleState(var value: Boolean, val type : Int)

    private lateinit var binding: ActivityMostrarCatalogoPruebaBinding
    private lateinit var animalDao: AnimalDao
    private lateinit var animals : MutableList<Animal>

    private var spiderState = ToggleState(true, 1)
    private var scorpionState = ToggleState(true, 2)
    private var snakeState = ToggleState(true, 3)

    private var listOfAnimalsTypes = mutableSetOf<Int>(1, 2, 3)

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

        //val tipo = 1
        //animals = animalDao.queryBuilder().where(AnimalDao.Properties.Tipo.eq(tipo.toString())).list()


        // Lista mezclada para que salga como tenga que salir
        animals = getAnimals(listOfAnimalsTypes)

        initUI()

        // Listener de estado para los botones ponzoñosos
        toggleImage(spiderState, binding.ivArania, R.drawable.colour_spider, R.drawable.whiteblack_spider)
        toggleImage(scorpionState, binding.ivEscorpion, R.drawable.colour_scorpion, R.drawable.whiteblack_scorpion)
        toggleImage(snakeState, binding.ivSerpiente, R.drawable.colour_snake, R.drawable.whiteblack_snake)
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

    fun dpToPx(dp: Float): Float {
        return dp * resources.displayMetrics.density
    }

    private fun toggleImage(
        state : ToggleState,
        imageView: ImageButton,
        image1: Int,
        image2: Int
    ) {
        imageView.setOnClickListener {
            state.value = !state.value

            // Se modifica la lista de tiposActivos
            if(state.value) listOfAnimalsTypes.add(state.type)
            else listOfAnimalsTypes.remove(state.type)

            // Cambia la lista de animales activos
            changeAnimalsState()

            // Toggle image
            Glide.with(this)
                .load(if (state.value) image1 else image2)
                .into(imageView)


        }
    }

    private fun changeAnimalsState(){
        animals.clear()
        animals.addAll(getAnimals(listOfAnimalsTypes))

        // Notificamos al adapter que la lista cambio
        adapter.notifyDataSetChanged()
    }

    private fun getAnimals(types : MutableSet<Int>) : MutableList<Animal>{
        val animals = animalDao.queryBuilder()
            .where(AnimalDao.Properties.Tipo.`in`(types))
            .list()
            .shuffled()

        return animals.toMutableList()
    }
}