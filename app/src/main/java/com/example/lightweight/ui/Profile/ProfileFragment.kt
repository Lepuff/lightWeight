package com.example.lightweight.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.app.AlertDialog
import android.content.ContentValues
import android.content.pm.PackageManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lightweight.Database
import com.example.lightweight.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.Validation
import com.example.lightweight.ViewModels.ProfileViewModel
import com.example.lightweight.adapters.UserAdapter
import com.example.lightweight.classes.User
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.example.lightweight.ui.login.LoginActivity
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.dialog_change_password.view.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private val itemPaddingTop = 5
    private val PICK_PHOTO_REQUEST = 1
    private lateinit var viewModel: ProfileViewModel
    private lateinit var friendAdapter: UserAdapter
    private var db = FirebaseFirestore.getInstance()
    private lateinit var textInputOldLayout: TextInputLayout
    private lateinit var textInputNewLayout: TextInputLayout
    private lateinit var textInputConfirmLayout: TextInputLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        viewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.isInEditState.observe(viewLifecycleOwner, Observer {
            handleEditStates()
        })
        loadUserInfo(root)

        val logoutButton = root.findViewById<Button>(R.id.profile_logout_button)
        logoutButton.setOnClickListener {
            logOutDialog()
        }
        val profilePicture = root.findViewById<CircleImageView>(R.id.profile_image)
        updateGlidePicture(Database.getUserPicture(), profilePicture)

        val cameraButton =
            root.findViewById<ImageButton>(R.id.profile_camera_button)
        cameraButton.setOnClickListener {
                checkStoragePermission()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PICK_PHOTO_REQUEST){
            if (permissions[0].equals(android.Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[0]
            == PackageManager.PERMISSION_GRANTED){
                pickPhotoFromGallery()
            }
        }
    }

    private fun checkStoragePermission(){
        //If permission is not already granted
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED){
            //Request permission to read storage
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PICK_PHOTO_REQUEST)
        } else{
            //if permission is already granted
            pickPhotoFromGallery()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        checkUpdates()
    }
    @SuppressLint("InflateParams")
    private fun changePasswordDialog() {
        val dialogView =
            LayoutInflater.from(this.context).inflate(R.layout.dialog_change_password, null)
        val builder = AlertDialog.Builder(this.context)
        builder.setView(dialogView)
        val dialog = builder.show()


        textInputOldLayout = dialogView.findViewById(R.id.dialog_old_password_layout)
        textInputNewLayout = dialogView.findViewById(R.id.dialog_new_password_layout)
        textInputConfirmLayout = dialogView.findViewById(R.id.dialog_confirm_password_layout)
        //adds textChangedListeners to all password fields
        setTextChangedListeners(dialogView)

        dialogView.findViewById<Button>(R.id.dialog_save_password_button).setOnClickListener {
            saveNewPassword(dialogView, dialog)
        }

    }

    private fun saveNewPassword(dialogView: View, dialog: AlertDialog) {
        val currentUser = FirebaseAuth.getInstance().currentUser!!
        val oldPassword =
            dialogView.findViewById<TextInputEditText>(R.id.dialog_old_password_editText).text
        val newPassword =
            dialogView.findViewById<TextInputEditText>(R.id.dialog_new_password_editText).text
        val confirmPassword =
            dialogView.findViewById<TextInputEditText>(R.id.dialog_confirm_password_editText).text

        //Re-authenticate user by having to type in correct password
        if (!TextUtils.isEmpty(oldPassword)) {
            val credential =
                EmailAuthProvider.getCredential(currentUser.email!!, oldPassword.toString())
            currentUser.reauthenticate(credential).addOnCompleteListener { auth ->
                if (auth.isSuccessful) {
                    if (TextUtils.isEmpty(newPassword) || !Validation.isValidPassword(newPassword)) {
                        dialogView.findViewById<TextInputEditText>(R.id.dialog_new_password_editText)
                            .error = getString(R.string.password_too_short)
                        dialogView.findViewById<TextInputEditText>(R.id.dialog_new_password_editText)
                            .requestFocus()
                        textInputNewLayout.endIconMode = TextInputLayout.END_ICON_NONE
                    } else if (TextUtils.isEmpty(confirmPassword) || !Validation.isValidPassword(
                            confirmPassword
                        )
                    ) {
                        dialogView.findViewById<TextInputEditText>(R.id.dialog_confirm_password_editText)
                            .error = getString(R.string.password_too_short)
                        dialogView.findViewById<TextInputEditText>(R.id.dialog_confirm_password_editText)
                            .requestFocus()
                        textInputConfirmLayout.endIconMode = TextInputLayout.END_ICON_NONE
                    } else if (newPassword.toString() != confirmPassword.toString()) {
                        dialogView.findViewById<TextInputEditText>(R.id.dialog_confirm_password_editText)
                            .error = getString(R.string.passwords_dont_match)
                        dialogView.findViewById<TextInputEditText>(R.id.dialog_confirm_password_editText)
                            .requestFocus()
                        textInputConfirmLayout.endIconMode = TextInputLayout.END_ICON_NONE
                    } else {
                        currentUser.updatePassword(confirmPassword.toString())
                        Toast.makeText(
                            this.context,
                            getString(R.string.password_successfully_changed),
                            Toast.LENGTH_LONG
                        ).show()
                        dialog.cancel()
                    }

                } else {
                    dialogView.findViewById<TextInputEditText>(R.id.dialog_old_password_editText)
                        .error = getString(R.string.invalid_password)
                    dialogView.findViewById<TextInputEditText>(R.id.dialog_old_password_editText)
                        .requestFocus()
                    textInputOldLayout.endIconMode = TextInputLayout.END_ICON_NONE
                }
            }
        } else {
            dialogView.findViewById<TextInputEditText>(R.id.dialog_old_password_editText)
                .error = getString(R.string.invalid_password)
            dialogView.findViewById<TextInputEditText>(R.id.dialog_old_password_editText)
                .requestFocus()
            textInputOldLayout.endIconMode = TextInputLayout.END_ICON_NONE
        }
    }


    private fun saveProfileInfo(view: View) {
        val firstName =
            view.findViewById<TextInputEditText>(R.id.fragment_profile_first_name_editText).text
        val lastName =
            view.findViewById<TextInputEditText>(R.id.fragment_profile_last_name_editText).text
        val email = view.findViewById<TextInputEditText>(R.id.fragment_profile_email_editText).text

        Database.updateUser(firstName.toString(), lastName.toString(), email.toString())
    }

    private fun loadUserInfo(view: View) {
        view.findViewById<TextView>(R.id.fragment_profile_first_name_editText).text =
            Database.getUserFirstName()
        view.findViewById<TextView>(R.id.fragment_profile_last_name_editText).text =
            Database.getUserLastName()
        view.findViewById<TextView>(R.id.fragment_profile_email_editText).text =
            Database.getUserEmail()
    }


    private fun setTextChangedListeners(dialogView: View) {
        dialogView.findViewById<TextInputEditText>(R.id.dialog_old_password_editText)
            .addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (dialogView.dialog_old_password_layout.endIconMode != TextInputLayout.END_ICON_PASSWORD_TOGGLE)
                        dialogView.dialog_old_password_layout.endIconMode =
                            TextInputLayout.END_ICON_PASSWORD_TOGGLE
                }

                override fun afterTextChanged(s: Editable?) {}
            })

        dialogView.dialog_new_password_editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (dialogView.dialog_new_password_layout.endIconMode != TextInputLayout.END_ICON_PASSWORD_TOGGLE)
                    dialogView.dialog_new_password_layout.endIconMode =
                        TextInputLayout.END_ICON_PASSWORD_TOGGLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        dialogView.dialog_confirm_password_editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (dialogView.dialog_confirm_password_layout.endIconMode != TextInputLayout.END_ICON_PASSWORD_TOGGLE)
                    dialogView.dialog_confirm_password_layout.endIconMode =
                        TextInputLayout.END_ICON_PASSWORD_TOGGLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun logOutDialog(){

        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.DialogStyle)
        builder.setTitle(R.string.log_out_message)
        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            logOut()
            dialog.cancel()
        }
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.cancel()
        }
        builder.show()

    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut() //sign out user
        LoginManager.getInstance().logOut()
        AccessToken.setCurrentAccessToken(null)
        startActivity(Intent(activity, LoginActivity::class.java))
        requireActivity().finish()
    }

    private fun updateGlidePicture(imageUri: Uri?, circleImageView: CircleImageView) {
        val requestOption = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_person_yellow_24dp)
            .fallback(R.drawable.ic_person_yellow_24dp)

        if (imageUri.toString() == "null") {
            Glide.with(this)
                .applyDefaultRequestOptions(requestOption)
                .load(R.drawable.ic_person_yellow_24dp)
                .into(circleImageView)
        } else {

            Glide.with(this)
                .applyDefaultRequestOptions(requestOption)
                .load(imageUri)
                .into(circleImageView)
        }
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
            updateGlidePicture(data.data!!, profile_image)
        }
    }

    private fun handleEditStates() {
        if (Database.isFacebookUser()) {
            requireView().findViewById<Button>(R.id.profile_change_password_button).visibility =
                View.GONE
            requireView().findViewById<Button>(R.id.profile_edit_profile_button).visibility =
                View.GONE
            requireView().findViewById<Button>(R.id.profile_save_profile_button).visibility =
                View.GONE
            requireView().findViewById<ImageButton>(R.id.profile_camera_button).visibility = View.GONE
        } else {

            
            requireView().findViewById<ImageButton>(R.id.profile_camera_button).visibility = View.VISIBLE
            requireView().findViewById<Button>(R.id.profile_change_password_button).visibility =
                View.VISIBLE
            if (viewModel.isInEditState.value!!) {
                fragment_profile_first_name_editText.isEnabled = true
                fragment_profile_last_name_editText.isEnabled = true
                fragment_profile_email_editText.isEnabled = true
                requireView().findViewById<Button>(R.id.profile_edit_profile_button).visibility =
                    View.GONE
                requireView().findViewById<Button>(R.id.profile_save_profile_button).visibility =
                    View.VISIBLE
            } else {
                fragment_profile_first_name_editText.isEnabled = false
                fragment_profile_last_name_editText.isEnabled = false
                fragment_profile_email_editText.isEnabled = false
                requireView().findViewById<Button>(R.id.profile_edit_profile_button).visibility =
                    View.VISIBLE
                requireView().findViewById<Button>(R.id.profile_save_profile_button).visibility =
                    View.GONE
            }
        }
    }


    private fun checkUpdates(){
        db.collection(Database.USERS).document(Database.getUserId()!!).collection(Database.FRIENDS).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(ContentValues.TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            for (dc in snapshot!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> Log.d(ContentValues.TAG, "New Friend: ${dc.document.data}")
                    DocumentChange.Type.MODIFIED -> Log.d(
                        ContentValues.TAG,
                        "Modified Friend: ${dc.document.data}"
                    )
                    DocumentChange.Type.REMOVED -> Log.d(
                        ContentValues.TAG,
                        "Removed Friend: ${dc.document.data}"
                    )
                }
            }
            getFriendsFromDb()
        }





    }

    private fun getFriendsFromDb() {
        friendAdapter.clearList()
        db.collection(Database.USERS).document(Database.getUserId()!!).collection(Database.FRIENDS)
            .get().addOnSuccessListener { friends ->

                if (friends != null) {
                    for (friend in friends) {
                        val user = User()
                        user.email = friend[Database.EMAIL].toString()
                        user.id = friend.id
                        friendAdapter.addUser(user)
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