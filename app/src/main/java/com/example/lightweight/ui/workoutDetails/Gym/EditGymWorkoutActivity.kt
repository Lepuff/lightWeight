package com.example.lightweight.ui.workoutDetails.Gym

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.R
import com.example.lightweight.adapters.ExerciseAdapter
import com.example.lightweight.classes.Exercise
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_new_gym_workout.*
import java.time.LocalDate

class EditGymWorkoutActivity : AppCompatActivity() {
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var viewModel: ViewEditGymWorkoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_gym_workout)
        viewModel = ViewModelProviders.of(this).get(ViewEditGymWorkoutViewModel::class.java)
        initRecyclerView()
        viewModel.init()
        viewModel.getExerciseList().observe(this,
            Observer<MutableList<Exercise>> { exerciseAdapter.notifyDataSetChanged() })
        exerciseAdapter.submitList(viewModel.getExerciseList().value!!)

        val addExerciseButton = findViewById<Button>(R.id.new_gym_add_exercise_button)
        addExerciseButton.setOnClickListener {

            exerciseAdapter.addExercise("tests")
        }

        val saveWorkoutButton = findViewById<Button>(R.id.new_gym_save_workout_button)
        saveWorkoutButton.setOnClickListener {
            val dialogView =
                LayoutInflater.from(this).inflate(R.layout.dialog_save_workout, null)
            val saveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)
            val currentDate = LocalDate.now().toString()
            dialogView.findViewById<TextInputEditText>(R.id.save_workout_date_editText)
                .setText(currentDate)

            val dialogBuilder = AlertDialog.Builder(this)
                .setView(dialogView)
            val dialog = dialogBuilder.show()

            saveButton.setOnClickListener {


                viewModel.getExerciseList()
                val workoutTitle =
                    dialogView.findViewById<TextInputEditText>(R.id.save_workout_title_editText)
                        .text

                val workoutDate =
                    dialogView.findViewById<TextInputEditText>(R.id.save_workout_date_editText)
                        .text

                //TODO en yttre for loop som går igenom size (alla exercises)
                //TODO en inre for loop som går igenom sets

                dialog.cancel()
                finish()

            }


        }

    }

    private fun initRecyclerView() {
        exercise_recycle_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            exerciseAdapter =
                ExerciseAdapter(this)
            adapter = exerciseAdapter
        }
    }


}