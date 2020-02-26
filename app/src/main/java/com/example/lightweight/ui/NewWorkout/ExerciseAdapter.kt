package com.example.lightweight.ui.Feed


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.ui.NewWorkout.Exercise


class ExerciseAdapter(private val exercises : List<Exercise>):
        RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>(){


class ExerviseViewHolder (val textView : TextView) : RecyclerView.ViewHolder(textView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ExerciseAdapter.ExerciseViewHolder {

        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_exercise.)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int  = exercises.size


    override fun onBindViewHolder(holder: ExerciseAdapter.ExerciseViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}








