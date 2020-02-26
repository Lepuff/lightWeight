package com.example.lightweight.ui.NewWorkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.R
import com.example.lightweight.ui.Feed.TopSpacingItemDecoration
import com.example.lightweight.ui.Feed.WorkOutAdapter
import kotlinx.android.synthetic.main.activity_new_gym_workout.*
import kotlinx.android.synthetic.main.fragment_feed.*

class NewGymWorkoutActivity : AppCompatActivity() {

private lateinit var exerciseAdapter: ExerciseAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_gym_workout)



        initRecyclerView()




    }

    private fun initRecyclerView() {
        new_gym_recycle_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            exerciseAdapter = ExerciseAdapter()
            adapter = exerciseAdapter
        }
    }







}
