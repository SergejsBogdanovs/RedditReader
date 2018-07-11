package lv.st.sbogdano.redditreader.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.arch.paging.PageKeyedDataSource;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.AuthenticationState;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.paginators.SubredditPaginator;
import net.dean.jraw.paginators.UserSubredditsPaginator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.sync.PostsSyncIntentService;
import lv.st.sbogdano.redditreader.sync.SubredditsSyncIntentService;
import lv.st.sbogdano.redditreader.ui.login.LoginActivity;
import lv.st.sbogdano.redditreader.util.AppExecutors;

public class DataNetworkSource {

    public static final String TAG = DataNetworkSource.class.getSimpleName();

    // The number of posts we want our API to return, set to 100 posts.
    public static final String ITEMS_PER_PAGE = "items_per_page";
    public static final String PAGES_COUNT = "pages_count";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static DataNetworkSource sInstance;
    private final Context mContext;
    private final AppExecutors mExecutors;

    private final RedditClient redditClient = AuthenticationManager.get().getRedditClient();

    private final MutableLiveData<List<PostEntry>> mDownloadedPosts;
    private final MutableLiveData<List<SubredditEntry>> mDownloadedSubreddits;
    private ItemKeyedDataSource.LoadCallback mLoadCallback;


    private DataNetworkSource(Context context, AppExecutors appExecutors) {
        mContext = context;
        mExecutors = appExecutors;
        mDownloadedPosts = new MutableLiveData<>();
        mDownloadedSubreddits = new MutableLiveData<>();
    }

    /**
     * Get the singleton for this class
     */
    public static DataNetworkSource getInstance(Context context, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new DataNetworkSource(context.getApplicationContext(), executors);
            }
        }
        return sInstance;
    }

//    public LiveData<List<PostEntry>> getCurrentPosts(int page, int itemsPerPage) {
//        startFetchPostsService(page, itemsPerPage);
//        return mDownloadedPosts;
//    }

    public LiveData<List<PostEntry>> getCurrentPosts(int page,
                                                     int itemsPerPage,
                                                     final ItemKeyedDataSource.LoadCallback<PostEntry> callback) {
        startFetchPostsService(page, itemsPerPage);
        mLoadCallback = callback;
        return mDownloadedPosts;
    }

    public LiveData<List<SubredditEntry>> getUserSubreddits() {
        return mDownloadedSubreddits;
    }

    public synchronized void startFetchPostsService(int page, int items) {
        Log.v(TAG, "startFetchPostsService");
        Intent intent = new Intent(mContext, PostsSyncIntentService.class);
        intent.putExtra(PAGES_COUNT, page);
        intent.putExtra(ITEMS_PER_PAGE, items);
        mContext.startService(intent);
    }

    public void startFetchSubredditsService() {
        Intent intent = new Intent(mContext, SubredditsSyncIntentService.class);
        mContext.startService(intent);
    }

    /**
     * Gets the newest posts
     */
    public void loadPosts(int pages,
                          int items,
                          ItemKeyedDataSource.LoadCallback<PostEntry> callback,
                          SubredditEntry subredditEntry) {

        AuthenticationState state = AuthenticationManager.get().checkAuthState();
        if (state == AuthenticationState.NEED_REFRESH) {
            try {
                AuthenticationManager.get().refreshAccessToken(LoginActivity.CREDENTIALS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.v(TAG, "fetchPosts: is started");

        if (redditClient.isAuthenticated()) {

            // Fetching Posts
            SubredditPaginator paginator = new SubredditPaginator(redditClient, subredditEntry.getSubredditName());
            paginator.setLimit(items);
            Listing<Submission> posts = null;
            for (int i = 0; i < pages; i++) {
                posts = paginator.next();
            }
            List<PostEntry> postEntries = new ArrayList<>();
            for (Submission post : posts) {
                postEntries.add(new PostEntry(
                        post.getId(),
                        post.getSubredditName(),
                        post.getTitle(),
                        post.getAuthor(),
                        post.getPermalink(),
                        post.getThumbnail(),
                        post.getScore(),
                        post.getCommentCount()));
            }
            Log.v(TAG, "loadPosts: " + postEntries.size());
            callback.onResult(postEntries);
        }
    }


    /**
     * Gets the user subscribed subreddits.
     */
    public void loadSubreddits() {
        mExecutors.networkIO().execute(() -> {
            Log.v(TAG, "fetchSubreddits: is started");

            AuthenticationState state = AuthenticationManager.get().checkAuthState();
            if (state == AuthenticationState.NEED_REFRESH) {
                try {
                    AuthenticationManager.get().refreshAccessToken(LoginActivity.CREDENTIALS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //RedditClient redditClient = AuthenticationManager.get().getRedditClient();

            if (redditClient.isAuthenticated()) {

                // Fetching user subscribed Subreddits
                UserSubredditsPaginator subredditsPaginator
                        = new UserSubredditsPaginator(redditClient, "subscriber");
                Listing<Subreddit> subreddits = subredditsPaginator.next();
                List<SubredditEntry> subredditEntries = new ArrayList<>();
                for (Subreddit subreddit : subreddits) {
                    subredditEntries.add(new SubredditEntry(subreddit.getDisplayName()));
                }

                mDownloadedSubreddits.postValue(subredditEntries);
            }
        });
    }

    public List<Subreddit> getSubscribedSubreddits() {

        //RedditClient redditClient = AuthenticationManager.get().getRedditClient();

        // Fetching user subreddits
        UserSubredditsPaginator subredditsPaginator
                = new UserSubredditsPaginator(redditClient, "subscriber");

        Listing<Subreddit> subreddits = subredditsPaginator.next();

        return new ArrayList<>(subreddits);
    }

}
