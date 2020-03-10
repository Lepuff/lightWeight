package com.example.lightweight.ui.newWorkout.running

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.lightweight.R
import com.google.android.material.textfield.TextInputEditText
import org.w3c.dom.Text
import java.time.LocalDate

class NewRunningWorkoutActivity : AppCompatActivity() {

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


        val date = dialogView.findViewById<TextInputEditText>(R.id.save_workout_date_editText)
        val title = dialogView.findViewById<TextInputEditText>(R.id.save_workout_title_editText)


        val distance = findViewById<TextInputEditText>(R.id.new_running_distance_editText).text
        val totalTime = findViewById<TextInputEditText>(R.id.new_cycling_total_time_editText).text
        val averageSpeed =
            findViewById<TextInputEditText>(R.id.new_running_average_speed_editText).text
        val topSpeed = findViewById<TextInputEditText>(R.id.new_running_top_speed_edittext).text
        val averagePulse = findViewById<TextInputEditText>(R.id.new_running_average_pulse_editText).text
        val maxPulse = findViewById<TextInputEditText>(R.id.new_running_max_pulse_editText).text
        val averageForce = findViewById<TextInputEditText>(R.id.new_running_average_force_editText).text
        val maxForce = findViewById<TextInputEditText>(R.id.new_running_max_force_editText).text
        val calories = findViewById<TextInputEditText>(R.id.new_running_calories_editText).text
        //todo samuel databas


    }
}

