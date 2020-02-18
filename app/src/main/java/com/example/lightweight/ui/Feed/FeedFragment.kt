package com.example.lightweight.ui.Feed

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
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var workOutAdapter: WorkOutAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedViewModel =
            ViewModelProviders.of(this).get(FeedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_feed, container, false)

        feedViewModel.text.observe(this, Observer {

        })

        return root
    }

    private fun addDataSet() {
        val data = DataSource.createDataSet()
        workOutAdapter.submitList(data)
    }

    private fun initRecyclerView() {
        feed_recycler_view.apply() {
            layoutManager = LinearLayoutManager(this.context)
            workOutAdapter = WorkOutAdapter()
            adapter = workOutAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        addDataSet()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}