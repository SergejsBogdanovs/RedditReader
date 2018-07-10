package lv.st.sbogdano.redditreader.data;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.content.Context;
import android.util.Log;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.managers.AccountManager;
import net.dean.jraw.models.Subreddit;

import java.util.List;

import lv.st.sbogdano.redditreader.data.database.DataLocalCache;
import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;
import lv.st.sbogdano.redditreader.data.network.posts.PostsBoundaryCallback;
import lv.st.sbogdano.redditreader.util.AppExecutors;

public class RedditDataRepository {

    private static final String TAG = "RedditDataRepository";
    private static final int POSTS_DATABASE_PAGE_SIZE = 20;

    private static final Object LOCK = new Object();
    private static RedditDataRepository sInstance;
    private final DataLocalCache mDataLocalCache;
    private final DataNetworkSource mDataNetworkSource;
    private final AppExecutors mExecutors;
    private Context mContext;
    private boolean mInitialized = false;
    private boolean needRefresh = false;

    private RedditDataRepository(Context context, DataLocalCache dataLocalCache,
                                 DataNetworkSource dataNetworkSource, AppExecutors executors) {
        mContext = context;
        mDataLocalCache = dataLocalCache;
        mDataNetworkSource = dataNetworkSource;
        mExecutors = executors;

        LiveData<List<SubredditEntry>> subredditEntryLiveData = mDataNetworkSource.getUserSubreddits();
        subredditEntryLiveData.observeForever(data -> mExecutors.diskIO().execute(() -> {
            deleteOldSubreddits();
            mDataLocalCache.insertSubreddits(data);
        }));
    }


    public synchronized static RedditDataRepository getInstance(Context context,
                                                                DataLocalCache dataLocalCache,
                                                                DataNetworkSource dataNetworkSource,
                                                                AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RedditDataRepository(context, dataLocalCache, dataNetworkSource, executors);
            }
        }
        return sInstance;
    }

    public LiveData<PagedList<PostEntry>> getPostsResult(boolean firsLoad) {

        if (firsLoad || needRefresh) {
            mDataLocalCache.deleteAllPosts();
        }

        // Get data source factory from the local cache
        DataSource.Factory<Integer, PostEntry> postsDataSourceFactory = mDataLocalCache.getPosts();


        // Construct the boundary callback
        PostsBoundaryCallback boundaryCallback =
                new PostsBoundaryCallback(mContext, mDataNetworkSource, mDataLocalCache);

        LiveData<PagedList<PostEntry>> posts =
                new LivePagedListBuilder(postsDataSourceFactory, POSTS_DATABASE_PAGE_SIZE)
                        .setBoundaryCallback(boundaryCallback)
                        .build();

        return posts;
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
        needRefresh = true;

        // Delete subscription from reddit account.
        mExecutors.networkIO().execute(() -> {
            Log.e(TAG, "deleteSubscription: execute");
            AccountManager manager = new AccountManager(AuthenticationManager.get().getRedditClient());
            List<Subreddit> subscribedSubreddits = mDataNetworkSource.getSubscribedSubreddits();
            for (Subreddit subreddit : subscribedSubreddits) {
                if (subreddit.getDisplayName().equals(subredditEntry.getSubredditName())) {
                    Log.e(TAG, "deleteSubscription: unsubscribe");
                    manager.unsubscribe(subreddit);
                }
            }
        });

        // Delete subreddit from db.
        mExecutors.diskIO().execute(() -> mDataLocalCache.deleteSubreddit(subredditEntry.getSubredditName()));

        // Delete posts from db.
        mExecutors.diskIO().execute(() -> mDataLocalCache.deletePosts(subredditEntry.getSubredditName()));

    }


}
