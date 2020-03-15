package com.example.lightweight.ui.newWorkout.cycling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.example.lightweight.Database
import com.example.lightweight.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class NewCyclingWorkoutActivity : AppCompatActivity() {

    private lateinit var viewModel: NewCyclingWorkoutViewModel
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycling_workout)

        viewModel = ViewModelProviders.of(this).get(NewCyclingWorkoutViewModel::class.java)
        val saveButton: Button = findViewById(R.id.cycling_save_button)

        findViewById<TextInputEditText>(R.id.cycling_total_time_editText).requestFocus()
        saveButton.setOnClickListener {
            saveCyclingDialog()
        }


    }

    private fun getCurrentDate() = LocalDate.now().toString()

    private fun saveCyclingDialog() {
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
            saveCyclingWorkout(dialogView)
            dialog.cancel()
            finish()
        }

    }


    private fun saveCyclingWorkout(dialogView: View) {



        var averageSpeed =
            findViewById<TextInputEditText>(R.id.cycling_average_speed_editText).text.toString()
        var topSpeed = findViewById<TextInputEditText>(R.id.cycling_top_speed_editText).text.toString()
        var totalTime = findViewById<TextInputEditText>(R.id.cycling_total_time_editText).text.toString()
        var averagePulse =
            findViewById<TextInputEditText>(R.id.cycling_average_pulse_editText).text.toString()
        var maxPulse = findViewById<TextInputEditText>(R.id.cycling_max_pulse_editText).text.toString()
        var averageForce =
            findViewById<TextInputEditText>(R.id.cycling_average_force_editText).text.toString()
        var maxForce = findViewById<TextInputEditText>(R.id.cycling_max_force_editText).text.toString()
        var averageCadence =
            findViewById<TextInputEditText>(R.id.cycling_average_cadence_editText).text.toString()
        var maxCadence = findViewById<TextInputEditText>(R.id.cycling_max_cadence_editText).text.toString()
        var calories = findViewById<TextInputEditText>(R.id.cycling_calories_editText).text.toString()
        val workoutTitle =
            dialogView.findViewById<TextInputEditText>(R.id.save_workout_title_editText)
                .text.toString()

        val workoutDate =
            dialogView.findViewById<TextInputEditText>(R.id.save_workout_date_editText)
                .text.toString()



        val currentCyclingWorkoutRef = db.collection("users")
            .document(Database.user.email!!).collection("workouts").document()


        val workoutInfo = hashMapOf(





            Database.timestamp to FieldValue.serverTimestamp(),
            Database.typeOfWorkout to "cyclingWorkout",
            Database.workoutTitle to workoutTitle,
            Database.workoutDate to workoutDate
        )

        //adds current workout to database
        currentCyclingWorkoutRef.set(workoutInfo)
    }
}
