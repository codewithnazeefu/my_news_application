package com.codewithnazeefo.mynewsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codewithnazeefo.mynewsapp.Adapter.ArticleAdapter
import com.codewithnazeefo.mynewsapp.MainActivity
import com.codewithnazeefo.mynewsapp.MainViewModel.NewsViewModel
import com.codewithnazeefo.mynewsapp.R
import com.codewithnazeefo.mynewsapp.databinding.FragmentSavedNewsBinding
import com.google.android.material.snackbar.Snackbar


class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
      lateinit var viewModel: NewsViewModel
      lateinit var savedAdapter : ArticleAdapter
      val binding by lazy {
           FragmentSavedNewsBinding.inflate(layoutInflater)
       }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        initRecyclerView()
        savedAdapter.setOnItemClickListener {
            initRecyclerView()
            val bundle : Bundle =Bundle().apply {
                putSerializable("name" , it)

            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,bundle
            )

        }
           val  itemTouchHelperCalBack =
               object : ItemTouchHelper.SimpleCallback(
                   ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                   ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
               ){
                   override fun onMove(
                       recyclerView: RecyclerView,
                       viewHolder: RecyclerView.ViewHolder,
                       target: RecyclerView.ViewHolder
                   ): Boolean {
                        return true
                   }

                   override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                       val position = viewHolder.adapterPosition
                       var article = savedAdapter.differ.currentList[position]
                       viewModel.deleteArticle(article)

                       Snackbar.make(view , "Article Deleted" , Snackbar.LENGTH_LONG).apply {
                           setAction("Undo"){
                               viewModel.savedArticle(article)
                           }   .show()


                        }
                   }


               }
         ItemTouchHelper(itemTouchHelperCalBack).apply {
             attachToRecyclerView(binding.rvSavedNews)
         }
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { article ->
            savedAdapter.differ.submitList(article)
        })

    }
    private fun initRecyclerView() {
        savedAdapter = ArticleAdapter()
        binding.rvSavedNews.apply {
            adapter = savedAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }}