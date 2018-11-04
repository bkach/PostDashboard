package com.example.boris.postdashboard.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boris.postdashboard.R
import com.example.boris.postdashboard.model.Post

class PostListAdapter : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

    var posts: List<Post> = listOf()

    var onClick: ((Post)-> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_post_recyclerview_item, parent, false)
        )

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (posts.isNotEmpty()) {
            holder.post = posts[position]
            holder.postTitleTextView.text = holder.post.title
            holder.clickListener {
                onClick?.invoke(posts[position])
            }
        }
    }

    fun updatePosts(posts: List<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        lateinit var post: Post
        val postTitleTextView: TextView = itemView.findViewById(R.id.recyclerview_item_textview)

        fun clickListener(callback: () -> Unit) {
            itemView.setOnClickListener { callback() }
        }
    }
}
