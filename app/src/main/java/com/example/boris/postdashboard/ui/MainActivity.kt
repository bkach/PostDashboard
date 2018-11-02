package com.example.boris.postdashboard.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.boris.postdashboard.R
import com.example.boris.postdashboard.viewmodel.Intent
import com.example.boris.postdashboard.viewmodel.State
import com.example.boris.postdashboard.viewmodel.ViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: ViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.state.observe(this, Observer { state ->
            when(state) {
                State.PostsLoadingError -> main_text_view.text = "Error"
                is State.PostsLoaded -> main_text_view.text = state.posts.size.toString()
            }
        })
    }
}
