package com.example.lightweight.ui.NewWorkout

data class Exercise(
    val name: String,
    val sets: List<Sets>
) {


    data class Sets(val number: Int = 0,
                    val weight: Int = 0,
                    val reps: Int = 0
                    ) {
    }
}