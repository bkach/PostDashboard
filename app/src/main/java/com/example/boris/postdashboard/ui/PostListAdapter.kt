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
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boris.postdashboard.R
import com.example.boris.postdashboard.model.PostWithMetadata
import com.squareup.picasso.Picasso

class PostListAdapter : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

    var posts: List<PostWithMetadata> = listOf()

    var onClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_post_recyclerview_item, parent, false)
        )

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (posts.isNotEmpty()) {
            holder.data = posts[position]
            holder.postTitleTextView.text = "${holder.data.post?.title}"
            holder.postAuthorTextView.text = holder.itemView.context.getString(R.string.authorString,
                holder.data.userList?.get(0)?.name)

            Picasso.get()
                .load(holder.data.photoList?.get(0)?.thumbnailUrl)
                .into(holder.postImageView)

            holder.clickListener {
                onClick?.invoke(holder.data.post!!.id - 1)
            }
        }
    }

    fun updatePosts(posts: List<PostWithMetadata>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        lateinit var data: PostWithMetadata
        val postTitleTextView: TextView = itemView.findViewById(R.id.recyclerview_item_textview_title)
        val postAuthorTextView: TextView = itemView.findViewById(R.id.recyclerview_item_textview_user)
        val postImageView: ImageView = itemView.findViewById(R.id.recyclerview_item_imageview)

        fun clickListener(callback: () -> Unit) {
            itemView.setOnClickListener { callback() }
        }
    }
}
