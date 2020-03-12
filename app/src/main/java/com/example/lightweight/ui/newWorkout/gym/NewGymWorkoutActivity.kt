package com.example.lightweight.ui.newWorkout.gym

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.Database
import com.example.lightweight.R

import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.adapters.ExerciseAdapter
import com.example.lightweight.classes.Exercise
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions


import kotlinx.android.synthetic.main.activity_new_gym_workout.*
import java.time.LocalDate
import java.time.LocalDateTime

class NewGymWorkoutActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var newGymWorkoutViewModel: NewGymWorkoutViewModel

    private val db = FirebaseFirestore.getInstance()

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


        val addExerciseButton = findViewById<Button>(R.id.new_gym_add_exercise_button)
        addExerciseButton.setOnClickListener {
            showNewExerciseDialog()
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


                val exerciseList = newGymWorkoutViewModel.getExerciseList().value!!
                val workoutTitle =
                    dialogView.findViewById<TextInputEditText>(R.id.save_workout_title_editText)
                        .text

                val workoutDate =
                    dialogView.findViewById<TextInputEditText>(R.id.save_workout_date_editText)
                        .text


                var currentGymWorkoutRef = db.collection("users")
                    .document(Database.user.email!!).collection("workouts").document()


                val workoutInfo = hashMapOf(
                    "exercises" to exerciseList,
                    "timestamp" to FieldValue.serverTimestamp(),
                    "typeOfWorkout" to "gymWorkout",
                    "workoutTitle" to workoutTitle.toString(),
                    "workoutDate" to workoutDate.toString()
                )

                //adds current workout to database
                currentGymWorkoutRef.set(workoutInfo)

                dialog.cancel()
                finish()

            }


        }


    }

    override fun onStart() {
        super.onStart()
        //TODO workoutsRef.addSnapshotListener
        //TODO findViewById<EditText>(R.id.sets_weight_editText).requestFocus()
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


    private fun showNewExerciseDialog() {

        val builder = AlertDialog.Builder(this,R.style.DialogStyle)
        builder.setTitle("Choose a new Exercise") //todo string res


         getExercisesFromDb().addOnSuccessListener { typeOfExercises ->
             //TODO cycle through "names" of the typeOfWorkout
             val typeOfExerciseList:  MutableList<String> = ArrayList()
             for (typeOfExercise in typeOfExercises){
                 typeOfExerciseList.add(typeOfExercise["name"].toString())
             }
             builder.setItems(typeOfExerciseList.toTypedArray()) {
                     dialog, which ->
                 exerciseAdapter.addExercise(typeOfExerciseList[which])

             }
             val dialog = builder.create()

             dialog.show()
         }




    }
    private fun getExercisesFromDb(): Task<QuerySnapshot> {
        val listOfExercisesRef =
            db.collection("typeOfExercise")
        return listOfExercisesRef.get()
    }
}

