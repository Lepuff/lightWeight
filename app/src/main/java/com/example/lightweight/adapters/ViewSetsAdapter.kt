package com.example.lightweight.adapters



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.classes.Sets
import kotlinx.android.synthetic.main.layout_details_sets_list_item.view.*
import kotlinx.android.synthetic.main.layout_sets_item.view.*


class ViewSetsAdapter() :
    RecyclerView.Adapter<ViewSetsAdapter.SetsViewHolder>() {

    private var sets: MutableList<Sets> = ArrayList()

    fun submitList(sets: MutableList<Sets>) {
        this.sets = sets
    }

    override fun getItemCount(): Int {
        return sets.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetsViewHolder {
        return SetsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_details_sets_list_item,
                parent,
                false
            )
        )
    }

    class SetsViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private var selectedSet: Sets? = null


        private val setNumber: TextView = itemView.details_sets_number_textView
        private val setWeight: EditText = itemView.details_sets_weight_textView
        private val setsReps: EditText = itemView.details_sets_reps_textView



        fun bind(set: Sets, position: Int) {
            this.selectedSet = set
            setNumber.text = (position + 1).toString()
            setWeight.setText(set.weight)
            setsReps.setText(set.reps)

        }
    }

    override fun onBindViewHolder(holder: SetsViewHolder, position: Int) {
        val set = sets[position]
        holder.bind(set, position)
    }
}