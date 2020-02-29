package com.example.lightweight.classes

import android.content.Context
import android.content.Intent
import com.example.lightweight.R
import com.example.lightweight.ui.Feed.GymWorkoutDetailsActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class GymWorkout(
    override var title: String,
    override var date: Date,
    override var image: String
) : AbstractWorkout(R.drawable.ic_fitness_center_yellow_24dp) {


    override fun showWorkout(context: Context) {
        val intent = Intent(context, GymWorkoutDetailsActivity::class.java)
        intent.putExtra("workoutTitle", title)


        context.startActivity(intent)

    }

    override fun editWorkout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addWorkoutToDb(email: String) {
        val db = FirebaseFirestore.getInstance()

        val set = 1  //TODO remove after testing
        val weight = 20  //TODO remove after testing
        val reps = 10 //TODO remove after testing

        val exercise = hashMapOf(
            "set" to set, //TODO probably needs to be removed
            "weight" to weight,
            "reps" to reps
        )

        //TODO fix ID. Currently "unoDosTres"
        db.collection("users").document(email)
            .collection("workouts").document("unoDosTres")
            .set(exercise)
    }
}
