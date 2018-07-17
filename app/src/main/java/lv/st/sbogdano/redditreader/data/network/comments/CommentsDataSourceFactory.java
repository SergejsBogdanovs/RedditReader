package lv.st.sbogdano.redditreader.data.network.comments;

import android.arch.paging.DataSource;

import net.dean.jraw.models.Submission;

import lv.st.sbogdano.redditreader.data.model.Post;
import lv.st.sbogdano.redditreader.data.model.PostComment;
import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;

public class CommentsDataSourceFactory extends DataSource.Factory<String, PostComment>{

    private DataNetworkSource mDataNetworkSource;
    private Submission mSubmission;

    public CommentsDataSourceFactory(DataNetworkSource dataNetworkSource, Submission submission) {
        mDataNetworkSource = dataNetworkSource;
        mSubmission = submission;
    }

    @Override
    public DataSource<String, PostComment> create() {
        return new CommentsDataSource(mDataNetworkSource, mSubmission);
    }
}