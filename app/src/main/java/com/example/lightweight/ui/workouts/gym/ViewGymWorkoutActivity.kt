package com.example.lightweight.ui.workouts.gym

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.adapters.ExerciseAdapter
import com.example.lightweight.classes.Exercise
import com.example.lightweight.classes.Sets
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_new_gym_workout.*

class ViewGymWorkoutActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var viewModel: GymViewModel
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_gym_workout)
        val id = intent.getStringExtra("id")//todo fix with constants

        viewModel = ViewModelProviders.of(this).get(GymViewModel::class.java)
        viewModel.exerciseLiveData.observe(this,
            Observer {
                exerciseAdapter.notifyDataSetChanged()
            })

        viewModel.date.observe(this, Observer {
            title = viewModel.title.value
        })

        if (viewModel.alreadyLoaded.value == false){
            getGymWorkoutFromDb(id!!)
            viewModel.alreadyLoaded.value = true
        }

        initRecyclerView()
        exerciseAdapter.submitList(viewModel.exerciseLiveData.value!!)
        exerciseAdapter.isEditable(false)


        val newExerciseButton: Button = findViewById(R.id.new_gym_add_exercise_button)
        newExerciseButton.visibility = View.GONE
        val savedButton: Button = findViewById(R.id.new_gym_save_workout_button)
        savedButton.visibility = View.GONE

        val editButton: Button = findViewById(R.id.new_gym_edit_workout_button)
        editButton.visibility = View.VISIBLE

        editButton.setOnClickListener {
            exerciseAdapter.isEditable(true)
            savedButton.visibility = View.VISIBLE
            newExerciseButton.visibility = View.VISIBLE
            editButton.visibility = View.GONE
        }
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
        }
    }
}
