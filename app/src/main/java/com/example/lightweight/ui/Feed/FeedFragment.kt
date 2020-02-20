package com.example.lightweight.ui.Feed

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.lightweight.DataSource
import com.example.lightweight.R
import com.example.lightweight.WorkOutTypeSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.layout_dialog_new_workout_list_item.*
import kotlinx.android.synthetic.main.layout_new_workout_dialog.*

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var workOutAdapter: WorkOutAdapter
    private lateinit var workoutTypeAdapter: WorkOutTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedViewModel =
            ViewModelProviders.of(this).get(FeedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_feed, container, false)
        val floatingActionBar =
            root.findViewById<FloatingActionButton>(R.id.feed_floating_action_button)
        feedViewModel.text.observe(this, Observer {

        })
        floatingActionBar.setOnClickListener {
            showNewWorkoutDialog(root.context)
        }
        return root
    }

    private fun addDataSet() {
        val data = DataSource.createDataSet()
        workOutAdapter.submitList(data)
    }

    private fun initRecyclerView() {
        feed_recycler_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            workOutAdapter = WorkOutAdapter()
            adapter = workOutAdapter
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        addDataSet()
    }

    private fun showNewWorkoutDialog(context: Context) {


        val dialog = MaterialDialog(context)
            .title(R.string.app_name)
            .customView(R.layout.layout_new_workout_dialog)



        //initNewWorkoutRecycleView()
        //addWorkoutTypeDataset()
        dialog.show()
    }

    /*private fun initNewWorkoutRecycleView() {
        dialog_recycle_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration = TopSpacingItemDecoration(15)
            addItemDecoration(topSpacingItemDecoration)
            workoutTypeAdapter = WorkOutTypeAdapter()
            adapter = workoutTypeAdapter
        }
    }*/

    /*private fun addWorkoutTypeDataset() {
        val data = WorkOutTypeSource.createNWDataSet()
        workoutTypeAdapter.submitList(data)
    }*/
}