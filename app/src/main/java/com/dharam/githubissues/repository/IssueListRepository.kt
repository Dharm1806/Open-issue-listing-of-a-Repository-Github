package com.dharam.githubissues.repository

import com.dharam.githubissues.App
import com.dharam.githubissues.repository.model.IssuesModel
import io.reactivex.Observable

class IssueListRepository {

    /*
   get issue list data from issue list api or from cache
*/
    fun getIssues(): Observable<List<IssuesModel>> = App.issuesApi.getIssuesLis()

}
