package com.example.lightweight


import com.example.lightweight.ui.Feed.WorkOutType


class WorkOutTypeSource {

    companion object {

        fun createNWDataSet(): ArrayList<WorkOutType> {
            val list = ArrayList<WorkOutType>()
            list.add(
                WorkOutType(
                    "gym",
                    icon = R.drawable.ic_fitness_center_yellow_24dp

                )
            )
            list.add(
                WorkOutType(
                    "stuff",
                    icon = R.drawable.ic_fitness_center_yellow_24dp

                )
            )
            list.add(
                WorkOutType(
                    "hey",
                    icon = R.drawable.ic_fitness_center_yellow_24dp

                )
            )
            list.add(
                WorkOutType(
                    "hehehe",
                    icon = R.drawable.ic_fitness_center_yellow_24dp

                )
            )
            list.add(
                WorkOutType(
                    "gym",
                    icon = R.drawable.ic_fitness_center_yellow_24dp

                )
            )
            list.add(
                WorkOutType(
                    "gym",
                    icon = R.drawable.ic_fitness_center_yellow_24dp

                )
            )




            return list
        }
    }
}