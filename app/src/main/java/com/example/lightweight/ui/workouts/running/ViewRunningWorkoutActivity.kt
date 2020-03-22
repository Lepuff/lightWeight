package com.example.lightweight.ui.workouts.running

import android.annotation.SuppressLint
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
import com.example.lightweight.ViewModels.RunningViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class ViewRunningWorkoutActivity : AppCompatActivity() {
    private lateinit var viewModel: RunningViewModel
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_workout)

        viewModel = ViewModelProviders.of(this).get(RunningViewModel::class.java)
        setObservers()

        val editButton = findViewById<Button>(R.id.running_edit_button)
        editButton.setOnClickListener {
            viewModel.isInEditState.value = true
        }

        val saveButton = findViewById<Button>(R.id.running_save_button)
        saveButton.setOnClickListener {
            saveRunningDialog()
        }
        val deleteButton = findViewById<Button>(R.id.running_delete_button)
        deleteButton.setOnClickListener {
            deleteWorkoutDialog()
        }
    }

    private fun deleteWorkoutDialog() {
        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        builder.setTitle(R.string.delete_workout_message)
        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            deleteGymWorkout()
            dialog.cancel()
            finish()
        }
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun deleteGymWorkout() {
        db.collection(Database.USERS).document(intent.getStringExtra("userId")!!)
            .collection(Database.WORKOUTS).document(intent.getStringExtra("id")!!).delete()
            .addOnSuccessListener {
                Log.d(
                    "viewRunningWorkoutActivity Delete:",
                    "DocumentSnapshot successfully deleted!"
                )
            }
            .addOnFailureListener { e ->
                Log.w(
                    "viewRunningWorkoutActivity Delete:",
                    "Error deleting document",
                    e
                )
            }
    }

    private fun setEditable(isEditable: Boolean) {
        findViewById<TextInputEditText>(R.id.running_distance_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.running_total_time_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.running_average_speed_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.running_top_speed_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.running_average_pulse_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.running_max_pulse_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.running_calories_editText).isEnabled = isEditable

        findViewById<TextInputEditText>(R.id.running_distance_editText).requestFocus()

        if (isEditable) {
            findViewById<Button>(R.id.running_edit_button).visibility = View.GONE
            findViewById<Button>(R.id.running_save_button).visibility = View.VISIBLE
            findViewById<Button>(R.id.running_delete_button).visibility = View.GONE
        } else {
            findViewById<Button>(R.id.running_edit_button).visibility = View.VISIBLE
            findViewById<Button>(R.id.running_save_button).visibility = View.GONE
            findViewById<Button>(R.id.running_delete_button).visibility = View.VISIBLE
        }
    }

    @SuppressLint("InflateParams")
    private fun saveRunningDialog() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_save_workout, null)
        val saveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)


        dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_title_editText)
            .setText(viewModel.title.value)
        dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_date_editText)
            .setText(viewModel.date.value)
        val dialog = dialogBuilder.show()

        saveButton.setOnClickListener {
            updateRunningWorkout(dialogView)
            viewModel.isInEditState.value = false
            dialog.cancel()
            finish()
        }
    }

    private fun updateRunningWorkout(dialogView: View) {
        val currentRunWorkoutRef = db.collection(Database.USERS)
            .document(intent.getStringExtra("userId")!!).collection(Database.WORKOUTS)
            .document(intent.getStringExtra("id")!!)
        currentRunWorkoutRef.update(
            Database.AVERAGE_PULSE,
            findViewById<TextInputEditText>(R.id.running_average_pulse_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.DISTANCE,
            findViewById<TextInputEditText>(R.id.running_distance_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.TOTAL_TIME,
            findViewById<TextInputEditText>(R.id.running_total_time_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.AVERAGE_SPEED,
            findViewById<TextInputEditText>(R.id.running_average_speed_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.TOP_SPEED,
            findViewById<TextInputEditText>(R.id.running_top_speed_editText).text.toString()
        )

        currentRunWorkoutRef.update(
            Database.MAX_PULSE,
            findViewById<TextInputEditText>(R.id.running_max_pulse_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.CALORIES,
            findViewById<TextInputEditText>(R.id.running_calories_editText).text.toString()
        )

        currentRunWorkoutRef.update(
            Database.WORKOUT_TITLE,
            dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_title_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.WORKOUT_DATE,
            dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_date_editText).text.toString()
        )
    }

    private fun getRunningInfoFromDb() {
        val currentRunWorkoutRef = db.collection(Database.USERS)
            .document(intent.getStringExtra("userId")!!).collection(Database.WORKOUTS)
            .document(intent.getStringExtra("id")!!)
        currentRunWorkoutRef.get().addOnSuccessListener { document ->
            if (document != null) {
                viewModel.title.value = document[Database.WORKOUT_TITLE].toString()
                viewModel.date.value = document[Database.WORKOUT_DATE].toString()
                viewModel.averagePulse.value =
                    document[Database.AVERAGE_PULSE].toString().toIntOrNull()
                viewModel.averageSpeed.value =
                    document[Database.AVERAGE_SPEED].toString().toFloatOrNull()
                viewModel.calories.value = document[Database.CALORIES].toString().toIntOrNull()
                viewModel.distance.value = document[Database.DISTANCE].toString().toFloatOrNull()
                viewModel.maxPulse.value = document[Database.MAX_PULSE].toString().toIntOrNull()
                viewModel.topSpeed.value = document[Database.TOP_SPEED].toString().toFloatOrNull()
                viewModel.totalTime.value = document[Database.TOTAL_TIME].toString().toFloatOrNull()

            } else {
                Log.d("view run workout document query:", "No such document")

            }
        }
    }

    private fun setObservers() {


        viewModel.isLoadedFromDb.observe(this, Observer {
            if (viewModel.isLoadedFromDb.value == false) {
                getRunningInfoFromDb()
                viewModel.isLoadedFromDb.value == true
            }
        })

        viewModel.isInEditState.observe(this, Observer {

            if (Database.getUserId() == intent.getStringExtra("userId")) {
                if (viewModel.isInEditState.value == true) {
                    setEditable(true)
                } else {
                    setEditable(false)
                }
            } else {
                setEditable(false)
                findViewById<Button>(R.id.running_edit_button).visibility = View.GONE
                findViewById<Button>(R.id.running_save_button).visibility = View.GONE
            }
        })

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
