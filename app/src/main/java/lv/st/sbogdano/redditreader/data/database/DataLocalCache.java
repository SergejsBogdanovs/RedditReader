package lv.st.sbogdano.redditreader.data.database;

import android.arch.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Maybe;
import lv.st.sbogdano.redditreader.data.database.submission.SubmissionDao;
import lv.st.sbogdano.redditreader.data.database.submission.SubmissionEntry;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditsDao;
import lv.st.sbogdano.redditreader.util.AppExecutors;

public class DataLocalCache {

    private final SubredditsDao mSubredditsDao;
    private final SubmissionDao mSubmissionDao;
    private final AppExecutors mExecutors;

    public DataLocalCache(SubredditsDao subredditsDao, SubmissionDao submissionDao, AppExecutors executors) {
        mSubredditsDao = subredditsDao;
        mSubmissionDao = submissionDao;
        mExecutors = executors;
    }

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

    public void saveSubmissionId(String submissionId, boolean isPostLiked) {
        mExecutors.diskIO().execute(() -> mSubmissionDao.insertSubmission(new SubmissionEntry(submissionId, isPostLiked)));
    }

    public Maybe<SubmissionEntry> getSubmissionId(String id) {
        return mSubmissionDao.getSubmission(id);
    }
}
