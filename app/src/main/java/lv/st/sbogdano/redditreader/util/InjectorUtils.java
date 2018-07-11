package lv.st.sbogdano.redditreader.util;

import android.content.Context;

import lv.st.sbogdano.redditreader.data.database.DataLocalCache;
import lv.st.sbogdano.redditreader.data.RedditDataRepository;
import lv.st.sbogdano.redditreader.data.database.RedditDatabase;
import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;

public class InjectorUtils {

    public static RedditDataRepository providePostsRepository(Context applicationContext) {
        AppExecutors executors = AppExecutors.getInstance();
        DataNetworkSource networkDataSource = provideNetworkDataSource(applicationContext);
        DataLocalCache dataLocalCache = provideLocalCache(applicationContext);
        return RedditDataRepository.getInstance(applicationContext, dataLocalCache, networkDataSource, executors);
    }

    public static DataNetworkSource provideNetworkDataSource(Context applicationContext) {
        //providePostsRepository(applicationContext);
        AppExecutors executors = AppExecutors.getInstance();
        return DataNetworkSource.getInstance(applicationContext, executors);
    }

    public static DataLocalCache provideLocalCache(Context applicationContext) {
        AppExecutors executors = AppExecutors.getInstance();
        RedditDatabase database = RedditDatabase.getInstance(applicationContext);
        return new DataLocalCache( database.subredditsDao(), executors);
    }

}
