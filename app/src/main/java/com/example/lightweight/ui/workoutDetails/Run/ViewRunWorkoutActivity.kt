package com.example.lightweight.ui.workoutDetails.Run

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.lightweight.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class ViewRunWorkoutActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_running_workout)







        setEditable(false)
        val editWorkoutButton = findViewById<Button>(R.id.new_running_edit_button)
        val saveButton = findViewById<Button>(R.id.new_running_save_button)
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


    private fun setEditable(boolean: Boolean){
        findViewById<TextInputEditText>(R.id.new_running_distance_editText).isEnabled = boolean
        findViewById<TextInputEditText>(R.id.new_running_total_time_editText).isEnabled = boolean
        findViewById<TextInputEditText>(R.id.new_running_average_speed_editText).isEnabled = boolean
        findViewById<TextInputEditText>(R.id.new_running_top_speed_editText).isEnabled = boolean
        findViewById<TextInputEditText>(R.id.new_running_average_pulse_editText).isEnabled = boolean
        findViewById<TextInputEditText>(R.id.new_running_max_pulse_editText).isEnabled = boolean
        findViewById<TextInputEditText>(R.id.new_running_calories_editText).isEnabled = boolean

    }
    private fun saveRunningDialog() {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_save_workout, null)
        val saveButton = dialogView.findViewById<Button>(R.id.save_workout_save_button)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val dialog = dialogBuilder.show()

        saveButton.setOnClickListener {
            //save
            dialog.cancel()
            finish()
        }

    }


    private fun getRunningInfoFromDb(){

    }



}
