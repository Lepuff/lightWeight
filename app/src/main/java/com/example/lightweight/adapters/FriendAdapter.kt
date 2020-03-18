package com.example.lightweight.adapters


import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.classes.AbstractWorkout
import com.example.lightweight.classes.User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_add_friends.view.*
import kotlinx.android.synthetic.main.layout_dialog_new_workout_list_item.view.*
import kotlinx.android.synthetic.main.layout_friend_item.view.*
import kotlin.math.log

class FriendAdapter(private val recyclerView: RecyclerView, private val db : FirebaseFirestore) :
    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {


    private var users: MutableList<User> = ArrayList()

    override fun getItemCount(): Int {
        return users.size
    }

    fun submitList(friendList: MutableList<User>) {
        users = friendList
    }

    fun removeitem(position: Int) {
        users.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItem(user: User) {
        users.add(user)
        notifyItemInserted(users.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_friend_item,
                parent,
                false

            ), recyclerView,db
        )
    }


    class FriendViewHolder constructor(itemView: View, recyclerView: RecyclerView, db: FirebaseFirestore) :
        RecyclerView.ViewHolder(itemView) {
        private var selectedUser: User? = null

        private var adapter = recyclerView.adapter as FriendAdapter
        var name: TextView = itemView.friend_item_text
        val addFriendButton  = itemView.findViewById<Button>(R.id.friend_add_button).apply {
            this.setOnClickListener {
                Log.d("addFriendButton", "clicked")


                val userInfo = hashMapOf(
                    "email" to selectedUser?.email,
                    "id" to selectedUser?.id
                )
                db.collection(Database.USERS).document(Database.getUserId()!!).collection(Database.FRIENDS).document().set(userInfo)


                val positon = adapterPosition
                adapter.removeitem(positon)
            }
        }



        fun bind(user: User) {

            this.selectedUser = user
            name.text = user.email.toString()
        }

    }


    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }
}