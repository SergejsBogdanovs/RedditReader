package lv.st.sbogdano.redditreader.ui.settings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import lv.st.sbogdano.redditreader.data.PostsRepository;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;

public class SubredditViewModel extends ViewModel{

    private final PostsRepository mPostsRepository;
    private final LiveData<List<SubredditEntry>> mMySubreddits;

    public SubredditViewModel(PostsRepository postsRepository) {
        mPostsRepository = postsRepository;
        mMySubreddits = mPostsRepository.getMySubreddits();
    }

    public LiveData<List<SubredditEntry>> getSubreddits() {
        return mMySubreddits;
    }
}
