package edu.unlpam.vet.ponzonosos

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import edu.unlpam.vet.ponzonosos.adapters.MostrarCatalogoAdapter
import edu.unlpam.vet.ponzonosos.databinding.ActivityMostrarCatalogoPruebaBinding
import edu.unlpam.vet.ponzonosos.model.Animal
import edu.unlpam.vet.ponzonosos.model.AnimalDao
import edu.unlpam.vet.ponzonosos.util.Edge


class MostrarCatalogoPruebaActivity : AppCompatActivity() {

    class ToggleState(var value: Boolean, val type : Int)

    private lateinit var binding: ActivityMostrarCatalogoPruebaBinding
    private lateinit var animalDao: AnimalDao
    private lateinit var animals : MutableList<Animal>

    private var spiderState = ToggleState(true, 1)
    private var scorpionState = ToggleState(true, 2)
    private var snakeState = ToggleState(true, 3)

    private var listOfAnimalsTypes = mutableSetOf(1, 2, 3)

    private var searchQuery: String = ""

    private var isFilterVisible: Boolean = false


    private enum class Buttons(var state: Boolean) {
        isPeligrosidadAltaOn(false),
        isPeligrosidadMediaOn(false),
        isPeligrosidadBajaOn(false),
        isTamanoGrandeoOn(false),
        isTamanoMedianoOn(false),
        isTamanoPequenoOn(false);

        fun toggle() {
            state=!state
        }
    }

    private var originalStates = mutableMapOf<Buttons, Boolean>()


    private lateinit var adapter: MostrarCatalogoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostrarCatalogoPruebaBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Padding dinamico para el header edgeToEdge
        Edge.applyDynamicEdgeAppBar(
            this,
            getWindow().getDecorView(),
            binding.iHeader.root,
            binding.iHeader.llHeader,
            60f
        )

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

        // Listeners para botones caution
        binding.btnPrecaution.setOnClickListener { navigateToPrecaution() }
        binding.btnAccident.setOnClickListener { navigateToAccident() }

        // Listeners de estado para los botones ponzoñosos
        toggleTypeStateHandler(spiderState, binding.ivArania, R.drawable.colour_spider, R.drawable.whiteblack_spider)
        toggleTypeStateHandler(scorpionState, binding.ivEscorpion, R.drawable.colour_scorpion, R.drawable.whiteblack_scorpion)
        toggleTypeStateHandler(snakeState, binding.ivSerpiente, R.drawable.colour_snake, R.drawable.whiteblack_snake)

        // Listener para searcher
        binding.etSearch.addTextChangedListener(object: TextWatcher{

            override fun beforeTextChanged( s: CharSequence?, start: Int, count: Int, after: Int){}
            override fun onTextChanged( s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                searchQuery = s.toString()
                filterAnimalsByQuery(searchQuery)
            }

        })

        // Listener del boton filtro para mostrar o ocultar los filtros
        binding.cvFilter.setOnClickListener {
            isFilterVisible = !isFilterVisible
            binding.iFilter.root.visibility = if (isFilterVisible) View.VISIBLE else View.GONE

            if (isFilterVisible) {
                // Guardar estado actual
                originalStates = Buttons.entries.associateWith { it.state }.toMutableMap()
            }
        }


        // Listener de los botones del filtro  - Peligrosidad
        binding.iFilter.btnPeligrosidadAlta.setOnClickListener {
            selectOnlyPeligrosidad(1)
        }

        binding.iFilter.btnPeligrosidadMedia.setOnClickListener {
            selectOnlyPeligrosidad(2)
        }

        binding.iFilter.btnPeligrosidadBaja.setOnClickListener {
            selectOnlyPeligrosidad(3)
        }

        binding.iFilter.btnGrande.setOnClickListener {
            selectOnlyTamano(1)
        }

        binding.iFilter.btnMediano.setOnClickListener {
            selectOnlyTamano(2)
        }

        binding.iFilter.btnPequeno.setOnClickListener {
            selectOnlyTamano(3)
        }


