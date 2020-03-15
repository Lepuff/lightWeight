package com.example.lightweight.ui.newWorkout.running

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.lightweight.Database
import com.example.lightweight.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class NewRunningWorkoutActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_running_workout)



        val saveButton = findViewById<Button>(R.id.new_running_save_button)
            saveButton.visibility = View.VISIBLE
        saveButton.setOnClickListener {
            saveRunningDialog()
        }
    }

    private fun saveRunningDialog() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_save_workout, null)
        val saveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)
        val currentDate = getCurrentDate()
        dialogView.findViewById<TextInputEditText>(R.id.save_workout_date_editText)
            .setText(currentDate)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val dialog = dialogBuilder.show()

        saveButton.setOnClickListener {
            saveRunningWorkout(dialogView)
            dialog.cancel()
            finish()
        }

    }

    private fun saveRunningWorkout(dialogView: View) {

        val date = dialogView.findViewById<TextInputEditText>(R.id.save_workout_date_editText).text.toString()
        val title = dialogView.findViewById<TextInputEditText>(R.id.save_workout_title_editText).text.toString()
        val currentRunningWorkoutRef = db.collection("users")
            .document(Database.user.email!!).collection("workouts").document()

        val workoutInfo = hashMapOf(
            "distance" to findViewById<TextInputEditText>(R.id.new_running_distance_editText).text.toString(),
            "totalTime" to findViewById<TextInputEditText>(R.id.new_running_total_time_editText).text.toString(),
            "averageSpeed" to findViewById<TextInputEditText>(R.id.new_running_average_speed_editText).text.toString(),
            "topSpeed" to findViewById<TextInputEditText>(R.id.new_running_top_speed_editText).text.toString(),
            "averagePulse" to findViewById<TextInputEditText>(R.id.new_running_average_pulse_editText).text.toString(),
            "maxPulse" to findViewById<TextInputEditText>(R.id.new_running_max_pulse_editText).text.toString(),
            "calories" to findViewById<TextInputEditText>(R.id.new_running_calories_editText).text.toString(),
            "timestamp" to FieldValue.serverTimestamp(),
            "typeOfWorkout" to "runningWorkout",
            "workoutTitle" to title,
            "workoutDate" to date
        )
        currentRunningWorkoutRef.set(workoutInfo)
    }

    private fun getCurrentDate() = LocalDate.now().toString()

}

