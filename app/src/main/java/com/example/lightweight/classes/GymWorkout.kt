package com.example.lightweight.classes

import android.content.Context
import android.content.Intent
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.ui.workoutDetails.ViewGymWorkoutActivity
import com.example.lightweight.ui.newWorkout.gym.NewGymWorkoutActivity
import kotlin.collections.ArrayList

class GymWorkout(
    override var title: String?,
    override var date: String?,
    override var image: String?
) : AbstractWorkout(R.drawable.ic_fitness_center_yellow_24dp) {


    private var exerciseList :MutableList<Exercise> = ArrayList()


    fun setExerciseList(exerciseList : MutableList<Exercise>){
        this.exerciseList = exerciseList
    }

    fun getExerciseList(): MutableList<Exercise>{
        return this.exerciseList
    }

    override fun showWorkout(context: Context) {
        val intent = Intent(context, ViewGymWorkoutActivity::class.java)
        intent.putExtra("workoutTitle", title)
        context.startActivity(intent)
    }

    override fun editWorkout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun newWorkout(context: Context) {
        val intent = Intent(context,
            NewGymWorkoutActivity::class.java)
        context.startActivity(intent)
    }

    override fun addWorkoutToDb(
        workoutTitle: Editable,
        workoutDate: Editable,
        exerciseList: MutableList<Exercise>,
        exerciseLiveData: MutableLiveData<MutableList<Exercise>>
    ) {
        Database.db.collection("users").document(Database.getUserEmail())
            .collection("workouts").document("$workoutTitle")
            .collection(exerciseList.toString()).document(exerciseLiveData.toString())
        //TODO Oskar, hur kommer man åt: set nummer, reps och vikt?
    }

}
