package com.example.lightweight.classes

import android.content.Context
import com.example.lightweight.R

class CyclingWorkout(
    override var title: String,
    override var date: String,
    override var image: String
) : AbstractWorkout(R.drawable.ic_directions_bike_black_24dp) {


    override fun showWorkout(context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editWorkout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun newWorkout(context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addWorkoutToDb(email: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}