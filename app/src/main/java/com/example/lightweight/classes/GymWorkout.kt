package com.example.lightweight.classes

import android.content.Context
import android.content.Intent
import com.example.lightweight.R
import com.example.lightweight.ui.workouts.gym.ViewGymWorkoutActivity
import com.example.lightweight.ui.workouts.gym.NewGymWorkoutActivity

class GymWorkout(
    override var id: String,
    override var title: String,
    override var date: String,
    override var userName: String,
    override var userImage: String?,
    override var userId: String

) : Workout(R.drawable.ic_fitness_center_yellow_24dp) {

    override fun viewWorkout(context: Context) {


        val intent = Intent(context, ViewGymWorkoutActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("userId", userId)
        context.startActivity(intent)
    }

    override fun newWorkout(context: Context) {
        val intent = Intent(
            context,
            NewGymWorkoutActivity::class.java
        )
        context.startActivity(intent)
    }

}
