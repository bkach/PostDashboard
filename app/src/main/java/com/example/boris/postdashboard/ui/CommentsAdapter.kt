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
import com.example.boris.postdashboard.model.Comment

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    private var commentList: List<Comment>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.detail_comment_recyclerview_item, parent, false))

    override fun getItemCount(): Int = commentList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.commentBodyTextView.text = commentList?.get(position)?.body ?: ""
        holder.commentEmailTextView.text = commentList?.get(position)?.email ?: ""
    }

    fun setComments(commentList: List<Comment>?) {
        this.commentList = commentList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentBodyTextView: TextView = itemView.findViewById(R.id.detail_comment_recyclerview_body_text)
        val commentEmailTextView: TextView = itemView.findViewById(R.id.detail_comment_recyclerview_email_text)
    }
}