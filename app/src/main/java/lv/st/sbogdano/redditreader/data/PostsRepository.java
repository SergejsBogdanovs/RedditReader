package lv.st.sbogdano.redditreader.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import lv.st.sbogdano.redditreader.AppExecutors;
import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;
import lv.st.sbogdano.redditreader.data.database.posts.PostsDao;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditsDao;
import lv.st.sbogdano.redditreader.data.network.PostsNetworkDataSource;

public class PostsRepository {

    private static final Object LOCK = new Object();
    private static PostsRepository sInstance;
    private final PostsDao mPostsDao;
    private final SubredditsDao mSubredditsDao;
    private final PostsNetworkDataSource mPostsNetworkDataSource;
    private final AppExecutors mExecutors;
    private boolean mInitialized = false;

    private PostsRepository(PostsDao postsDao, SubredditsDao subredditsDao, PostsNetworkDataSource postsNetworkDataSource, AppExecutors executors) {
        mPostsDao = postsDao;
        mSubredditsDao = subredditsDao;
        mPostsNetworkDataSource = postsNetworkDataSource;
        mExecutors = executors;

        LiveData<List<PostEntry>> networkPostData = mPostsNetworkDataSource.getCurrentPosts();
        networkPostData.observeForever(newPostsFromNetwork -> mExecutors.diskIO().execute(() -> {
            deleteOldPosts();
            mPostsDao.insertAll(newPostsFromNetwork);
        }));

        LiveData<List<SubredditEntry>> networkSubredditData = mPostsNetworkDataSource.getUserSubreddits();
        networkSubredditData.observeForever(newSubredditsFromNetwork -> mExecutors.diskIO().execute(() -> {
            deleteOldSubreddits();
            mSubredditsDao.insertAll(newSubredditsFromNetwork);
        }));
    }

    public synchronized static PostsRepository getInstance(
            PostsDao postsDao,
            SubredditsDao subredditsDao,
            PostsNetworkDataSource postsNetworkDataSource,
            AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PostsRepository(postsDao, subredditsDao, postsNetworkDataSource, executors);
            }
        }
        return sInstance;
    }

    public LiveData<List<PostEntry>> getCurrentPosts() {
        initializeData();
        return mPostsDao.getPosts();
    }

    public LiveData<List<SubredditEntry>> getMySubreddits() {
        return mSubredditsDao.getSubbreddits();
    }

    private void initializeData() {
        if (mInitialized) {
            return;
        }
        mInitialized = true;

        mPostsNetworkDataSource.scheduleRecurringFetchPostsSync();
        mPostsNetworkDataSource.startFetchPostsService();
    }

    private synchronized void deleteOldPosts() {
        mPostsDao.deleteOldPosts();
    }

    private synchronized void deleteOldSubreddits() {
        mSubredditsDao.deleteSubreddits();
    }

    public void refreshPosts() {
        mInitialized = false;
        initializeData();
    }

}
