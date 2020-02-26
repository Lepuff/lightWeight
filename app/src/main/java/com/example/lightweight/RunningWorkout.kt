package com.example.lightweight

import android.content.Context
import java.util.*

class RunningWorkout(
    override var date: Date,
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

}