package com.example.lightweight.ui.Profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lightweight.Database
import com.example.lightweight.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.adapters.UserAdapter
import com.example.lightweight.classes.User
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.ui.login.LoginActivity
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private val PICK_PHOTO_REQUEST = 1
    private lateinit var profilePicture: CircleImageView
    private lateinit var friendAdapter: UserAdapter
    private var db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        profilePicture = root.findViewById<CircleImageView>(R.id.profile_image)
        val logoutButton = root.findViewById<Button>(R.id.profile_logout_button)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut() //sign out user
            LoginManager.getInstance().logOut()
            AccessToken.setCurrentAccessToken(null)
            startActivity(Intent(activity, LoginActivity::class.java))
            activity!!.finish()
        }

        root.findViewById<TextView>(R.id.fragment_profile_first_name_editText).text = Database.getUserFirstName()
        root.findViewById<TextView>(R.id.fragment_profile_last_name_editText).text = Database.getUserLastName()
        root.findViewById<TextView>(R.id.fragment_profile_email_editText).text = Database.getUserEmail()


        //get profile pic and load into glide
        updateGlidePicture(Database.getUserPicture())

        profilePicture.setOnClickListener {
            pickPhotoFromGallery()
        }


        if (Database.isFacebookUser()) {
            root.findViewById<Button>(R.id.profile_change_password_button).visibility = View.GONE
            root.findViewById<Button>(R.id.profile_edit_profile_button).visibility = View.GONE
        }


        val addFriendsButton = root.findViewById<Button>(R.id.profile_add_friends_button)
        addFriendsButton.setOnClickListener {
            val intent = Intent(activity, AddFriendsActivity::class.java)
            startActivity(intent)
        }


        val editProfileButton = root.findViewById<Button>(R.id.profile_edit_profile_button)
        editProfileButton.setOnClickListener {
            editProfileButtonVisiblity()
            fragment_profile_first_name_editText.isEnabled = true
            fragment_profile_last_name_editText.isEnabled = true
            fragment_profile_email_editText.isEnabled = true

        }

        val editPasswordButton = root.findViewById<Button>(R.id.profile_change_password_button)
        editPasswordButton.setOnClickListener {


            val dialogView =
                LayoutInflater.from(this.context).inflate(R.layout.dialog_change_password, null)
            val builder = AlertDialog.Builder(this.context)
            builder.setView(dialogView)
            val dialog = builder.show()
        }

        val saveProfileButton = root.findViewById<Button>(R.id.profile_save_profile_button)
        saveProfileButton.setOnClickListener {
            saveProfileButtonVisibility()
            fragment_profile_first_name_editText.isEnabled = false
            fragment_profile_last_name_editText.isEnabled = false
            fragment_profile_email_editText.isEnabled = false
            //todo spara ändrad info
        }
        return root

    }

    private fun updateGlidePicture(imageUri: Uri?){

        val requestOption = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_error_layer)
            .fallback(R.drawable.ic_fallback_foreground)

        Glide.with(this)
            .applyDefaultRequestOptions(requestOption)
            .load(imageUri)
            .into(profilePicture)
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
            updateGlidePicture(data.data!!)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        initRecyclerView()

    }


    override fun onStart() {
        super.onStart()
    }


    override fun onResume() {
        super.onResume()
        getFriendsFromDb()
    }

    override fun onPause() {
        super.onPause()
        friendAdapter.clearList()
    }


    private fun getFriendsFromDb() {
        db.collection(Database.USERS).document(Database.getUserId()!!).collection(Database.FRIENDS).get().addOnSuccessListener {friends ->

            if (friends!=null){
                for (friend in friends){
                    val user = User()
                    user.email = friend[Database.EMAIL].toString()
                    user.id = friend[Database.ID].toString()
                    friendAdapter.addItem(user)
                }
            }
        }
    }




    fun editProfileButtonVisiblity() {
        profile_edit_profile_button.visibility = View.GONE
        profile_save_profile_button.visibility = View.VISIBLE
    }

    fun saveProfileButtonVisibility() {
        profile_edit_profile_button.visibility = View.VISIBLE
        profile_save_profile_button.visibility = View.GONE
    }

    private fun initRecyclerView() {
        profile_recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(5)//todo fix
            addItemDecoration(topSpacingItemDecoration)
            friendAdapter = UserAdapter(this, db, false)
            adapter = friendAdapter
        }
    }

}