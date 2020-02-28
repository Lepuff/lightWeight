package com.example.lightweight.ui.NewWorkout


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.layout_exercises_list_item.view.*


class ExerciseAdapter(private val parentRecyclerView: RecyclerView) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {


    private var exercises: MutableList<Exercise> = ArrayList()

    fun submitList(workOutList: MutableList<Exercise>) {
        exercises = workOutList
    }

    fun addExercise(name: String) {
        exercises.add(Exercise(name))
        notifyItemInserted(exercises.size)
    }

    fun deleteExercise(position: Int) {
        exercises.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }


    override fun getItemCount(): Int {
        return exercises.size
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_exercises_list_item,
                parent,
                false
            ), parentRecyclerView
        )
    }


    class ExerciseViewHolder constructor(
        itemView: View,
        private val parentRecyclerView: RecyclerView
    ) : RecyclerView.ViewHolder(itemView) {
        private var selectedExercise: Exercise? = null
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.sets_recycle_view)
        private val exerciseName: TextView = itemView.exercise_name_textView


        val deleteExerciseButton: ImageButton =
            itemView.findViewById<ImageButton>(R.id.delete_exercise_button).apply {
                setOnClickListener {
                    val exerciseAdapter = parentRecyclerView.adapter as ExerciseAdapter
                    exerciseAdapter.deleteExercise(
                        parentRecyclerView.getChildLayoutPosition(
                            itemView
                        )
                    )

                }
            }


        val newSetButton: Button = itemView.findViewById<Button>(R.id.new_set_button).apply {
            setOnClickListener {
                val setsAdapter = childRecyclerView.adapter as SetsAdapter
                setsAdapter.addSet()
            }
        }


        fun bind(exercise: Exercise) {
            this.selectedExercise = exercise
            exerciseName.text = exercise.name
        }
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        lateinit var setsAdapter: SetsAdapter
        holder.childRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration = TopSpacingItemDecoration(10)
            addItemDecoration(topSpacingItemDecoration)
            setsAdapter = SetsAdapter(this)
            adapter = setsAdapter
        }

        val exercise = exercises[position]
        setsAdapter.submitList(exercise.sets)
        holder.bind(exercise)
    }
}