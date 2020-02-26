package com.dharam.githubissues.repository
import com.dharam.githubissues.App
import com.dharam.githubissues.repository.model.Comments
import io.reactivex.Observable

class CommentsRepository() {

    /*
       get comment data from comment list api or from cache by using issue number
    */
    fun getComments(number: String): Observable<List<Comments>> = App.issuesApi.getComments(number)

}
