package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnimalAdapter(
    private var animalList: List<Animal>
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.animalNameTextView)
        val speciesTextView: TextView = view.findViewById(R.id.animalSpeciesTextView)
        val sexTextView: TextView = view.findViewById(R.id.animalSexTextView)
        val birthDateTextView: TextView = view.findViewById(R.id.animalBirthDateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animalList[position]

        holder.nameTextView.text = "Nom : ${animal.nom}"
        holder.speciesTextView.text = "Espèce ID : ${animal.especeid}" // Vous pouvez afficher le nom de l'espèce si disponible
        holder.sexTextView.text = "Sexe : ${if (animal.sexe == "M") "Mâle" else "Femelle"}"
        holder.birthDateTextView.text = "Date de naissance : ${animal.datedenaissance}"
    }

    override fun getItemCount(): Int = animalList.size

    fun updateData(newAnimalList: List<Animal>) {
        animalList = newAnimalList
        notifyDataSetChanged()
    }
}
