package com.example.lightweight.classes

import android.content.Context
import android.content.Intent
import com.example.lightweight.R
import com.example.lightweight.ui.workouts.cycling.NewCyclingWorkoutActivity
import com.example.lightweight.ui.workouts.cycling.ViewCyclingWorkoutActivity

class CyclingWorkout(
    override var id: String,
    override var title: String,
    override var date: String

    ) : AbstractWorkout(R.drawable.ic_directions_bike_yellow_24dp) {

    override fun showWorkout(context: Context) {
        val intent = Intent(context, ViewCyclingWorkoutActivity::class.java)
        intent.putExtra("id",id)
        context.startActivity(intent)
    }



    override fun newWorkout(context: Context) {
         val intent = Intent(context,
             NewCyclingWorkoutActivity::class.java)
        context.startActivity(intent)

    }

}