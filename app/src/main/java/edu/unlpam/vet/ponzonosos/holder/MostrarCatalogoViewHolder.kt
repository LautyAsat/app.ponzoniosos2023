package edu.unlpam.vet.ponzonosos.holder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.unlpam.vet.ponzonosos.databinding.ItemAnimalViewBinding
import edu.unlpam.vet.ponzonosos.model.Animal
import edu.unlpam.vet.ponzonosos.model.Imagen

class MostrarCatalogoViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemAnimalViewBinding.bind(view)

    fun render(animalData : Animal, onSelectedItem: () -> Unit){
        binding.tvAnimalName.text = animalData.nombre

        val principalImage: Imagen? = animalData.getPrincipalImage()
        val pathImage = binding.ivAnimal.context.filesDir.toString() + "/" + principalImage?.img

        if (principalImage != null) {
            Glide.with(binding.ivAnimal.context).load(pathImage).into(binding.ivAnimal)
        }


        Log.i("ponzolau", pathImage)


        binding.root.setOnClickListener { onSelectedItem() }
    }
}