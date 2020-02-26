package com.dharam.githubissues.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dharam.githubissues.R
import com.dharam.githubissues.constants.Constants
import com.dharam.githubissues.constants.Constants.EXTRA_NUMBER
import com.dharam.githubissues.viewmodel.CommentsViewModel
import com.dharam.githubissues.viewmodel.uiData.CommentList
import com.dharam.githubissues.views.adapters.CommentsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_comments.*
import java.net.ConnectException
import java.net.UnknownHostException


class CommentsActivity : AppCompatActivity() {

    private lateinit var mCommentViewModel: CommentsViewModel
    val subscriptions = CompositeDisposable()

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        //initialize view model
        mCommentViewModel = ViewModelProviders.of(this)
                .get(CommentsViewModel::class.java)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = Constants.COMMENTS_TITLE
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //get the comment list
        getComments()

        //retry to get comment list on click over the error message
        error_message.setOnClickListener { getComments() }
    }

    //onstop function of activity lifecycle
    @Override
    override fun onStop() {
        super.onStop()
        subscriptions.clear()
    }

    @Override
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    //get the comment list data from repository

    private fun getComments() {

        subscribe(mCommentViewModel.getComments(intent.getStringExtra(EXTRA_NUMBER))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    hideProgressbar()
                    showComments(it)
                }, {
                    hideProgressbar()
                    showError(it.message.toString())
                }))
    }


    private fun showComments(commentData: CommentList) = if (commentData.error == null) {
        //sort the comment list
        val commentList = commentData.commentList.sortedWith(compareBy { it.updated_at })
        //set the the adapter to comment list
        commentsList.adapter = CommentsAdapter(commentList)
        //set the layout manager to comment list view
        commentsList.layoutManager = LinearLayoutManager(this)
        //hide the error view
        hideErrorView()

    } else if (commentData.error is ConnectException || commentData.error is UnknownHostException) {
        //show the connection error
        showError(commentData.error.localizedMessage)

    } else {
        //show the errror received from server

        showError(commentData.message.toString())
    }

    private fun showError(error: String) {
        if (error.length != 0)
            error_message.text = error
        error_message.visibility = View.VISIBLE
    }

    private fun subscribe(disposable: Disposable): Disposable {
        subscriptions.add(disposable)
        return disposable
    }

    //hide the error view
    private fun hideErrorView() {
        error_message.visibility = View.GONE
    }

    //hide the progressbar
    private fun hideProgressbar() {
        progressBar.visibility = View.GONE
    }
}
