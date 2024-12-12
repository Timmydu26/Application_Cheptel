package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnimalAdapter(private val animalList: List<Animal>) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.textViewName)
        val speciesTextView: TextView = view.findViewById(R.id.textViewSpecies)
        val sexTextView: TextView = view.findViewById(R.id.textViewSex)
        val birthDateTextView: TextView = view.findViewById(R.id.textViewBirthDate)
        val lastAppointmentTextView: TextView = view.findViewById(R.id.textViewLastAppointment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animalList[position]
        holder.nameTextView.text = "Nom : ${animal.name}"
        holder.speciesTextView.text = "Espèce : ${animal.species}"
        holder.sexTextView.text = "Sexe : ${animal.sex}"
        holder.birthDateTextView.text = "Date de naissance : ${animal.birthDate}"
        holder.lastAppointmentTextView.text = "Dernier RDV : ${animal.lastAppointment}"
    }

    override fun getItemCount(): Int = animalList.size
}
