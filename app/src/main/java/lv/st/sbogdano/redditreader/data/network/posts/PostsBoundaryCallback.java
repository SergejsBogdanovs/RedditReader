//package lv.st.sbogdano.redditreader.data.network.posts;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.lifecycle.MutableLiveData;
//import android.arch.paging.PagedList;
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.util.Log;
//
//import lv.st.sbogdano.redditreader.data.database.posts.PostEntry;
//import lv.st.sbogdano.redditreader.data.database.DataLocalCache;
//import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;
//import lv.st.sbogdano.redditreader.ui.posts.PostsActivity;
//
//import static lv.st.sbogdano.redditreader.data.network.DataNetworkSource.TAG;
//
//public class PostsBoundaryCallback extends PagedList.BoundaryCallback<PostEntry> {
//
//    private static final int NETWORK_PAGE_SIZE = 100;
//
//    private final DataNetworkSource service;
//    private final DataLocalCache cache;
//    private Context mContext;
//
//    private int lastRequestedPage = 1;
//
//    // avoid triggering multiple requests in the same time
//    private volatile boolean isRequestInProgress = false;
//
//    private final PostsActivity activity;
//    private boolean noNeedToLoad = false;
//
//    public PostsBoundaryCallback(Context context, DataNetworkSource service, DataLocalCache cache) {
//        mContext = context;
//        this.service = service;
//        this.cache = cache;
//        activity = (PostsActivity) mContext;
//    }
//
//
//    @Override
//    public void onZeroItemsLoaded() {
//        Log.v(TAG, "onZeroItemsLoaded: ");
//        if (noNeedToLoad) return;
//        requestAndSavePostData();
//    }
//
//    @Override
//    public void onItemAtEndLoaded(@NonNull PostEntry itemAtEnd) {
//        Log.v(TAG, "onItemAtEnd: ");
//        if (noNeedToLoad) return;
//        requestAndSavePostData();
//    }
//
//    private void requestAndSavePostData() {
//        if (isRequestInProgress) return;
//
//        isRequestInProgress = true;
//
//        service.getCurrentPosts(lastRequestedPage, NETWORK_PAGE_SIZE).observe(activity, postEntries -> {
//            Log.v(TAG, "requestAndSavePostData: " + postEntries.size());
//            if (postEntries != null && postEntries.size() != 0) {
//                cache.insertPosts(postEntries);
//                lastRequestedPage++;
//                isRequestInProgress = false;
//            } else {
//                noNeedToLoad = true;
//                isRequestInProgress = true;
//            }
//        });
//    }
//
//}
