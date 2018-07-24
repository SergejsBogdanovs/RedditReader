package lv.st.sbogdano.redditreader.data;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.util.Log;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.managers.AccountManager;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;

import java.util.List;

import io.reactivex.Maybe;
import lv.st.sbogdano.redditreader.data.database.DataLocalCache;
import lv.st.sbogdano.redditreader.data.database.submission.SubmissionEntry;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.data.model.PostComment;
import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;
import lv.st.sbogdano.redditreader.data.network.comments.CommentsDataSourceFactory;
import lv.st.sbogdano.redditreader.data.network.posts.PostsDataSourceFactory;
import lv.st.sbogdano.redditreader.util.AppExecutors;

public class RedditDataRepository {

    private static final String TAG = "RedditDataRepository";

    private static final Object LOCK = new Object();
    private static RedditDataRepository sInstance;
    private final DataLocalCache mDataLocalCache;
    private final DataNetworkSource mDataNetworkSource;
    private final AppExecutors mExecutors;
    private boolean mInitialized = false;

    private RedditDataRepository(DataLocalCache dataLocalCache,
                                 DataNetworkSource dataNetworkSource, AppExecutors executors) {
        mDataLocalCache = dataLocalCache;
        mDataNetworkSource = dataNetworkSource;
        mExecutors = executors;

        LiveData<List<SubredditEntry>> subredditEntryLiveData = mDataNetworkSource.getUserSubreddits();
        subredditEntryLiveData.observeForever(data -> mExecutors.diskIO().execute(() -> {
            deleteOldSubreddits();
            mDataLocalCache.insertSubreddits(data);
        }));
    }

    public synchronized static RedditDataRepository getInstance(DataLocalCache dataLocalCache,
                                                                DataNetworkSource dataNetworkSource,
                                                                AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RedditDataRepository(dataLocalCache, dataNetworkSource, executors);
            }
        }
        return sInstance;
    }

    public LiveData<PagedList<Submission>> getPostsResult(SubredditEntry subredditEntry) {

        PagedList.Config config = new PagedList.Config.Builder().setPageSize(20).build();

        PostsDataSourceFactory factory = new PostsDataSourceFactory(mDataNetworkSource, subredditEntry);

        LiveData<PagedList<Submission>> posts = new LivePagedListBuilder(factory, config)
                .setFetchExecutor(mExecutors.networkIO())
                .build();

        return posts;
    }

    public LiveData<PagedList<PostComment>> getCommentResult(Submission submission) {

        PagedList.Config config = new PagedList.Config.Builder().setPageSize(20).build();

        CommentsDataSourceFactory factory = new CommentsDataSourceFactory(mDataNetworkSource, submission);

        LiveData<PagedList<PostComment>> postComments = new LivePagedListBuilder(factory, config)
                .setFetchExecutor(mExecutors.networkIO())
                .build();

        return postComments;
    }

    public LiveData<List<SubredditEntry>> getSubredditResults() {
        initializeData();
        return mDataLocalCache.getSubreddits();
    }

    private void initializeData() {
        if (mInitialized) {
            return;
        }
        mInitialized = true;

        mDataNetworkSource.startFetchSubredditsService();
    }

    private void deleteOldSubreddits() {
        mDataLocalCache.deleteOldSubreddits();
    }

    public void deleteSubscription(SubredditEntry subredditEntry) {

        // Delete subscription from reddit account.
        mExecutors.networkIO().execute(() -> {
            Log.e(TAG, "deleteSubscription: execute");
            AccountManager manager = new AccountManager(AuthenticationManager.get().getRedditClient());
            List<Subreddit> subscribedSubreddits = mDataNetworkSource.getSubscribedSubreddits();
            for (Subreddit subreddit : subscribedSubreddits) {
                if (subreddit.getDisplayName().equals(subredditEntry.getSubredditName())) {
                    Log.e(TAG, "deleteSubscription: unsubscribe");
                    manager.unsubscribe(subreddit);
                    mDataLocalCache.deleteSubreddit(subredditEntry.getSubredditName());
                }
            }
        });
    }

    public void saveSubmissionId(String submissionId, boolean isPostLiked) {
        mDataLocalCache.saveSubmissionId(submissionId, isPostLiked);
    }

    public Maybe<SubmissionEntry> getSubmission(String id) {
        return mDataLocalCache.getSubmissionId(id);
    }
}
