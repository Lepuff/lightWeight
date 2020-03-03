package com.example.lightweight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lightweight.classes.AbstractWorkout
import kotlinx.android.synthetic.main.layout_wo_list_item.view.*


class WorkOutAdapter : RecyclerView.Adapter<WorkOutAdapter.WorkOutViewHolder>() {

    private var workouts: MutableList<AbstractWorkout> = ArrayList()
    override fun getItemCount(): Int {
        return workouts.size
    }

    fun submitList(workOutList: MutableList<AbstractWorkout>) {
        workouts = workOutList
    }

    fun addWorkout(workOut: AbstractWorkout){
        workouts.add(workOut)
        this.notifyItemInserted(workouts.size-1)
    }
    fun removeWorkout(position: Int){
        workouts.removeAt(position)
        this.notifyDataSetChanged()
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


    class WorkOutViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private var selectedWorkout : AbstractWorkout? = null

        init {
            itemView.setOnClickListener(this)
        }



        val workoutImage = itemView.workout_image
        val workoutIcon = itemView.workout_icon
        val workoutTitle = itemView.workout_title
        val workoutDate = itemView.workout_date

        fun bind(workOut: AbstractWorkout) {
            this.selectedWorkout = workOut
            workoutTitle.text = workOut.title
            workoutDate.text = workOut.date
            workoutIcon.setImageResource(workOut.icon)

            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error_layer)
                .fallback(R.drawable.ic_fallback_foreground)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)
                .load(workOut.image)
                .into(workoutImage)
        }

        override fun onClick(v: View?) {
            selectedWorkout?.showWorkout(itemView.context)
        }
    }


    override fun onBindViewHolder(holder: WorkOutViewHolder, position: Int) {

        val workout = workouts[position]
        holder.bind(workout)
    }
}
