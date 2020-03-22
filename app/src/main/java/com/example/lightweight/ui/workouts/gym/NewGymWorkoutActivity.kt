package com.example.lightweight.ui.workouts.gym

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.ViewModels.GymViewModel

import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.adapters.ExerciseAdapter
import com.example.lightweight.ui.workouts.Keyboard
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


import kotlinx.android.synthetic.main.activity_gym_workout.*
import java.time.LocalDate

class NewGymWorkoutActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var viewModel: GymViewModel
    private val db = FirebaseFirestore.getInstance()
    private lateinit var workoutTitle: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_workout)

        viewModel = ViewModelProviders.of(this).get(
            GymViewModel::class.java
        )
        viewModel.exerciseLiveData.observe(
            this, Observer {
                exerciseAdapter.notifyDataSetChanged()
            }
        )
        initRecyclerView()
        exerciseAdapter.submitList(viewModel.exerciseLiveData.value!!)

        findViewById<Button>(R.id.gym_delete_workout_button).visibility =View.GONE
        findViewById<Button>(R.id.gym_edit_workout_button).visibility =View.GONE

        val addExerciseButton = findViewById<Button>(R.id.gym_add_exercise_button)
        addExerciseButton.visibility = View.VISIBLE
        addExerciseButton.setOnClickListener {
            showNewExerciseDialog()
        }

        val saveWorkoutButton = findViewById<Button>(R.id.gym_save_workout_button)
        saveWorkoutButton.visibility = View.VISIBLE
        saveWorkoutButton.setOnClickListener {
            saveGymDialog()
        }
    }

    private fun initRecyclerView() {
        gym_exercises_recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(20)
            addItemDecoration(topSpacingItemDecoration)
            exerciseAdapter =
                ExerciseAdapter(this)
            adapter = exerciseAdapter

        }
    }

    private fun showNewExerciseDialog() {
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        builder.setTitle(getString(R.string.choose_a_new_exercise))
        val listOfExercisesRef = db.collection(Database.TYPE_OF_EXERCISE).get()

        listOfExercisesRef.addOnSuccessListener { typeOfExercises ->
            //TODO cycle through "names" of the typeOfWorkout
            val typeOfExerciseList: MutableList<String> = ArrayList()
            for (typeOfExercise in typeOfExercises) {
                typeOfExerciseList.add(typeOfExercise[Database.NAME].toString())
            }
            builder.setItems(typeOfExerciseList.toTypedArray()) { _, which ->
                exerciseAdapter.addExercise(typeOfExerciseList[which])

            }
            val dialog = builder.create()

            dialog.show()
        }
    }

    @SuppressLint("InflateParams")
    private fun saveGymDialog() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_save_workout, null)
        val saveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)
        val currentDate = LocalDate.now().toString()
        dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_date_editText)
            .setText(currentDate)
        workoutTitle = dialogView.findViewById(R.id.dialog_save_workout_title_editText)
        workoutTitle.requestFocus()
        Keyboard().showKeyboard(this)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val dialog = dialogBuilder.show()

        saveButton.setOnClickListener {
            saveGymWorkout(dialogView, dialog)
            Keyboard().closeKeyboard(this)
        }
    }

    private fun saveGymWorkout(dialogView: View, dialog: AlertDialog) {

        val workoutDate =
            dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_date_editText)
                .text

        val currentGymWorkoutRef = db.collection(Database.USERS)
            .document(Database.getUserId()!!).collection(Database.WORKOUTS).document()
        if (TextUtils.isEmpty(workoutTitle.text.toString())) {
            workoutTitle.error = getString(R.string.field_cant_be_empty)
        } else {
            val workoutInfo = hashMapOf(
                Database.EXERCISES to viewModel.exerciseLiveData.value,
                Database.TIMESTAMP to FieldValue.serverTimestamp(),
                Database.TYPE_OF_WORKOUT to "gymWorkout",
                Database.WORKOUT_TITLE to workoutTitle.text.toString(),
                Database.WORKOUT_DATE to workoutDate.toString()
            )
            currentGymWorkoutRef.set(workoutInfo)
            dialog.cancel()
            finish()
        }
    }

}

