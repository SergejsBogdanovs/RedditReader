package lv.st.sbogdano.redditreader.data;

import android.arch.paging.DataSource;

import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;

public class RedditDataSourceFactory extends DataSource.Factory<String, PostEntry> {

    DataNetworkSource mDataNetworkSource;
    SubredditEntry mSubredditEntry;

    public RedditDataSourceFactory(DataNetworkSource dataNetworkSource, SubredditEntry subredditEntry) {
        mDataNetworkSource = dataNetworkSource;
        mSubredditEntry = subredditEntry;
    }

    @Override
    public DataSource<String, PostEntry> create() {
        RedditDataSource dataSource = new RedditDataSource(this.mDataNetworkSource, mSubredditEntry);
        return dataSource;
    }
}
