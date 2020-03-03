package com.example.lightweight.ui.NewWorkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.R

import com.example.lightweight.TopSpacingItemDecoration
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_new_gym_workout.*
import java.time.LocalDate

class NewGymWorkoutActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var newGymWorkoutViewModel: NewGymWorkoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_gym_workout)
        val db = FirebaseFirestore.getInstance() //gets access to db
        newGymWorkoutViewModel = ViewModelProviders.of(this).get(
            NewGymWorkoutViewModel::class.java
        )
        newGymWorkoutViewModel.init()
        newGymWorkoutViewModel.getExerciseList().observe(this,
            Observer<MutableList<Exercise>> { exerciseAdapter.notifyDataSetChanged() })



        initRecyclerView()
        exerciseAdapter.submitList(newGymWorkoutViewModel.getExerciseList().value!!)


        val addExerciseButton = findViewById<Button>(R.id.new_gym_add_exercise_button)
        addExerciseButton.setOnClickListener {

            exerciseAdapter.addExercise("tests")


        }

        val saveWorkoutButton = findViewById<Button>(R.id.new_gym_save_workout_button)
        saveWorkoutButton.setOnClickListener {
            val dialogView =
                LayoutInflater.from(this).inflate(R.layout.dialog_save_gym_workout, null)
            val saveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)
            val currentDate = LocalDate.now().toString()
            dialogView.findViewById<TextInputEditText>(R.id.new_workout_date_editText)
                .setText(currentDate)

            val dialogBuilder = AlertDialog.Builder(this)
                .setView(dialogView)


            val dialog = dialogBuilder.show()




            saveButton.setOnClickListener {


                newGymWorkoutViewModel.getExerciseList() //todo detta är sjävla listan.
                val workoutTitle =
                    dialogView.findViewById<TextInputEditText>(R.id.new_workout_name_editText)
                        .text //todo titel
                // TODO db.collection("users")
                val workoutDate =
                    dialogView.findViewById<TextInputEditText>(R.id.new_workout_date_editText)
                        .text //todo datum

                dialog.cancel()
                finish()

            }


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




