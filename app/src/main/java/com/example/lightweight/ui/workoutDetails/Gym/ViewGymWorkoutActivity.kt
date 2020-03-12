package com.example.lightweight.ui.workoutDetails.Gym

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.adapters.ViewExerciseAdapter
import com.example.lightweight.classes.Exercise
import com.example.lightweight.classes.Sets
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_gym_workout_details.*

class ViewGymWorkoutActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter: ViewExerciseAdapter
    private lateinit var viewModel: ViewEditGymWorkoutViewModel
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_workout_details)
        val id = intent.getStringExtra("id") //todo samme , detta är id i string.
        val workoutTitle = intent.getStringExtra("workoutTitle")
        title = workoutTitle
        viewModel = ViewModelProviders.of(this).get(ViewEditGymWorkoutViewModel::class.java)
        viewModel.exerciseList.observe(this,
            Observer<MutableList<Exercise>> { exerciseAdapter.notifyDataSetChanged() })
        initRecyclerView()
        exerciseAdapter.submitList(viewModel.exerciseList.value!!)

        Log.d("Test after intent", id!!.toString())
        val currentGymWorkoutRef = db.collection("users")
            .document(Database.user.email!!).collection("workouts").document(id)

        val exerciseList: MutableList<Exercise> = ArrayList()
        currentGymWorkoutRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val tempExerciseList = document["exercises"] as ArrayList<*>
                    for (exercise in tempExerciseList) {
                        val currentExercise = exercise as HashMap<*, *>
                        val sets = currentExercise["sets"] as ArrayList<*>
                        val tempExercise = Exercise(currentExercise["name"].toString())
                        for (set in sets) {
                            val currentSet = set as HashMap<*, *>
                            val tempSet = Sets()
                            tempSet.weight = currentSet["weight"].toString()
                            tempSet.reps = currentSet["reps"].toString()
                            tempExercise.sets.add(tempSet)
                        }
                        exerciseList.add(tempExercise)
                    }
                    Log.d("view gym workout tempList :", tempExerciseList.toString())
                    exerciseAdapter.submitList(exerciseList)
                    exerciseAdapter.notifyDataSetChanged()


                } else {
                    Log.d("view gym workout document query:", "No such document")
                }
            }



        val editButton: Button = findViewById(R.id.gym_details_edit_button)
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
