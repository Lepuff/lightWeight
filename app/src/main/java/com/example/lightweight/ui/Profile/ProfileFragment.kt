package com.example.lightweight.ui.Profile

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isGone
import com.example.lightweight.Database

import com.example.lightweight.R
import com.example.lightweight.ui.login.LoginActivity
import com.facebook.AccessToken
import com.facebook.appevents.AppEventsConstants
import com.facebook.login.LoginManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val logoutButton = root.findViewById<Button>(R.id.profile_logout_button)

        logoutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut() //sign out user
            LoginManager.getInstance().logOut()
            AccessToken.setCurrentAccessToken(null)
            startActivity(Intent(activity, LoginActivity::class.java))

        }

        var profileFirstName = root.findViewById<TextView>(R.id.fragment_profile_first_name_editText).text
        var profileLastName = root.findViewById<TextView>(R.id.fragment_profile_last_name_editText).text
        var profileEmail = root.findViewById<TextView>(R.id.fragment_profile_email_editText).text

       /* if(Database.isFacebookUser()){
            root.findViewById<Button>(R.id.profile_change_password_button).isEnabled = false
            root.findViewById<Button>(R.id.profile_edit_profile_button).isEnabled = false
            //todo måste ändras så knapparna ej syns
        }
        */

        val profilePicture = root.findViewById<CircleImageView>(R.id.profile_image)
        profilePicture.setOnClickListener {
            //todo
        }

        val editProfile = root.findViewById<Button>(R.id.profile_edit_profile_button)
        editProfile.setOnClickListener {
            editProfileButtonVisiblity()
            fragment_profile_first_name_editText.isEnabled = true
            fragment_profile_last_name_editText.isEnabled = true
            fragment_profile_email_editText.isEnabled = true

        }

        val saveProfile = root.findViewById<Button>(R.id.profile_save_password_button)
        saveProfile.setOnClickListener {
            saveProfileButtonVisibility()
            fragment_profile_first_name_editText.isEnabled = false
            fragment_profile_last_name_editText.isEnabled = false
            fragment_profile_email_editText.isEnabled = false
            //todo spara ändrad info
        }

        val editPasswordButton = root.findViewById<Button>(R.id.profile_change_password_button)
        editPasswordButton.setOnClickListener {


            val dialogView = LayoutInflater.from(this.context).inflate(R.layout.dialog_change_password, null)
            val builder = AlertDialog.Builder(this.context)
            builder.setView(dialogView)
            val dialog = builder.show()
        }

        return root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }



    fun editProfileButtonVisiblity(){
        profile_edit_profile_button.visibility = View.GONE
        profile_save_password_button.visibility = View.VISIBLE
    }

    fun saveProfileButtonVisibility(){
        profile_edit_profile_button.visibility = View.VISIBLE
        profile_save_password_button.visibility = View.GONE
    }

}