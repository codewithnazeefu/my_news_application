package com.codewithnazeefo.mynewsapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codewithnazeefo.mynewsapp.Adapter.ArticleAdapter
import com.codewithnazeefo.mynewsapp.MainActivity
import com.codewithnazeefo.mynewsapp.R

import com.codewithnazeefo.mynewsapp.databinding.FragmentSearchNewsBinding
import com.codewithnazeefo.mynewsapp.util.Constant
import com.codewithnazeefo.mynewsapp.util.Constant.Companion.SEARCH_NEWS_TIME_DELAY
import com.codewithnazeefo.mynewsapp.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalStateException


class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter : ArticleAdapter
    private lateinit var bundle : Bundle
    var isLoading = false
    var islastPage = false
    var isScrolling = false
    private var TAG  = "SearchNewsFragment"
    private val binding : FragmentSearchNewsBinding by lazy {
        FragmentSearchNewsBinding.inflate(layoutInflater)
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
                R.id.action_searchNewsFragment_to_articleFragment, bundle
            )



        }
            var job: Job? = null
            binding.etSearch.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_NEWS_TIME_DELAY)
                    editable?.let {
                        if (editable.toString().isNotEmpty()) {
                            viewModel.searchNews(editable.toString())
                        }
                    }
                }
            }




              viewModel.searchNews.observe(viewLifecycleOwner,) { response ->
                  when (response){
                      is Resource.Success ->{
                          hideProgressBar()
                          response.data?.let {newsResponse ->

                              newsAdapter.differ.submitList(newsResponse.articles.toList())
                              val totalPage = newsResponse.totalResults / Constant.QUERY_PAGE_SIZE + 2
                              islastPage = viewModel.searchNewsPage == totalPage
                              if (islastPage){
                                  binding.rvSearchNews.setPadding(0,0,0,0)
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

                      }
                  }
              }


        }
    private fun hideProgressBar(){
        val progressBar = view?.findViewById<ProgressBar>(R.id.paginationProgressBar)
        progressBar?.visibility = View.GONE

    }
    private fun showProgressBar(){
        val progressBar = view?.findViewById<ProgressBar>(R.id.paginationProgressBar)
        progressBar?.visibility = View.VISIBLE

    }
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
            val isTotalMoreThanVisible = totalitemCount >= Constant.QUERY_PAGE_SIZE + 2
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBegining
                    && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate){

                isScrolling = false

            }
            else {
                binding.rvSearchNews.setPadding(0, 0, 0, 0)
            }
        }
    }


    private fun initRecyclerView(){
        newsAdapter = ArticleAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


    }