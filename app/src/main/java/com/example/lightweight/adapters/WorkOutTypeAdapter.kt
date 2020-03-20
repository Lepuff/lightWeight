package com.example.lightweight.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.classes.AbstractWorkout
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.layout_dialog_new_workout_list_item.view.*


class WorkOutTypeAdapter(private val dialog: Dialog) : RecyclerView.Adapter<WorkOutTypeAdapter.WorkOutTypeViewHolder>() {

    private var items: MutableList<AbstractWorkout> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(workOutTypeList: MutableList<AbstractWorkout>){
        items = workOutTypeList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOutTypeViewHolder {
        return WorkOutTypeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_dialog_new_workout_list_item,
                parent,
                false
            ), dialog
        )
    }



    class WorkOutTypeViewHolder constructor(itemView: View, private val dialog: Dialog) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var  selectedWorkoutType : AbstractWorkout? = null
        init {
           itemView.setOnClickListener(this)
        }

        val icon: AppCompatImageView = itemView.new_workout_icon
        val title: MaterialTextView = itemView.new_workout_textView

        fun bind(workOutType: AbstractWorkout) {
            this.selectedWorkoutType = workOutType
            icon.setImageResource(workOutType.icon)
            title.text = workOutType.title
        }

        override fun onClick(v: View?) {
            dialog.dismiss()
            selectedWorkoutType?.newWorkout(itemView.context)
        }
    }

    override fun onBindViewHolder(holder: WorkOutTypeViewHolder, position: Int) {
        val workoutType = items[position]
        holder.bind(workoutType)
    }
}