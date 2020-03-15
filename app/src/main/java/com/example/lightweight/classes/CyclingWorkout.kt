package com.example.lightweight.classes

import android.content.Context
import android.content.Intent
import com.example.lightweight.R
import com.example.lightweight.ui.workouts.cycling.NewCyclingWorkoutActivity

class CyclingWorkout(
    override var id: String?,
    override var title: String?,
    override var date: String?

    ) : AbstractWorkout(R.drawable.ic_directions_bike_yellow_24dp) {

    override fun showWorkout(context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editWorkout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun newWorkout(context: Context) {
         val intent = Intent(context,
             NewCyclingWorkoutActivity::class.java)
        context.startActivity(intent)

    }

}