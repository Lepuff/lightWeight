package com.example.lightweight

import com.example.lightweight.classes.AbstractWorkout
import com.example.lightweight.classes.GymWorkout
import java.util.*
import kotlin.collections.ArrayList


class DataSource {

    companion object {

        fun createDataSet(): MutableList<AbstractWorkout> {
            val list = ArrayList<AbstractWorkout>()
            list.add(
                GymWorkout(

                    "test1",
                    "2020-01-01",
                    "https://i.pravatar.cc/300"
                )
            )
            list.add(
                GymWorkout(

                    "test1",
                    "2020-01-01",
                    "https://i.pravatar.cc/300"
                )
            )
            list.add(
                GymWorkout(

                    "test1",
                    "2020-01-01",
                    "https://i.pravatar.cc/300"
                )
            )
            list.add(
                GymWorkout(

                    "test1",
                    "2020-01-01",
                    "https://i.pravatar.cc/300"
                )
            )
            list.add(
                GymWorkout(

                    "test1",
                    "2020-01-01",
                    "https://i.pravatar.cc/300"
                )
            )
            list.add(
                GymWorkout(

                    "test1",
                    "2020-01-01",
                    "https://i.pravatar.cc/300"
                )
            )


















            return list
        }
    }
}