package com.example.lightweight.ui.workoutDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.R
import com.example.lightweight.adapters.ExerciseAdapter
import com.example.lightweight.adapters.ViewExerciseAdapter
import com.example.lightweight.classes.Exercise
import com.example.lightweight.classes.Sets
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.ui.newWorkout.gym.NewGymWorkoutActivity
import kotlinx.android.synthetic.main.activity_gym_workout_details.*
import kotlinx.android.synthetic.main.activity_new_gym_workout.*

class ViewGymWorkoutActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter : ViewExerciseAdapter
    private lateinit var viewModel : ViewEditGymWorkoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_workout_details)

        viewModel = ViewModelProviders.of(this).get(ViewEditGymWorkoutViewModel::class.java)
        viewModel.init()
        viewModel.getExerciseList().observe(this,
            Observer<MutableList<Exercise>> { exerciseAdapter.notifyDataSetChanged() })
        initRecyclerView()
        exerciseAdapter.submitList(viewModel.getExerciseList().value!!)


        val editButton : Button = findViewById(R.id.gym_details_edit_button)
        editButton.setOnClickListener {
            val intent = Intent(this, EditGymWorkoutActivity::class.java)
            startActivity(intent)

        }
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
