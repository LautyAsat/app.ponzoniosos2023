package edu.unlpam.vet.ponzonosos.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.unlpam.vet.ponzonosos.holder.MostrarCatalogoViewHolder
import edu.unlpam.vet.ponzonosos.model.Animal
import edu.unlpam.vet.ponzonosos.R

class MostrarCatalogoAdapter(
    val animals: List<Animal>,
    val onSelectItem: (Long) -> Unit
) : RecyclerView.Adapter<MostrarCatalogoViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MostrarCatalogoViewHolder {
        return MostrarCatalogoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_animal_view, parent, false))
    }

    override fun onBindViewHolder(holder: MostrarCatalogoViewHolder, position: Int) {
        holder.render(animals[position], onSelectItem)
    }

    override fun getItemCount() = animals.size

}