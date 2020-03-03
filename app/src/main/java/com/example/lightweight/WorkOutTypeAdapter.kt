package com.example.lightweight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.classes.AbstractWorkout
import kotlinx.android.synthetic.main.layout_dialog_new_workout_list_item.view.*


class WorkOutTypeAdapter : RecyclerView.Adapter<WorkOutTypeAdapter.WorkOutTypeViewHolder>() {

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
            )
        )
    }



    class WorkOutTypeViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var  selectedWorkoutType : AbstractWorkout? = null
        init {
           itemView.setOnClickListener(this)
        }

        val icon = itemView.new_workout_icon
        val title = itemView.new_workout_textView

        fun bind(workOutType: AbstractWorkout) {
            this.selectedWorkoutType = workOutType
            icon.setImageResource(workOutType.icon)
            title.text = workOutType.title
        }

        override fun onClick(v: View?) {
            selectedWorkoutType?.newWorkout(itemView.context)
        }
    }

    override fun onBindViewHolder(holder: WorkOutTypeViewHolder, position: Int) {
        val workoutType = items[position]
        holder.bind(workoutType)
    }
}