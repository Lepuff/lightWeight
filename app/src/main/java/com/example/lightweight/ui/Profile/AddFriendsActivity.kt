package com.example.lightweight.ui.Profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.adapters.FriendAdapter
import com.example.lightweight.adapters.WorkOutAdapter
import com.example.lightweight.classes.User
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_friends.*
import kotlinx.android.synthetic.main.fragment_feed.*

class AddFriendsActivity : AppCompatActivity() {


    private lateinit var friendAdapter: FriendAdapter
    private var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        initRecyclerView()
        getUsers()


    }
    private fun getUsers(){
        db.collection(Database.USERS).get().addOnSuccessListener { users ->

            if (users != null) {
                for (user in users) {
                    val _user = User()
                    _user.email = user[Database.EMAIL].toString()
                    _user.id = user[Database.ID].toString()
                    friendAdapter.addItem(_user)
                }
            }
        }
    }



    private fun initRecyclerView() {
        add_friend_recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(5)
            addItemDecoration(topSpacingItemDecoration)
            friendAdapter = FriendAdapter(this)
            adapter = friendAdapter
        }
    }
}
