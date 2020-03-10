package com.example.lightweight.ui.Feed

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
import com.example.lightweight.classes.AbstractWorkout
import com.example.lightweight.classes.CyclingWorkout
import com.example.lightweight.classes.GymWorkout
import com.example.lightweight.classes.RunningWorkout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_show_cycling_activity.*
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var workOutAdapter: WorkOutAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedViewModel =
            ViewModelProviders.of(this).get(FeedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_feed, container, false)

  
        feedViewModel.workoutList.observe(viewLifecycleOwner, Observer<MutableList<AbstractWorkout>> {
            workOutAdapter.notifyDataSetChanged()
        } )
        val floatingActionButton =
            root.findViewById<FloatingActionButton>(R.id.feed_floating_action_button)

        floatingActionButton.setOnClickListener {
            NewWorkoutDialog().show(childFragmentManager,"test")

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
        val id : String = ""
        val title : String = ""
        val date : String = ""

        workOutAdapter.submitList(feedViewModel.workoutList.value!!)
        workOutAdapter.addWorkout(GymWorkout(id,title,date))
        workOutAdapter.addWorkout(CyclingWorkout(id,title,date))
        workOutAdapter.addWorkout(RunningWorkout(id,title,date))

    }

    private enum class WorkoutType{
        GYM,
        RUNNING,
        CYCLING
    }

    fun addWorkoutToFeed(){
        val db = FirebaseFirestore.getInstance()

        //TODO sorterar alla workouts
        db.collection("users").document(Database.user.email!!).collection("workouts")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()

        //TODO hämtar alla gymWorkouts
        db.collection("users").document(Database.user.email!!).collection("workouts")
            .whereEqualTo("typeOfWorkout", "gym")
            .get()
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents", exception)
            }

        //TODO hämtar alla cyclingWorkouts
        db.collection("users").document(Database.user.email!!).collection("workouts")
            .whereEqualTo("typeOfWorkout", "cycling")
            .get()
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents", exception)
            }

        //TODO hämtar alla runningWorkouts
        db.collection("users").document(Database.user.email!!).collection("workouts")
            .whereEqualTo("typeOfWorkout", "running")
            .get()
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents", exception)
            }

        var workoutType : WorkoutType = WorkoutType.CYCLING
        val id : String = ""
        val title : String = ""
        val date : String = ""

        when(workoutType){
            WorkoutType.GYM -> workOutAdapter.addWorkout(GymWorkout(id,title,date))
            WorkoutType.CYCLING -> workOutAdapter.addWorkout(GymWorkout(id,title,date))
            WorkoutType.RUNNING -> workOutAdapter.addWorkout(GymWorkout(id,title,date))
        }
    }
}