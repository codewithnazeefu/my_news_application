package com.codewithnazeefo.mynewsapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Registry.NoImageHeaderParserException
import com.codewithnazeefo.mynewsapp.MainActivity
import com.codewithnazeefo.mynewsapp.Model.Article
import com.codewithnazeefo.mynewsapp.R
import com.codewithnazeefo.mynewsapp.databinding.FragmentArticleBinding
import com.codewithnazeefo.mynewsapp.util.Resource
import com.google.android.material.snackbar.Snackbar
import retrofit2.http.Url

class ArticleFragment : Fragment(R.layout.fragment_article) {

 lateinit var viewModel: NewsViewModel
    val safeArgs : ArticleFragmentArgs by navArgs()


    val binding :FragmentArticleBinding by lazy {
            FragmentArticleBinding.inflate(layoutInflater)
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
        val article = safeArgs.name
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        binding.fab.setOnClickListener {
            viewModel.savedArticle(article)
            Snackbar.make(view , "Article saved successfully" , Snackbar.LENGTH_LONG).show()

    }

        }
    }
}