package edu.unlpam.vet.ponzonosos

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.ScrollView
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import edu.unlpam.vet.ponzonosos.adapters.MostrarCatalogoAdapter
import edu.unlpam.vet.ponzonosos.databinding.ActivityMostrarCatalogoPruebaBinding
import edu.unlpam.vet.ponzonosos.model.Animal
import edu.unlpam.vet.ponzonosos.model.AnimalDao
import edu.unlpam.vet.ponzonosos.util.Edge
import edu.unlpam.vet.ponzonosos.util.Measures
import kotlin.math.min


class MostrarCatalogoPruebaActivity : AppCompatActivity() {

    class ToggleState(var value: Boolean, val type: Int)

    private lateinit var binding: ActivityMostrarCatalogoPruebaBinding
    private lateinit var animalDao: AnimalDao
    private lateinit var animals: MutableList<Animal>

    private var spiderState = ToggleState(true, 1)
    private var scorpionState = ToggleState(true, 2)
    private var snakeState = ToggleState(true, 3)

    private var listOfAnimalsTypes = mutableSetOf(1, 2, 3)

    private var searchQuery: String = ""

    private var isFilterVisible: Boolean = false


    private enum class Buttons(var state: Boolean) {
        IsPeligrosidadAltaOn(false),
        IsPeligrosidadMediaOn(false),
        IsPeligrosidadBajaOn(false),
        IsTamanoGrandeoOn(false),
        IsTamanoMedianoOn(false),
        IsTamanoPequenoOn(false);

        fun toggle() {
            state = !state
        }
    }

    private var originalStates = mutableMapOf<Buttons, Boolean>()


    private lateinit var adapter: MostrarCatalogoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostrarCatalogoPruebaBinding.inflate(layoutInflater)

        enableEdgeToEdge(
              statusBarStyle = SystemBarStyle.light(
                Color.WHITE,  // fondo blanco
                Color.BLACK   // color de íconos (texto/íconos oscuros)
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.WHITE,  // fondo blanco
                Color.BLACK   // íconos oscuros en la nav bar
            )
        )

