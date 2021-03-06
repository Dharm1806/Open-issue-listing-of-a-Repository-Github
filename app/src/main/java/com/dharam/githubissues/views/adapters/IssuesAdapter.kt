package com.dharam.githubissues.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dharam.githubissues.R
import com.dharam.githubissues.constants.Constants.getModifiedIssueBody
import com.dharam.githubissues.constants.Constants.parseDate
import com.dharam.githubissues.repository.model.IssuesModel
import kotlinx.android.synthetic.main.item_issue_list.view.*

class IssueHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(issue: IssuesModel, clickListener: OnItemClickListener) {

        //set item tile with first charactar capital
        itemView.item_issue_title.text = issue.title.capitalize()

        //set updated time of issue with refortmat date
        itemView.item_issue_updated_time.text = parseDate(issue.updated_at)

        var issueBody: String = ""

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            issueBody = (android.text.Html.fromHtml(issue.body,
                    android.text.Html.FROM_HTML_MODE_COMPACT)).toString()
        else issueBody = (android.text.Html.fromHtml(issue.body).toString())

        //set the issue body with modified body with maximum 140 chars and ...
        itemView.item_issue_body.text = getModifiedIssueBody(issueBody)

        //handle item click event
        itemView.setOnClickListener {
            clickListener.onItemClicked(issue)
        }
    }

}


class IssuesAdapter(private var issues: List<IssuesModel>,
                    private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<IssueHolder>() {

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): IssueHolder {
        //inflate the view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_issue_list, parent, false)
        return IssueHolder(view)
    }

    //return the item count of issue list
    @Override
    override fun getItemCount(): Int = issues.size

    //bind the viewholder
    @Override
    override fun onBindViewHolder(myHolder: IssueHolder, position: Int) =
            myHolder.bind(issues.get(position), itemClickListener)
}


interface OnItemClickListener {
    fun onItemClicked(issue: IssuesModel)
}