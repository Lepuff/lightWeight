package com.example.lightweight.classes

import android.content.Context
import android.content.Intent
import com.example.lightweight.R
import com.example.lightweight.ui.workouts.running.NewRunningWorkoutActivity
import com.example.lightweight.ui.workouts.running.ViewRunWorkoutActivity

class RunningWorkout(
    override var id: String,
    override var title: String,
    override var date: String


) : AbstractWorkout(R.drawable.ic_directions_run_yellow_24dp) {


    override fun showWorkout(context: Context) {
        val intent = Intent(context, ViewRunWorkoutActivity::class.java)
        intent.putExtra("id", id)
        context.startActivity(intent)
    }

    override fun newWorkout(context: Context) {
        val intent = Intent(
            context, NewRunningWorkoutActivity::class.java
        )
        context.startActivity(intent)
    }

}