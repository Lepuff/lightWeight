package com.example.lightweight.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.adapters.UserAdapter
import com.example.lightweight.classes.User
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_friends.*

class AddFriendsActivity : AppCompatActivity() {
    val itemPaddingTop = 5
    private lateinit var userAdapter: UserAdapter
    private var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        initRecyclerView()
        findPossibleFriends()
    }

    private fun findPossibleFriends() {
        db.collection(Database.USERS).get().addOnSuccessListener { allUsers ->
            if (allUsers != null) {
                db.collection(Database.USERS).document(Database.getUserId()!!)
                    .collection(Database.FRIENDS).get().addOnSuccessListener { friends ->
                        if (friends != null) {
                            for (user in allUsers) {
                                var isPossibleFriend = true
                                if (user[Database.ID].toString() == Database.getUserId()) {
                                    isPossibleFriend = false
                                }
                                for (friend in friends) {
                                    if (user[Database.ID].toString() == friend[Database.ID].toString()) {
                                        isPossibleFriend = false
                                    }
                                }
                                if (isPossibleFriend) {
                                    val _user = User()
                                    _user.email = user[Database.EMAIL].toString()
                                    _user.id = user[Database.ID].toString()
                                    userAdapter.addUser(_user)
                                }
                            }

                        }
                    }
            }
        }
    }

    private fun initRecyclerView() {
        add_friend_recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration =
                TopSpacingItemDecoration(itemPaddingTop)
            addItemDecoration(topSpacingItemDecoration)
            userAdapter = UserAdapter(this, db, true)
            adapter = userAdapter
        }
    }
}
