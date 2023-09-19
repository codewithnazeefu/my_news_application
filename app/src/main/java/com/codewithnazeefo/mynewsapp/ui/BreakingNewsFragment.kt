package com.codewithnazeefo.mynewsapp.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment

import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AbsListView.RecyclerListener


import android.widget.ProgressBar
import android.widget.Toast

import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


import com.codewithnazeefo.mynewsapp.Adapter.ArticleAdapter
import com.codewithnazeefo.mynewsapp.MainActivity
import com.codewithnazeefo.mynewsapp.MainViewModel.NewsViewModel

import com.codewithnazeefo.mynewsapp.R
import com.codewithnazeefo.mynewsapp.databinding.FragmentBreakingNewsBinding
import com.codewithnazeefo.mynewsapp.databinding.FragmentSearchNewsBinding
import com.codewithnazeefo.mynewsapp.util.Constant
import com.codewithnazeefo.mynewsapp.util.Constant.Companion.QUERY_PAGE_SIZE

import com.codewithnazeefo.mynewsapp.util.Resource
import com.google.android.material.snackbar.Snackbar


class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter : ArticleAdapter
    private lateinit var bundle : Bundle
    private var TAG  = "BreakingNewsFragment"
    private val binding : FragmentBreakingNewsBinding by lazy {
        FragmentBreakingNewsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        initRecyclerView()
        binding.refreshLayout.setOnRefreshListener {
            viewModel.breakingNews.observe(viewLifecycleOwner){
                it.data?.let { res ->
                    newsAdapter.differ.submitList(res.articles)
                }
            }
        }

        newsAdapter.setOnItemClickListener {
            bundle = Bundle().apply {
                putSerializable("name", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment, bundle
            )
        }
           viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
             when (response){
                is Resource.Success ->{
                    hideProgressBar()
                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)

                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    binding.refreshLayout.isRefreshing = false
                    response.message?.let {message ->
                        Log.e(TAG ,"An error occured : $message")
                    }
                }
                is Resource.Loading ->{
                    showProgressBar()
                    binding.refreshLayout.isRefreshing = false
                }
            }
        }
    }
    private fun hideProgressBar(){
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility = View.GONE

    }

    private fun showProgressBar(){
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility = View.VISIBLE


    }



    private fun initRecyclerView(){
          newsAdapter = ArticleAdapter()
          binding.rvBreakingNews.apply {
              adapter = newsAdapter
              layoutManager = LinearLayoutManager(activity)

          }
    }
}