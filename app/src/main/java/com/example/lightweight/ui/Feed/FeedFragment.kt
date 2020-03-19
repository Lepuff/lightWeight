package com.example.lightweight.ui.Feed

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.adapters.WorkOutAdapter
import com.example.lightweight.classes.*

import com.google.android.material.floatingactionbutton.FloatingActionButton


import com.google.firebase.firestore.DocumentChange

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_feed.*


class FeedFragment : Fragment() {
    private val itemPadding = 30
    private lateinit var viewModel: WorkoutFeedViewModel
    private lateinit var workOutAdapter: WorkOutAdapter
    private var db = FirebaseFirestore.getInstance()

    private var workoutsRef = db.collection(Database.USERS).document(Database.getUserId()!!)
        .collection(Database.WORKOUTS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(WorkoutFeedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_feed, container, false)


        viewModel.workoutList.observe(
            viewLifecycleOwner,
            Observer<MutableList<AbstractWorkout>> {
                workOutAdapter.notifyDataSetChanged()
            })
        val floatingActionButton =
            root.findViewById<FloatingActionButton>(R.id.feed_floating_action_button)

        floatingActionButton.setOnClickListener {
            NewWorkoutDialog().show(childFragmentManager, "test")//todo fix.

        }
        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        workOutAdapter.submitList(viewModel.workoutList.value!!)

    }

    private fun initRecyclerView() {
        feed_recycler_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(itemPadding)
            addItemDecoration(topSpacingItemDecoration)
            workOutAdapter = WorkOutAdapter()
            adapter = workOutAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        checkDatabaseForUpdates()
    }

    private fun checkDatabaseForUpdates() {
        val snapShotListener = workoutsRef.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            for (dc in snapshots!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> Log.d(TAG, "New workout: ${dc.document.data}")
                    DocumentChange.Type.MODIFIED -> Log.d(
                        TAG,
                        "Modified workout: ${dc.document.data}"
                    )
                    DocumentChange.Type.REMOVED -> Log.d(
                        TAG,
                        "Removed workout: ${dc.document.data}"
                    )
                }

            }
            addWorkoutToFeed()
        }
    }

    override fun onStop() {
        super.onStop()

        //todo remove listener?

    }

    private fun addWorkoutToFeed() {

        workoutsRef.orderBy(Database.WORKOUT_DATE, Query.Direction.DESCENDING).get()
            .addOnSuccessListener { workouts ->
                workOutAdapter.clearList()

                if (workouts != null) {
                    for (workout in workouts) {
                        val id = workout.id
                        val type = workout[Database.TYPE_OF_WORKOUT].toString()
                        val date = workout[Database.WORKOUT_DATE].toString()
                        val title = workout[Database.WORKOUT_TITLE].toString()
                        val name = Database.getUserName()
                        val profilePicture = Database.getUserPicture()
                        when (type) {
                            "gymWorkout" ->
                                workOutAdapter.addWorkout(
                                    GymWorkout(
                                        id,
                                        title,
                                        date,
                                        name,
                                        profilePicture,
                                        Database.getUserId().toString()
                                    )
                                )
                            "runningWorkout" ->
                                workOutAdapter.addWorkout(
                                    RunningWorkout(
                                        id,
                                        title,
                                        date,
                                        name,
                                        profilePicture,
                                        Database.getUserId().toString()
                                    )
                                )
                            "cyclingWorkout" ->
                                workOutAdapter.addWorkout(
                                    CyclingWorkout(
                                        id,
                                        title,
                                        date,
                                        name,
                                        profilePicture,
                                        Database.getUserId().toString()
                                    )
                                )
                        }
                    }
                }
            }
    }
}
