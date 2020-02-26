package com.example.lightweight.ui.NewWorkout


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.Exercise
import com.example.lightweight.R


class ExerciseAdapter : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    private var exercises: List<Exercise> = ArrayList()
    override fun getItemCount(): Int {
        return exercises.size
    }

    fun submitList(workOutList: List<Exercise>) {
        exercises = workOutList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {

        return ExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_exercises_list_item,
                parent,
                false
            )
        )
    }


    class ExerciseViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var selectedworkout: Exercise? = null


        fun bind(workOut: Exercise) {
            this.selectedworkout = workOut
        }

        override fun onClick(v: View?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}