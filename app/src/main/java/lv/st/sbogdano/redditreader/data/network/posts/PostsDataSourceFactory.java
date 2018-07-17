package lv.st.sbogdano.redditreader.data.network.posts;

import android.arch.paging.DataSource;

import net.dean.jraw.models.Submission;

import lv.st.sbogdano.redditreader.data.model.Post;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;

public class PostsDataSourceFactory extends DataSource.Factory<String, Submission> {

    private DataNetworkSource mDataNetworkSource;
    private SubredditEntry mSubredditEntry;


    public PostsDataSourceFactory(DataNetworkSource dataNetworkSource, SubredditEntry subredditEntry) {
        mDataNetworkSource = dataNetworkSource;
        mSubredditEntry = subredditEntry;
    }

    @Override
    public DataSource<String, Submission> create() {
        return new PostsDataSource(mDataNetworkSource, mSubredditEntry);
    }
}
