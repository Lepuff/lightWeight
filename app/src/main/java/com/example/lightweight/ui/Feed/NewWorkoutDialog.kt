package com.example.lightweight.ui.Feed

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.adapters.WorkOutTypeAdapter
import com.example.lightweight.WorkOutTypeSource


class NewWorkoutDialog : DialogFragment() {


    private lateinit var workoutTypeAdapter : WorkOutTypeAdapter
    private fun initNewWorkoutRecycleView(recyclerView: RecyclerView,dialog: Dialog) {

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(5)
            addItemDecoration(topSpacingItemDecoration)
            workoutTypeAdapter =
                WorkOutTypeAdapter(dialog)
            adapter = workoutTypeAdapter
        }
    }


    private fun addWorkoutTypeDataSet() {
        val data =
            WorkOutTypeSource.createNWDataSet()
        workoutTypeAdapter.submitList(data)
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
        initNewWorkoutRecycleView(recyclerView!!,dialog)
        addWorkoutTypeDataSet()
        return dialog
    }
}
