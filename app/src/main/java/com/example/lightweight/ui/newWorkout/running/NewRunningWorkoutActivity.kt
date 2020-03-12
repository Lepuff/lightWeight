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
import org.w3c.dom.Text
import java.time.LocalDate

class NewRunningWorkoutActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_running_workout)
    }


    private fun getCurrentDate() = LocalDate.now().toString()
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

        val runningWorkoutList: MutableList<String> = ArrayList()

        val date = dialogView.findViewById<TextInputEditText>(R.id.save_workout_date_editText)
        val title = dialogView.findViewById<TextInputEditText>(R.id.save_workout_title_editText)


        val distance = findViewById<TextInputEditText>(R.id.new_running_distance_editText).text.toString()
        val totalTime = findViewById<TextInputEditText>(R.id.new_cycling_total_time_editText).text.toString()
        val averageSpeed =
            findViewById<TextInputEditText>(R.id.new_running_average_speed_editText).text.toString()
        val topSpeed = findViewById<TextInputEditText>(R.id.new_running_top_speed_edittext).text.toString()
        val averagePulse = findViewById<TextInputEditText>(R.id.new_running_average_pulse_editText).text.toString()
        val maxPulse = findViewById<TextInputEditText>(R.id.new_running_max_pulse_editText).text.toString()
        val averageForce = findViewById<TextInputEditText>(R.id.new_running_average_force_editText).text.toString()
        val maxForce = findViewById<TextInputEditText>(R.id.new_running_max_force_editText).text.toString()
        val calories = findViewById<TextInputEditText>(R.id.new_running_calories_editText).text.toString()

        val currentRunningWorkoutRef = db.collection("users")
            .document(Database.user.email!!).collection("workouts").document()

        runningWorkoutList.add(distance)
        runningWorkoutList.add(totalTime)
        runningWorkoutList.add(topSpeed)
        runningWorkoutList.add(averageSpeed)
        runningWorkoutList.add(maxPulse)
        runningWorkoutList.add(averagePulse)
        runningWorkoutList.add(maxForce)
        runningWorkoutList.add(averageForce)
        runningWorkoutList.add(calories)

        val workoutInfo = hashMapOf(
            "exercises" to runningWorkoutList,
            "timestamp" to FieldValue.serverTimestamp(),
            "typeOfWorkout" to "runningWorkout",
            "workoutTitle" to title,
            "workoutDate" to date
        )

        //adds current workout to database
        currentRunningWorkoutRef.set(workoutInfo)


    }
}

