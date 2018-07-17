package lv.st.sbogdano.redditreader.data.network.comments;

import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import net.dean.jraw.models.Submission;

import lv.st.sbogdano.redditreader.data.model.Post;
import lv.st.sbogdano.redditreader.data.model.PostComment;
import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;

public class CommentsDataSource extends ItemKeyedDataSource<String, PostComment>{

    private DataNetworkSource mDataNetworkSource;
    private Submission mSubmission;

    public CommentsDataSource(DataNetworkSource dataNetworkSource, Submission submission) {
        mDataNetworkSource = dataNetworkSource;
        mSubmission = submission;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<PostComment> callback) {
        mDataNetworkSource.loadComments(mSubmission, callback);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<PostComment> callback) {
        mDataNetworkSource.loadComments(mSubmission, callback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<PostComment> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull PostComment item) {
        return item.getCommentBody();
    }
}
