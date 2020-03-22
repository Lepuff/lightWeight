package com.example.lightweight.ui.workouts.gym

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.viewModels.GymViewModel
import com.example.lightweight.adapters.ExerciseAdapter
import com.example.lightweight.classes.Exercise
import com.example.lightweight.classes.Sets
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_gym_workout.*


class ViewGymWorkoutActivity : AppCompatActivity() {


    private val itemPaddingTop = 30
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var viewModel: GymViewModel
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_workout)
        viewModel = ViewModelProviders.of(this).get(GymViewModel::class.java)
        setObservers()
        initRecyclerView()

        val newExerciseButton: Button = findViewById(R.id.gym_add_exercise_button)
        newExerciseButton.setOnClickListener {
            showNewExerciseDialog()
        }

        val saveButton = findViewById<Button>(R.id.gym_save_workout_button)
        saveButton.setOnClickListener {
            updateGymDialog()
        }

        val editButton = findViewById<Button>(R.id.gym_edit_workout_button)
        editButton.setOnClickListener {
            viewModel.isInEditState.value = true
        }

        val deleteButton = findViewById<Button>(R.id.gym_delete_workout_button)
        deleteButton.setOnClickListener {
            deleteWorkoutDialog()
        }

    }
    private fun deleteWorkoutDialog() {

        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        builder.setTitle(R.string.delete_workout_message)
        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            deleteGymWorkout()
            dialog.cancel()
            finish()
        }
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun deleteGymWorkout() {
        db.collection(Database.USERS).document(intent.getStringExtra("userId")!!)
            .collection(Database.WORKOUTS).document(intent.getStringExtra("id")!!).delete()
            .addOnSuccessListener {
                Log.d(
                    "viewGymWorkoutActivity Delete:",
                    "DocumentSnapshot successfully deleted!"
                )
            }
            .addOnFailureListener { e ->
                Log.w(
                    "viewGymWorkoutActivity Delete:",
                    "Error deleting document",
                    e
                )
            }
    }


    private fun setObservers() {
        viewModel.exercisesList.observe(this,
            Observer {
                exerciseAdapter.notifyDataSetChanged()
            })
        viewModel.date.observe(this, Observer {
            title = viewModel.title.value
        })

        viewModel.isInEditState.observe(this, Observer {

            if (Database.getUserId() == intent.getStringExtra("userId")) {
                if (viewModel.isInEditState.value == true) {
                    exerciseAdapter.isEditable()
                    findViewById<Button>(R.id.gym_save_workout_button).visibility = View.VISIBLE
                    findViewById<Button>(R.id.gym_add_exercise_button).visibility = View.VISIBLE
                    findViewById<Button>(R.id.gym_edit_workout_button).visibility = View.GONE
                    findViewById<Button>(R.id.gym_delete_workout_button).visibility = View.GONE
                } else {
                    exerciseAdapter.isNotEditable()
                    findViewById<Button>(R.id.gym_save_workout_button).visibility = View.GONE
                    findViewById<Button>(R.id.gym_add_exercise_button).visibility = View.GONE
                    findViewById<Button>(R.id.gym_edit_workout_button).visibility = View.VISIBLE
                    findViewById<Button>(R.id.gym_delete_workout_button).visibility = View.VISIBLE
                }
            }
            else
            {
                exerciseAdapter.isNotEditable()
                findViewById<Button>(R.id.gym_save_workout_button).visibility = View.GONE
                findViewById<Button>(R.id.gym_add_exercise_button).visibility = View.GONE
                findViewById<Button>(R.id.gym_edit_workout_button).visibility = View.GONE
                findViewById<Button>(R.id.gym_delete_workout_button).visibility = View.GONE
            }
        })
        viewModel.isLoadedFromDb.observe(this, Observer {
            if (viewModel.isLoadedFromDb.value == false) {
                getGymWorkoutFromDb()
                viewModel.isLoadedFromDb.value = true
            }
        })
    }

    private fun getGymWorkoutFromDb() {

        val currentGymWorkoutRef = db.collection(Database.USERS)
            .document(intent.getStringExtra("userId")!!).collection(Database.WORKOUTS)
            .document(intent.getStringExtra("id")!!)// todo fix constant
        currentGymWorkoutRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val exerciseList = document[Database.EXERCISES] as ArrayList<*>
                    for (exercise in exerciseList) {
                        val currentExercise = exercise as HashMap<*, *>
                        val sets = currentExercise[Database.SETS] as ArrayList<*>
                        val tempExercise = Exercise(currentExercise[Database.NAME].toString())
                        for (set in sets) {
                            val currentSet = set as HashMap<*, *>
                            val tempSet = Sets()
                            tempSet.weight = currentSet[Database.WEIGHT].toString().toInt()
                            tempSet.reps = currentSet[Database.REPS].toString().toInt()
                            tempExercise.sets.add(tempSet)
                        }
                        exerciseAdapter.addExercise(tempExercise)
                    }
                    viewModel.title.value = document[Database.WORKOUT_TITLE].toString()
                    viewModel.date.value = document[Database.WORKOUT_DATE].toString()

                } else {
                    Log.d("view gym workout document query:", "No such document")
                }
            }
    }

    private fun initRecyclerView() {
        gym_exercises_recycle_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(itemPaddingTop)
            addItemDecoration(topSpacingItemDecoration)
            exerciseAdapter = ExerciseAdapter(this)
            adapter = exerciseAdapter
            exerciseAdapter.submitList(viewModel.exercisesList.value!!)
        }
    }

    private fun showNewExerciseDialog() {
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        builder.setTitle(getString(R.string.choose_a_new_exercise))
        val listOfExercisesRef = db.collection(Database.TYPE_OF_EXERCISE).get()
        listOfExercisesRef.addOnSuccessListener { typeOfExercises ->
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
    private fun updateGymDialog() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_save_workout, null)
        val saveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)

        val dateEditText = dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_date_editText)
            dateEditText.setText(viewModel.date.value.toString())

        val titleEditText = dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_title_editText)
        titleEditText.setText(viewModel.title.value.toString())

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val dialog = dialogBuilder.show()
        saveButton.setOnClickListener {
            viewModel.isInEditState.value = false
            updateGymWorkout(dialogView)
            finish()
            dialog.cancel()
        }
    }



    private fun updateGymWorkout(dialogView: View) {

        val currentGymWorkoutRef = db.collection(Database.USERS)
            .document(intent.getStringExtra("userId")!!).collection(Database.WORKOUTS)
            .document(intent.getStringExtra("id")!!) //todo fix constants

        currentGymWorkoutRef.update(
            Database.WORKOUT_TITLE,
            dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_title_editText).text.toString()
        )
        currentGymWorkoutRef.update(
            Database.WORKOUT_DATE,
            dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_date_editText).text.toString()
        )
        currentGymWorkoutRef.update(Database.EXERCISES, viewModel.exercisesList.value)

    }
}
