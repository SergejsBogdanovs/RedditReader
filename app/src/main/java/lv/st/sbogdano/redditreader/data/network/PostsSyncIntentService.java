package lv.st.sbogdano.redditreader.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import lv.st.sbogdano.redditreader.util.InjectorUtils;

public class PostsSyncIntentService extends IntentService{

    public PostsSyncIntentService() {
        super("PostsSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        PostsNetworkDataSource postsNetworkDataSource =
                InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        postsNetworkDataSource.fetchPosts();
    }
}
