package com.example.lightweight.ui.Social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lightweight.DataSource
import com.example.lightweight.R
import com.example.lightweight.ui.Feed.TopSpacingItemDecoration
import com.example.lightweight.ui.Feed.WorkOutAdapter
import kotlinx.android.synthetic.main.fragment_feed.*

class SocialFragment : Fragment() {

    private lateinit var socialViewModel: SocialViewModel
    private lateinit var socialAdapter: SocialAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        socialViewModel =
            ViewModelProviders.of(this).get(SocialViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_social, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        socialViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }

    private fun addDataSet(){
        val data = DataSource.createDataSet()
        socialAdapter.submitList(data)
    }


    private fun initRecyclerView() {
        feed_recycler_view.apply {
            layoutManager = LinearLayoutManager(this.context)
            val topSpacingItemDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingItemDecoration)
            socialAdapter = SocialAdapter()
            adapter = socialAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}