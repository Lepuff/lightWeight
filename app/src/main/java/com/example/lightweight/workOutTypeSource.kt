package com.example.lightweight

import com.example.lightweight.classes.Workout
import com.example.lightweight.classes.CyclingWorkout
import com.example.lightweight.classes.GymWorkout
import com.example.lightweight.classes.RunningWorkout
import kotlin.collections.ArrayList


class WorkOutTypeSource {

    companion object {

        fun createNWDataSet(): MutableList<Workout> {
            val list = ArrayList<Workout>()
            list.add(
                GymWorkout(
                    "","", "","", "","") //todo string res
            )
            list.add(
                RunningWorkout("", "Run", "","", "","")
            )
            list.add(
                CyclingWorkout("","Cycling","","", "","")
            )
            return list
        }
    }
}