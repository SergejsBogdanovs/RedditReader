package lv.st.sbogdano.redditreader.sync;

        import android.app.IntentService;
        import android.content.Intent;
        import android.support.annotation.Nullable;

        import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;
        import lv.st.sbogdano.redditreader.util.InjectorUtils;

public class PostsSyncIntentService extends IntentService{

    public PostsSyncIntentService() {
        super("PostsSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DataNetworkSource dataNetworkSource =
                InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        int pages = intent != null ? intent.getIntExtra(DataNetworkSource.PAGES_COUNT, 1) : 1;
        int items = intent != null ? intent.getIntExtra(DataNetworkSource.ITEMS_PER_PAGE, 10) : 10;
        //dataNetworkSource.loadPosts(pages, items);
    }
}
