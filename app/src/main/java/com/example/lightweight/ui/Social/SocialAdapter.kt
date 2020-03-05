package com.example.lightweight.ui.Social

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lightweight.classes.AbstractWorkout
import com.example.lightweight.R
import kotlinx.android.synthetic.main.layout_wo_list_item.view.*

class SocialAdapter : RecyclerView.Adapter<SocialAdapter.SocialViewHolder>() {
    private var items: List<AbstractWorkout> = mutableListOf()

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(socialList: List<AbstractWorkout>){
        items = socialList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocialViewHolder {
        return SocialViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_wo_list_item,
                parent,
                false
            )
        )
    }


    class SocialViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var selectedworkout : AbstractWorkout? = null
        init {
            itemView.setOnClickListener(this)
        }

        val socialImage = itemView.workout_image
        val socialIcon = itemView.workout_icon
        val socialTitle = itemView.workout_title
        val socialDate = itemView.workout_date


        fun bind(socialWorkOut: AbstractWorkout){
            this.selectedworkout = socialWorkOut
            socialTitle.text = socialWorkOut.title
            socialDate.text = socialWorkOut.date.toString()
            socialIcon.setImageResource(socialWorkOut.icon)

            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error_layer)
                .fallback(R.drawable.ic_fallback_foreground)

            Glide.with(itemView.context)

                .applyDefaultRequestOptions(requestOption)
                .load("") //todo Load image from fb
                .into(socialImage)

        }

        override fun onClick(v: View?) {
            selectedworkout?.showWorkout(itemView.context)
        }
    }


    override fun onBindViewHolder(holder: SocialViewHolder, position: Int) {
        val socialWorkout = items[position]
        holder.bind(socialWorkout)
    }
}
