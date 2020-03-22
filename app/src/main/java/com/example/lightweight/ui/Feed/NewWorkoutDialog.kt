package com.example.lightweight.ui.feed

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.adapters.WorkOutTypeAdapter

import com.example.lightweight.classes.CyclingWorkout
import com.example.lightweight.classes.GymWorkout
import com.example.lightweight.classes.RunningWorkout
import com.example.lightweight.classes.Workout


class NewWorkoutDialog : DialogFragment() {
    private val itemPaddingTop = 5

    private lateinit var workoutTypeAdapter: WorkOutTypeAdapter
    private fun initNewWorkoutRecycleView(recyclerView: RecyclerView, dialog: Dialog) {

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(itemPaddingTop)
            addItemDecoration(topSpacingItemDecoration)
            workoutTypeAdapter =
                WorkOutTypeAdapter(dialog)
            adapter = workoutTypeAdapter
        }
    }


    private fun addWorkoutTypeDataSet() {
        val workoutTypeList: MutableList<Workout> = ArrayList()
        //for using the abstract functions calls in Workout.
        workoutTypeList.add(GymWorkout("", getString(R.string.gym), "", "", "", ""))
        workoutTypeList.add(CyclingWorkout("", getString(R.string.cycling), "", "", "", ""))
        workoutTypeList.add(RunningWorkout("", getString(R.string.run), "", "", "", ""))
        workoutTypeAdapter.submitList(workoutTypeList)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.layout_new_workout_dialog, null)


        val dialog = AlertDialog.Builder(context!!)
            .setView(dialogView)
            .create()
        dialog.setCanceledOnTouchOutside(true)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.dialog_recycle_view)
        initNewWorkoutRecycleView(recyclerView!!, dialog)
        addWorkoutTypeDataSet()
        return dialog
    }
}
