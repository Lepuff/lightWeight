package com.example.lightweight.ui.Feed

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.WorkOutTypeSource


class NewWorkoutFragment : DialogFragment() {
    private lateinit var workoutTypeAdapter: WorkOutTypeAdapter
    private fun initNewWorkoutRecycleView(recyclerView: RecyclerView) {
        Log.d("funkar", "haha")
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration = TopSpacingItemDecoration(15)
            addItemDecoration(topSpacingItemDecoration)
            workoutTypeAdapter = WorkOutTypeAdapter()
            adapter = workoutTypeAdapter

            Log.d("funkar igen", "haha")

            //ToDo remove log.d

        }
    }


    private fun addWorkoutTypeDataset() {
        val data =
            WorkOutTypeSource.createNWDataSet()
        workoutTypeAdapter.submitList(data)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.layout_new_workout_dialog, null)


        val dialog = AlertDialog.Builder(context!!)
            .setView(dialogView)
            .create()

        dialog.setCanceledOnTouchOutside(true)

        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.dialog_recycle_view)

        initNewWorkoutRecycleView(recyclerView!!)
        addWorkoutTypeDataset()

        return dialog
    }
}
