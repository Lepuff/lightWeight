package com.example.lightweight.classes

import com.example.lightweight.classes.Sets

data class Exercise(
    var name: String

) {
    var sets: MutableList<Sets> = arrayListOf()
}