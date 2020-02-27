package com.example.lightweight.ui.NewWorkout

data class Exercise(
    val name: String

) {
    val sets: MutableList<Sets> = arrayListOf()
}