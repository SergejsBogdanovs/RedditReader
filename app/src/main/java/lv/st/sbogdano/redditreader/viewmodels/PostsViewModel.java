package lv.st.sbogdano.redditreader.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;

import net.dean.jraw.models.Submission;

import lv.st.sbogdano.redditreader.data.RedditDataRepository;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;

public class PostsViewModel extends ViewModel{

    private final RedditDataRepository mRedditDataRepository;

    public PostsViewModel(RedditDataRepository redditDataRepository) {
        mRedditDataRepository = redditDataRepository;
    }

    public LiveData<PagedList<Submission>>getPosts(SubredditEntry subredditEntry) {
        return mRedditDataRepository.getPostsResult(subredditEntry);
    }

}
