package com.example.lightweight.ui.newWorkout.gym

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.Database
import com.example.lightweight.R

import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.adapters.ExerciseAdapter
import com.example.lightweight.classes.Exercise
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_new_gym_workout.*
import java.time.LocalDate

class NewGymWorkoutActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var newGymWorkoutViewModel: NewGymWorkoutViewModel
    private lateinit var exerciseName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_gym_workout)

        newGymWorkoutViewModel = ViewModelProviders.of(this).get(
            NewGymWorkoutViewModel::class.java
        )
        newGymWorkoutViewModel.init()
        newGymWorkoutViewModel.getExerciseList().observe(this,
            Observer<MutableList<Exercise>> { exerciseAdapter.notifyDataSetChanged() })



        initRecyclerView()
        exerciseAdapter.submitList(newGymWorkoutViewModel.getExerciseList().value!!)

        var i = 1
        val addExerciseButton = findViewById<Button>(R.id.new_gym_add_exercise_button)
        addExerciseButton.setOnClickListener {
            exerciseName = "Test $i" //todo remove
            exerciseAdapter.addExercise(exerciseName)
            i++

        }

        val saveWorkoutButton = findViewById<Button>(R.id.new_gym_save_workout_button)
        saveWorkoutButton.setOnClickListener {
            val dialogView =
                LayoutInflater.from(this).inflate(R.layout.dialog_save_workout, null)
            val saveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)
            val currentDate = LocalDate.now().toString()
            dialogView.findViewById<TextInputEditText>(R.id.new_workout_date_editText)
                .setText(currentDate)

            val dialogBuilder = AlertDialog.Builder(this)
                .setView(dialogView)


            val dialog = dialogBuilder.show()

            saveButton.setOnClickListener {


                val db = FirebaseFirestore.getInstance()

                val exerciseList = newGymWorkoutViewModel.getExerciseList().value!!
                val workoutTitle =
                    dialogView.findViewById<TextInputEditText>(R.id.new_workout_name_editText)
                        .text

                val workoutDate =
                    dialogView.findViewById<TextInputEditText>(R.id.new_workout_date_editText)
                        .text


                val workoutInfo = hashMapOf(
                    "Title of workout" to workoutTitle,
                    "Date of workout" to workoutDate
                )

                var currentGymWorkoutRef =
                    db.collection("users").document(Database.user.email!!).collection("workouts")
                        .document("Gym")//.collection("Gym Workouts").document()

                currentGymWorkoutRef.set(workoutInfo)

                exerciseList.forEach {
                    var setNumber = 0
                    for (sets in it.sets) {
                        setNumber++
                        currentGymWorkoutRef.collection(it.name).document("Set $setNumber")
                            .set(sets)
                            .addOnSuccessListener { documentReference ->
                                Log.d("TAG", "DocumentSnapshot added with ID: $documentReference")
                            }
                            .addOnFailureListener { e ->
                                Log.d("TAG", "Error adding Set", e)
                            }
                    }
                }


                for (exercise in exerciseList) {


                dialog.cancel()
                finish()

            }


        }
    }

    private fun initRecyclerView() {
        exercise_recycle_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingItemDecoration)
            exerciseAdapter =
                ExerciseAdapter(this)
            adapter = exerciseAdapter
        }
    }
}




