package com.dharam.githubissues.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dharam.githubissues.repository.IssueListRepository
import com.dharam.githubissues.viewmodel.uiData.IssueList
import io.reactivex.Observable

class IssuesListViewModel(application: Application) : AndroidViewModel(Application()) {


    /*
    call issue list data from issue list repository  and @return observable with issuelist
*/
    fun getIssues(): Observable<IssueList> = IssueListRepository().getIssues()
            .map {
                IssueList(it, "")
            }
            .onErrorReturn {
                IssueList(emptyList(), it.localizedMessage, it)
            }
}
