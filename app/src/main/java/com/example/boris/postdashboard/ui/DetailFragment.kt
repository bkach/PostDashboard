package com.example.boris.postdashboard.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.boris.postdashboard.R
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.viewmodel.DashboardViewModel
import com.example.boris.postdashboard.viewmodel.State
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DetailFragment : Fragment() {

    private val dashboardViewModel: DashboardViewModel by sharedViewModel()
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dashboardViewModel.state.observe(this, Observer { state -> when (state) {
            State.Error -> {
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
        } })
    }

    private fun setFields(post: Post) {
        detail_name_text_view.text = post.userName
        detail_body_text_view.text = post.body
        detail_title_text_view.text = post.title
        detail_num_comments_text_view.text = resources.getQuantityString(R.plurals.comments, post.numComments!!, post.numComments!!)
    }

    private fun showError() {
        snackbar = Snackbar.make(detail_view_swipe_refresh_layout, resources.getString(R.string.error_string), LENGTH_INDEFINITE)
        snackbar?.show()
    }

    private fun hideError() {
        snackbar?.dismiss()
    }

    private fun setLoadingSpinner(isLoading: Boolean) {
        detail_view_swipe_refresh_layout.isRefreshing = isLoading
    }
}
