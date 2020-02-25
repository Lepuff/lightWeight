package com.example.lightweight.ui.Feed

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lightweight.AbstractWorkout
import com.example.lightweight.R
import kotlinx.android.synthetic.main.layout_wo_list_item.view.*


class WorkOutAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<AbstractWorkout> = ArrayList()
    private lateinit var mListener : OnItemClickedListener



    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(workOutList: List<AbstractWorkout>) {
        items = workOutList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WorkOutViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_wo_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WorkOutViewHolder -> {
                holder.bind(items[position])
            }

        }
    }
    interface OnItemClickedListener{
        fun onItemClick(position:Int)
    }
    fun setOnItemClickListener(listener : OnItemClickedListener){
        mListener = listener
    }

    class WorkOutViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        init {
            itemView.setOnClickListener {
                Log.d("workoutClick","Clicked")
                val intent = Intent(itemView.context,GymWorkoutDetailsActivity::class.java)
                intent.putExtra("workoutName","test") //ToDo fix correct workoutname to toolbar
                intent.putExtra("id",111)
                itemView.context.startActivity(intent)
            }
        }

        val workoutImage = itemView.workout_image
        val workoutIcon = itemView.workout_icon
        val workoutTitle = itemView.workout_title
        val workoutDate = itemView.workout_date






        fun bind(workOut: AbstractWorkout) {

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
    }
}