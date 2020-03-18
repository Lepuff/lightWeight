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
import com.example.lightweight.adapters.WorkOutAdapter
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.classes.*
import com.example.lightweight.ui.Feed.WorkoutFeedViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_social.*
import kotlinx.android.synthetic.main.layout_wo_list_item.*


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
            Observer<MutableList<AbstractWorkout>> {
                workOutAdapter.notifyDataSetChanged()
            })

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
        /*  val snapShotListener = db.collection(Database.USERS).addSnapshotListener { snapshots, e ->
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

          }*/
    }

    override fun onStop() {
        super.onStop()

        //todo remove listener?

    }

    private fun addWorkoutToFeed() {
        db.collection(Database.USERS).document(Database.getUserId()!!).collection(Database.FRIENDS)
            .get()
            .addOnSuccessListener { friends ->
                workOutAdapter.clearList()
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
                                                                    friend.id
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
                                                                    friend.id
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
                                                                    friend.id
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
