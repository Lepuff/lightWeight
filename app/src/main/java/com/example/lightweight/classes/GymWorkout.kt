package com.example.lightweight.classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.lightweight.R
import com.example.lightweight.ui.workouts.gym.ViewGymWorkoutActivity
import com.example.lightweight.ui.workouts.gym.NewGymWorkoutActivity

class GymWorkout(
    override var id: String,
    override var title: String,
    override var date: String,
    override var userName: String,
    override var userImage: String

) : AbstractWorkout(R.drawable.ic_fitness_center_yellow_24dp) {

    override fun showWorkout(context: Context) {

        Log.d("Test before intent", id)
        val intent = Intent(context, ViewGymWorkoutActivity::class.java)
        intent.putExtra("id", id)
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
