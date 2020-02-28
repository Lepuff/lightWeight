package com.example.lightweight.ui.NewWorkout

data class Exercise(
    var name: String

) {
    var sets: MutableList<Sets> = arrayListOf()
}