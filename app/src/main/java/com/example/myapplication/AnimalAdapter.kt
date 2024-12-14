package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnimalAdapter(
    private val animalList: List<AnimalWithRelations> // Modèle avec relations pour inclure les noms d'espèces et autres
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textViewName)
        val speciesTextView: TextView = view.findViewById(R.id.textViewSpecies)
        val sexTextView: TextView = view.findViewById(R.id.textViewSex)
        val birthDateTextView: TextView = view.findViewById(R.id.textViewBirthDate)
        val identificationTextView: TextView = view.findViewById(R.id.textViewIdentification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animalWithRelations = animalList[position]
        val animal = animalWithRelations.animal
        val espece = animalWithRelations.espece

        holder.nameTextView.text = "Nom : ${animal.nom}"
        holder.speciesTextView.text = "Espèce : ${espece?.nom ?: "Inconnue"}"
        holder.sexTextView.text = "Sexe : ${if (animal.sexe == "M") "Mâle" else "Femelle"}"
        holder.birthDateTextView.text = "Date de naissance : ${animal.dateDeNaissance}"
        holder.identificationTextView.text = "Identification : ${animal.identification ?: "Aucune"}"
    }

    override fun getItemCount(): Int = animalList.size
}
