package com.example.lightweight

import java.util.*

class GymWorkout(
                 override var title: String,
                 override var date: Date,
                 override var image: String)
    : AbstractWorkout(R.drawable.ic_fitness_center_yellow_24dp){


    override fun showWorkout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editWorkout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}