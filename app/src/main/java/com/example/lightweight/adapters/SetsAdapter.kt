package com.example.lightweight.adapters


import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.classes.Sets
import kotlinx.android.synthetic.main.layout_sets_item.view.*


class SetsAdapter(private val childRecyclerView: RecyclerView) :
    RecyclerView.Adapter<SetsAdapter.SetsViewHolder>() {
    private var isEditable: Boolean = true
    private var sets: MutableList<Sets> = ArrayList()

    fun submitList(sets: MutableList<Sets>) {
        this.sets = sets
    }


    fun deleteSet(position: Int) {
        sets.removeAt(position - 1)
        this.notifyDataSetChanged() // need to reload full list because of the set numbers
    }


    fun addSet() {
        var weight: Int? = 0
        var rep : Int? = 0

        if (sets.isNotEmpty()) {
            rep = sets[sets.size - 1].reps
            weight = sets[sets.size - 1].weight
        }

        sets.add(Sets(weight, rep))
        this.notifyItemInserted(sets.size - 1)
    }

    fun isEditable(boolean: Boolean) {
        isEditable = boolean
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return sets.size
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

    class SetsViewHolder constructor(
        itemView: View,
        private val recyclerView: RecyclerView

    ) :
        RecyclerView.ViewHolder(itemView) {
        private var selectedSet: Sets? = null
        private val adapter = recyclerView.adapter as SetsAdapter

        private val setNumber: TextView = itemView.sets_number_textView
        private val setWeight: EditText = itemView.sets_weight_editText.apply {
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val position = adapterPosition
                    val weight = s.toString()
                    if (weight.isEmpty()){
                        adapter.sets[position].weight = 0

                    }else {
                        adapter.sets[position].weight = s.toString().toInt()
                    }
                }


            })

        }
        private val setsReps: EditText = itemView.sets_reps_editText.apply {
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }


                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val position = adapterPosition
                    val reps = s.toString()
                    if (reps.isEmpty()){
                        adapter.sets[position].reps = 0

                    }else {
                        adapter.sets[position].reps = s.toString().toInt()
                    }
                }
            }
            )
        }
        val button: Button = itemView.findViewById<Button>(R.id.delete_set_button).apply {
            setOnClickListener {
                adapter.deleteSet(recyclerView.getChildLayoutPosition(itemView) + 1)
            }
        }

        fun setEditable(editable: Boolean) {

            if (editable){
                button.visibility = View.VISIBLE
            }else{
                button.visibility = View.GONE
            }
            setWeight.isEnabled = editable
            setsReps.isEnabled = editable
        }


        fun bind(set: Sets, position: Int) {
            this.selectedSet = set
            setNumber.text = (position + 1).toString()// +1 for correct set numbers

            if (set.weight == 0)setWeight.setText("")
            else setWeight.setText(set.weight.toString())

            if (set.reps == 0) setsReps.setText("")
            else setsReps.setText(set.reps.toString())


        }
    }

    override fun onBindViewHolder(holder: SetsViewHolder, position: Int) {
        holder.setEditable(isEditable)
        val set = sets[position]
        holder.bind(set, position)
    }
}