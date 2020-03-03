package com.example.lightweight.ui.workoutDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.R
import com.example.lightweight.adapters.ExerciseAdapter
import com.example.lightweight.adapters.ViewExerciseAdapter
import com.example.lightweight.classes.Exercise
import com.example.lightweight.classes.Sets
import com.example.lightweight.ui.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_gym_workout_details.*
import kotlinx.android.synthetic.main.activity_new_gym_workout.*

class GymWorkoutDetailsActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter : ViewExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_workout_details)

        

        //Todo remove after testing
        val exerciseList : MutableList<Exercise> = ArrayList()
        val setTest = Sets("3","3")
        val exerciseTest = Exercise("test")
        exerciseTest.sets.add(setTest)
        exerciseTest.sets.add(setTest)
        exerciseTest.sets.add(setTest)
        exerciseList.add(exerciseTest)
        exerciseList.add(exerciseTest)
        exerciseList.add(exerciseTest)
        initRecyclerView()
        exerciseAdapter.submitList(exerciseList)




    }
    private fun initRecyclerView() {
        gym_details_recycle_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            exerciseAdapter =
                ViewExerciseAdapter()
            adapter = exerciseAdapter
        }
    }
}
