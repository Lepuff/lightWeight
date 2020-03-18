package com.example.lightweight.adapters


import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.lightweight.R
import com.example.lightweight.classes.AbstractWorkout
import com.example.lightweight.classes.User
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.layout_dialog_new_workout_list_item.view.*
import kotlinx.android.synthetic.main.layout_friend_item.view.*

class FriendAdapter() : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    private var users: MutableList<User> = ArrayList()

    override fun getItemCount(): Int {
        return users.size
    }

    fun submitList(friendList: MutableList<User>){
        users = friendList
    }
    fun addItem(user:User){
        users.add(user)
        notifyItemInserted(users.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_friend_item,
                parent,
                false
            )
        )
    }



    class FriendViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){

        var name: TextView = itemView.friend_item_text
        val addButton: Button = itemView.friend_add_button.apply {
            setOnClickListener {

            }
        }
        fun bind(user: User) {
            name.text = user.email.toString()
        }
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }
}