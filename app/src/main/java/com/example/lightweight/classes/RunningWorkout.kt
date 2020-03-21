package com.example.lightweight.classes

import android.content.Context
import android.content.Intent
import com.example.lightweight.R
import com.example.lightweight.ui.workouts.running.NewRunningWorkoutActivity
import com.example.lightweight.ui.workouts.running.ViewRunWorkoutActivity

class RunningWorkout(
    override var id: String,
    override var title: String,
    override var date: String,
    override var userName: String,
    override var userImage: String?,
    override var userId: String

) : Workout(R.drawable.ic_directions_run_yellow_24dp) {


    override fun viewWorkout(context: Context) {
        val intent = Intent(context, ViewRunWorkoutActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("userId", userId)
        context.startActivity(intent)
    }

    override fun newWorkout(context: Context) {
        val intent = Intent(
            context, NewRunningWorkoutActivity::class.java
        )
        context.startActivity(intent)
    }
    
}