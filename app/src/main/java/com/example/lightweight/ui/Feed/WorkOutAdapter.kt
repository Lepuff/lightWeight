package com.example.lightweight.ui.Feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lightweight.R
import kotlinx.android.synthetic.main.layout_workout_list_item.view.*

class WorkOutAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<WorkOut> = ArrayList()

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(workOutList: List<WorkOut>) {
        items = workOutList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WorkOutViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_workout_list_item,
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
        val workOut_image = itemView.blog_image
        val workOut_title = itemView.blog_title
        val workOut_userName = itemView.blog_author

        fun bind(workOut: WorkOut) {


            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_error_layer)
                .fallback(R.drawable.ic_fallback_foreground)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)
                .load("https://picsum.photos/200/300")
                .into(workOut_image)

            workOut_title.text = workOut.title
            workOut_userName.text = workOut.userName
        }
    }


}