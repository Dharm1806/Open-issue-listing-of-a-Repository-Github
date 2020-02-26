package com.dharam.githubissues.viewmodel.uiData

import com.dharam.githubissues.repository.model.Comments
import com.dharam.githubissues.repository.model.IssuesModel


data class IssueList(val issues: List<IssuesModel>, val message: String, val error: Throwable? = null)

data class CommentList(val commentList: List<Comments>, val message: String, val error:Throwable?= null)