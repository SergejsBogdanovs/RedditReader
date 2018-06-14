package lv.st.sbogdano.redditreader.util;

import android.content.Context;

import lv.st.sbogdano.redditreader.AppExecutors;
import lv.st.sbogdano.redditreader.data.PostsRepository;
import lv.st.sbogdano.redditreader.data.database.PostsDatabase;
import lv.st.sbogdano.redditreader.data.network.PostsNetworkDataSource;

public class InjectorUtils {

    public static PostsRepository providePostsRepository(Context applicationContext) {
        AppExecutors executors = AppExecutors.getInstance();
        PostsNetworkDataSource networkDataSource =
                PostsNetworkDataSource.getInstance(applicationContext, executors);
        PostsDatabase database = PostsDatabase.getInstance(applicationContext);
        return PostsRepository.getInstance(database.postsDao(), database.subredditsDao(), networkDataSource, executors);
    }

    public static PostsNetworkDataSource provideNetworkDataSource(Context applicationContext) {
        // This call to provide repository is necessary if the app starts from a service - in this
        // case the repository will not exist unless it is specifically created.
        providePostsRepository(applicationContext);
        AppExecutors executors = AppExecutors.getInstance();
        return PostsNetworkDataSource.getInstance(applicationContext, executors);
    }
}
