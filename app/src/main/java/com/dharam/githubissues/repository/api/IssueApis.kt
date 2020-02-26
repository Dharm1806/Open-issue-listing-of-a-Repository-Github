package com.dharam.githubissues.repository.api

import com.dharam.githubissues.constants.Constants.COMMENT_LIST_END_POINTS
import com.dharam.githubissues.constants.Constants.EXTRA_NUMBER
import com.dharam.githubissues.constants.Constants.ISSUELIST_END_POINTS
import com.dharam.githubissues.repository.model.Comments
import com.dharam.githubissues.repository.model.IssuesModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface IssueApis {

    /*  get the issueList*/
    @GET(ISSUELIST_END_POINTS)
    fun getIssuesLis(): Observable<List<IssuesModel>>

    /* get the comment list of issue with issue number */
    @GET(COMMENT_LIST_END_POINTS)
    fun getComments(@Path(EXTRA_NUMBER) number: String): Observable<List<Comments>>
}