        binding.iFilter.applyFilter.setOnClickListener(){
            filterAnimals()

        }
        binding.iFilter.cancelFilter.setOnClickListener(){
            cancelNewFilterOptions()
        }

    }

    private fun initUI(){
        adapter = MostrarCatalogoAdapter(animals){ navigateToDetail(it) }

        binding.rvCatalogo.layoutManager = GridLayoutManager(this, 2)
        binding.rvCatalogo.adapter = adapter
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

    private fun toggleTypeStateHandler(
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

    private fun filterAnimalsByQuery(query: String){
        val filteredList = getAnimals(listOfAnimalsTypes).filter {
            it.nombre.contains(searchQuery, ignoreCase = true)
        }

        adapter.updateData(filteredList)
    }
    //funcion para truncar texto del tamaño
    private fun extraerTamanioMinimo(texto: String?): Double? {
        if (texto.isNullOrBlank()) return null

        // Reemplaza comas por puntos, y guiones por espacios
        val limpio = texto.replace(",", ".")
            .replace("–", " ")
            .replace("-", " ")
            .lowercase()

        // Busca el primer número en el texto
        val regex = Regex("""\d+(\.\d+)?""")
        val encontrado = regex.find(limpio)

        return encontrado?.value?.toDoubleOrNull()
    }


    private fun changeAnimalsState() {
        // 1. Obtener la lista base según los tipos (1, 2, 3)
        val baseList = getAnimals(listOfAnimalsTypes)

        // 2. Aplicar filtros de peligrosidad y tamaño si hay alguno activado
        val filteredByOptions = filterByOptions(baseList)

        // 3. Aplicar búsqueda si hay texto
        val filteredFinal = if (searchQuery.isNotEmpty()) {
            filteredByOptions.filter {
                it.nombre.contains(searchQuery, ignoreCase = true)
            }
        } else {
            filteredByOptions
        }

        // 4. Actualizar la lista en el adaptador
        animals.clear()
        animals.addAll(filteredFinal)
        adapter.notifyDataSetChanged()
    }

    private fun filterByOptions(listaBase: List<Animal>): List<Animal> {
        val peligrosidadSeleccionada = when {
            Buttons.isPeligrosidadAltaOn.state -> 1
            Buttons.isPeligrosidadMediaOn.state -> 2
            Buttons.isPeligrosidadBajaOn.state -> 3
            else -> null
        }

        val tamanoSeleccionado = when {
            Buttons.isTamanoGrandeoOn.state -> "grande"
            Buttons.isTamanoMedianoOn.state -> "mediano"
            Buttons.isTamanoPequenoOn.state -> "pequeño"
            else -> null
        }


        return listaBase.filter { animal ->
            val coincidePeligrosidad = peligrosidadSeleccionada?.let { animal.agresividad == it } ?: true
            val valorAnimal = animal.tamaño.trim().lowercase()
            val coincideTamano = tamanoSeleccionado?.let {
                valorAnimal.contains(it)
            } ?: true


            coincidePeligrosidad && coincideTamano
        }
    }


    private fun filterAnimals() {
        var peligrosidadSeleccionada: Int? = null
        var tamanoSeleccionado: Int? = null

        // Detectar la peligrosidad seleccionada
        peligrosidadSeleccionada = when {
            Buttons.isPeligrosidadAltaOn.state -> 1
            Buttons.isPeligrosidadMediaOn.state -> 2
            Buttons.isPeligrosidadBajaOn.state -> 3
            else -> null
        }

        // Detectar el tamaño seleccionado
        tamanoSeleccionado = when {
            Buttons.isTamanoGrandeoOn.state -> 3
            Buttons.isTamanoMedianoOn.state -> 2
            Buttons.isTamanoPequenoOn.state -> 1
            else -> null
        }

        // Obtener animales según los tipos activos (araña, escorpión, serpiente)
        val listaBase = getAnimals(listOfAnimalsTypes)

        // Filtrar por peligrosidad y tamaño
        val animalesFiltrados = listaBase.filter { animal ->
            val coincidePeligrosidad = peligrosidadSeleccionada?.let { animal.agresividad == it } ?: true
            val coincideTamano = tamanoSeleccionado?.let { filtroTamano ->
                val minTam = extraerTamanioMinimo(animal.tamaño)
                when (filtroTamano) {
                    1 -> minTam != null && minTam <= 3
                    2 -> minTam != null && minTam > 3 && minTam <= 7
                    3 -> minTam != null && minTam > 7
                    else -> true
                }
            } ?: true



            coincidePeligrosidad && coincideTamano
        }

        // Actualizar el adapter con la lista filtrada
        adapter.updateData(animalesFiltrados)

        // Ocultar el panel de filtro si querés
        isFilterVisible = false
        binding.iFilter.root.visibility = View.GONE
    }


    private fun cancelNewFilterOptions(){
            // Restaurar estados guardados
            originalStates.forEach { (Button, state) ->
                Button.state = state
            }

            // Actualizar visualmente los botones
            updatePeligrosidadBackgrounds()
            updateTamanoBackgrounds()

            // Ocultar el panel de filtro si querés
            isFilterVisible = false
            binding.iFilter.root.visibility = View.GONE

    }

    private fun selectOnlyPeligrosidad(option: Int) {

    when(option){
        2 ->{
            Buttons.isPeligrosidadMediaOn.toggle()
            Buttons.isPeligrosidadAltaOn.state=false
            Buttons.isPeligrosidadBajaOn.state=false
        }
        3->{
            Buttons.isPeligrosidadBajaOn.toggle()
            Buttons.isPeligrosidadMediaOn.state=false
            Buttons.isPeligrosidadAltaOn.state=false
        }
        else -> {
            Buttons.isPeligrosidadAltaOn.toggle()
            Buttons.isPeligrosidadMediaOn.state=false
            Buttons.isPeligrosidadBajaOn.state=false
        }
    }
        updatePeligrosidadBackgrounds()
    }

    private fun selectOnlyTamano(option: Int) {
        when(option) {
            2 -> {
                Buttons.isTamanoMedianoOn.toggle()
                Buttons.isTamanoGrandeoOn.state = false
                Buttons.isTamanoPequenoOn.state = false
            }

            3 -> {
                Buttons.isTamanoPequenoOn.toggle()
                Buttons.isTamanoGrandeoOn.state = false
                Buttons.isTamanoMedianoOn.state = false
            }

            else -> {
                Buttons.isTamanoGrandeoOn.toggle()
                Buttons.isTamanoMedianoOn.state = false
                Buttons.isTamanoPequenoOn.state = false
            }
        }
        updateTamanoBackgrounds()
    }




    private fun updatePeligrosidadBackgrounds() {


        binding.iFilter.btnPeligrosidadAlta.setBackgroundResource(
            if (Buttons.isPeligrosidadAltaOn.state) R.drawable.red_button else R.drawable.red_texture
        )
        binding.iFilter.btnPeligrosidadMedia.setBackgroundResource(
            if (Buttons.isPeligrosidadMediaOn.state) R.drawable.yellow_button else R.drawable.yellow_texture
        )
        binding.iFilter.btnPeligrosidadBaja.setBackgroundResource(
            if (Buttons.isPeligrosidadBajaOn.state) R.drawable.green_button else R.drawable.green_texture
        )
    }

    private fun updateTamanoBackgrounds() {

        val selected = R.drawable.selected_button
        val notSelected = R.drawable.gridborder

        binding.iFilter.btnGrande.setBackgroundResource(
            if (Buttons.isTamanoGrandeoOn.state) selected else notSelected
        )
        binding.iFilter.btnMediano.setBackgroundResource(
            if (Buttons.isTamanoMedianoOn.state) selected else notSelected
        )
        binding.iFilter.btnPequeno.setBackgroundResource(
            if (Buttons.isTamanoPequenoOn.state) selected else notSelected
        )
    }




    private fun getAnimals(types : MutableSet<Int>) : MutableList<Animal>{
        val animals = animalDao.queryBuilder()
            .where(AnimalDao.Properties.Tipo.`in`(types))
            .list()
            .sortedBy { it.nombre.lowercase() }

        return animals.toMutableList()
    }

    private fun navigateToDetail(animalId: Long ){
        val intent = Intent(this, InfoAnimal::class.java)

        intent.putExtra("obj", animalId)

        startActivity(intent)
    }

    private fun navigateToPrecaution(){
        val intent = Intent(this, PreventionMeasuresActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAccident(){
        val intent = Intent(this, WhatToDoActivity::class.java)
        startActivity(intent)
    }
}