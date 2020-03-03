package com.example.lightweight

import com.example.lightweight.classes.AbstractWorkout
import com.example.lightweight.classes.GymWorkout
import com.example.lightweight.classes.RunningWorkout
import kotlin.collections.ArrayList


class WorkOutTypeSource {

    companion object {

        fun createNWDataSet(): MutableList<AbstractWorkout> {
            val list = ArrayList<AbstractWorkout>()
            list.add(
                GymWorkout(
                    "Gym", "test", "test"

                )
            )
            list.add(
                RunningWorkout("Running", "test", "test")
            )
            return list
        }
    }
}