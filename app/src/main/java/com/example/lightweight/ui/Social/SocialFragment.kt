package com.example.lightweight.ui.Social

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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.adapters.WorkOutAdapter
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.classes.*
import com.example.lightweight.ViewModels.WorkoutFeedViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_social.*


class SocialFragment : Fragment() {
    private val itemPadding = 30
    private lateinit var viewModel: WorkoutFeedViewModel
    private lateinit var workOutAdapter: WorkOutAdapter
    private var db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(WorkoutFeedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_social, container, false)

        viewModel.workoutList.observe(
            viewLifecycleOwner,
            Observer<MutableList<Workout>> {
                workOutAdapter.notifyDataSetChanged()
            })

       val swipeRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.social_swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            addWorkoutToFeed()
            swipeRefreshLayout.isRefreshing = false
        }




        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        workOutAdapter.submitList(viewModel.workoutList.value!!)
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
        addWorkoutToFeed()
    }


    private fun addWorkoutToFeed() {
        workOutAdapter.clearList()
        db.collection(Database.USERS).document(Database.getUserId()!!).collection(Database.FRIENDS)
            .get()
            .addOnSuccessListener { friends ->

                if (friends != null) {
                    for (friend in friends) {

                        db.collection(Database.USERS).document(friend[Database.ID].toString()).get()
                            .addOnSuccessListener { user ->
                                if (user != null) {
                                    val userName: String =
                                        user[Database.FIRST_NAME].toString() + " " + user[Database.LAST_NAME].toString()
                                    val profilePicture = user[Database.PICTURE_URI].toString()
                                    db.collection(Database.USERS)
                                        .document(friend[Database.ID].toString())
                                        .collection(Database.WORKOUTS).get()
                                        .addOnSuccessListener { workouts ->
                                            if (workouts != null) {
                                                for (workout in workouts) {
                                                    val id = workout.id
                                                    val type =
                                                        workout[Database.TYPE_OF_WORKOUT].toString()
                                                    val date =
                                                        workout[Database.WORKOUT_DATE].toString()
                                                    val title =
                                                        workout[Database.WORKOUT_TITLE].toString()

                                                    when (type) {
                                                        "gymWorkout" ->
                                                            workOutAdapter.addWorkout(
                                                                GymWorkout(
                                                                    id,
                                                                    title,
                                                                    date,
                                                                    userName,
                                                                    profilePicture,
                                                                    user.id
                                                                )
                                                            )
                                                        "runningWorkout" ->
                                                            workOutAdapter.addWorkout(
                                                                RunningWorkout(
                                                                    id,
                                                                    title,
                                                                    date,
                                                                    userName,
                                                                    profilePicture,
                                                                    user.id
                                                                )
                                                            )
                                                        "cyclingWorkout" ->
                                                            workOutAdapter.addWorkout(
                                                                CyclingWorkout(
                                                                    id,
                                                                    title,
                                                                    date,
                                                                    userName,
                                                                    profilePicture,
                                                                    user.id
                                                                )
                                                            )
                                                    }
                                                }
                                            }
                                        }
                                }
                            }
                    }
                }
            }
    }
}
