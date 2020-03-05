package com.example.lightweight.ui.newWorkout.cycling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import com.example.lightweight.R
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate

class NewCyclingWorkoutActivity : AppCompatActivity() {

    private lateinit var viewModel: NewCyclingWorkoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_cycling_workout)

        viewModel = ViewModelProviders.of(this).get(NewCyclingWorkoutViewModel::class.java)
        val saveButton: Button = findViewById(R.id.new_cycling_save_button)

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
        dialogView.findViewById<TextInputEditText>(R.id.new_workout_date_editText)
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
            findViewById<TextInputEditText>(R.id.new_cycling_average_speed_editText).text
        var topSpeed = findViewById<TextInputEditText>(R.id.new_cycling_top_speed_editText).text
        var totalTime = findViewById<TextInputEditText>(R.id.new_cycling_total_time_editText).text
        var averagePulse =
            findViewById<TextInputEditText>(R.id.new_cycling_average_pulse_editText).text
        var maxPulse = findViewById<TextInputEditText>(R.id.new_cycling_max_pulse_editText).text
        var averageForce =
            findViewById<TextInputEditText>(R.id.new_cycling_average_force_editText).text
        var maxForce = findViewById<TextInputEditText>(R.id.new_cycling_max_force_editText).text
        var averageCadence =
            findViewById<TextInputEditText>(R.id.new_cycling_average_cadence_editText).text
        var maxCadence = findViewById<TextInputEditText>(R.id.new_cycling_max_cadence_editText).text
        var calories = findViewById<TextInputEditText>(R.id.new_cycling_calories_editText).text
        val workoutTitle =
            dialogView.findViewById<TextInputEditText>(R.id.new_workout_name_editText)
                .text

        val workoutDate =
            dialogView.findViewById<TextInputEditText>(R.id.new_workout_date_editText)
                .text

        //TODO spara allt där typ
    }
}
