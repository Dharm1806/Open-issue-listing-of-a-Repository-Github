package com.dharam.githubissues.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dharam.githubissues.R
import com.dharam.githubissues.constants.Constants.EXTRA_NUMBER
import com.dharam.githubissues.constants.Constants.ISSUE_LIST_TITLE
import com.dharam.githubissues.repository.model.IssuesModel
import com.dharam.githubissues.viewmodel.IssuesListViewModel
import com.dharam.githubissues.viewmodel.uiData.IssueList
import com.dharam.githubissues.views.adapters.IssuesAdapter
import com.dharam.githubissues.views.adapters.OnItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_issue_list.*
import java.net.ConnectException
import java.net.UnknownHostException


class IssueListActivity : AppCompatActivity(), OnItemClickListener {

    lateinit var mProgressBar: ProgressBar
    private lateinit var mIssuesListViewModel: IssuesListViewModel
    val subscriptions = CompositeDisposable()

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue_list)
        //initialize view model
        mIssuesListViewModel = ViewModelProviders.of(this).get(IssuesListViewModel::class.java)



        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = ISSUE_LIST_TITLE
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        //get issues list
        getIssuesList()

        //try again
        error_message.setOnClickListener { getIssuesList() }
    }

    /*
    on stop function of activity lifecycle
    */
    @Override
    override fun onStop() {
        super.onStop()
        //clear composit disposable
        subscriptions.clear()
    }

    @Override
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /*
        handle item click listener of issue list
    */
    @Override
    override fun onItemClicked(issue: IssuesModel) {


        if (issue.comments == 0) {
            showNoCommentMessage()
        } else {
            val intent = Intent(this, CommentsActivity::class.java)
            intent.putExtra(EXTRA_NUMBER, issue.number.toString())
            startActivity(intent)
        }
    }


    private fun getIssuesList() {

        subscribe(mIssuesListViewModel.getIssues()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    hideProgressbar()
                    showIssuesList(it)
                }, {
                    hideProgressbar()
                    showError(it.localizedMessage)
                }))
    }

    private fun subscribe(disposable: Disposable): Disposable {

        subscriptions.add(disposable)
        return disposable
    }

    private fun showIssuesList(issuesData: IssueList) = if (issuesData.error == null) {
        //sort issue list
        val issueList = issuesData.issues.sortedWith(compareBy({ it.updated_at })).reversed()
        //set adapter to issuelist recyclerview
        issueListView.adapter = IssuesAdapter(issueList, this)
        //set the layoutmanager to issuelist recyclerview
        issueListView.layoutManager = LinearLayoutManager(this)
        //hide the errorView
        hideErrorView()
    } else if (issuesData.error is ConnectException || issuesData.error is UnknownHostException) {
        //show the connection error
        showError(issuesData.error.localizedMessage)
    } else {
        //show the error received from server api call
        showError(issuesData.message)
    }

    //handle the error view visibility
    private fun showError(error: String) {

        if (error.isNotEmpty())
            error_message.text = error
        error_message.visibility = View.VISIBLE
    }

    //show the no comment message
    private fun showNoCommentMessage() {

        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this)


        //set message for alert dialog
        builder.setMessage(R.string.mssg_no_comment_found)
        builder.setIcon(android.R.drawable.ic_dialog_alert)


        //performing positive action
        builder.setPositiveButton(getString(R.string.ok_button)) { _, _ ->
            alertDialog?.dismiss()
        }
        // Create the AlertDialog

        alertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

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
