/*
 * PostDashboard
 * Copyright (C) 2018 Boris Kachscovsky
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