        val root = binding.root

        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            v.setPadding(
                v.paddingLeft,
                v.paddingTop,
                v.paddingRight,
                navBarHeight
            )
            insets
        }

        setContentView(binding.root)

        // Padding dinamico para el header edgeToEdge
        Edge.applyDynamicEdgeAppBar(
            this,
            window,
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
        toggleTypeStateHandler(
            spiderState,
            binding.ivArania.context,
            binding.ivArania,
            R.drawable.colour_spider,
            R.drawable.whiteblack_spider
        )
        toggleTypeStateHandler(
            scorpionState,
            binding.ivEscorpion.context,
            binding.ivEscorpion,
            R.drawable.colour_scorpion,
            R.drawable.whiteblack_scorpion
        )
        toggleTypeStateHandler(
            snakeState,
            binding.ivSerpiente.context,
            binding.ivSerpiente,
            R.drawable.colour_snake,
            R.drawable.whiteblack_snake
        )

        // Listeners para searcher

        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {

                // Ocultar el teclado
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)

                true
            } else {
                false
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                searchQuery = s.toString()
                filterAnimalsByQuery()
            }

        })

        detectFocusSearcher()

        // Listener del boton filtro para mostrar o ocultar los filtros
        binding.cvFilter.setOnClickListener {
            isFilterVisible = !isFilterVisible

            showFilterPopup(it)

            if (isFilterVisible) {
                originalStates = Buttons.entries.associateWith { it.state }.toMutableMap()
            }
        }

        val btnMenu: ImageButton = findViewById(R.id.btnMenu)
        btnMenu.setOnClickListener {
            val popup = PopupMenu(this, it)
            popup.menuInflater.inflate(R.menu.menu_show_catalog, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.about -> {
                        navigateToAbout()
                        true
                    }

                    R.id.contact -> {
                        navigateToContact()
                        true
                    }

                    else -> false
                }
            }
            popup.show()
        }

    }

    private fun initUI() {
        adapter = MostrarCatalogoAdapter(animals) { navigateToDetail(it) }

        binding.rvCatalogo.layoutManager = GridLayoutManager(this, 2)
        binding.rvCatalogo.adapter = adapter
    }


    private fun dinamicPadding() {

        // 1. Obtener las dimensiones de la pantalla
        val displayMetrics = resources.displayMetrics
        val screenWidthPx =
            displayMetrics.widthPixels - dpToPx().toInt() // Ancho de la pantalla en píxeles

        // 2. Definir el ancho de tus ítems y el espaciado deseado (en dp)
        val itemWidthDp = 170f // Ancho de tu item_animal.xml
        val itemSpacingDp = 8f // Espacio que deseas entre los ítems (el marginEnd del item)

        // 3. Convertir DP a Píxeles
        val itemWidthPx =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemWidthDp, displayMetrics)
                .toInt()
        val itemSpacingPx =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, itemSpacingDp, displayMetrics)
                .toInt()

        // 4. Calcular el ancho total que ocuparán los dos ítems y el espaciado entre ellos.
        // Esto es (ancho_item1 + margen_derecho_item1) + (ancho_item2)
        val contentWidthPx = (itemWidthPx + itemSpacingPx) + itemWidthPx

        // 5. Calcular el padding horizontal necesario para centrar
        val horizontalPaddingPx = (screenWidthPx - contentWidthPx) / 2

        // 6. Aplicar el padding al RecyclerView
        if (horizontalPaddingPx > 0) {
            binding.rvCatalogo.setPadding(
                horizontalPaddingPx,
                binding.rvCatalogo.paddingTop,
                horizontalPaddingPx,
                binding.rvCatalogo.paddingBottom
            )
            binding.rvCatalogo.clipToPadding = false
        }

    }

    private fun dpToPx(): Float {
        return 50f * resources.displayMetrics.density
    }

    private fun toggleTypeStateHandler(
        state: ToggleState,
        imageContext: Context,
        imageView: ImageButton,
        image1: Int,
        image2: Int
    ) {
        imageView.setOnClickListener {
            state.value = !state.value

            // Se modifica la lista de tiposActivos
            if (state.value) listOfAnimalsTypes.add(state.type)
            else listOfAnimalsTypes.remove(state.type)

            // Cambia la lista de animales activos
            changeAnimalsState()

            // Toggle image
            Glide.with(imageContext)
                .load(if (state.value) image1 else image2)
                .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView)

        }
    }

    private fun filterAnimalsByQuery() {
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

        // Busca el último número en el texto
        val regex = Regex("""\d+(\.\d+)?""")
        val encontrado = regex.findAll(limpio).lastOrNull()

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
        adapter.updateData(filteredFinal)
    }

    private fun filterByOptions(listaBase: List<Animal>): List<Animal> {
        val peligrosidadSeleccionada = when {
            Buttons.IsPeligrosidadAltaOn.state -> 1
            Buttons.IsPeligrosidadMediaOn.state -> 2
            Buttons.IsPeligrosidadBajaOn.state -> 3
            else -> null
        }

        val tamanoSeleccionado = when {
            Buttons.IsTamanoGrandeoOn.state -> 1
            Buttons.IsTamanoMedianoOn.state -> 2
            Buttons.IsTamanoPequenoOn.state -> 3
            else -> null
        }


        return listaBase.filter { animal ->
            val coincidePeligrosidad = peligrosidadSeleccionada?.let { animal.agresividad == it } ?: true
            val coincideTamano = tamanoSeleccionado?.let { filtroTamano ->
                val minTam = extraerTamanioMinimo(animal.tamaño)
                when (filtroTamano) {
                    3 -> minTam != null && minTam <= 3
                    2 -> minTam != null && minTam > 3 && minTam <= 7
                    1 -> minTam != null && minTam > 7
                    else -> true
                }
            } ?: true

            coincidePeligrosidad && coincideTamano
        }
    }


    private fun cancelNewFilterOptions(view: View){
        // Restaurar estados guardados
        originalStates.forEach { (button, state) ->
            button.state = state
        }

        // Actualizar visualmente los botones
        updatePeligrosidadBackgrounds(view)
        updateTamanoBackgrounds(view)

        // Ocultar el panel de filtro si querés
        isFilterVisible = false
        binding.iFilter.root.visibility = View.GONE

    }

    private fun selectOnlyPeligrosidad(option: Int, view : View) {

        when(option){
            2 ->{
                Buttons.IsPeligrosidadMediaOn.toggle()
                Buttons.IsPeligrosidadAltaOn.state=false
                Buttons.IsPeligrosidadBajaOn.state=false
            }
            3->{
                Buttons.IsPeligrosidadBajaOn.toggle()
                Buttons.IsPeligrosidadMediaOn.state=false
                Buttons.IsPeligrosidadAltaOn.state=false
            }
            else -> {
                Buttons.IsPeligrosidadAltaOn.toggle()
                Buttons.IsPeligrosidadMediaOn.state=false
                Buttons.IsPeligrosidadBajaOn.state=false
            }
        }
        updatePeligrosidadBackgrounds(view)
    }

    private fun selectOnlyTamano(option: Int, view: View) {
        when(option) {
            2 -> {
                Buttons.IsTamanoMedianoOn.toggle()
                Buttons.IsTamanoGrandeoOn.state = false
                Buttons.IsTamanoPequenoOn.state = false
            }

            3 -> {
                Buttons.IsTamanoPequenoOn.toggle()
                Buttons.IsTamanoGrandeoOn.state = false
                Buttons.IsTamanoMedianoOn.state = false
            }

            else -> {
                Buttons.IsTamanoGrandeoOn.toggle()
                Buttons.IsTamanoMedianoOn.state = false
                Buttons.IsTamanoPequenoOn.state = false
            }
        }
        updateTamanoBackgrounds(view)
    }

    private fun updatePeligrosidadBackgrounds(root: View) {
        root.findViewById<ImageButton>(R.id.btn_peligrosidad_alta).setBackgroundResource(
            if (Buttons.IsPeligrosidadAltaOn.state) R.drawable.red_button else R.drawable.red_texture
        )
        root.findViewById<ImageButton>(R.id.btn_peligrosidad_media).setBackgroundResource(
            if (Buttons.IsPeligrosidadMediaOn.state) R.drawable.yellow_button else R.drawable.yellow_texture
        )
        root.findViewById<ImageButton>(R.id.btn_peligrosidad_baja).setBackgroundResource(
            if (Buttons.IsPeligrosidadBajaOn.state) R.drawable.green_button else R.drawable.green_texture
        )
    }

    private fun updateTamanoBackgrounds(root: View) {

        val selected = R.drawable.selected_button
        val notSelected = R.drawable.gridborder

        root.findViewById<Button>(R.id.btn_grande).setBackgroundResource(
            if (Buttons.IsTamanoGrandeoOn.state) selected else notSelected
        )
        root.findViewById<Button>(R.id.btn_mediano).setBackgroundResource(
            if (Buttons.IsTamanoMedianoOn.state) selected else notSelected
        )
        root.findViewById<Button>(R.id.btn_pequeno).setBackgroundResource(
            if (Buttons.IsTamanoPequenoOn.state) selected else notSelected
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

    private fun navigateToAbout(){
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToContact(){
        val intent = Intent(this, ContactUsActivity::class.java)
        startActivity(intent)
    }


    fun showFilterPopup(anchor: View) {
        val inflater = LayoutInflater.from(anchor.context)
        val popupView = inflater.inflate(R.layout.dialog_filteranimals, null)

        val displayMetrics = anchor.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val popupWidth = (screenWidth - (Measures.dpToPx(this, 16f) * 2)).toInt()

        val popupWindow = PopupWindow(
            popupView,
            popupWidth,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        updatePeligrosidadBackgrounds(popupView)
        updateTamanoBackgrounds(popupView)


        // Listeners elementos internos del popup
        popupView.findViewById<View>(R.id.btn_peligrosidad_alta).setOnClickListener {
            selectOnlyPeligrosidad(1, popupView)
        }

        popupView.findViewById<View>(R.id.btn_peligrosidad_media).setOnClickListener {
            selectOnlyPeligrosidad(2, popupView)
        }

        popupView.findViewById<View>(R.id.btn_peligrosidad_baja).setOnClickListener {
            selectOnlyPeligrosidad(3, popupView)
        }

        popupView.findViewById<View>(R.id.btn_grande).setOnClickListener {
            selectOnlyTamano(1, popupView)
        }

        popupView.findViewById<View>(R.id.btn_mediano).setOnClickListener {
            selectOnlyTamano(2, popupView)
        }

        popupView.findViewById<View>(R.id.btn_pequeno).setOnClickListener {
            selectOnlyTamano(3, popupView)
        }

        popupView.findViewById<View>(R.id.apply_filter).setOnClickListener{
            changeAnimalsState()

            popupWindow.dismiss()
            isFilterVisible = false

        }

        popupView.findViewById<View>(R.id.cancel_filter).setOnClickListener{
            popupWindow.dismiss()
            cancelNewFilterOptions(popupView)
        }


        // Animación
        popupView.findViewById<View>(R.id.cvPopup).apply {
            scaleX = 0.8f
            scaleY = 0.8f
            alpha = 0f
            animate().scaleX(1f).scaleY(1f).alpha(1f)
                .setDuration(300)
                .setInterpolator(DecelerateInterpolator())
                .start()
        }


        popupWindow.showAtLocation(anchor.rootView, Gravity.CENTER, 0, anchor.height)
    }

    private fun detectFocusSearcher(){
        val rootView = findViewById<View>(android.R.id.content)

        var isKeyboardVisible = false

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            val keyboardNowVisible = keypadHeight > screenHeight * 0.15

            if (keyboardNowVisible != isKeyboardVisible) {
                isKeyboardVisible = keyboardNowVisible

                if (!isKeyboardVisible) {
                    binding.etSearch.clearFocus()
                }
            }
        }
    }
}
