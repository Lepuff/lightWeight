package com.example.lightweight.ui.NewWorkout


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import kotlinx.android.synthetic.main.layout_sets_item.view.*


class SetsAdapter(private val childRecyclerView: RecyclerView) :
    RecyclerView.Adapter<SetsAdapter.SetsViewHolder>() {

    private var sets: MutableList<Sets> = ArrayList()

    fun getSets() = sets

    override fun getItemCount(): Int {
        return sets.size
    }


    fun deleteSet(position: Int) {
        sets.removeAt(position)
        this.notifyDataSetChanged() // need to reload full list because of the set numbers
    }

    fun addSet() {
        var weight = 0
        var rep = 0

        if (sets.isNotEmpty()) {
            rep = sets[sets.size - 1].reps
            weight = sets[sets.size - 1].weight
        }

        sets.add(Sets(weight, rep))
        this.notifyItemInserted(sets.size - 1)
    }

    fun submitList(sets: MutableList<Sets>) {
        this.sets = sets
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetsViewHolder {
        return SetsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_sets_item,
                parent,
                false
            ), childRecyclerView
        )
    }


    class SetsViewHolder constructor(itemView: View, private val recyclerView: RecyclerView) :
        RecyclerView.ViewHolder(itemView) {
        private var selectedSet: Sets? = null

        private val setNumber: TextView = itemView.sets_number_textView
        private val setWeight: EditText = itemView.sets_weight_editText
        private val setsReps: EditText = itemView.sets_reps_editText
        val button: Button = itemView.findViewById<Button>(R.id.delete_set_button).apply {
            setOnClickListener {
                val adapter = recyclerView.adapter as SetsAdapter
                adapter.deleteSet(recyclerView.getChildLayoutPosition(itemView))
            }
        }


        fun bind(set: Sets,position: Int) {
            this.selectedSet = set

            setNumber.text = (position+1).toString()
            setWeight.setText(set.weight.toString())
            setsReps.setText(set.reps.toString())

        }
    }

    override fun onBindViewHolder(holder: SetsViewHolder, position: Int) {

        val set = sets[position]
        holder.bind(set,position)
    }
}