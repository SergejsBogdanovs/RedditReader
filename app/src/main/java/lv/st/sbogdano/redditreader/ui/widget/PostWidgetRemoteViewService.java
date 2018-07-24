package lv.st.sbogdano.redditreader.ui.widget;

import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import lv.st.sbogdano.redditreader.R;
import lv.st.sbogdano.redditreader.data.model.UserSubreddit;

public class PostWidgetRemoteViewService extends RemoteViewsService {

    private final PostWidgetManager mPostWidgetManager = new PostWidgetManager();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private final List<UserSubreddit> mUserSubreddits = new ArrayList<>();

            @Override
            public void onCreate() {
                if (mPostWidgetManager.getUserSubreddits() != null) {
                    mUserSubreddits.addAll(mPostWidgetManager.getUserSubreddits());
                }
            }

            @Override
            public void onDataSetChanged() {
                mUserSubreddits.clear();

                if (mPostWidgetManager.getUserSubreddits() != null) {
                    mUserSubreddits.addAll(mPostWidgetManager.getUserSubreddits());
                }
            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return mUserSubreddits.size();
            }

            @Override
            public RemoteViews getViewAt(int i) {
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.post_widget_item);
                UserSubreddit userSubreddit = mUserSubreddits.get(i);
                remoteViews.setTextViewText(R.id.widget_subreddit_name, userSubreddit.getName());
                return remoteViews;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}
