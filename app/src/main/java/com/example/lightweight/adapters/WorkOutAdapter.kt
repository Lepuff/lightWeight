package com.example.lightweight.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lightweight.R
import com.example.lightweight.classes.Workout
import com.google.android.material.textview.MaterialTextView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.layout_wo_list_item.view.*


class WorkOutAdapter : RecyclerView.Adapter<WorkOutAdapter.WorkOutViewHolder>() {

    private var workouts: MutableList<Workout> = ArrayList()
    override fun getItemCount(): Int {
        return workouts.size
    }

    fun submitList(workOutList: MutableList<Workout>) {
        workouts = workOutList
    }

    fun addWorkout(workOut: Workout) {
        workouts.add(workOut)
        this.notifyItemInserted(workouts.size - 1)
    }

    fun removeWorkout(position: Int) {
        workouts.removeAt(position)
        this.notifyDataSetChanged()
    }

    fun clearList() {
        workouts.clear()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkOutViewHolder {

        return WorkOutViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_wo_list_item,
                parent,
                false
            )
        )
    }


    class WorkOutViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private var selectedWorkout: Workout? = null

        init {
            itemView.setOnClickListener(this)
        }

        private val userName: MaterialTextView = itemView.workout_user_name
        private val userImage: CircleImageView = itemView.workout_image
        private val workoutIcon: AppCompatImageView = itemView.workout_icon_imageView
        private val workoutTitle: MaterialTextView = itemView.workout_title
        private val workoutDate: MaterialTextView = itemView.workout_date

        fun bind(workOut: Workout) {
            this.selectedWorkout = workOut

            workoutTitle.text = workOut.title
            workoutDate.text = workOut.date
            workoutIcon.setImageResource(workOut.icon)
            userName.text = workOut.userName

            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error_layer)
                .fallback(R.drawable.ic_fallback_foreground)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)

                .load(workOut.userImage)
                .into(userImage)
        }

        override fun onClick(v: View?) {
            selectedWorkout?.viewWorkout(itemView.context)
        }
    }


    override fun onBindViewHolder(holder: WorkOutViewHolder, position: Int) {

        val workout = workouts[position]
        holder.bind(workout)
    }
}
