package com.example.lightweight

import android.net.Uri
import com.example.lightweight.classes.AbstractWorkout
import com.example.lightweight.classes.CyclingWorkout
import com.example.lightweight.classes.GymWorkout
import com.example.lightweight.classes.RunningWorkout
import kotlin.collections.ArrayList


class WorkOutTypeSource {

    companion object {

        fun createNWDataSet(): MutableList<AbstractWorkout> {
            val list = ArrayList<AbstractWorkout>()
            list.add(
                GymWorkout(
                    "", "Gym", "","", "") //todo string res
            )
            list.add(
                RunningWorkout("", "Run", "","", "")
            )
            list.add(
                CyclingWorkout("","Cycling","","", "")
            )
            return list
        }
    }
}