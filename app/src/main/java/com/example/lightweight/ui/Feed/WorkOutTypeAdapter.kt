package com.example.lightweight.ui.Feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lightweight.R
import kotlinx.android.synthetic.main.layout_dialog_new_workout_list_item.view.*
import kotlinx.android.synthetic.main.layout_workout_list_item.view.*

class WorkOutTypeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<WorkOutType> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(workOutTypeList: List<WorkOutType>) {
        items = workOutTypeList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WorkOutViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_dialog_new_workout_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WorkOutViewHolder -> {
                holder.bind(items.get(position))
            }

        }
    }

    class WorkOutViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        val icon = itemView.new_workout_icon
        val title = itemView.new_workout_textView

        fun bind(workOutType: WorkOutType) {

            icon.setImageResource(workOutType.icon)
            title.text = workOutType.name


        }
    }


}