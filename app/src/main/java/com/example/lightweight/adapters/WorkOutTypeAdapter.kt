package com.example.lightweight.adapters

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.classes.Workout
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.layout_dialog_new_workout_list_item.view.*


class WorkOutTypeAdapter(private val dialog: Dialog) : RecyclerView.Adapter<WorkOutTypeAdapter.WorkOutTypeViewHolder>() {

    private var items: MutableList<Workout> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(workOutTypeList: MutableList<Workout>){
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
        private var  selectedWorkoutType : Workout? = null
        init {
           itemView.setOnClickListener(this)
        }

        val icon: AppCompatImageView = itemView.new_workout_icon_imageView
        val title: MaterialTextView = itemView.new_workout_textView

        fun bind(workOutType: Workout) {
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