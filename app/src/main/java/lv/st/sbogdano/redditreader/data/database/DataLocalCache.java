package lv.st.sbogdano.redditreader.data.database;

import android.arch.lifecycle.LiveData;

import java.util.List;

import lv.st.sbogdano.redditreader.data.database.posts.PostsDao;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditsDao;
import lv.st.sbogdano.redditreader.util.AppExecutors;

public class DataLocalCache {

    private PostsDao mPostsDao;
    private SubredditsDao mSubredditsDao;
    private AppExecutors mExecutors;

    public DataLocalCache( SubredditsDao subredditsDao, AppExecutors executors) {
        //mPostsDao = postsDao;
        mSubredditsDao = subredditsDao;
        mExecutors = executors;
    }

//    public DataSource.Factory<Integer, Post> getPosts() {
//        return mPostsDao.getPosts();
//    }
//
//    public void insertPosts(List<Post> postEntries) {
//        mExecutors.diskIO().execute(() -> mPostsDao.insertAll(postEntries));
//    }

    public LiveData<List<SubredditEntry>> getSubreddits() {
        return mSubredditsDao.getSubbreddits();
    }

    public void insertSubreddits(List<SubredditEntry> subredditEntries) {
        mSubredditsDao.insertAll(subredditEntries);
    }

    public void deleteOldSubreddits() {
        mSubredditsDao.deleteSubreddits();
    }

    public void deleteSubreddit(String name) {
        mSubredditsDao.deleteSubreddit(name);
    }

//    public void deletePosts(String subredditName) {
//        mPostsDao.deletePostsByName(subredditName);
//    }
//
//    public void deleteAllPosts() {
//        mExecutors.diskIO().execute(() -> mPostsDao.deleteAllPosts());
//    }
}
