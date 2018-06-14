package lv.st.sbogdano.redditreader.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

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

import lv.st.sbogdano.redditreader.AppExecutors;
import lv.st.sbogdano.redditreader.data.MySubreddit;
import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.ui.login.LoginActivity;

public class PostsNetworkDataSource {

    public static final String TAG = PostsNetworkDataSource.class.getSimpleName();

    // The number of posts we want our API to return, set to 100 posts.
    public static final int NUM_POSTS = 100;

    // Interval at which to sync with the Reddit posts.
    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS
            .toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;
    private static final String POSTS_SYNC_TAG = "posts-sync";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static PostsNetworkDataSource sInstance;
    private final Context mContext;
    private final AppExecutors mExecutors;

    private final MutableLiveData<List<PostEntry>> mDownloadedPosts;
    private final MutableLiveData<List<SubredditEntry>> mDownloadedSubreddits;

    private PostsNetworkDataSource(Context context, AppExecutors appExecutors) {
        mContext = context;
        mExecutors = appExecutors;
        mDownloadedPosts = new MutableLiveData<>();
        mDownloadedSubreddits = new MutableLiveData<>();
    }

    /**
     * Get the singleton for this class
     */
    public static PostsNetworkDataSource getInstance(Context context, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PostsNetworkDataSource(context.getApplicationContext(), executors);
            }
        }
        return sInstance;
    }

    public LiveData<List<PostEntry>> getCurrentPosts() {
        return mDownloadedPosts;
    }

    public LiveData<List<SubredditEntry>> getUserSubreddits() {
        return mDownloadedSubreddits;
    }

    /**
     * Starts an intent service to fetch the posts.
     */
    public void startFetchPostsService() {
        Intent intentToFetch = new Intent(mContext, PostsSyncIntentService.class);
        mContext.startService(intentToFetch);
    }

    /**
     * Schedules a repeating job service which fetches the posts.
     */
    public void scheduleRecurringFetchPostsSync() {
        Driver driver = new GooglePlayDriver(mContext);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        // Create the Job to periodically sync Reddit posts
        Job syncSunshineJob = dispatcher.newJobBuilder()
                .setService(PostsFirebaseJobService.class)
                .setTag(POSTS_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        // Schedule the Job with the dispatcher
        dispatcher.schedule(syncSunshineJob);
    }

    /**
     * Gets the newest posts and subreddits
     */
    void fetchPosts() {
        Log.v(TAG, "fetchPosts: is started" );

        mExecutors.networkIO().execute(() -> {

            AuthenticationState state = AuthenticationManager.get().checkAuthState();
            if (state == AuthenticationState.NEED_REFRESH) {
                try {
                    AuthenticationManager.get().refreshAccessToken(LoginActivity.CREDENTIALS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            RedditClient redditClient = AuthenticationManager.get().getRedditClient();

            if (redditClient.isAuthenticated()) {

                // Fetching Posts
                SubredditPaginator paginator = new SubredditPaginator(redditClient);
                paginator.setLimit(NUM_POSTS);
                Listing<Submission> posts = paginator.next();

                List<PostEntry> postEntries = new ArrayList<>();
                for (Submission post : posts) {
                    postEntries.add(new PostEntry(
                            post.getSubredditName(),
                            post.getTitle(),
                            post.getAuthor(),
                            post.getPermalink(),
                            post.getThumbnail(),
                            post.getScore(),
                            post.getCommentCount()));
                }

                mDownloadedPosts.postValue(postEntries);

                // Fetching user subreddits
                UserSubredditsPaginator subredditsPaginator
                        = new UserSubredditsPaginator(redditClient, "subscriber");

                Listing<Subreddit> subreddits = subredditsPaginator.next();
                List<SubredditEntry> subredditEntries = new ArrayList<>();
                for (Subreddit subreddit : subreddits) {
                    subredditEntries.add(new SubredditEntry(subreddit.getDisplayName(), true));
                }

                mDownloadedSubreddits.postValue(subredditEntries);
            }
        });
    }
}
