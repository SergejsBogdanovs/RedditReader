//package lv.st.sbogdano.redditreader.data.network.subreddits;
//
//import android.arch.paging.PagedList;
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.util.Log;
//
//import lv.st.sbogdano.redditreader.data.database.DataLocalCache;
//import lv.st.sbogdano.redditreader.data.database.subreddits.SubredditEntry;
//import lv.st.sbogdano.redditreader.data.network.DataNetworkSource;
//
//public class SubredditsBoundaryCallback extends PagedList.BoundaryCallback<SubredditEntry>{
//
//    private static final String TAG = "SubredditsBoundaryCallb";
//
//    private final DataNetworkSource service;
//    private final DataLocalCache cache;
//    private Context mContext;
//
//    private int lastRequestedPage = 1;
//
//    // avoid triggering multiple requests in the same time
//    private boolean isRequestInProgress = false;
//
//
//    public SubredditsBoundaryCallback(Context context, DataNetworkSource service, DataLocalCache cache) {
//        mContext = context;
//        this.service = service;
//        this.cache = cache;
//    }
//
//    @Override
//    public void onZeroItemsLoaded() {
//        Log.v(TAG, "SubredditsBoundaryCallback onZeroItemsLoaded: ");
//        requestAndSaveData();
//    }
//
//    @Override
//    public void onItemAtEndLoaded(@NonNull SubredditEntry itemAtEnd) {
//        Log.v(TAG, "SubredditsBoundaryCallback onItemAtEnd: ");
//        //requestAndSaveData();
//    }
//
//
//}
