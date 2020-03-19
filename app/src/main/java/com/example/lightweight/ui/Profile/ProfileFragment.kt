package com.example.lightweight.ui.Profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.app.AlertDialog
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lightweight.Database
import com.example.lightweight.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Registry
import com.example.lightweight.adapters.UserAdapter
import com.example.lightweight.classes.User
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.ui.login.LoginActivity
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private val itemPaddingTop = 5

    private val PICK_PHOTO_REQUEST = 1


    private lateinit var viewModel: ProfileViewModel
    private lateinit var friendAdapter: UserAdapter
    private var db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        viewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.isInEditState.observe(viewLifecycleOwner, Observer {
            handelEditStates()
        })
        loadUserInfo(root)

        val logoutButton = root.findViewById<Button>(R.id.profile_logout_button)
        logoutButton.setOnClickListener {
            logOut()
        }

        val profilePicture = root.findViewById<CircleImageView>(R.id.profile_image)
        updateGlidePicture(Database.getUserPicture(),profilePicture)
        profilePicture.setOnClickListener {
            pickPhotoFromGallery()
        }


        val addFriendsButton = root.findViewById<Button>(R.id.profile_add_friends_button)
        addFriendsButton.setOnClickListener {
            val intent = Intent(activity, AddFriendsActivity::class.java)
            startActivity(intent)
        }


        val editProfileButton = root.findViewById<Button>(R.id.profile_edit_profile_button)
        editProfileButton.setOnClickListener {
            viewModel.isInEditState.value = true
        }

        val editPasswordButton = root.findViewById<Button>(R.id.profile_change_password_button)
        editPasswordButton.setOnClickListener {
            changePasswordDialog()
        }

        val saveProfileButton = root.findViewById<Button>(R.id.profile_save_profile_button)
        saveProfileButton.setOnClickListener {
            saveProfileInfo(root)
            viewModel.isInEditState.value = false
        }
        return root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        getFriendsFromDb()
    }

    override fun onPause() {
        super.onPause()
        friendAdapter.clearList()
    }

    @SuppressLint("InflateParams")
    private fun changePasswordDialog() {
        val dialogView =
            LayoutInflater.from(this.context).inflate(R.layout.dialog_change_password, null)
        val builder = AlertDialog.Builder(this.context)
        builder.setView(dialogView)
        val dialog = builder.show()

        dialogView.findViewById<Button>(R.id.dialog_save_password_button).setOnClickListener {
            saveNewPassword(dialogView)
            dialog.cancel()
        }

    }


    private fun saveNewPassword(dialogView: View) {
        dialogView.findViewById<TextInputEditText>(R.id.dialog_old_password_editText).text
        dialogView.findViewById<TextInputEditText>(R.id.dialog_new_password_editText).text
        dialogView.findViewById<TextInputEditText>(R.id.dialog_confirm_password_editText).text

        //todo samuel save stuff
    }

    private fun saveProfileInfo(view: View) {
        view.findViewById<TextInputEditText>(R.id.fragment_profile_first_name_editText).text
        view.findViewById<TextInputEditText>(R.id.fragment_profile_last_name_editText).text
        view.findViewById<TextInputEditText>(R.id.fragment_profile_email_editText).text

        //todo samuel
    }

    private fun loadUserInfo(view: View) {
        view.findViewById<TextView>(R.id.fragment_profile_first_name_editText).text =
            Database.getUserFirstName()
        view.findViewById<TextView>(R.id.fragment_profile_last_name_editText).text =
            Database.getUserLastName()
        view.findViewById<TextView>(R.id.fragment_profile_email_editText).text =
            Database.getUserEmail()
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut() //sign out user
        LoginManager.getInstance().logOut()
        AccessToken.setCurrentAccessToken(null)
        startActivity(Intent(activity, LoginActivity::class.java))
        activity!!.finish()
    }

    private fun updateGlidePicture(imageUri: Uri?,circleImageView: CircleImageView) {
        val requestOption = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_error_layer)
            .fallback(R.drawable.ic_person_yellow_24dp)


        Log.d("image Uri value::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: ",imageUri.toString())
        Glide.with(this)
            .applyDefaultRequestOptions(requestOption)
            .load(imageUri)

            .into(circleImageView)
    }

    private fun pickPhotoFromGallery() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_PHOTO_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_PHOTO_REQUEST &&
            data != null && data.data != null
        ) {
            Database.setUserPicture(data.data!!)
            updateGlidePicture(data.data!!,profile_image)
        }
    }

    private fun handelEditStates() {
        if (Database.isFacebookUser()) {
            view!!.findViewById<Button>(R.id.profile_change_password_button).visibility =
                View.GONE
            view!!.findViewById<Button>(R.id.profile_edit_profile_button).visibility =
                View.GONE
        } else {
            view!!.findViewById<Button>(R.id.profile_change_password_button).visibility =
                View.VISIBLE
            if (viewModel.isInEditState.value!!) {
                fragment_profile_first_name_editText.isEnabled = true
                fragment_profile_last_name_editText.isEnabled = true
                fragment_profile_email_editText.isEnabled = true
                view!!.findViewById<Button>(R.id.profile_edit_profile_button).visibility =
                    View.GONE
                view!!.findViewById<Button>(R.id.profile_save_profile_button).visibility =
                    View.VISIBLE
            } else {
                fragment_profile_first_name_editText.isEnabled = false
                fragment_profile_last_name_editText.isEnabled = false
                fragment_profile_email_editText.isEnabled = false
                view!!.findViewById<Button>(R.id.profile_edit_profile_button).visibility =
                    View.VISIBLE
                view!!.findViewById<Button>(R.id.profile_save_profile_button).visibility =
                    View.GONE
            }
        }
    }


    private fun getFriendsFromDb() {
        db.collection(Database.USERS).document(Database.getUserId()!!).collection(Database.FRIENDS)
            .get().addOnSuccessListener { friends ->

                if (friends != null) {
                    for (friend in friends) {
                        val user = User()
                        user.email = friend[Database.EMAIL].toString()
                        user.id = friend[Database.ID].toString()
                        friendAdapter.addItem(user)
                    }
                }
            }
    }

    private fun initRecyclerView() {
        profile_recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(itemPaddingTop)
            addItemDecoration(topSpacingItemDecoration)
            friendAdapter = UserAdapter(this, db, false)
            adapter = friendAdapter
        }
    }
}