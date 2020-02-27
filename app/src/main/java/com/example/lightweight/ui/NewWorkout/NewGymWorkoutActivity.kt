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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_gym_workout)



        initRecyclerView()

        val testlist : ArrayList<Sets> = arrayList()
        testlist.add(Sets(1,3,33))
        val testexercise : Exercise = Exercise("testsname",testlist)

        val testExerciselist : ArrayList<Exercise> = arrayList()
        testExerciselist.add(testexercise)
        exerciseAdapter.submitList(testExerciselist)


        val addExerciseButton = findViewById<Button>(R.id.new_gym_add_exercise_button)
        addExerciseButton.setOnClickListener {



        }

    }

    private fun initRecyclerView() {
        new_gym_recycle_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            exerciseAdapter = ExerciseAdapter()
            adapter = exerciseAdapter
        }
    }







}
