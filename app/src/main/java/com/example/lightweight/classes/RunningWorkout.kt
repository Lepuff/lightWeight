package com.example.lightweight.classes

import android.content.Context
import com.example.lightweight.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class RunningWorkout(
    override var date: String,
    override var image: String,
    override var title: String
) : AbstractWorkout(R.drawable.ic_directions_run_yellow_24dp) {


    var time : Int = 0
    var distance : Int = 0

   fun setDuration(time : Int){
        this.time = time
    }
    fun getDuration() : Int {
        return this.time
    }

    fun setRunningDist(distance : Int){
        this.distance = distance
    }
    fun getRunningDist():Int{
         return this.distance
    }


    override fun showWorkout(context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editWorkout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun newWorkout(context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addWorkoutToDb(email: String) {
        val db = FirebaseFirestore.getInstance()

        time = 10  //TODO remove after testing
        distance = 20  //TODO remove after testing

        val exercise = hashMapOf(
            "distance" to distance,
            "time" to time
        )

        //TODO fix ID. Currently "unoDosTres"
        db.collection("users").document(email)
            .collection("workouts").document("unoDosTres")
            .set(exercise)
    }

}