package com.example.lightweight.ui.Profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.adapters.UserAdapter
import com.example.lightweight.classes.User
import com.example.lightweight.ui.TopSpacingItemDecoration
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_friends.*

class AddFriendsActivity : AppCompatActivity() {


    private lateinit var userAdapter: UserAdapter
    private var db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        initRecyclerView()
        getUsers()
    }

    private fun getUsers() {
        db.collection(Database.USERS).get().addOnSuccessListener { allUsers ->
            if (allUsers != null) {
                db.collection(Database.USERS).document(Database.getUserId()!!)
                    .collection(Database.FRIENDS).get().addOnSuccessListener { friends ->
                        if (friends != null) {
                            for (user in allUsers) {
                                var isNotFriend: Boolean = true
                                for (friend in friends) {
                                    Log.d("friend:", friend[Database.ID].toString())
                                    Log.d("user:", user[Database.ID].toString())
                                    if (user[Database.ID].toString() ==friend[Database.ID].toString())   {
                                        isNotFriend = false
                                    }
                                    else if (user[Database.ID].toString() == Database.getUserId()){
                                        isNotFriend = false
                                    }
                                }
                                if (isNotFriend) { //todo borde finnas en bättre lösning...
                                    Log.d("friend added!!:", user[Database.ID].toString())
                                    val _user = User()
                                    _user.email = user[Database.EMAIL].toString()
                                    _user.id = user[Database.ID].toString()
                                    userAdapter.addItem(_user)
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
                TopSpacingItemDecoration(5)//todo fix
            addItemDecoration(topSpacingItemDecoration)
            userAdapter = UserAdapter(this, db,true)
            adapter = userAdapter
        }
    }
}
