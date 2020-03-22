package com.example.lightweight.ui.workouts.cycling

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.ui.workouts.Keyboard
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

class NewCyclingWorkoutActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var workoutTitle: TextInputEditText
    private lateinit var workoutDistance: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cycling_workout)

        workoutDistance = findViewById(R.id.cycling_distance_editText)

        val saveButton: Button = findViewById(R.id.cycling_save_button)
        saveButton.visibility = View.VISIBLE
        saveButton.setOnClickListener {
            saveCyclingDialog()
        }


    }

    private fun getCurrentDate() = LocalDate.now().toString()

    @SuppressLint("InflateParams")
    private fun saveCyclingDialog() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_save_workout, null)
        val dialogSaveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)
        val currentDate = getCurrentDate()
        dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_date_editText)
            .setText(currentDate)

        workoutTitle = dialogView.findViewById(R.id.dialog_save_workout_title_editText)
        workoutTitle.requestFocus()
        Keyboard().showKeyboard(this)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val dialog = dialogBuilder.show()

        dialogSaveButton.setOnClickListener {
            saveCyclingWorkout(dialogView, dialog)
            Keyboard().closeKeyboard(this)
        }

    }


    private fun saveCyclingWorkout(dialogView: View, dialog: AlertDialog) {
        val averageSpeed =
            findViewById<TextInputEditText>(R.id.cycling_average_speed_editText).text.toString()
        val topSpeed =
            findViewById<TextInputEditText>(R.id.cycling_top_speed_editText).text.toString()
        val totalTime =
            findViewById<TextInputEditText>(R.id.cycling_total_time_editText).text.toString()
        val averagePulse =
            findViewById<TextInputEditText>(R.id.cycling_average_pulse_editText).text.toString()
        val maxPulse =
            findViewById<TextInputEditText>(R.id.cycling_max_pulse_editText).text.toString()
        val averageForce =
            findViewById<TextInputEditText>(R.id.cycling_average_force_editText).text.toString()
        val maxForce =
            findViewById<TextInputEditText>(R.id.cycling_max_force_editText).text.toString()
        val averageCadence =
            findViewById<TextInputEditText>(R.id.cycling_average_cadence_editText).text.toString()
        val maxCadence =
            findViewById<TextInputEditText>(R.id.cycling_max_cadence_editText).text.toString()
        val calories =
            findViewById<TextInputEditText>(R.id.cycling_calories_editText).text.toString()


        val workoutDate =
            dialogView.findViewById<TextInputEditText>(R.id.dialog_save_workout_date_editText)
                .text.toString()


        val currentCyclingWorkoutRef = db.collection("users")
            .document(Database.getUserId()!!).collection("workouts").document()

        if (TextUtils.isEmpty(workoutTitle.text.toString())) {
            workoutTitle.error = getString(R.string.field_cant_be_empty)
        } else {
            val workoutInfo = hashMapOf(
                Database.DISTANCE to workoutDistance.text.toString(),
                Database.AVERAGE_SPEED to averageSpeed,
                Database.TOP_SPEED to topSpeed,
                Database.TOTAL_TIME to totalTime,
                Database.AVERAGE_PULSE to averagePulse,
                Database.MAX_PULSE to maxPulse,
                Database.AVERAGE_FORCE to averageForce,
                Database.MAX_FORCE to maxForce,
                Database.AVERAGE_CADENCE to averageCadence,
                Database.MAX_CADENCE to maxCadence,
                Database.CALORIES to calories,
                Database.TIMESTAMP to FieldValue.serverTimestamp(),
                Database.TYPE_OF_WORKOUT to "cyclingWorkout",
                Database.WORKOUT_TITLE to workoutTitle.text.toString(),
                Database.WORKOUT_DATE to workoutDate
            )
            currentCyclingWorkoutRef.set(workoutInfo)
            dialog.cancel()
            finish()
        }
    }
}
