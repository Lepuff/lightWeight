package com.example.lightweight.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.Database
import com.example.lightweight.R
import com.example.lightweight.classes.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.layout_friend_item.view.*

class UserAdapter(private val recyclerView: RecyclerView, private val db : FirebaseFirestore, private val isInAddMode: Boolean) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    private var users: MutableList<User> = ArrayList()

    override fun getItemCount(): Int {
        return users.size
    }

    fun submitList(friendList: MutableList<User>) {
        users = friendList
    }
    fun clearList(){
        users.clear()
        notifyDataSetChanged()
    }

    fun removeitem(position: Int) {
        users.removeAt(position)
        notifyItemRemoved(position)
    }
    fun addItem(user: User) {
        users.add(user)
        notifyItemInserted(users.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_friend_item,
                parent,
                false
            ), recyclerView,db
        )
    }


    class UserViewHolder constructor(itemView: View, recyclerView: RecyclerView, db: FirebaseFirestore) :
        RecyclerView.ViewHolder(itemView) {
        private var selectedUser: User? = null

        private var adapter = recyclerView.adapter as UserAdapter
        var name: TextView = itemView.friend_item_text

        val addFriendButton  = itemView.findViewById<Button>(R.id.friend_add_button).apply {
            this.setOnClickListener {
                val userInfo = hashMapOf(
                    Database.EMAIL to selectedUser?.email,
                    Database.ID to selectedUser?.id
                )
                db.collection(Database.USERS).document(Database.getUserId()!!).collection(Database.FRIENDS).document().set(userInfo)




                val positon = adapterPosition
                adapter.removeitem(positon)
            }
        }
        val removeFriendButton = itemView.findViewById<Button>(R.id.friend_remove_button).apply {
            setOnClickListener {


            }
        }



        fun bind(user: User) {

            this.selectedUser = user
            name.text = user.email.toString()
        }

    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        if (isInAddMode){
            holder.addFriendButton.visibility = View.VISIBLE
            holder.removeFriendButton.visibility = View.GONE
        }else{
            holder.addFriendButton.visibility = View.GONE
            holder.removeFriendButton.visibility = View.VISIBLE
        }


        val user = users[position]
        holder.bind(user)
    }
}