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

import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


import com.codewithnazeefo.mynewsapp.Adapter.ArticleAdapter
import com.codewithnazeefo.mynewsapp.MainActivity

import com.codewithnazeefo.mynewsapp.R
import com.codewithnazeefo.mynewsapp.databinding.FragmentBreakingNewsBinding
import com.codewithnazeefo.mynewsapp.databinding.FragmentSearchNewsBinding
import com.codewithnazeefo.mynewsapp.util.Constant
import com.codewithnazeefo.mynewsapp.util.Constant.Companion.QUERY_PAGE_SIZE

import com.codewithnazeefo.mynewsapp.util.Resource


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


        newsAdapter.setOnItemClickListener {
            bundle = Bundle().apply {
                putSerializable("name", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment, bundle
            )
        }




        viewModel.breakingNews.observe(viewLifecycleOwner,) { response ->
            when (response){
                is Resource.Success ->{
                    hideProgressBar()
                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                        val totalPage = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        islastPage = viewModel.breakingNewsPage == totalPage
                        if (islastPage){
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }

                        }

                }
                is Resource.Error ->{
                    hideProgressBar()
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
        isLoading = false
    }

    private fun showProgressBar(){
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBar)
        progressBar?.visibility = View.VISIBLE
        isLoading = true

    }

     var isLoading = false
     var islastPage = false
     var isScrolling = false


     val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

       override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalitemCount = layoutManager.itemCount


            val isNotLoadingAndNotLastPage =  !isLoading && !islastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalitemCount
            val isNotAtBegining = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalitemCount >= QUERY_PAGE_SIZE + 2
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBegining
                    && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate){

                viewModel.getBreakingNews("us")
                isScrolling = false

            }
            else {
                binding.rvBreakingNews.setPadding(0, 0, 0, 0)
            }
        }
    }
    private fun initRecyclerView(){
        newsAdapter = ArticleAdapter()
          binding.rvBreakingNews.apply {
              adapter = newsAdapter
              layoutManager = LinearLayoutManager(activity)
              addOnScrollListener(this@BreakingNewsFragment.scrollListener)
          }
    }
}