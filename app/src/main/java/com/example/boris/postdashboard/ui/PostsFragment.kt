package com.example.boris.postdashboard.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boris.postdashboard.R
import com.example.boris.postdashboard.model.Post
import com.example.boris.postdashboard.viewmodel.DashboardViewModel
import com.example.boris.postdashboard.viewmodel.Intent
import com.example.boris.postdashboard.viewmodel.State
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import kotlinx.android.synthetic.main.fragment_posts.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PostsFragment : Fragment() {

    private val dashboardViewModel: DashboardViewModel by sharedViewModel<DashboardViewModel>()
    private var postListAdapter: PostListAdapter? = null
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        disableManualRefresh()
        setupRecyclerView()
        subscribeToOnClick()
        observeState()

        dashboardViewModel.sendIntent(Intent.InitialIntent)
    }
    private fun disableManualRefresh() {
        post_list_swipe_refresh_layout.isEnabled = false
    }

    private fun setupRecyclerView() {
        if (postListAdapter == null) {
            postListAdapter = PostListAdapter()
        }

        post_list_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postListAdapter
        }
    }

    private fun subscribeToOnClick() {
        postListAdapter?.onClick = {
            dashboardViewModel.sendIntent(Intent.SelectPostIntent(it))
        }
    }

    private fun observeState() {
        dashboardViewModel.state.removeObservers(this)
        dashboardViewModel.state.observe(this, Observer { state ->
            when (state) {
                State.Error -> {
                    setLoadingSpinner(false)
                    showError()
                }
                is State.PostsLoaded -> {
                    setLoadingSpinner(false)
                    hideError()
                    updatePosts(state.posts)
                }
                State.DetailsLoading -> {
                    hideError()
                    navigateToDetail()
                }
                State.PostsLoading -> setLoadingSpinner(true)
            }
        })
    }

    private fun updatePosts(posts: List<Post>) {
        postListAdapter?.updatePosts(posts)
    }

    private fun showError() {
        snackbar = Snackbar.make(post_list_swipe_refresh_layout,
            resources.getString(R.string.error_string), LENGTH_INDEFINITE)
        snackbar?.show()
    }

    private fun hideError() {
        snackbar?.dismiss()
    }

    private fun navigateToDetail() {
        view?.findNavController()?.navigate(R.id.action_postsFragment_to_detailFragment)
    }

    private fun setLoadingSpinner(isLoading: Boolean) {
         post_list_swipe_refresh_layout.isRefreshing = isLoading
    }

}
