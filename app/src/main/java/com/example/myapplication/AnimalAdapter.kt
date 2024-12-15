package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnimalAdapter(
    private var animalList: List<AnimalListeActivity.AnimalWithSpecies>,
    private val onClick: (AnimalListeActivity.AnimalWithSpecies) -> Unit
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameAndStatusTextView: TextView = view.findViewById(R.id.textViewNameAndStatus)
        val speciesTextView: TextView = view.findViewById(R.id.textViewSpecies)
        val identificationTextView: TextView = view.findViewById(R.id.textViewIdentification)
        val birthDateTextView: TextView = view.findViewById(R.id.textViewBirthDate)
        val parentsTextView: TextView = view.findViewById(R.id.textViewParents)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animalWithSpecies = animalList[position]
        val animal = animalWithSpecies.animal

        // Afficher le nom et le statut sur la même ligne
        val statusSymbol = if (animal.vivant) "" else "†"
        holder.nameAndStatusTextView.text = "Nom : ${animal.nom} $statusSymbol"

        holder.speciesTextView.text = "Espèce : ${animalWithSpecies.especeNom}"
        holder.identificationTextView.text = "Identification : ${animal.identification}"
        holder.birthDateTextView.text = "Date de naissance : ${animal.datedenaissance}"
        holder.parentsTextView.text = "Parents : ${animal.identificationparent1 ?: "Inconnue"} & ${animal.identificationparent2 ?: "Inconnu"}"

        // Ajouter un événement de clic sur l'élément
        holder.itemView.setOnClickListener {
            onClick(animalWithSpecies)
        }
    }

    override fun getItemCount(): Int = animalList.size

    fun updateData(newData: List<AnimalListeActivity.AnimalWithSpecies>) {
        animalList = newData
        notifyDataSetChanged()
    }
}
