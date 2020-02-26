package com.dharam.githubissues.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dharam.githubissues.constants.Constants.ERROR_MESSAGE
import com.dharam.githubissues.repository.CommentsRepository
import com.dharam.githubissues.viewmodel.uiData.CommentList
import io.reactivex.Observable

class CommentsViewModel(application: Application):AndroidViewModel(Application()) {

    /*
    call comment list data from comment list repository  and  @return observable with comment list
    */
    fun getComments(number:String): Observable<CommentList> =
            CommentsRepository().getComments(number)
                    .map {
                        CommentList(it, "")
                    }
                    .onErrorReturn {
                        CommentList(emptyList(), ERROR_MESSAGE, it)
                    }
}
