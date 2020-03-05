package com.example.lightweight.classes

import android.content.Context
import android.content.Intent
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.R
import com.example.lightweight.ui.newWorkout.cycling.NewCyclingWorkoutActivity

class CyclingWorkout(
    override var title: String?,
    override var date: String?,
    override var image: String?
) : AbstractWorkout(R.drawable.ic_directions_bike_yellow_24dp) {

    var averageSpeed : String = ""
    var topSpeed : String = ""
    var totalTime : String = ""
    var averagePulse : String = ""
    var maxPulse : String = ""
    var averageForce: String = ""
    var maxForce : String = ""
    var averageCadence : String = ""
    var maxCadence : String = ""
    var calories : String = ""

            // Average speed , top speed , total time , average puls , max puls , average force , max force ,average kadens , max kadens , calories
    override fun showWorkout(context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editWorkout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun newWorkout(context: Context) {
         var intent = Intent(context,
             NewCyclingWorkoutActivity::class.java)
        context.startActivity(intent)

    }

    override fun addWorkoutToDb(
        workoutTitle: Editable,
        workoutDate: Editable,
        exerciseList: MutableList<Exercise>,
        exerciseLiveData: MutableLiveData<MutableList<Exercise>>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}