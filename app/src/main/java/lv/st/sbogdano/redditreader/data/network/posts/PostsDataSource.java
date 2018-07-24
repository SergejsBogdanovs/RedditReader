package lv.st.sbogdano.redditreader.data.network.posts;

import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import net.dean.jraw.models.Submission;

import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;

public class PostsDataSource extends ItemKeyedDataSource<String, Submission>{
    
    private final DataNetworkSource mDataNetworkSource;
    private final SubredditEntry mSubredditEntry;
    private int lastRequestedPage = 1;

    public PostsDataSource(DataNetworkSource dataNetworkSource, SubredditEntry subredditEntry) {
        mDataNetworkSource = dataNetworkSource;
        mSubredditEntry = subredditEntry;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Submission> callback) {
        mDataNetworkSource.loadPosts(lastRequestedPage, params.requestedLoadSize, callback, mSubredditEntry);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Submission> callback) {
        lastRequestedPage++;
        mDataNetworkSource.loadPosts(lastRequestedPage, params.requestedLoadSize, callback, mSubredditEntry);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<Submission> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull Submission item) {
        return item.getSubredditId();
    }
}
