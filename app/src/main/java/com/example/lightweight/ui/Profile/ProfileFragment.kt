package com.example.lightweight.ui.Profile

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.lightweight.Database

import com.example.lightweight.R
import com.example.lightweight.ui.login.LoginActivity
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

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

        var profileName = root.findViewById<TextView>(R.id.text_profile_name).text
        var profileAge = root.findViewById<TextView>(R.id.text_profile_age).text
        var profileEmail = root.findViewById<TextView>(R.id.text_profile_mail).text
        var profileNewPassword = root.findViewById<TextInputEditText>(R.id.profile_new_password).text
        var profileConfirmPassword = root.findViewById<TextInputEditText>(R.id.profile_confirm_password).text



        return root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }


}
