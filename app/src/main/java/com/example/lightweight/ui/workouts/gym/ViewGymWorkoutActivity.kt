package com.example.lightweight.ui.workouts.gym

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
import com.example.lightweight.adapters.ExerciseAdapter
import com.example.lightweight.classes.Exercise
import com.example.lightweight.classes.Sets
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_new_gym_workout.*


class ViewGymWorkoutActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var viewModel: GymViewModel
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_gym_workout)
        viewModel = ViewModelProviders.of(this).get(GymViewModel::class.java)
        setObservers()
        initRecyclerView()

        val newExerciseButton: Button = findViewById(R.id.new_gym_add_exercise_button)
        newExerciseButton.setOnClickListener {
            showNewExerciseDialog()
        }

        val savedButton: Button = findViewById(R.id.new_gym_save_workout_button)
        savedButton.setOnClickListener {
            updateGymDialog()
        }

        val editButton: Button = findViewById(R.id.new_gym_edit_workout_button)
        editButton.setOnClickListener {
            viewModel.isInEditState.value = true
        }
    }


    private fun setObservers(){
        viewModel.exerciseLiveData.observe(this,
            Observer {
                exerciseAdapter.notifyDataSetChanged()
            })
        viewModel.date.observe(this, Observer {
            title = viewModel.title.value
        })

        viewModel.isInEditState.observe(this, Observer {

            if (viewModel.isInEditState.value == true){
                exerciseAdapter.isEditable()
                findViewById<Button>(R.id.new_gym_save_workout_button).visibility = View.VISIBLE
                findViewById<Button>(R.id.new_gym_add_exercise_button).visibility = View.VISIBLE
                findViewById<Button>(R.id.new_gym_edit_workout_button).visibility = View.GONE
            }else {
                exerciseAdapter.isNotEditable()
                findViewById<Button>(R.id.new_gym_save_workout_button).visibility = View.GONE
                findViewById<Button>(R.id.new_gym_add_exercise_button).visibility = View.GONE
                findViewById<Button>(R.id.new_gym_edit_workout_button).visibility = View.VISIBLE
            }
        })
        viewModel.isLoadedFromDb.observe(this , Observer {
            if (viewModel.isLoadedFromDb.value == false){
                getGymWorkoutFromDb(intent.getStringExtra("id")!!) // todo fix constant
                viewModel.isLoadedFromDb.value = true
            }
        })
    }

    private fun getGymWorkoutFromDb(id: String) {

        val currentGymWorkoutRef = db.collection(Database.USERS)
            .document(Database.user.email!!).collection(Database.WORKOUTS).document(id)
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
                            tempSet.weight = currentSet[Database.WEIGHT].toString()
                            tempSet.reps = currentSet[Database.REPS].toString()
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
        exercise_recycle_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            exerciseAdapter = ExerciseAdapter(this)
            adapter = exerciseAdapter
            exerciseAdapter.submitList(viewModel.exerciseLiveData.value!!)
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

    private fun updateGymDialog() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_save_workout, null)
        val saveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)
        val workoutDate = viewModel.date.value
        dialogView.findViewById<TextInputEditText>(R.id.save_workout_date_editText).setText(workoutDate)
        val workoutTitle = viewModel.title.value
        dialogView.findViewById<TextInputEditText>(R.id.save_workout_title_editText).setText(workoutTitle)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val dialog = dialogBuilder.show()
        saveButton.setOnClickListener {
            updateGymWorkout(dialogView)
            dialog.cancel()
            finish()
        }
    }

    private fun updateGymWorkout(dialogView: View) {

        val currentGymWorkoutRef = db.collection(Database.USERS)
            .document(Database.user.email!!).collection(Database.WORKOUTS).document(intent.getStringExtra("id")!!) //todo fix constants

        currentGymWorkoutRef.update(Database.WORKOUT_TITLE,dialogView.findViewById<TextInputEditText>(R.id.save_workout_title_editText).text.toString())
        currentGymWorkoutRef.update(Database.WORKOUT_DATE,dialogView.findViewById<TextInputEditText>(R.id.save_workout_date_editText).text.toString())
        currentGymWorkoutRef.update(Database.EXERCISES,viewModel.exerciseLiveData.value)

    }
}
