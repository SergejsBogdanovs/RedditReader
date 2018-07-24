package lv.st.sbogdano.redditreader.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import lv.st.sbogdano.redditreader.data.RedditDataRepository;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;

public class SubredditViewModel extends ViewModel{

    private final RedditDataRepository mRedditDataRepository;


    public SubredditViewModel(RedditDataRepository redditDataRepository) {
        mRedditDataRepository = redditDataRepository;
    }

    public LiveData<List<SubredditEntry>>getSubreddits() {
        return mRedditDataRepository.getSubredditResults();
    }

    public void deleteSubscription(SubredditEntry subredditEntry) {
        mRedditDataRepository.deleteSubscription(subredditEntry);
    }

}
