package com.example.lightweight.ui.NewWorkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.R

import com.example.lightweight.TopSpacingItemDecoration
import com.facebook.internal.Utility.arrayList

import kotlinx.android.synthetic.main.activity_new_gym_workout.*

class NewGymWorkoutActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter: ExerciseAdapter
    private  var exerciseList: MutableList<Exercise> = arrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_gym_workout)



        initRecyclerView()


        //ToDo samuel!! så här binder du en mutablelist av exercises till recycleviews,
        //ToDo de är gjorda med referens så du kan spara denna lista till db.
        exerciseAdapter.submitList(exerciseList)




        val addExerciseButton = findViewById<Button>(R.id.new_gym_add_exercise_button)
        addExerciseButton.setOnClickListener {

            exerciseAdapter.addExercise("tests")
        }

    }

    private fun initRecyclerView() {
        exercise_recycle_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            exerciseAdapter = ExerciseAdapter(this)
            adapter = exerciseAdapter
        }
    }


}
