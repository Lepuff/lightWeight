package com.example.lightweight.ui.workouts.running

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.classes.Keyboard
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class NewRunningWorkoutActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var workoutTitle: TextInputEditText
    private lateinit var workoutDistance: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_workout)

        workoutDistance = findViewById(R.id.running_distance_editText)

        val saveButton = findViewById<Button>(R.id.running_save_button)
        saveButton.visibility = View.VISIBLE
        saveButton.setOnClickListener {
            saveRunningDialog()
        }
    }

    @SuppressLint("InflateParams")
    private fun saveRunningDialog() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_save_workout, null)
        val saveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)
        val currentDate = getCurrentDate()
        dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_date_editText)
            .setText(currentDate)

        workoutTitle = dialogView.findViewById(R.id.dialog_save_workout_title_editText)
        workoutTitle.requestFocus()
        Keyboard().showKeyboard(this)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val dialog = dialogBuilder.show()

        saveButton.setOnClickListener {
            saveRunningWorkout(dialogView, dialog)
            Keyboard().closeKeyboard(this)
        }
    }

    private fun saveRunningWorkout(dialogView: View, dialog: AlertDialog) {

        if (TextUtils.isEmpty(workoutTitle.text.toString())) {
            workoutTitle.error = getString(R.string.field_cant_be_empty)
        } else {
            val date =
                dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_date_editText)
                    .text.toString()
            val currentRunningWorkoutRef = db.collection(Database.USERS)
                .document(Database.getUserId()!!).collection(Database.WORKOUTS).document()

            val workoutInfo = hashMapOf(
                Database.DISTANCE to workoutDistance.text.toString(),
                Database.TOTAL_TIME to findViewById<TextInputEditText>(R.id.running_total_time_editText).text.toString(),
                Database.AVERAGE_SPEED to findViewById<TextInputEditText>(R.id.running_average_speed_editText).text.toString(),
                Database.TOP_SPEED to findViewById<TextInputEditText>(R.id.running_top_speed_editText).text.toString(),
                Database.AVERAGE_PULSE to findViewById<TextInputEditText>(R.id.running_average_pulse_editText).text.toString(),
                Database.MAX_PULSE to findViewById<TextInputEditText>(R.id.running_max_pulse_editText).text.toString(),
                Database.CALORIES to findViewById<TextInputEditText>(R.id.running_calories_editText).text.toString(),
                Database.TIMESTAMP to FieldValue.serverTimestamp(),
                Database.TYPE_OF_WORKOUT to Database.RUNNING_WORKOUT,
                Database.WORKOUT_TITLE to workoutTitle.text.toString(),
                Database.WORKOUT_DATE to date
            )
            currentRunningWorkoutRef.set(workoutInfo)
            dialog.cancel()
            finish()
        }
    }

    private fun getCurrentDate() = LocalDate.now().toString()

}

