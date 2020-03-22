package com.example.lightweight.ui.workouts.cycling

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
import com.example.lightweight.ViewModels.CyclingViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class ViewCyclingWorkoutActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var viewModel: CyclingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycling_workout)
        viewModel = ViewModelProviders.of(this).get(CyclingViewModel::class.java)
        setObservers()

        val editButton = findViewById<Button>(R.id.cycling_edit_button)
        editButton.setOnClickListener {
            viewModel.isInEditState.value = true
        }

        val saveButton = findViewById<Button>(R.id.cycling_save_button)
        saveButton.setOnClickListener {
            saveCyclingDialog()
        }


        val deleteButton = findViewById<Button>(R.id.cycling_delete_button)
        deleteButton.setOnClickListener {
            deleteWorkoutDialog()

        }


    }

    @SuppressLint("InflateParams")
    private fun saveCyclingDialog() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_save_workout, null)
        val saveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)

        dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_date_editText)
            .setText(viewModel.title.value.toString())
        dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_date_editText)
            .setText(viewModel.date.value.toString())
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val dialog = dialogBuilder.show()
        saveButton.setOnClickListener {
            viewModel.isInEditState.value = false
            updateCyclingWorkout(dialogView)
            finish()
            dialog.cancel()
        }
    }

    private fun deleteWorkoutDialog() {

        val builder = AlertDialog.Builder(this, R.style.DialogStyle)
        builder.setTitle(R.string.delete_workout_message)
        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            deleteCyclingWorkout()
            dialog.cancel()
            finish()
        }
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun deleteCyclingWorkout() {

        db.collection(Database.USERS).document(intent.getStringExtra("userId")!!)
            .collection(Database.WORKOUTS).document(intent.getStringExtra("id")!!).delete()
            .addOnSuccessListener {
                Log.d(
                    "viewCyclingWorkoutActivity Delete:",
                    "DocumentSnapshot successfully deleted!"
                )
            }
            .addOnFailureListener { e ->
                Log.w(
                    "viewCyclingWorkoutActivity Delete:",
                    "Error deleting document",
                    e
                )
            }
    }


    private fun setEditable(isEditable: Boolean) {
        findViewById<TextInputEditText>(R.id.cycling_distance_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.cycling_total_time_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.cycling_average_speed_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.cycling_top_speed_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.cycling_average_pulse_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.cycling_max_pulse_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.cycling_average_cadence_editText).isEnabled =
            isEditable
        findViewById<TextInputEditText>(R.id.cycling_calories_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.cycling_max_cadence_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.cycling_average_force_editText).isEnabled = isEditable
        findViewById<TextInputEditText>(R.id.cycling_max_force_editText).isEnabled = isEditable


        findViewById<TextInputEditText>(R.id.cycling_distance_editText).requestFocus()
        if (isEditable) {
            findViewById<Button>(R.id.cycling_save_button).visibility = View.VISIBLE
            findViewById<Button>(R.id.cycling_edit_button).visibility = View.GONE
            findViewById<Button>(R.id.cycling_delete_button).visibility = View.GONE
        } else {
            findViewById<Button>(R.id.cycling_save_button).visibility = View.GONE
            findViewById<Button>(R.id.cycling_edit_button).visibility = View.VISIBLE
            findViewById<Button>(R.id.cycling_delete_button).visibility = View.VISIBLE
        }
    }


    private fun setObservers() {

        viewModel.isInEditState.observe(this, Observer {
            if (intent.getStringExtra("userId")!! == Database.getUserId()) {
                if (viewModel.isInEditState.value == true) {
                    setEditable(true)
                } else {
                    setEditable(false)
                }
            } else {
                setEditable(false)
                findViewById<Button>(R.id.cycling_save_button).visibility = View.GONE
                findViewById<Button>(R.id.cycling_edit_button).visibility = View.GONE
                findViewById<Button>(R.id.cycling_delete_button).visibility = View.GONE
            }
        })

        viewModel.isLoadedFromDb.observe(this, Observer {
            if (viewModel.isLoadedFromDb.value == false) {
                getCyclingInfoFromDb()
                viewModel.isLoadedFromDb.value == true
            }
        })

        viewModel.averagePulse.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.cycling_average_pulse_editText).setText(
                viewModel.averagePulse.value.toString()
            )
        })

        viewModel.distance.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.cycling_distance_editText).setText(viewModel.distance.value.toString())
        })

        viewModel.totalTime.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.cycling_total_time_editText).setText(viewModel.totalTime.value.toString())
        })

        viewModel.averageSpeed.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.cycling_average_speed_editText).setText(
                viewModel.averageSpeed.value.toString()
            )
        })

        viewModel.topSpeed.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.cycling_top_speed_editText).setText(viewModel.topSpeed.value.toString())
        })

        viewModel.maxPulse.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.cycling_max_pulse_editText).setText(viewModel.maxPulse.value.toString())
        })

        viewModel.calories.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.cycling_calories_editText).setText(viewModel.calories.value.toString())
        })

        viewModel.title.observe(this, Observer {
            title = viewModel.title.value
        })

        viewModel.maxCadence.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.cycling_max_cadence_editText).setText(viewModel.maxCadence.value.toString())

        })
        viewModel.averageCadence.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.cycling_average_cadence_editText).setText(viewModel.averageCadence.value.toString())
        })
        viewModel.maxForce.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.cycling_max_force_editText).setText(viewModel.maxForce.value.toString())
        })

        viewModel.averageForce.observe(this, Observer {
            findViewById<TextInputEditText>(R.id.cycling_average_force_editText).setText(viewModel.averageForce.value.toString())
        })
    }

    private fun getCyclingInfoFromDb() {
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
                viewModel.averageCadence.value =
                    document[Database.AVERAGE_CADENCE].toString().toIntOrNull()
                viewModel.maxCadence.value = document[Database.MAX_CADENCE].toString().toIntOrNull()
                viewModel.averageForce.value =
                    document[Database.AVERAGE_FORCE].toString().toIntOrNull()
                viewModel.maxForce.value = document[Database.MAX_FORCE].toString().toIntOrNull()
            } else {
                Log.d("view cycling workout document query:", "No such document")

            }
        }
    }


    private fun updateCyclingWorkout(dialogView: View) {
        val currentRunWorkoutRef = db.collection(Database.USERS)
            .document(Database.getUserId()!!).collection(Database.WORKOUTS)
            .document(intent.getStringExtra("id")!!)//todo fix constants

        currentRunWorkoutRef.update(
            Database.DISTANCE,
            findViewById<TextInputEditText>(R.id.cycling_distance_editText).text.toString()
        )

        currentRunWorkoutRef.update(
            Database.TOTAL_TIME,
            findViewById<TextInputEditText>(R.id.cycling_total_time_editText).text.toString()
        )

        currentRunWorkoutRef.update(
            Database.AVERAGE_SPEED,
            findViewById<TextInputEditText>(R.id.cycling_average_speed_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.TOP_SPEED,
            findViewById<TextInputEditText>(R.id.cycling_top_speed_editText).text.toString()
        )

        currentRunWorkoutRef.update(
            Database.MAX_PULSE,
            findViewById<TextInputEditText>(R.id.cycling_max_pulse_editText).text.toString()
        )

        currentRunWorkoutRef.update(
            Database.AVERAGE_PULSE,
            findViewById<TextInputEditText>(R.id.cycling_average_pulse_editText).text.toString()
        )

        currentRunWorkoutRef.update(
            Database.CALORIES,
            findViewById<TextInputEditText>(R.id.cycling_calories_editText).text.toString()
        )

        currentRunWorkoutRef.update(
            Database.MAX_FORCE,
            findViewById<TextInputEditText>(R.id.cycling_max_force_editText).text.toString()
        )
        currentRunWorkoutRef.update(
            Database.AVERAGE_FORCE,
            findViewById<TextInputEditText>(R.id.cycling_average_force_editText).text.toString()
        )

        currentRunWorkoutRef.update(
            Database.MAX_CADENCE,
            findViewById<TextInputEditText>(R.id.cycling_max_cadence_editText).text.toString()
        )

        currentRunWorkoutRef.update(
            Database.AVERAGE_CADENCE,
            findViewById<TextInputEditText>(R.id.cycling_average_cadence_editText).text.toString()
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


}
