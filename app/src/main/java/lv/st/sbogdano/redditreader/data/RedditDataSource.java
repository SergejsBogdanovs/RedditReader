package lv.st.sbogdano.redditreader.data;

import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;

public class RedditDataSource extends ItemKeyedDataSource<String, PostEntry>{

    private static final int NETWORK_PAGE_SIZE = 50;

    private DataNetworkSource mDataNetworkSource;
    private SubredditEntry mSubredditEntry;
    private int lastRequestedPage = 1;

    public RedditDataSource(DataNetworkSource dataNetworkSource, SubredditEntry subredditEntry) {
        mDataNetworkSource = dataNetworkSource;
        mSubredditEntry = subredditEntry;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<PostEntry> callback) {
        mDataNetworkSource.loadPosts(lastRequestedPage, params.requestedLoadSize, callback, mSubredditEntry);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<PostEntry> callback) {
        lastRequestedPage++;
        mDataNetworkSource.loadPosts(lastRequestedPage, params.requestedLoadSize, callback, mSubredditEntry);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<PostEntry> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull PostEntry item) {
        return item.getSubredditId();
    }
}
