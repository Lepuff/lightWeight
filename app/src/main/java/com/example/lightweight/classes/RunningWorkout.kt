package com.example.lightweight.classes

import android.content.Context
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.Database
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

    override fun addWorkoutToDb(
        workoutTitle: Editable,
        workoutDate: Editable,
        exerciseList: MutableList<Exercise>,
        exerciseLiveData: MutableLiveData<MutableList<Exercise>>
    ) {
        Database.db.collection("users").document(Database.getUserEmail())
            .collection("workouts").document("$workoutTitle")
            .collection(exerciseList.toString())
            .document("setNumber")
        //TODO Oskar, hur kommer man åt: duration, distance?
    }


}