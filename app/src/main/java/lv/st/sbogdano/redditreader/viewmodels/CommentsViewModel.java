package lv.st.sbogdano.redditreader.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import net.dean.jraw.models.Submission;

import lv.st.sbogdano.redditreader.data.RedditDataRepository;
import lv.st.sbogdano.redditreader.data.model.PostComment;
import lv.st.sbogdano.redditreader.data.model.Post;

public class CommentsViewModel extends ViewModel {

    private final RedditDataRepository mRedditDataRepository;

    public CommentsViewModel(RedditDataRepository redditDataRepository) {
        mRedditDataRepository = redditDataRepository;
    }

    public LiveData<PagedList<PostComment>> getComments(Submission submission) {
        return mRedditDataRepository.getCommentResult(submission);
    }
}
