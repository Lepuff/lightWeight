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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

import kotlinx.android.synthetic.main.activity_new_gym_workout.*
import java.time.LocalDate

class NewGymWorkoutActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var newGymWorkoutViewModel: NewGymWorkoutViewModel
    private lateinit var exerciseName: String
    private val db = FirebaseFirestore.getInstance()
    private var workoutsRef = db.collection("users").document(Database.user.email!!)
        .collection("workouts")

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


                val exerciseList = newGymWorkoutViewModel.getExerciseList().value!!
                val workoutTitle =
                    dialogView.findViewById<TextInputEditText>(R.id.new_workout_name_editText)
                        .text

                val workoutDate =
                    dialogView.findViewById<TextInputEditText>(R.id.new_workout_date_editText)
                        .text


                val workoutInfo = hashMapOf(
                    "timestamp" to FieldValue.serverTimestamp(),
                    "typeOfWorkout" to "gymWorkout",
                    "workoutTitle" to workoutTitle.toString(),
                    "workoutDate" to workoutDate.toString()
                )

                var currentGymWorkoutRef = db.collection("users")
                    .document(Database.user.email!!).collection("workouts").document()

                /*db.collection("users")
                    .document(Database.user.email!!).collection("workouts").document("test123").set(workoutInfo)
                    .addOnFailureListener { e ->
                        Log.w("TAG", "Error adding document", e)
                    }*/


                for (exercise in exerciseList) {
                    var setNumber = 0
                    val currentExercise = currentGymWorkoutRef.collection(exercise.name)
                    for (sets in exercise.sets) {
                        setNumber++
                        currentExercise.document("Set $setNumber").set(sets)
                    }
                }
                currentGymWorkoutRef.set(workoutInfo)
                    .addOnFailureListener { e ->
                        Log.w("TAG", "Error adding document", e)
                    }

                dialog.cancel()
                finish()

            }


        }


    }

    override fun onStart() {
        super.onStart()
        //TODO workoutsRef.addSnapshotListener
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
