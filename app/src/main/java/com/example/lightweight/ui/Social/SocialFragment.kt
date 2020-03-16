package com.example.lightweight.ui.Social

import android.content.ContentValues.TAG
import android.os.Bundle
import android.provider.ContactsContract
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
import com.example.lightweight.ui.Feed.FeedViewModel
import com.example.lightweight.ui.Feed.NewWorkoutDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_social.*


class SocialFragment : Fragment() {
    private val itemPadding = 30
    private lateinit var viewModel: FeedViewModel
    private lateinit var workOutAdapter: WorkOutAdapter
    private var db = FirebaseFirestore.getInstance()
    private var workoutsRef = db.collection(Database.USERS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(FeedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_social, container, false)


        viewModel.workoutList.observe(
            viewLifecycleOwner,
            Observer<MutableList<AbstractWorkout>> {
                workOutAdapter.notifyDataSetChanged()
            })


        dbTest()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        workOutAdapter.submitList(viewModel.workoutList.value!!)
    }

    private fun dbTest() { //todo remove after testing
        workoutsRef.get()
            .addOnSuccessListener { workouts ->


                workOutAdapter.clearList()
                if (workouts != null) {
                    for (workout in workouts) {
                        Log.d(
                            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaah",
                            workout.toString()
                        )

                    }
                }
                Log.d(
                    "failed: ",
                    workouts.toString()
                )

            }
    }

    private fun initRecyclerView() {
        social_recycler_view.apply {
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


/*
        val snapShotListener = workoutsRef.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.w("Social Fragment : snap shot listener :", "Listen failed.", e)
                return@addSnapshotListener
            } else {
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
            }
            addWorkoutToFeed()
        }

 */
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
                        Log.d("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaah",workout.toString())
                        val id = workout.id
                        val type = workout[Database.TYPE_OF_WORKOUT].toString()
                        val date = workout[Database.WORKOUT_DATE].toString()
                        val title = workout[Database.WORKOUT_TITLE].toString()
                        when (type) {
                            "gymWorkout" ->
                                workOutAdapter.addWorkout(GymWorkout(id, title, date))
                            "runningWorkout" ->
                                workOutAdapter.addWorkout(RunningWorkout(id, title, date))
                            "cyclingWorkout" ->
                                workOutAdapter.addWorkout(
                                    CyclingWorkout(id, title, date)
                                )
                        }
                    }
                }
            }
    }
}
