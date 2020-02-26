package com.example.lightweight.ui.Feed.Dialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lightweight.R

class GymWorkoutDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_workout_details)


        val navBarTitle = intent.getStringExtra("workoutTitle")
        supportActionBar?.title = navBarTitle
    }
}
