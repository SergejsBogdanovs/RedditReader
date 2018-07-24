package lv.st.sbogdano.redditreader.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;
import lv.st.sbogdano.redditreader.util.InjectorUtils;

public class SubredditsSyncIntentService extends IntentService{

    public SubredditsSyncIntentService() {
        super("SubredditsSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DataNetworkSource dataNetworkSource =
                InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        dataNetworkSource.loadSubreddits();
    }
}
