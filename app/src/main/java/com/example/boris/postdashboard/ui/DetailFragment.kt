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

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boris.postdashboard.R
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.model.PostWithMetadata
import com.example.boris.postdashboard.viewmodel.DashboardViewModel
import com.example.boris.postdashboard.viewmodel.Intent
import com.example.boris.postdashboard.viewmodel.State
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail_initial.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * Fragment containing the Detail view of a given [Post]
 *
 * Observes the [State] emitted by the [DashboardViewModel].
 */
class DetailFragment : Fragment() {

    private val dashboardViewModel: DashboardViewModel by sharedViewModel()

    private val initialConstraint = ConstraintSet()
    private val commentsShownConstraint= ConstraintSet()
    private val commentsHiddenConstraint = ConstraintSet()
    var applyInitialConstraint = {
        commentsHiddenConstraint.applyTo(detail_constraintlayout)
    }

    private var snackbar: Snackbar? = null
    private val commentsAdapter = CommentsAdapter()
    private var commentsVisible = false



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_initial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        disableRefresh()
        setupRecyclerView()
        observeViewModelState()
        setupAnimations(view)
        setupCommentClickListener()
    }

    private fun setupRecyclerView() {
        comments_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentsAdapter
        }
    }

    private fun disableRefresh() {
        detail_view_swipe_refresh_layout.setOnRefreshListener {
            if (detail_view_swipe_refresh_layout.isRefreshing) {
                detail_view_swipe_refresh_layout.isRefreshing = false
            }
        }
    }

    private fun observeViewModelState() {
        dashboardViewModel.state.removeObservers(this)
        dashboardViewModel.state.observe(this,
            Observer { state ->
                when (state) {
                    is State.Error -> {
                        setLoadingSpinner(false)
                        showError()
                    }
                    is State.DetailsLoaded -> {
                        hideError()
                        setLoadingSpinner(false)
                        setFields(state.post)
                    }
                    State.DetailsLoading -> {
                        hideError()
                        setLoadingSpinner(true)
                    }
                    is State.ShowComments -> {
                        setFields(state.post)
                        showComments()
                    }
                    is State.HideComments -> {
                        setFields(state.post)
                        hideComments()
                    }
                }
            })
    }

    private fun setupAnimations(view: View) {
        initialConstraint.clone(detail_constraintlayout)
        commentsShownConstraint.clone(context, R.layout.fragment_detail_comments_shown)
        commentsHiddenConstraint.clone(context, R.layout.fragment_detail_default)

        view.post {
            animateIn()
        }
    }

    private fun animateIn() {
        TransitionManager.beginDelayedTransition(detail_constraintlayout)
        applyInitialConstraint()
    }

    private fun setupCommentClickListener() {
        val clickListener: View.OnClickListener = View.OnClickListener {
            dashboardViewModel.sendIntent(Intent.CommentTapped(commentsVisible))
        }

        detail_num_comments_text_view.setOnClickListener(clickListener)
        detail_comment_arrow.setOnClickListener(clickListener)
    }

    private fun setFields(data: PostWithMetadata) {
        detail_name_text_view.text = resources.getString(R.string.authorString, data.userList?.get(0)?.name)
        detail_body_text_view.text = data.post?.body
        detail_title_text_view.text = data.post?.title

        Picasso.get()
            .load(data.photoList?.get(0)?.url)
            .into(detail_imageview)

        commentsAdapter.setComments(data.commentList)

        val numComments = data.commentList?.size ?: 0
        detail_num_comments_text_view.text = resources.getQuantityString(R.plurals.comments, numComments, numComments)
    }

    private fun showComments() {
        TransitionManager.beginDelayedTransition(detail_constraintlayout)
        applyInitialConstraint = {
            commentsShownConstraint.applyTo(detail_constraintlayout)
        }
        applyInitialConstraint()
        commentsVisible = true
    }

    private fun hideComments() {
        TransitionManager.beginDelayedTransition(detail_constraintlayout)
        applyInitialConstraint = {
            commentsHiddenConstraint.applyTo(detail_constraintlayout)
        }
        applyInitialConstraint()
        commentsVisible = false
    }

    private fun showError() {
        snackbar = Snackbar.make(detail_view_swipe_refresh_layout,
            resources.getString(R.string.error_string), LENGTH_INDEFINITE)
        snackbar?.show()
    }

    private fun hideError() {
        snackbar?.dismiss()
    }

    private fun setLoadingSpinner(isLoading: Boolean) {
        detail_view_swipe_refresh_layout.isRefreshing = isLoading
    }

    fun onBackPressed() {
        dashboardViewModel.sendIntent(Intent.LeaveDetailIntent)
    }

}
