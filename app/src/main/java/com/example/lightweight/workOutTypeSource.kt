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
                    "", "Gym", "","", null,"") //todo string res
            )
            list.add(
                RunningWorkout("", "Run", "","", null,"")
            )
            list.add(
                CyclingWorkout("","Cycling","","", null,"")
            )
            return list
        }
    }
}