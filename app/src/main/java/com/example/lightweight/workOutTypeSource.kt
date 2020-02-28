package com.example.lightweight

import com.example.lightweight.classes.WorkOutType


class WorkOutTypeSource {

    companion object {

        fun createNWDataSet(): ArrayList<WorkOutType> {
            val list = ArrayList<WorkOutType>()
            list.add(
                WorkOutType(
                    "Gym Workout",
                    icon = R.drawable.ic_fitness_center_yellow_24dp

                )
            )
            list.add(
                WorkOutType(
                    "Running",
                    icon = R.drawable.ic_fitness_center_yellow_24dp

                )
            )
            list.add(
                WorkOutType(
                    "Cycling",
                    icon = R.drawable.ic_fitness_center_yellow_24dp

                )
            )
            return list
        }
    }
}