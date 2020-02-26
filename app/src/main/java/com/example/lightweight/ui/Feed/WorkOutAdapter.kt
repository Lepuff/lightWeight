package com.example.lightweight.ui.Feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lightweight.Exercise
import com.example.lightweight.R
import kotlinx.android.synthetic.main.layout_wo_list_item.view.*


class WorkOutAdapter : RecyclerView.Adapter<WorkOutAdapter.WorkOutViewHolder>() {

    private var items: List<Exercise> = ArrayList()
    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(workOutList: List<Exercise>) {
        items = workOutList
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
        private var selectedworkout : Exercise? = null

        init {
            itemView.setOnClickListener(this)
        }



        val workoutImage = itemView.workout_image
        val workoutIcon = itemView.workout_icon
        val workoutTitle = itemView.workout_title
        val workoutDate = itemView.workout_date

        fun bind(workOut: Exercise) {
            this.selectedworkout = workOut
            workoutTitle.text = workOut.title
            workoutDate.text = workOut.date.toString()
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
            selectedworkout?.showWorkout(itemView.context)
        }


    }


    override fun onBindViewHolder(holder: WorkOutViewHolder, position: Int) {

        val workout = items[position]
        holder.bind(workout)
    }
}
