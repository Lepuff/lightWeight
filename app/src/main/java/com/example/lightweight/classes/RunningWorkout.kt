package com.example.lightweight.classes

import android.content.Context
import android.content.Intent
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.ui.newWorkout.cycling.NewCyclingWorkoutActivity
import com.example.lightweight.ui.newWorkout.running.NewRunningWorkoutActivity
import com.example.lightweight.ui.workoutDetails.Run.ViewRunWorkoutActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class RunningWorkout(
    override var id: String?,
    override var title: String?,
    override var date: String?


) : AbstractWorkout(R.drawable.ic_directions_run_yellow_24dp) {


    override fun showWorkout(context: Context) {
        val intent = Intent(context, ViewRunWorkoutActivity::class.java)
        intent.putExtra("id", id)
        context.startActivity(intent)
    }

    override fun editWorkout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun newWorkout(context: Context) {
        val intent = Intent(
            context, NewRunningWorkoutActivity::class.java
        )
        context.startActivity(intent)
    }

}