package com.example.lightweight

import android.content.Context
import android.content.Intent
import com.example.lightweight.ui.Feed.GymWorkoutDetailsActivity
import java.util.*

class GymWorkout(
                 override var title: String,
                 override var date: Date,
                 override var image: String)
    : AbstractWorkout(R.drawable.ic_fitness_center_yellow_24dp){


    override fun showWorkout(context: Context) {
        val intent = Intent(context,GymWorkoutDetailsActivity::class.java)
        intent.putExtra("workoutTitle",title)


        context.startActivity(intent)

    }

    override fun editWorkout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}