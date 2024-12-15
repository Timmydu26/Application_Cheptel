package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnimalAdapter(
    private var animalList: List<AnimalListeActivity.AnimalWithParents>
) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textViewName)
        val birthDateTextView: TextView = view.findViewById(R.id.textViewBirthDate)
        val parentsTextView: TextView = view.findViewById(R.id.textViewParents)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animalList[position]

        holder.nameTextView.text = "Nom : ${animal.nom}"
        holder.birthDateTextView.text = "Date de naissance : ${animal.datedenaissance}"
        holder.parentsTextView.text = "Parents : ${animal.parent1Name} & ${animal.parent2Name}"
    }

    override fun getItemCount(): Int = animalList.size
}