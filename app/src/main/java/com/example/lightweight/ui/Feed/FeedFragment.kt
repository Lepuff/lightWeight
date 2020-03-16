package com.example.lightweight.ui.Feed

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
import com.facebook.Profile
import com.facebook.ProfileTracker
import com.facebook.internal.Mutable
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var workOutAdapter: WorkOutAdapter
    private var db = FirebaseFirestore.getInstance()
    private lateinit var workoutsRef: CollectionReference
    //val profile: Profile = Profile.getCurrentProfile()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedViewModel =
            ViewModelProviders.of(this).get(FeedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_feed, container, false)


        feedViewModel.workoutList.observe(
            viewLifecycleOwner,
            Observer<MutableList<AbstractWorkout>> {
                workOutAdapter.notifyDataSetChanged()
            })
        val floatingActionButton =
            root.findViewById<FloatingActionButton>(R.id.feed_floating_action_button)

        floatingActionButton.setOnClickListener {
            NewWorkoutDialog().show(childFragmentManager, "test")

        }

        return root
    }

    private fun initRecyclerView() {
        feed_recycler_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            workOutAdapter = WorkOutAdapter()
            adapter = workOutAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()

        workOutAdapter.submitList(feedViewModel.workoutList.value!!)

    }
    override fun onResume() {
        super.onResume()
        addWorkoutToFeed()

    }

    override fun onPause() {
        super.onPause()
        feedViewModel.clear()
    }







    private fun addWorkoutToFeed() {
        workoutsRef = db.collection("users").document(Database.getUserId()!!)
            .collection("workouts")

        workoutsRef.orderBy("workoutDate", Query.Direction.DESCENDING).get().addOnSuccessListener { workouts ->

            if (workouts != null) {
                for (workout in workouts) {
                    val id = workout.id
                    val type = workout["typeOfWorkout"].toString()
                    val date = workout["workoutDate"].toString()
                    val title = workout["workoutTitle"].toString()
                    when (type) {
                        "gymWorkout" -> workOutAdapter.addWorkout(GymWorkout(id, title, date))
                        "runningWorkout" -> workOutAdapter.addWorkout(RunningWorkout(id, title, date))
                        "cyclingWorkout" -> workOutAdapter.addWorkout(CyclingWorkout(id, title, date))
                    }
                }
            }
        }
    }
}
