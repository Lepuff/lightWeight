package com.example.lightweight.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.classes.Exercise
import kotlinx.android.synthetic.main.layout_exercises_list_item.view.*


class ViewExerciseAdapter() :
    RecyclerView.Adapter<ViewExerciseAdapter.ExerciseViewHolder>() {


    private var exercises: MutableList<Exercise> = ArrayList()

    fun submitList(workOutList: MutableList<Exercise>) {
        exercises = workOutList

    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_view_exercise_list_item,
                parent,
                false
            )
        )
    }


    class ExerciseViewHolder constructor(
        itemView: View

    ) : RecyclerView.ViewHolder(itemView) {
        private var selectedExercise: Exercise? = null
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.details_sets_recycle_view)
        private val exerciseName: TextView = itemView.exercise_name_textView


        fun bind(exercise: Exercise) {
            this.selectedExercise = exercise
            exerciseName.text = exercise.name
        }
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        lateinit var setsAdapter: ViewSetsAdapter
        holder.childRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(10)
            addItemDecoration(topSpacingItemDecoration)
            setsAdapter = ViewSetsAdapter()
            adapter = setsAdapter
            val exercise = exercises[position]
            setsAdapter.submitList(exercise.sets)
            holder.bind(exercise)
        }
    }
}