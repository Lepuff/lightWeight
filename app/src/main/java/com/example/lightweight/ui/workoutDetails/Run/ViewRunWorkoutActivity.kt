package com.example.lightweight.ui.workoutDetails.Run

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.ui.newWorkout.running.RunViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class ViewRunWorkoutActivity : AppCompatActivity() {
    private lateinit var viewModel: RunViewModel
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_workout)

        val id = intent.getStringExtra("id")
        viewModel = ViewModelProviders.of(this).get(RunViewModel::class.java)
        setObservers()
        getRunningInfoFromDb(id!!)
        setEditable(false)


        val editWorkoutButton = findViewById<Button>(R.id.running_edit_button)
        val saveButton = findViewById<Button>(R.id.running_save_button)
        editWorkoutButton.visibility = View.VISIBLE
        editWorkoutButton.setOnClickListener {
            editWorkoutButton.visibility = View.GONE
            saveButton.visibility = View.VISIBLE
            setEditable(true)
        }

        saveButton.setOnClickListener {
            saveRunningDialog()
        }

    }
    
    private fun setEditable(boolean: Boolean) {
        findViewById<TextInputEditText>(R.id.running_distance_editText).isEnabled = boolean
        findViewById<TextInputEditText>(R.id.running_total_time_editText).isEnabled = boolean
        findViewById<TextInputEditText>(R.id.running_average_speed_editText).isEnabled = boolean
        findViewById<TextInputEditText>(R.id.running_top_speed_editText).isEnabled = boolean
        findViewById<TextInputEditText>(R.id.running_average_pulse_editText).isEnabled = boolean
        findViewById<TextInputEditText>(R.id.running_max_pulse_editText).isEnabled = boolean
        findViewById<TextInputEditText>(R.id.running_calories_editText).isEnabled = boolean

    }

    private fun saveRunningDialog() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_save_workout, null)
        val saveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)


        dialogView.findViewById<TextInputEditText>(R.id.save_workout_title_editText)
            .setText(viewModel.title.value)
        dialogView.findViewById<TextInputEditText>(R.id.save_workout_date_editText)
            .setText(viewModel.date.value)
        val dialog = dialogBuilder.show()



        saveButton.setOnClickListener {
            saveRunningWorkout(dialogView)
            dialog.cancel()
            finish()
            findViewById<Button>(R.id.running_edit_button).visibility = View.VISIBLE
            findViewById<Button>(R.id.running_save_button).visibility = View.GONE
        }
    }

    private fun saveRunningWorkout(dialogView: View) {
        val currentRunWorkoutRef = db.collection(Database.USERS)
            .document(Database.user.email!!).collection(Database.WORKOUTS)
            .document(intent.getStringExtra("id")!!)//todo fix constants
        currentRunWorkoutRef.update(
            Database.averagePulse,
            findViewById<TextInputEditText>(R.id.running_average_pulse_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.distance,
            findViewById<TextInputEditText>(R.id.running_distance_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.totalTime,
            findViewById<TextInputEditText>(R.id.running_total_time_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.averageSpeed,
            findViewById<TextInputEditText>(R.id.running_average_speed_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.topSpeed,
            findViewById<TextInputEditText>(R.id.running_top_speed_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.averagePulse,
            findViewById<TextInputEditText>(R.id.running_average_pulse_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.maxPulse,
            findViewById<TextInputEditText>(R.id.running_max_pulse_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.calories,
            findViewById<TextInputEditText>(R.id.running_calories_editText).text.toString()
        )

        currentRunWorkoutRef.update(
            Database.workoutTitle,
            dialogView.findViewById<TextInputEditText>(R.id.save_workout_title_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.workoutDate,
            dialogView.findViewById<TextInputEditText>(R.id.save_workout_date_editText).text.toString()
        )
    }


    private fun getRunningInfoFromDb(id: String) {
        val currentRunWorkoutRef = db.collection("users")
            .document(Database.user.email!!).collection("workouts").document(id)
        currentRunWorkoutRef.get().addOnSuccessListener { document ->
            if (document != null) {
                viewModel.title.value = document["workoutTitle"].toString()
                viewModel.date.value = document["workoutDate"].toString() //todo fix constants
                viewModel.averagePulse.value = document["averagePulse"].toString().toInt()
                viewModel.averageSpeed.value = document["averageSpeed"].toString().toFloat()
                viewModel.calories.value = document["calories"].toString().toInt()
                viewModel.distance.value = document["distance"].toString().toFloat()
                viewModel.maxPulse.value = document["maxPulse"].toString().toInt()
                viewModel.topSpeed.value = document["topSpeed"].toString().toFloat()
                viewModel.totalTime.value = document["totalTime"].toString().toFloat()

            } else {
                Log.d("view run workout document query:", "No such document")

            }
        }
    }

    private fun setObservers() {

        viewModel.averagePulse.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.running_average_pulse_editText).setText(
                viewModel.averagePulse.value.toString()
            )
        })

        viewModel.distance.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.running_distance_editText).setText(viewModel.distance.value.toString())
        })

        viewModel.totalTime.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.running_total_time_editText).setText(viewModel.totalTime.value.toString())
        })

        viewModel.averageSpeed.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.running_average_speed_editText).setText(
                viewModel.averageSpeed.value.toString()
            )
        })

        viewModel.topSpeed.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.running_top_speed_editText).setText(viewModel.topSpeed.value.toString())
        })

        viewModel.maxPulse.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.running_max_pulse_editText).setText(viewModel.maxPulse.value.toString())
        })

        viewModel.calories.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.running_calories_editText).setText(viewModel.calories.value.toString())
        })

        viewModel.title.observe(this, Observer {
            title = viewModel.title.value
        })
    }
}
