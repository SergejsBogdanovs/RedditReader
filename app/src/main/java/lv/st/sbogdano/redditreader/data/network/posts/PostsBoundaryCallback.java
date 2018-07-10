package lv.st.sbogdano.redditreader.data.network.posts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;
import lv.st.sbogdano.redditreader.data.database.DataLocalCache;
import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;
import lv.st.sbogdano.redditreader.ui.posts.PostsActivity;

import static lv.st.sbogdano.redditreader.data.network.DataNetworkSource.TAG;

public class PostsBoundaryCallback extends PagedList.BoundaryCallback<PostEntry>{

    private static final int NETWORK_PAGE_SIZE = 50;

    private final DataNetworkSource service;
    private final DataLocalCache cache;
    private Context mContext;

    private int lastRequestedPage = 1;
    private final MutableLiveData<String> _networkErrors = new MutableLiveData<>();
    LiveData<String> networkErrors;

    // avoid triggering multiple requests in the same time
    private boolean isRequestInProgress = false;


    public PostsBoundaryCallback(Context context, DataNetworkSource service, DataLocalCache cache) {
        mContext = context;
        this.service = service;
        this.cache = cache;
    }

    public LiveData<String> getNetworkErrors() {
        return _networkErrors;
    }

    @Override
    public void onZeroItemsLoaded() {
        Log.v(TAG, "onZeroItemsLoaded: ");
        requestAndSavePostData();
    }

    @Override
    public void onItemAtEndLoaded(@NonNull PostEntry itemAtEnd) {
        Log.v(TAG, "onItemAtEnd: ");
        requestAndSavePostData();
    }

    private void requestAndSavePostData() {
        if (isRequestInProgress) return;

        isRequestInProgress = true;

        PostsActivity activity = (PostsActivity) mContext;

        service.getCurrentPosts(lastRequestedPage, NETWORK_PAGE_SIZE).observe(activity, postEntries -> {
            if (postEntries != null) {
                cache.insertPosts(postEntries);
                lastRequestedPage++;
                isRequestInProgress = false;
            } else {
                getNetworkErrors();
                isRequestInProgress = false;
            }
        });
    }

}
