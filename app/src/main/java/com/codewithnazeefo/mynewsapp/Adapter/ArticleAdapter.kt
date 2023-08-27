package com.codewithnazeefo.mynewsapp.Adapter


import android.view.LayoutInflater

import android.view.ViewGroup


import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment


import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codewithnazeefo.mynewsapp.Model.Article
import com.codewithnazeefo.mynewsapp.R
import com.codewithnazeefo.mynewsapp.databinding.BreakingNewsItemDesignBinding
import com.codewithnazeefo.mynewsapp.ui.BreakingNewsFragment


class ArticleAdapter() :RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    private var onItemClickListener: ((Article) -> Unit)? = null


    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
     class ArticleViewHolder(binding : BreakingNewsItemDesignBinding ):RecyclerView.ViewHolder(binding.root)


    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
             return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return  oldItem ==newItem
        }

    }

    val differ = AsyncListDiffer(this , differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        var binding = BreakingNewsItemDesignBinding.inflate(LayoutInflater.from(parent.context)
        ,parent,false)
     return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]



        holder.itemView.apply {
            val ivArticleImage: ImageView = findViewById(R.id.ivArticleImage)
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
          val tvSource = findViewById<TextView>(R.id.tvSource)
          tvSource.text = article.source.name
            val tvTitle: TextView = findViewById(R.id.tvTitle)
            val tvDescription: TextView = findViewById(R.id.tvDescription)
//            val tvAuthor: TextView = findViewById(R.id.)
            val tvPublishedAt: TextView = findViewById(R.id.tvPublishedAt)
            tvTitle.text = article.title
//            tvAuthor.text = article.author
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt

        }

            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(article) }
            }


    }


}